package com.example.onlineschoolapp.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * The deserializer accepts 4 patterns: "yyyy-MM-dd HH:mm:ss" , "yyyy-MM-dd" , "yyyy-MM-dd'T'HH:mm:ss" , and standard ISO_INSTANT, 'yyyy-MM-ddTHH:mm:ssZ'
 */
@Component
public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    private DateTimeFormatter fmtIsoDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter fmtIsoDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        Instant result = null;
        try{
            result = Instant.parse(p.getText());
        }catch (DateTimeException e){
            try{
                result = Instant.from(fmt.parse(p.getText()));
            }catch (DateTimeParseException ex){
                try{
                    result = LocalDate.parse(p.getText(), fmtIsoDate).atStartOfDay().toInstant(ZoneOffset.UTC);
                }catch (DateTimeParseException ex2){
                    result = LocalDateTime.parse(p.getText(), fmtIsoDateTime).toInstant(ZoneOffset.UTC);
                }
            }
        }
        return result;
    }
}
