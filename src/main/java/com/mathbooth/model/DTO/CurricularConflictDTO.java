package com.mathbooth.model.DTO;

import java.time.LocalTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CurricularConflictDTO {
    private ArrayList<String> programList;
    private String weekday;
    private String lecture;
    private LocalTime startTime;
    private LocalTime endTime;
}
