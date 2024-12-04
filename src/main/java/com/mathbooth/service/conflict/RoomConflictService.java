package com.mathbooth.service.conflict;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import com.mathbooth.model.DTO.RoomConflictDTO;
import com.mathbooth.model.entity.Event;
import com.mathbooth.repository.util.RepoUtil;

public class RoomConflictService {
    private final RepoUtil repoUtil;

    public RoomConflictService(RepoUtil repoUtil){
        this.repoUtil = repoUtil;
    };

    //at any given time, there can only be one class/lecture per room
    public HashMap<Integer,RoomConflictDTO[]> findRoomConflict(){
        ArrayList<Event> events = repoUtil.Events().fetchAll();
        HashMap<Integer,RoomConflictDTO[]> conflictingRoomEntries = new HashMap<>();
        events.sort(Comparator.comparing(Event::getRoomId).thenComparing(Event::getWeekdayId));
        int roomConflictId = 0;

            for(int x = 0; x < events.size()-1; x++){
                Event eventA = events.get(x);

                for(int y = x+1; y < events.size(); y++){
                    Event eventB = events.get(y);

                    if(eventA.getRoomId()!= eventB.getRoomId() || eventA.getWeekdayId() != eventB.getWeekdayId()){
                        break;
                    }

                    if(conflictExists(eventA, eventB)){

                        RoomConflictDTO conflictA = createRoomConflictDTO(eventA, x);
                        RoomConflictDTO conflictB = createRoomConflictDTO(eventB, y);

                        conflictingRoomEntries.put(roomConflictId++, new RoomConflictDTO[]{conflictA, conflictB});
                    }
                }
            }

        return conflictingRoomEntries;
    }

    public Boolean conflictExists(Event a, Event b){

        return a.getStartTime().isBefore(b.getEndTime()) &&
               a.getEndTime().isAfter(b.getStartTime());
    }

    public RoomConflictDTO createRoomConflictDTO(Event event, int eventId){
        String lectureName = repoUtil.Lectures().getNameById(event.getLectureId());
        String roomName = repoUtil.Rooms().getNameById(event.getRoomId());
        String weekdayName = repoUtil.Weekdays().getNameById(event.getWeekdayId());
        LocalTime startTime = event.getStartTime();
        LocalTime endTime = event.getEndTime();

        return new RoomConflictDTO(lectureName, roomName, weekdayName, startTime, endTime);
    }
}
