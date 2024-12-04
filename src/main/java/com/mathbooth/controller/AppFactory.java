package com.mathbooth.controller;

import com.mathbooth.service.ServiceFactory;
import com.mathbooth.service.display.DisplayService;

public class AppFactory {
     public static AppController createAppController(){
        DisplayService displayService = ServiceFactory.createDisplayService();
        return new AppController(displayService);
    }
}
