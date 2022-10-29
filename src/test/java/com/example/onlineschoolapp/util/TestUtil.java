package com.example.onlineschoolapp.util;

import com.example.onlineschoolapp.models.CustomInstantSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * MediaType for Json UTF8
 */
public class TestUtil {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            StandardCharsets.UTF_8
    );

    /**
     * Convert an onject to Json byte array.
     *
     * @param object the object to convert
     * @return the Json byte array
     * @throws IOException if something goes wrong
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Instant.class, new CustomInstantSerializer());

        mapper.registerModule(simpleModule);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);

        return mapper.writeValueAsBytes(object);
    }

    /**
     * Create a byte array with a specific size filled with specified data.
     *
     * @param size the size of the byte array
     * @param data the data to put int the byte array
     * @return the JSON byte array
     */
    public static byte[] createByteArray(int size, String data){
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++){
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    /**
     * A matcher that tests that the examined string represents the same instant as the reference datetime
     */
    public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String>{
        private final ZonedDateTime date;

        public ZonedDateTimeMatcher(ZonedDateTime date){
            this.date = date;
        }

        @Override
        protected boolean matchesSafely(String item, Description mismatchDescription) {
            try {
                if (!date.isEqual(ZonedDateTime.parse(item))) {
                    mismatchDescription.appendText("was ").appendValue(item);
                    return false;
                }
                return true;
            } catch (DateTimeParseException e) {
                mismatchDescription.appendText("was ").appendValue(item)
                        .appendText(", which could not be parsed as ZoneDateTime");
                return false;
            }
        }

        @Override
        public void describeTo(Description description) {}
    }

    /**
     *
     */

    /**
     * Creates a matcher that matches when the examined string represents the same instant as the reference date
     *
     * @param date the reference datetime against which the examined string is checked
     */
    public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date){
        return new ZonedDateTimeMatcher(date);
    }

    /**
     * Verifies the equals /hashcode contract on the domain object.
     */
    @SuppressWarnings("unchecked")
    public static void equalsVerifier(Class clazz) throws Exception{
        Object domainObject1 = clazz.getConstructor().newInstance();
        assertThat(domainObject1.toString()).isNotNull();
        assertThat(domainObject1).isEqualTo(domainObject1);
        assertThat(domainObject1).hasSameHashCodeAs(domainObject1);
        //Test with an instance of another class
        Object testOtherObject = new Object();
        assertThat(domainObject1).isNotEqualTo(testOtherObject);
        assertThat(domainObject1).isNotEqualTo(null);
        //Test with an instance of the same class
        Object domainObject2 = clazz.getConstructor().newInstance();
        assertThat(domainObject1).isNotEqualTo(domainObject2);
        //HashCodes are equals because the objects are not persisted yet
        assertThat(domainObject1).hasSameHashCodeAs(domainObject2);
    }

    /**
     * Create a FormattingConversionService which use ISO date format, instead of the localized one.
     *
     * @return the FormattingConversionService
     */
    public static FormattingConversionService createFormattingConversionService(){
        DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(dfcs);
        return dfcs;
    }
}
