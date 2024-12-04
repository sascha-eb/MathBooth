package com.mathbooth.service.conflict;

import java.util.ArrayList;
import java.util.HashMap;

import com.mathbooth.model.DTO.CurricularConflictDTO;
import com.mathbooth.model.entity.Event;
import com.mathbooth.model.entity.Program;
import com.mathbooth.repository.util.RepoUtil;

public class CurricularConflictService {
    private final RepoUtil repoUtil;

    public CurricularConflictService(RepoUtil repoUtil){
        this.repoUtil = repoUtil;
    }

     //lectures from the same program cannot be held at the same time
    public HashMap<Integer, CurricularConflictDTO[]> findCurricularConflict(){
        ArrayList<Event> events = repoUtil.Events().fetchAll();
        ArrayList<Program> programs = repoUtil.Programs().fetchAll();
        HashMap<Integer, CurricularConflictDTO[]> curricularConflicts = new HashMap<>();
        int curricularConflictId = 0;

        for(Program program: programs){
            ArrayList<Integer> groupedLectures = repoUtil.Curricula().getRelatedLectureIds(program.getProgramId());
            ArrayList<Event> flaggedEvents = flagOverlapPotential(events, groupedLectures);

            for(int x = 0; x < flaggedEvents.size()-1; x++){

                for(int y = x+1; y < flaggedEvents.size(); y++){
                    Event eventA = flaggedEvents.get(x);
                    Event eventB = flaggedEvents.get(y);

                    if(conflictExists(eventA, eventB)){
                        CurricularConflictDTO[] conflictPair = {createConflictDTO(eventA), createConflictDTO(eventB)};
                        curricularConflicts.put(curricularConflictId++, conflictPair);
                    }
                }
            }
        }

        return curricularConflicts;
    }

    public ArrayList<Event> flagOverlapPotential(ArrayList<Event> events, ArrayList<Integer> groupedLectureIds){
        ArrayList<Event> flaggedEvents = new ArrayList<>();

        for(Event event:events){

            if(groupedLectureIds.contains(event.getLectureId())){
                flaggedEvents.add(event);
            }
        }

        return flaggedEvents;
    }

    public Boolean conflictExists(Event a, Event b){

        return a.getWeekdayId() == b.getWeekdayId() &&
                a.getStartTime().isBefore(b.getEndTime())  &&
                a.getEndTime().isAfter(b.getStartTime());
    }

    public CurricularConflictDTO createConflictDTO(Event event){
        ArrayList<String> programList =  getRelatedProgramNames(event.getLectureId());
        String weekdayName = repoUtil.Weekdays().getNameById(event.getWeekdayId());
        String lectureName = repoUtil.Lectures().getNameById(event.getLectureId());

        return new CurricularConflictDTO(programList, weekdayName, lectureName, event.getStartTime(), event.getEndTime());
    }

    public ArrayList<String> getRelatedProgramNames(int lectureId){
        ArrayList<Integer> relatedProgramIds = repoUtil.Curricula().getRelatedProgramIds(lectureId);
        ArrayList<String> programList = new ArrayList<>();

        for(Integer id: relatedProgramIds){
            String lectureName = repoUtil.Programs().getNameById(id);
            programList.add(lectureName);
        }

        return programList;
    }
}
