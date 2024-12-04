package com.mathbooth.service.conflict;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.mathbooth.model.DTO.RoomConflictDTO;
import com.mathbooth.model.entity.Event;
import com.mathbooth.repository.entity.EventRepository;
import com.mathbooth.repository.entity.LectureRepository;
import com.mathbooth.repository.entity.RoomRepository;
import com.mathbooth.repository.entity.WeekdayRepository;
import com.mathbooth.repository.util.RepoUtil;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoomConflictServiceTest {
    private RepoUtil repoUtilMock;
    private EventRepository eventRepositoryMock;
    private RoomConflictService roomConflictService;
    private LectureRepository lectureRepositoryMock;
    private RoomRepository roomRepositoryMock;
    private WeekdayRepository weekdayRepositoryMock;

    @BeforeEach
    void setUp() {
        repoUtilMock = Mockito.mock(RepoUtil.class);
        eventRepositoryMock = Mockito.mock(EventRepository.class);
        lectureRepositoryMock = Mockito.mock(LectureRepository.class);
        weekdayRepositoryMock = Mockito.mock(WeekdayRepository.class);
        roomRepositoryMock = Mockito.mock(RoomRepository.class);

        when(repoUtilMock.Lectures()).thenReturn(lectureRepositoryMock);
        when(repoUtilMock.Rooms()).thenReturn(roomRepositoryMock);
        when(repoUtilMock.Weekdays()).thenReturn(weekdayRepositoryMock);
        when(repoUtilMock.Events()).thenReturn(eventRepositoryMock);

        roomConflictService = new RoomConflictService(repoUtilMock);

    }

    @Test
    void testFindRoomConflict_noEvents_returnsEmptyMap(){
        HashMap<Integer, RoomConflictDTO[]> result = roomConflictService.findRoomConflict();

        assertEquals(0, result.size(), "Expected no room conflicts when no there are no Events");
    }

    @Test
    void testFindRoomConflict_noConflicts_returnsNoConflicts(){

        ArrayList<Event> mockEvents = new ArrayList<>();

        mockEvents.add(new Event(1,1,1, 1, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        mockEvents.add(new Event(1,1,1, 3, LocalTime.of(9, 0), LocalTime.of(12, 0)));

        when(eventRepositoryMock.fetchAll()).thenReturn(mockEvents);

        HashMap<Integer, RoomConflictDTO[]> result = roomConflictService.findRoomConflict();

        assertEquals(0, result.size(), "Expected no conflict between non-overlapping events");
    }


    @Test
    void testFindRoomConflict_twoOverlappingEvents_returnsConflict(){
        ArrayList<Event> mockEvents = new ArrayList<>();

        mockEvents.add(new Event(1,1,1, 1, LocalTime.of(10, 0), LocalTime.of(12, 0)));
        mockEvents.add(new Event(2,1,1,1, LocalTime.of(11, 0), LocalTime.of(13, 0)));

        when(eventRepositoryMock.fetchAll()).thenReturn(mockEvents);

        HashMap<Integer, RoomConflictDTO[]> result = roomConflictService.findRoomConflict();

        assertEquals(1, result.size(), "Expected one conflict between overlapping events");
        assertEquals(2, result.get(0).length, "Each conflict should have two entries");
    }


    @Test
    void testFindRoomConflict_multipleConflicts_returnsCorrectCount(){

        ArrayList<Event> mockEvents = new ArrayList<>();

        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(10, 30), LocalTime.of(12, 0))); //A
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(10, 35), LocalTime.of(13, 0))); //B
        mockEvents.add(new Event(1, 1, 1, 1, LocalTime.of(9, 0), LocalTime.of(10, 32)));  //C

        when(repoUtilMock.Events().fetchAll()).thenReturn(mockEvents);

        HashMap<Integer, RoomConflictDTO[]> result = roomConflictService.findRoomConflict();

        assertEquals(2, result.size(), "Expected two conflicts for overlapping events");
        //Conflict between A-B and B-A should be identified as 1 conflict
    }
}