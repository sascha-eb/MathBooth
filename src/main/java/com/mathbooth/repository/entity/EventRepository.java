package com.mathbooth.repository.entity;

import java.time.LocalTime;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Event;
import com.mathbooth.repository.util.RepoUtil;

public class EventRepository{
     
    private ArrayList<Event> cachedEvents = null;

    public ArrayList<Event> fetchAll(){
        if(cachedEvents == null){
            cachedEvents = loadEventsFromFile();
        }

        return cachedEvents;
    }

    private static ArrayList<Event> loadEventsFromFile(){
        RepoUtil repoUtil = new RepoUtil();
        ArrayList<Event> events = new ArrayList<>();
        int eventId = 0;

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode lecturesNode = rootNode.path("lectures").path("lecture");

            for(JsonNode lectureNode: lecturesNode){
                JsonNode bookingsNode = lectureNode.path("roombookings").path("booking");
                String lectureName = lectureNode.path("id").asText();
                int lectureId = repoUtil.Lectures().getIdByName(lectureName);

                if(bookingsNode.isArray()){

                    for(JsonNode bookingNode: bookingsNode){
                        String room = bookingNode.path("room").asText();
                        int roomId = repoUtil.Rooms().getIdByName(room);
                        String weekday = bookingNode.path("weekday").asText();
                        int weekdayId = repoUtil.Weekdays().getIdByName(weekday);
                        String startTimeString = bookingNode.path("startTime").asText();
                        String endTimeString = bookingNode.path("endTime").asText();
                        LocalTime startTime = LocalTime.parse(startTimeString);
                        LocalTime endTime = LocalTime.parse(endTimeString);

                        events.add(new Event(eventId++, lectureId, roomId, weekdayId, startTime, endTime));
                    }

                }else{
                    String room = bookingsNode.path("room").asText();
                    int roomId = repoUtil.Rooms().getIdByName(room);
                    String weekday = bookingsNode.path("weekday").asText();
                    int weekdayId = repoUtil.Weekdays().getIdByName(weekday);
                    String startTimeString = bookingsNode.path("startTime").asText();
                    String endTimeString = bookingsNode.path("endTime").asText();
                    LocalTime startTime = LocalTime.parse(startTimeString);
                    LocalTime endTime = LocalTime.parse(endTimeString);

                    events.add(new Event(eventId++, lectureId, roomId, weekdayId, startTime, endTime));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't get entries. Error: " + e);
        }

        return events;
    }

    public String getValueForField(int eventId, String eventField){
        RepoUtil repoUtil = new RepoUtil();
        ArrayList<Event> events = repoUtil.Events().fetchAll();

        switch(eventField){
            case "lecture":
                return repoUtil.Lectures().getNameById(events.get(eventId).getLectureId());
            case "room":
                return repoUtil.Rooms().getNameById(events.get(eventId).getRoomId());
            case "weekday":
                return repoUtil.Weekdays().getNameById(events.get(eventId).getWeekdayId());
            case "startTime":
                return String.valueOf(events.get(eventId).getStartTime().getHour());
            case "endTime":
                return String.valueOf(events.get(eventId).getEndTime().getHour());
            default:
                return "Event field could not be found. Event fields are: \"lecture\", \"room\", \"weekday\", \"startTime\", \"endTime\"";

        }
    }
}
