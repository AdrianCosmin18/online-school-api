package com.example.onlineschoolapp.services;

import com.example.onlineschoolapp.dto.BookDTO;
import com.example.onlineschoolapp.dto.StudentDTO;
import com.example.onlineschoolapp.exceptions.*;
import com.example.onlineschoolapp.models.Book;
import com.example.onlineschoolapp.models.Course;
import com.example.onlineschoolapp.models.Student;
import com.example.onlineschoolapp.repository.BookRepo;
import com.example.onlineschoolapp.repository.CourseRepo;
import com.example.onlineschoolapp.repository.StudentRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.averagingDouble;

@Service
public class StudentAndBookService {

    private StudentRepo studentRepo;
    private BookRepo bookRepo;
    private CourseRepo courseRepo;

    public StudentAndBookService(StudentRepo studentRepo, BookRepo bookRepo, CourseRepo courseRepo) {
        this.studentRepo = studentRepo;
        this.bookRepo = bookRepo;
        this.courseRepo = courseRepo;
    }

    public List<Student> getAllStudents(){

        List<Student>  students = studentRepo.findAll();
        if(students.size() == 0){
            throw new NoStudentsException("Student list empty");
        }
        else{
            return students;
        }
    }

    public Student getStudentById(long id){

        Optional<Student> student = studentRepo.findById(id);
        if(!student.isPresent()){
            throw new StudentNotFoundById(id);
        }
        else{
            return student.get();
        }
    }

    public List<Book> getAllBooks(){
        List<Book> books = bookRepo.findAll();
        if (books.size() == 0){
            throw new NoBooksFoundException("Books list empty");
        }
        else{
            return books;
        }
    }

    public Book getBookById(long id){
        Optional<Book> book = bookRepo.findById(id);
        if (!book.isPresent())
            throw new BookNotFoundById(id);
        return book.get();
    }

    public void addStudent(StudentDTO s){
        Optional<Student> existingStudent = studentRepo.getStudentByEmail(s.getEmail());
        if(existingStudent.isPresent()){
            throw new StudentEmailAlreadyExistsException(s.getEmail());
        }
        studentRepo.saveAndFlush(new Student(s.getFirstName(), s.getLastName(), s.getEmail(), s.getAge()));
    }

    public void addBookToStudent(long id, BookDTO book){
        Student student = studentRepo.findById(id).orElseThrow(() -> new StudentNotFoundById(id));

        Optional<Book> existingBook = bookRepo.getBookByName(book.getName());
        if (!existingBook.equals(Optional.empty())){
            throw new BookNameAlreadyExistsException();
        }
        student.addBook(new Book(book.getName(), book.getCreatedAt()));
        studentRepo.saveAndFlush(student);
    }

    public void deleteBookFromStudent(long studentId, long bookId){
        Optional<Book> existingBook = bookRepo.findById(bookId);
        if (existingBook.equals(Optional.empty())){
            throw new BookNotFoundById(bookId);
        }
        Optional<Student> existingStudent = studentRepo.findById(studentId);
        if (existingStudent.equals(Optional.empty())){
            throw new StudentNotFoundById(studentId);
        }
        existingStudent.get().removeBook(existingBook.get());
        studentRepo.save(existingStudent.get());
    }

    public void deleteStudent(long id){
        Optional<Student> student = studentRepo.findById(id);
        if (student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }
        studentRepo.deleteById(id);
    }

    public void updateStudent(long id, StudentDTO studentDTO){

        if (!studentRepo.findById(id).isPresent()){
            throw new StudentNotFoundById(id);
        }

        if (studentRepo.getStudentByEmail(studentDTO.getEmail()).isPresent()){
            throw new StudentEmailAlreadyExistsException(studentDTO.getEmail());
        }
        studentRepo.updateById(id, studentDTO.getFirstName(), studentDTO.getLastName(), studentDTO.getEmail(), studentDTO.getAge());
    }

    public void updateBook(long id, BookDTO book){
        bookRepo.findById(id).map(b -> {
            Optional<Book> existingBook = bookRepo.getBookByName(book.getName());
            if (!existingBook.equals(Optional.empty())){
                throw new BookNameAlreadyExistsException();
            }
            b.setName(book.getName());
            b.setCreatedAt(book.getCreatedAt());
            return bookRepo.save(b);
        }).orElseThrow(()-> new BookNotFoundById(id));
    }

