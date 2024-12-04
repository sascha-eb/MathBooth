package com.mathbooth.repository.util;

import com.mathbooth.repository.entity.CurriculumRepository;
import com.mathbooth.repository.entity.EventRepository;
import com.mathbooth.repository.entity.LectureRepository;
import com.mathbooth.repository.entity.ProgramRepository;
import com.mathbooth.repository.entity.RoomRepository;
import com.mathbooth.repository.entity.WeekdayRepository;

public class RepoUtil {

    public CurriculumRepository Curricula(){
        return new CurriculumRepository();
    }

    public EventRepository Events(){
        return new EventRepository();
    }

    public LectureRepository Lectures(){
        return new LectureRepository();
    }

    public ProgramRepository Programs(){
        return new ProgramRepository();
    }

    public RoomRepository Rooms(){
        return new RoomRepository();
    }

    public WeekdayRepository Weekdays(){
        return new WeekdayRepository();
    }
}
