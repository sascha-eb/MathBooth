package com.mathbooth.service.conflict;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mathbooth.model.DTO.CurricularConflictDTO;
import com.mathbooth.model.entity.Event;
import com.mathbooth.model.entity.Program;
import com.mathbooth.repository.entity.CurriculumRepository;
import com.mathbooth.repository.entity.EventRepository;
import com.mathbooth.repository.entity.LectureRepository;
import com.mathbooth.repository.entity.ProgramRepository;
import com.mathbooth.repository.entity.WeekdayRepository;
import com.mathbooth.repository.util.RepoUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CurricularConflictServiceTest {
    private RepoUtil repoUtilMock;
    private EventRepository eventRepositoryMock;
    private ProgramRepository programRepositoryMock;
    private CurriculumRepository curriculumRepositoryMock;
    private CurricularConflictService curricularConflictService;
    private WeekdayRepository weekdayRepositoryMock;
    private LectureRepository lectureRepositoryMock;

    @BeforeEach
    void setUp(){
        repoUtilMock = Mockito.mock(RepoUtil.class);
        eventRepositoryMock = Mockito.mock(EventRepository.class);
        programRepositoryMock = Mockito.mock(ProgramRepository.class);
        curriculumRepositoryMock = Mockito.mock(CurriculumRepository.class);
        weekdayRepositoryMock = Mockito.mock(WeekdayRepository.class);
        lectureRepositoryMock = Mockito.mock(LectureRepository.class);

        when(repoUtilMock.Events()).thenReturn(eventRepositoryMock);
        when(repoUtilMock.Programs()).thenReturn(programRepositoryMock);
        when(repoUtilMock.Curricula()).thenReturn(curriculumRepositoryMock);
        when(repoUtilMock.Weekdays()).thenReturn(weekdayRepositoryMock);
        when(repoUtilMock.Lectures()).thenReturn(lectureRepositoryMock);

        curricularConflictService = new CurricularConflictService(repoUtilMock);
    }

    @Test
    void findCurricularConflict_noEvents_returnEmptyMap(){
        when(eventRepositoryMock.fetchAll()).thenReturn(new ArrayList<>());

        HashMap<Integer, CurricularConflictDTO[]> result = curricularConflictService.findCurricularConflict();

        assertEquals(0, result.size(), "Expected no curricular conflicts when there are no events");
    }

     @Test
    void testFindCurricularConflict_noConflicts_returnsNoConflicts() {
        ArrayList<Program> programs = new ArrayList<>();
        programs.add(new Program(1, "Program A"));
        programs.add(new Program(2, "Program B"));
        when(repoUtilMock.Programs().fetchAll()).thenReturn(programs);

        ArrayList<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(10, 30), LocalTime.of(12, 0)));
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(13, 35), LocalTime.of(14, 0)));
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(15, 0), LocalTime.of(15, 32)));
        when(repoUtilMock.Events().fetchAll()).thenReturn(mockEvents);

        when(repoUtilMock.Curricula().getRelatedLectureIds(1)).thenReturn(new ArrayList<>(List.of(1, 2)));
        when(repoUtilMock.Weekdays().getNameById(1)).thenReturn("Wed");
        when(repoUtilMock.Lectures().getNameById(1)).thenReturn("l-0");

        HashMap<Integer, CurricularConflictDTO[]> result = curricularConflictService.findCurricularConflict();

        assertTrue(result.isEmpty(), "Expected no conflicts between non-conflicting entries.");
    }

     @Test
    void testFindCurricularConflict_multipleConflicts_returnCorrectCount() {
        ArrayList<Program> programs = new ArrayList<>();
        programs.add(new Program(1, "Program A"));
        programs.add(new Program(2, "Program B"));
        when(repoUtilMock.Programs().fetchAll()).thenReturn(programs);

        ArrayList<Event> mockEvents = new ArrayList<>();
        mockEvents.add(new Event(1, 0, 1, 1, LocalTime.of(10, 00), LocalTime.of(12, 0)));
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(10, 35), LocalTime.of(11, 0)));
        mockEvents.add(new Event(4, 7, 5, 3, LocalTime.of(15, 0), LocalTime.of(17, 32)));
        when(repoUtilMock.Events().fetchAll()).thenReturn(mockEvents);

        when(repoUtilMock.Curricula().getRelatedLectureIds(1)).thenReturn(new ArrayList<>(List.of(0, 1)));
        when(repoUtilMock.Curricula().getRelatedLectureIds(2)).thenReturn(new ArrayList<>(List.of(0,1)));

        HashMap<Integer, CurricularConflictDTO[]> result = curricularConflictService.findCurricularConflict();

        assertEquals(2, result.size(), "There should be 2 conflict.");
        //should return a conflict for every program the conflict affects
    }
}

