package com.mathbooth.repository.entity;

import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Lecture;
import com.mathbooth.repository.util.RepoUtil;
import com.mathbooth.repository.util.RepositoryInterface;

public class LectureRepository implements RepositoryInterface<Lecture> {
    private RepoUtil repoUtil = new RepoUtil();
    private ArrayList<Lecture> cachedLectures = null;

    @Override
    public ArrayList<Lecture> fetchAll(){
        if(cachedLectures == null){
            cachedLectures = loadLecturesFromFile();
        }

        return cachedLectures;
    }

    private static ArrayList<Lecture> loadLecturesFromFile(){
        ArrayList<Lecture> lectures = new ArrayList<>();
        HashSet<String> addedLectures = new HashSet<>();
        int lectureId = 0;

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode curriculaNode = rootNode.path("curricula").path("curriculum");

            for(JsonNode curriculumNode: curriculaNode){
                JsonNode lecturesNode = curriculumNode.path("lecture");

                for(JsonNode lectureNode: lecturesNode){
                    String lectureName = lectureNode.asText();
                    String lectureFullName = lectureNode.path("name").asText();

                    if(!lectureName.isEmpty() && !addedLectures.contains(lectureName)){
                        addedLectures.add(lectureName);
                        lectures.add(new Lecture(lectureId, lectureName, lectureFullName));
                        lectureId++;
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't get entries. Error: " + e);
        }

        return lectures;
    }

    public int getIdByName(String lectureName){
        ArrayList<Lecture> allLectures = repoUtil.Lectures().fetchAll();
        int lectureId = -1;

        for(Lecture lecture: allLectures){
            if(lecture.getLectureName().equals(lectureName)){
                lectureId = lecture.getLectureId();
            }
        }

        return lectureId;
    }

    @Override
    public String getNameById(int lectureId){
        ArrayList<Lecture> allLectures = repoUtil.Lectures().fetchAll();
        String lectureName = "No Lecture for that ID";

        for(Lecture lecture: allLectures){
            if(lecture.getLectureId() == lectureId){
                lectureName = lecture.getLectureName();
            }

        }
        return lectureName;
    }

    @Override
    public Boolean existsById(int lectureId){
        ArrayList<Lecture> allLectures = repoUtil.Lectures().fetchAll();
        Boolean existsById = false;

        for(Lecture lecture: allLectures){
            if(lecture.getLectureId() == lectureId){
                existsById = true;
            }
        }

        return existsById;
    }
}
