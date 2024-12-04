package com.mathbooth.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Lecture {
    private int lectureId;
    private String lectureName; //this is the XML files lecture ID
    private String lectureFullName; //this is the XML name tag for lectures
}
