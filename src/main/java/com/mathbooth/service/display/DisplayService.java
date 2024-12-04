package com.mathbooth.service.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mathbooth.model.DTO.CurricularConflictDTO;
import com.mathbooth.model.DTO.RoomConflictDTO;
import com.mathbooth.repository.DisplayRepository;
import com.mathbooth.service.conflict.CurricularConflictService;
import com.mathbooth.service.conflict.RoomConflictService;

public class DisplayService {
    public Boolean neitherViolationFound = false;
    private final RoomConflictService roomConflictService;
    private final CurricularConflictService curricularConflictService;
    public DisplayService(RoomConflictService roomConflictService, CurricularConflictService curricularConflictService) {
        this.roomConflictService = roomConflictService;
        this.curricularConflictService = curricularConflictService;
    }

    public void printTimetable(){
        HashMap<Integer, ArrayList<String>> timetable = DisplayRepository.getTimetable();
        String tablePadding = String.format("\033[44m%-87s\033[0m", "");
        String[] formatting = {"%-16s", "%-24s", "%-12s", "%-11s", "%-12s", "%-12s"};

        System.out.println(tablePadding);
        System.out.printf("\033[37;104;1m%-15s %-23s %-11s %-10s %-11s %-17s" + "\n","      lecture", "name", "room", "weekday", "start", "end         " + "\033[0m");

        System.out.println(tablePadding);

        for(Map.Entry<Integer,ArrayList<String>> timetableRow: timetable.entrySet()){
            StringBuilder row = new StringBuilder("\033[30;47;1m");

            ArrayList<String> timetableEntries = timetableRow.getValue();
            row.append(String.format(formatting[0], "      " + timetableEntries.get(0)));

            for(int i = 1; i<timetableEntries.size(); i++){
                row.append(String.format(formatting[i], timetableEntries.get(i)));
            }

           row.append("\033[0m");
           System.out.println(row);
        }

        System.out.println(tablePadding);
        System.out.println("\n");
    }

    public void printCurricula(){
        HashMap<String, ArrayList<String>> curricula = DisplayRepository.getCurricula();
        String tablePadding = String.format("\033[106m%-49s\033[0m", "");

        System.out.println(String.format("\033[30;106m%-19s %-29s\033[0m", "", "Curricula"));

        for(Map.Entry<String,ArrayList<String>> curriculumRow: curricula.entrySet()){
            ArrayList<String> curriculumEntries = curriculumRow.getValue();
            String formattedKey = "\033[30;106m  \033[0m"  + String.format("\033[30;106m%-9s\033[0m", curriculumRow.getKey() + ": ");
            StringBuilder row = new StringBuilder(formattedKey);

            System.out.println(tablePadding);

            for(int i = 0; i<curriculumEntries.size(); i++){
                row.append(String.format("\033[30;47;1m%-5s", " " + curriculumEntries.get(i) + " \033[0m"));
                row.append("\033[106m \033[0m");
            }

            row.append("\033[106m  \033[0m");
            System.out.println(row);
            System.out.println(tablePadding);
        }

        System.out.println("\n");
    }

    public Boolean roomConflicts(){
        HashMap<Integer, RoomConflictDTO[]> roomConflictPairs = roomConflictService.findRoomConflict();
        Boolean violationOcurred = false;

        System.out.println(String.format("\033[44m%-14s %-29s\033[0m", "", "Room conflicts") + "\n");

        if(!roomConflictPairs.isEmpty()){
            violationOcurred = true;

            for(Map.Entry<Integer, RoomConflictDTO[]> roomConflictDTOs : roomConflictPairs.entrySet() ){
                RoomConflictDTO[] conflictPair = roomConflictDTOs.getValue();

                System.out.println("for:  " + conflictPair[0].getRoom() + " on   \033[38;5;206m(" + conflictPair[0].getWeekday() + ")\033[0m ");

                for(RoomConflictDTO conflict: conflictPair){

                    System.out.println(conflict.getLecture() + " [ " + conflict.getStartTime() + " - " + conflict.getEndTime() + " ] ");
                }

                System.out.println();
            }

        }else{

           noViolation(violationOcurred,"roomConflict");
        }

        return violationOcurred;
    }


    public Boolean curricularConflicts(){
        HashMap<Integer, CurricularConflictDTO[]> curricularConflictPairs = curricularConflictService.findCurricularConflict();
        Boolean violationOccured = false;

        System.out.println(String.format("\033[30;106m%-10s %-34s\033[0m", "", "Curricular conflicts") + "\n");

        if(!curricularConflictPairs.isEmpty()){
            violationOccured = true;

            for(Map.Entry<Integer, CurricularConflictDTO[]> curricularConflictDTOs: curricularConflictPairs.entrySet()){
                CurricularConflictDTO[] conflictPair = curricularConflictDTOs.getValue();

                System.out.print("Curricular conflict on:  ");
                System.out.println(" \033[38;5;206m(" + conflictPair[0].getWeekday() + ")\033[0m ");

                for(CurricularConflictDTO conflict: conflictPair){

                    System.out.print(conflict.getLecture() + " [ " + conflict.getStartTime() + " - " + conflict.getEndTime() + " ]");

                    for(int i= 0; i<conflict.getProgramList().size();i++){
                        System.out.print(" " + conflict.getProgramList().get(i) + " ");
                    }

                    System.out.print("\n");

                }

                System.out.println();
            }

        }else{

            noViolation(violationOccured,"curricularConflict");
        }

        return violationOccured;
    }


    private static void noViolation(Boolean violationOcurred, String conflictType){

        if(!violationOcurred && conflictType.equals("roomConflict")){

            System.out.println("No room conflicts found!");

        }else if(!violationOcurred && conflictType.equals("curricularConflict")){

            System.out.println("No curricular conflicts found!");

        }
    }
}
