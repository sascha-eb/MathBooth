package com.mathbooth.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Config {
    public static final String TIMETABLE_FILE_PATH;
    public static Boolean validatedXML = true;

    static {
        try{
            XMLValidator.validate("timetable.xml", "timetable.xsd");
        }catch(Exception e){
            validatedXML = false;
            System.out.println("XML could not be validated " + e);
        }

        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("timetable.xml")) {

            if (inputStream == null) {
                throw new FileNotFoundException("timetable.xml not found");
            }

            TIMETABLE_FILE_PATH = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Error reading timetable.xml", e);
        }
    }

    public static Boolean xmlWasValidated(){
        return validatedXML;
    }

}
