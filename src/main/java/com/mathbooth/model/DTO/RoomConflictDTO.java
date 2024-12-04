package com.mathbooth.model.DTO;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RoomConflictDTO {
    private String lecture;
    private String room;
    private String weekday;
    private LocalTime startTime;
    private LocalTime endTime;
}
