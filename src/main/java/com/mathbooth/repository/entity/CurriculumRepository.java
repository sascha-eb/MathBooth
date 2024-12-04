package com.mathbooth.repository.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Curriculum;
import com.mathbooth.repository.util.RepoUtil;

public class CurriculumRepository{
    private static RepoUtil repoUtil = new RepoUtil();
    private ArrayList<Curriculum> cachedCurricula = null;

    public ArrayList<Curriculum> fetchAll(){
        if(cachedCurricula == null){
            cachedCurricula = loadCurriculaFromFile();
        }

        return cachedCurricula;
    }

    private static ArrayList<Curriculum> loadCurriculaFromFile(){
        ArrayList<Curriculum> curricula = new ArrayList<>();

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode curriculaNode = rootNode.path("curricula").path("curriculum");

            for(JsonNode curriculumNode: curriculaNode){
                String programName = curriculumNode.path("name").asText();
                int programID = repoUtil.Programs().getIdByName(programName);

                JsonNode lecturesNode = curriculumNode.path("lecture");

                for(JsonNode lectureNode: lecturesNode){
                    String lectureName = lectureNode.asText();
                    int lectureID = repoUtil.Lectures().getIdByName(lectureName);
                    curricula.add(new Curriculum(programID, lectureID));

                }
            }

        }catch (Exception e){
            System.out.println("Couldn't get entries. Error: " + e);
        }

        return curricula;
    }


    public ArrayList<String> getRelatedLectureNames(String programName){
        ArrayList<Curriculum> curricula = repoUtil.Curricula().fetchAll();
        ArrayList<String> relatedLectures = new ArrayList<>();

        int ccmProgramId= repoUtil.Programs().getIdByName(programName);

        System.out.println("Lectures in " + programName + ": ");

        for(Curriculum curriculum: curricula){
            if(curriculum.getProgramId() == ccmProgramId){
                int lectureId = curriculum.getLectureId();
                String relatedLectureName = repoUtil.Lectures().fetchAll().get(lectureId).getLectureName();
                relatedLectures.add(relatedLectureName);
            }
        }

        if(relatedLectures.isEmpty()){
            System.out.println("The program " + programName +  " couldn't be found.");
        }

        return relatedLectures;
    }


    public ArrayList<Integer> getRelatedLectureIds(int programId){
        ArrayList<Curriculum> curricula = repoUtil.Curricula().fetchAll();
        ArrayList<Integer> relatedLectureIds = new ArrayList<>();

        if(repoUtil.Programs().fetchAll().size() <= programId){
            System.out.println("The program ID " + programId + " couldn't be found");
            return relatedLectureIds;
        }

        int ccmProgramId= repoUtil.Programs().fetchAll().get(programId).getProgramId();

        for(Curriculum curriculum: curricula){
            if(curriculum.getProgramId() == ccmProgramId){
                int lectureId = curriculum.getLectureId();
                int relatedLectureId = repoUtil.Lectures().fetchAll().get(lectureId).getLectureId();
                relatedLectureIds.add(relatedLectureId);
            }
        }

        return relatedLectureIds;
    }


    public ArrayList<String> getRelatedProgramNames(String lectureName){
        ArrayList<Curriculum> curricula =  repoUtil.Curricula().fetchAll();
        ArrayList<String> relatedPrograms = new ArrayList<>();

        int ccmLectureId = repoUtil.Lectures().getIdByName(lectureName);

        for(Curriculum curriculum: curricula){
            if(curriculum.getLectureId() == ccmLectureId){
                int programId = curriculum.getProgramId();
                String relatedProgramName = repoUtil.Programs().fetchAll().get(programId).getProgramName();
                relatedPrograms.add(relatedProgramName);

            }
        }

        if(relatedPrograms.isEmpty()){
            System.out.println("The lecture " + lectureName +  " couldn't be found.");
        }

        return relatedPrograms;
    }

    public ArrayList<Integer> getRelatedProgramIds(int lectureId){
        ArrayList<Curriculum> curricula = repoUtil.Curricula().fetchAll();
        ArrayList<Integer> relatedProgramIds = new ArrayList<>();

        if(repoUtil.Lectures().fetchAll().size() <= lectureId){
            System.out.println("The lecture ID " + lectureId + " couldn't be found");
            return relatedProgramIds;
        }

        int ccmLectureId = repoUtil.Lectures().fetchAll().get(lectureId).getLectureId();

        for(Curriculum curriculum: curricula){
            if(curriculum.getLectureId() == ccmLectureId){
                int programId = curriculum.getProgramId();
                int relatedProgramId = repoUtil.Programs().fetchAll().get(programId).getProgramId();
                relatedProgramIds.add(relatedProgramId);
            }
        }

        return relatedProgramIds;
    }
}
