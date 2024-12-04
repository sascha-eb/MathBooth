package com.mathbooth.repository.entity;

import java.util.ArrayList;
import java.util.HashSet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.mathbooth.config.Config;
import com.mathbooth.model.entity.Program;
import com.mathbooth.repository.util.RepoUtil;
import com.mathbooth.repository.util.RepositoryInterface;

public class ProgramRepository implements RepositoryInterface<Program> {
    private RepoUtil repoUtil = new RepoUtil();
    private ArrayList<Program> cachedPrograms = null;

    @Override
    public ArrayList<Program> fetchAll(){
        if(cachedPrograms == null){
            cachedPrograms = loadProgramsFromFile();
        }

        return cachedPrograms;
    }

    private static ArrayList<Program> loadProgramsFromFile(){
        ArrayList<Program> programs = new ArrayList<>();
        HashSet<String> addedprograms = new HashSet<>();
        int programId = 0;

        try{
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode rootNode = xmlMapper.readTree(Config.TIMETABLE_FILE_PATH);
            JsonNode curriculaNode = rootNode.path("curricula").path("curriculum");

            for(JsonNode curriculumNode: curriculaNode){
                String programName= curriculumNode.path("name").asText();

                if(!programName.isEmpty() && !addedprograms.contains(programName)){
                    addedprograms.add(programName);
                    programs.add(new Program(programId++, programName));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Couldn't read entries. Error: " + e);
        }

        return programs;
    }


    public int getIdByName(String programName){
        ArrayList<Program> allPrograms = repoUtil.Programs().fetchAll();
        int programId = -1;
        for(Program program: allPrograms){

            if(program.getProgramName().equals(programName)){
                programId = program.getProgramId();
            }
        }

        return programId;
    }

    @Override
    public String getNameById(int programId){
        ArrayList<Program> allPrograms = repoUtil.Programs().fetchAll();
        String programName = "";
        for(Program program: allPrograms){
            if(program.getProgramId() == programId){
                programName = program.getProgramName();
            }
        }

        return programName;
    }

    @Override
    public Boolean existsById(int programId){
        ArrayList<Program> allPrograms = repoUtil.Programs().fetchAll();
        Boolean existsById = false;

        for(Program program: allPrograms){
            if(program.getProgramId() == programId){
                existsById = true;
            }
        }

        return existsById;
    }
}
