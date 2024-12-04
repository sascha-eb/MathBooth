package com.mathbooth.service;

import com.mathbooth.repository.util.RepoUtil;
import com.mathbooth.service.conflict.CurricularConflictService;
import com.mathbooth.service.conflict.RoomConflictService;
import com.mathbooth.service.display.DisplayService;

public class ServiceFactory {
    private static final RepoUtil repoUtil = new RepoUtil();
    private static DisplayService displayService;
    private static RoomConflictService roomConflictService;
    private static CurricularConflictService curricularConflictService;

    public static RoomConflictService createRoomConflictService(){
        if(roomConflictService == null){
            roomConflictService = new RoomConflictService(repoUtil);
        }
        return roomConflictService;
    }

    public static CurricularConflictService createCurricularConflictService(){
        if(curricularConflictService == null){
            curricularConflictService = new CurricularConflictService(repoUtil);
        }
        return curricularConflictService;
    }

     public static DisplayService createDisplayService() {
        if (displayService == null) {
            displayService = new DisplayService(createRoomConflictService(), createCurricularConflictService());
        }
        return displayService;
    }

}
