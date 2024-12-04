package com.mathbooth.controller;

import java.util.Scanner;

import com.mathbooth.config.Config;
import com.mathbooth.service.display.DisplayService;
import com.mathbooth.view.Display;

public class AppController {
    private Boolean roomConflictsOcurred;
    private Boolean curricularConflictsOcurred;

    public AppController(DisplayService displayService){
        this.roomConflictsOcurred = displayService.roomConflicts().booleanValue();
        this.curricularConflictsOcurred =displayService.curricularConflicts().booleanValue();
    }

    public static Boolean running = true;

        public void run() {

            if(!Config.validatedXML){
                running = false;
            }

            Scanner scanner = new Scanner(System.in);
            Display display = new Display();

            while (running) {
                clearConsole();
                display.displayTimetable();
                display.displayCurricula();

                menuSwitch(scanner);
            }

            running=false;
            System.out.println("Program closed");
            scanner.close();
        }

        private static void waitForAction(Scanner scanner, String action){
            System.out.println("Press Enter to " + action + "...");
            scanner.nextLine();
        }

        private static void clearConsole() {
            try{
                if(System.getProperty("os.name").contains("Windows")){
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

                }else{
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                }

            }catch(Exception e){
                System.out.println("Could not clear the console.");
            }
        }

        public void menuSwitch(Scanner scanner){
            Display display = new Display();
            Boolean menuLooping = true;

            if(roomConflictsOcurred || curricularConflictsOcurred){
            while(menuLooping){
            System.out.println("\nOptions:");
            System.out.println("1. Show Room Conflicts");
            System.out.println("2. Show Curricular Conflicts");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    clearConsole();
                    display.displayRoomConflicts();
                    waitForAction(scanner, "return");
                    menuLooping = false;
                    break;
                case "2":
                    clearConsole();
                    display.displayCurricularConflicts();
                    waitForAction(scanner, "return");
                    menuLooping = false;
                    break;
                case "3":
                    running = false;
                    System.out.println("Exiting...");
                    menuLooping = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    waitForAction(scanner, "continue");
                    menuLooping = false;
                    break;
            }
        }

        }else if(!roomConflictsOcurred && ! curricularConflictsOcurred){
            System.out.println("No scheduling conflicts in this table. You must be using MathLab :p");
            System.out.println("Press any key to exit");;
            scanner.nextLine();
            running = false;
            System.out.println("Exiting...");
        }
    }
}
