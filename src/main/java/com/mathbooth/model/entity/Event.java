package com.mathbooth.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Event {
    private int eventId;
    private int lectureId;
    private int roomId;
    private int weekdayId;
    private LocalTime startTime;
    private LocalTime endTime;
}