    public void addCourseToStudent(long id, String courseName){

        Course course = courseRepo.getCourseByName(courseName).orElseThrow(() -> new CourseNotFoundByName(courseName));

        Optional<Student> student = studentRepo.findById(id);
        if (student.equals(Optional.empty())){
            throw new StudentNotFoundById(id);
        }

        //daca exista deja un curs cu acelasi nume in orarul lui
        if(student.get().getCourses().stream().filter(c -> c.getName().equals(courseName)).collect(Collectors.toList()).size() > 0){
            throw new AlreadyHasThisCourseException();
        }

        student.get().addCourse(course);
        studentRepo.save(student.get());
    }

    public void deleteCourseFromStudent(long studentId, String courseName){
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundById(studentId));

        Course course = courseRepo.getCourseByName(courseName)
                .orElseThrow(() -> new CourseNotFoundByName(courseName));

        List<Course> studentCourses = student.getCourses();
        System.out.println(studentCourses);
        if (studentCourses.stream().anyMatch(c -> c.getName().equals(courseName))){
            student.removeCourse(course);
            studentRepo.save(student);
        }
        else{
            throw new StudentNotHavingCourseException();
        }
    }

    //1) cursul cu cei mai multi studenti inscrisi la el
    //nu stiu cum sa o fac direct pe enrollment
    public Set<Course> mostPopularCourse(){

        Map<Course, Integer> map = new HashMap<>();
        List<Student> students = getAllStudents();
        for(Student s: students){

            List<Course> courses = s.getCourses();
            for(Course c : courses){

                if (map.containsKey(c)){

                    map.put(c, map.get(c) + 1);
                }
                else{
                    map.put(c, 1);
                }
            }
        }

        if (map.size() == 0){
            throw new NoEnrollToAnyCourseException();
        }

        HashSet<Course> hashSet = null;
        int maxValue = 0;
        for(Map.Entry<Course, Integer> entry : map.entrySet()){
            if(entry.getValue() > maxValue){
                maxValue = entry.getValue();
                hashSet = new HashSet<>();
                hashSet.add(entry.getKey());
            }
            else if(entry.getValue() == maxValue){
                hashSet.add(entry.getKey());
            }
        }
        System.out.println( "MAXIMUM VALUE : " + maxValue + '\n' + hashSet);
        return hashSet;
    }

    //2) cursul cu cei mai putini inscrisi la el
    //nu stiu cum sa o fac direct pe enrollment
    public Set<Course> mostUnpopularCourse(){

        Map<Course, Integer> map = new HashMap<>();
        List<Student> students = getAllStudents();
        for(Student s: students){

            List<Course> courses = s.getCourses();
            for(Course c : courses){

                if (map.containsKey(c)){

                    map.put(c, map.get(c) + 1);
                }
                else{
                    map.put(c, 1);
                }
            }
        }

        if (map.size() == 0){
            throw new NoEnrollToAnyCourseException();
        }

        HashSet<Course> hashSet = null;
        Course minCourse = null;
        int minValue = 99999;
        for(Map.Entry<Course, Integer> entry : map.entrySet()){
            if(entry.getValue() < minValue){
                minValue = entry.getValue();
                minCourse = entry.getKey();
                hashSet = new HashSet<>();
                hashSet.add(entry.getKey());
            }
            else if(entry.getValue() == minValue){
                hashSet.add(entry.getKey());
            }
        }
        System.out.println( "Minimum VALUE : " + minValue + '\n' + hashSet);
        return hashSet;
    }

    //3) studentul care are cele mai multe carti
    //cum fac direct pe jpql
    public Student studentWithTheMostBooks(){

        List<Student> students = getAllStudents();

        Student maxStudent = null;
        int maxBooks = -1;
        for(Student s : students){
            if (s.getBooks().size() > maxBooks){
                maxBooks = s.getBooks().size();
                maxStudent = s;
            }
        }

        if (maxBooks == -1){
            throw new NoStudentHasBooksException();
        }

        System.out.println("MAXBOOKS: " + maxBooks);
        return maxStudent;
    }

    //4) studentul cu cele mai putine carti
    //studentul cu cele mai putin carti dar mai mult decat una
    public Student studentWithTheFewestBooks(){

        List<Student> students = getAllStudents();

        Student minStudent = null;
        int minBooks = 999999999;
        for(Student s : students){
            if (s.getBooks().size() < minBooks && s.getBooks().size() > 0){
                minBooks = s.getBooks().size();
                minStudent = s;
            }
        }

        if (minStudent == null){
            throw new NoStudentHasBooksException();
        }

        System.out.println("MINBOOKS: " + minBooks);
        return minStudent;
    }

    //6) studentul care e inscris la cele mai multe cursuri
    public Student studentWithTheMostCourses(){

        List<Student> students = getAllStudents();

        Student maxStudent = null;
        int maxCourses = -1;
        for(Student s : students){
            if (s.getCourses().size() > maxCourses){
                maxCourses = s.getCourses().size();
                maxStudent = s;
            }
        }

        if (maxCourses == -1){
            throw new NoStudentHasCoursesException();
        }

        System.out.println("MAXCourses: " + maxCourses);
        System.out.println(maxStudent.getCourses());
        return maxStudent;
    }

    // 7)anul in care s-au inchiriat cele mai multe carti
    public Integer yearWithMostRentedBooks(){

        List<Book> books = getAllBooks();
        if(books.size() == 0){
            throw new NoBooksFoundException("there is no book rented by any student");
        }

        Map<Integer, Integer> map = new HashMap<>();
        for(Book b : books){
            if (map.containsKey(b.getCreatedAt().getYear())){
                map.put(b.getCreatedAt().getYear(), map.get(b.getCreatedAt().getYear()) + 1);
            }
            else{
                map.put(b.getCreatedAt().getYear(), 1);
            }
        }

        int maxYear = 0;
        int maxValue = 0;
        Iterator<Map.Entry<Integer, Integer>> itr = map.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<Integer, Integer> entry = itr.next();
            if(entry.getValue() > maxValue){
                maxYear = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxYear;
    }

    // 8)anul in care s-au inchiriat cele mai putine carti
    public Integer yearWithFewestRentedBooks(){

        List<Book> books = getAllBooks();
        if(books.size() == 0){

            throw new NoBooksFoundException("there is no book rented by any student");
        }

        Map<Integer, Integer> map = new HashMap<>();
        for(Book b : books){
            if (map.containsKey(b.getCreatedAt().getYear())){

                map.put(b.getCreatedAt().getYear(), map.get(b.getCreatedAt().getYear()) + 1);
            }
            else{
                map.put(b.getCreatedAt().getYear(), 1);
            }
        }

        int minYear = 9999;
        int minValue = 999999999;
        Iterator<Map.Entry<Integer, Integer>> itr = map.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry<Integer, Integer> entry = itr.next();
            if(entry.getValue() < minValue){
                minYear = entry.getKey();
                minValue = entry.getValue();
            }
        }

        System.out.println("MinYearValue:" + minValue);
        return minYear;
    }

    // 9)afisare in ordine alfabetica dupa nume a studentiilor inscrisi la un anumit curs (nume curs)
    public Set<Student> getStudentsEnrolledToCourse(String courseName){

        Set<Student> students = new TreeSet<>(Comparator.comparing(Student::getLastName));
        Course course = courseRepo.getCourseByName(courseName)
                .orElseThrow(() -> new CourseNotFoundByName(courseName));

        students.addAll(course.getStudents());
        return students;
    }

    // 10)cursurile fiecarui departament si numele lor
    public Map<String, List<String>> getDepartmentAndItsCourses(){

        Map<String, List<String>> map = new HashMap<>();
        List<Course> courses = courseRepo.findAll();
        if (courses.size() == 0){
            throw new NoCoursesFoundException();
        }

        for (Course course : courses){

            if (!map.containsKey(course.getDepartment())){
                map.put(course.getDepartment(), new ArrayList<>());
            }
            map.get(course.getDepartment()).add(course.getName());
        }
        return map;
    }

    // 11)care departament are cei mai multi studenti inscrisi
    public String getDepartmentWithMostEnrolledStudents(){

        HashMap<String, HashSet<Student>> departmentPairStudents = new HashMap<>();
        HashSet<Student> studentsSet;
        Map<String, List<String>> map = getDepartmentAndItsCourses();
        for(Map.Entry<String, List<String>> entry : map.entrySet()){

            List<String> courseNames = entry.getValue();
            for (String courseName: courseNames){

                studentsSet = new HashSet<>();
                Course course = courseRepo.getCourseByName(courseName).get();
                if (!departmentPairStudents.containsKey(course.getDepartment())){

                    for(Student s : course.getStudents()){
                        studentsSet.add(s);
                    }
                    departmentPairStudents.put(course.getDepartment(), studentsSet);
                }
                else{
                    for(Student s : course.getStudents()){
                        studentsSet.add(s);
                    }
                    HashSet<Student> hashSet = departmentPairStudents.get(course.getDepartment());
                    hashSet.addAll(studentsSet);
                    departmentPairStudents.put(course.getDepartment(), hashSet);
                }
            }
        }

        System.out.println(departmentPairStudents);
        String maxDepartment = "";
        int maxNumber = 0;
        for(Map.Entry<String, HashSet<Student>> entry : departmentPairStudents.entrySet()){

            if (entry.getValue().size() > maxNumber){
                maxDepartment = entry.getKey();
                maxNumber = entry.getValue().size();
            }
        }

        return maxDepartment + ": " + maxNumber + " students";
    }

    // 12)nr de studenti al unui departament (nume departament)
    public Integer getNumberStudentsOfAnDepartment(String department){

        List<Course> courses = courseRepo.getCoursesByDepartment(department)
                .orElseThrow(() -> new CourseNotFoundByDepartment(department));


        Set<Student> studentsSet = new HashSet<>();
        Map<String, List<String>> map = getDepartmentAndItsCourses();
        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            if (entry.getKey().equals(department)){

                List<String> coursesName = entry.getValue();
                for (String courseName: coursesName){

                    Course c = courseRepo.getCourseByName(courseName).get();
                    for(Student s : c.getStudents()){
                        studentsSet.add(s);
                    }
                }
                break;
            }
        }
        return studentsSet.size();
    }


    // 13)media de varsta pentru fiecare curs (nume curs)
    public Double getAverageAgeForCourse(String courseName){

        Optional<Course> course = courseRepo.getCourseByName(courseName);
        if (!course.isPresent()){
            throw new CourseNotFoundByName(courseName);
        }

        List<Student> students = course.get().getStudents();
        if (students.isEmpty()){
            throw new NoStudentsException("at this course didn't enrolled any student yet");
        }
        double averageAge = students
                .stream()
                .collect(averagingDouble(Student::getAge));

        return averageAge;
    }

    // 14)de cate ori imprumutat un student carti (email)
    public Integer getNumberBooksRentedByStudent(String email){

        Optional<Student> student = studentRepo.getStudentByEmail(email);
        if (!student.isPresent()){
            throw new StudentNotFoundByEmail(email);
        }

        int nr = student.get().getBooks().size();
        if (nr == 0){
            throw new StudentNotRentBookEception();
        }

        return nr;
    }

    // 15)data in care un anumit elev a imprumutat o carte(input: nume carte)
    public LocalDate getRentedBookDate(String bookName){
        Optional<Book> book = bookRepo.getBookByName(bookName);
        if (!book.isPresent()){
            throw new BookNotFoundByName(bookName);
        }

        return book.get().getCreatedAt();
    }

    // 16)de cate ori intr-un an a imprumutat un elev carti(input an, idStudent)
    public Integer getBookNumberRentedInAnYear(Integer year, long studentId){

        Optional<Student> student = studentRepo.findById(studentId);
        if (!student.isPresent()){
            throw new StudentNotFoundById(studentId);
        }

        List<Book> books = student.get().getBooks();
        int nr = (int) books.stream().filter(book -> book.getCreatedAt().getYear() == year).count();

        return nr;
    }

}