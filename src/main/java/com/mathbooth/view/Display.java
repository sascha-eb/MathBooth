package com.mathbooth.view;

import com.mathbooth.service.ServiceFactory;
import com.mathbooth.service.display.DisplayService;

public class Display {
    private DisplayService displayService;

    public Display(){
        this.displayService = ServiceFactory.createDisplayService();
    }

    public void displayTimetable(){
        displayService.printTimetable();
    }

    public void displayCurricula(){
        displayService.printCurricula();
    }

    public void displayRoomConflicts(){
        displayService.roomConflicts();
    }

    public void displayCurricularConflicts(){
        displayService.curricularConflicts();
    }
}
