package com.mathbooth.repository;

import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;

public class DisplayRepository {
    public static HashMap<Integer, ArrayList<String>> getTimetable(){
        HashMap<Integer, ArrayList<String>> tableEntries = new HashMap<>();

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode lecturesNode = rootNode.path("lectures").path("lecture");
            int rowId = 0;

            for(JsonNode lectureNode: lecturesNode){
                JsonNode bookingsNode = lectureNode.path("roombookings").path("booking");

                if(bookingsNode.isArray()){

                    for(JsonNode bookingNode: bookingsNode){
                        ArrayList<String> tableRow = new ArrayList<>();
                        tableRow.add(lectureNode.path("id").asText());
                        tableRow.add(lectureNode.path("name").asText());
                        tableRow.add(bookingNode.path("room").asText());
                        tableRow.add(bookingNode.path("weekday").asText());
                        tableRow.add(bookingNode.path("startTime").asText());
                        tableRow.add(bookingNode.path("endTime").asText());
                        tableEntries.put(rowId++, tableRow);
                    }

                }else{
                    ArrayList<String> tableRow = new ArrayList<>();
                    tableRow.add(lectureNode.path("id").asText());
                    tableRow.add(lectureNode.path("name").asText());
                    tableRow.add(bookingsNode.path("room").asText());
                    tableRow.add(bookingsNode.path("weekday").asText());
                    tableRow.add(bookingsNode.path("startTime").asText());
                    tableRow.add(bookingsNode.path("endTime").asText());
                    tableEntries.put(rowId++, tableRow);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return tableEntries;
    }

    public static HashMap<String, ArrayList<String>> getCurricula(){
        HashMap<String, ArrayList<String>> tableEntries = new HashMap<>();

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode curriculaNode = rootNode.path("curricula").path("curriculum");

            for(JsonNode curriculumNode: curriculaNode){
                ArrayList<String> tableRow = new ArrayList<>();

                for (JsonNode lectureNode : curriculumNode.path("lecture")) {
                    tableRow.add(lectureNode.asText());
                }

                tableEntries.put(curriculumNode.path("name").asText(), tableRow);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        return tableEntries;
    }
}
