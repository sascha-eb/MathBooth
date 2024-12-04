package com.mathbooth.repository.entity;

import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Weekday;
import com.mathbooth.repository.util.RepoUtil;
import com.mathbooth.repository.util.RepositoryInterface;

public class WeekdayRepository implements RepositoryInterface<Weekday>{
    private RepoUtil repoUtil = new RepoUtil();
    private ArrayList<Weekday> cachedWeekdays = null;

    @Override
    public ArrayList<Weekday> fetchAll(){
        if(cachedWeekdays == null){
            cachedWeekdays = loadWeekdaysFromFile();
        }

        return cachedWeekdays;
    }

    private static ArrayList<Weekday> loadWeekdaysFromFile(){
        ArrayList<Weekday> weekdays = new ArrayList<>();
        HashSet<String> addedWeekdays = new HashSet<>();

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode lecturesNode = rootNode.path("lectures").path("lecture");
            int weekdayID = 0;

            for(JsonNode lectureNode: lecturesNode){
                JsonNode bookingsNode = lectureNode.path("roombookings").path("booking");
                for(JsonNode bookingNode: bookingsNode){
                    String weekday = bookingNode.path("weekday").asText();

                    if(!weekday.isEmpty() && !addedWeekdays.contains(weekday)){
                        addedWeekdays.add(weekday);
                        weekdays.add(new Weekday(weekdayID, weekday));
                        weekdayID++;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't get weekdays. Error: " + e);
        }

        return weekdays;
    }

    public int getIdByName(String weekdayName){
        ArrayList<Weekday> allWeekdays = repoUtil.Weekdays().fetchAll();
        int weekdayID = -1;

        for(Weekday weekday: allWeekdays){
            if(weekday.getWeekdayName().equals(weekdayName)){
                weekdayID = weekday.getWeekdayId();
            }
        }

        return weekdayID;
    }

    @Override
    public String getNameById(int weekdayId){
        ArrayList<Weekday> allWeekdays = repoUtil.Weekdays().fetchAll();
        String weekdayName = "No weekday for that ID";

        for(Weekday weekday: allWeekdays){
            if(weekday.getWeekdayId() == weekdayId){
                weekdayName = weekday.getWeekdayName();
            }

        }

        return weekdayName;
    }

    @Override
    public Boolean existsById(int weekdayId){
        ArrayList<Weekday> allWeekdays = repoUtil.Weekdays().fetchAll();
        Boolean existsById = false;

        for(Weekday weekday: allWeekdays){
            if(weekday.getWeekdayId() == weekdayId){
                existsById = true;
            }
        }

        return existsById;
    }
}
