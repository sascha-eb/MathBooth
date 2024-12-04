package com.mathbooth;

import com.mathbooth.controller.AppController;
import com.mathbooth.controller.AppFactory;

public class App
{
    public static void main( String[] args )
    {
        AppController appController = AppFactory.createAppController();

        appController.run();
    }
}
