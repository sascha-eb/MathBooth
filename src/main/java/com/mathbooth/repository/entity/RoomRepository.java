package com.mathbooth.repository.entity;

import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Room;
import com.mathbooth.repository.util.RepoUtil;
import com.mathbooth.repository.util.RepositoryInterface;

public class RoomRepository implements RepositoryInterface<Room> {
    private RepoUtil repoUtil = new RepoUtil();
    private ArrayList<Room> cachedRooms = null;

    @Override
    public ArrayList<Room> fetchAll(){
        if(cachedRooms == null){
            cachedRooms = loadRoomsFromFile();
        }

        return cachedRooms;
    }

    private static ArrayList<Room> loadRoomsFromFile(){
        ArrayList<Room> rooms = new ArrayList<>();
        HashSet<String> addedRooms = new HashSet<>();
        int roomIndex  = 0;

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode lecturesNode = rootNode.path("lectures").path("lecture");

            for(JsonNode lectureNode: lecturesNode){
                JsonNode bookingsNode = lectureNode.path("roombookings").path("booking");

                for(JsonNode bookingNode: bookingsNode){
                    String roomName = bookingNode.path("room").asText();

                    if(!roomName.isEmpty() && !addedRooms.contains(roomName)){
                        addedRooms.add(roomName);
                        rooms.add(new Room(roomIndex++, roomName));
                    }
                }
            }

        }catch (Exception e){
            System.out.println("Couldn't read entries. Error: " + e);
        }

        return rooms;
    }

    public int getIdByName(String roomName){
        ArrayList<Room> allRooms = repoUtil.Rooms().fetchAll();
        int roomId = -1;

        for(Room room: allRooms){
            if(room.getRoomName().equals(roomName)){
                roomId = room.getRoomId();
            }
        }

        return roomId;
    }

    @Override
    public String getNameById(int roomId){
        ArrayList<Room> allRooms = repoUtil.Rooms().fetchAll();
        String roomName = "No Room found";

        for(Room room: allRooms){
            if(room.getRoomId() == roomId){
                roomName = room.getRoomName();
            }
        }

        return roomName;
    }

    @Override
    public Boolean existsById(int roomId){
        ArrayList<Room> allRooms = repoUtil.Rooms().fetchAll();
        Boolean existsById = false;

        for(Room room: allRooms){
            if(room.getRoomId() == roomId){
                existsById = true;
            }
        }

        return existsById;
    }
}
