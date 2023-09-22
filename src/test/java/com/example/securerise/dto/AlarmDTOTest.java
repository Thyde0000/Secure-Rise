package com.example.securerise.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.time.LocalTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
class AlarmDTOTest {

    static LocalTime localTime = LocalTime.now();
    private static Stream<Arguments> mockDTOs() {

        return Stream.of(
                Arguments.of(new AlarmDTO(12L, "Alarm 3", localTime, "sound.wav", false, true))
        );
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void getId(AlarmDTO alarmDTO) {
        assertEquals(alarmDTO.getId(),12L);
        assertNotNull(alarmDTO.getId());
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void getAlarmName(AlarmDTO alarmDTO) {
        assertNotNull(alarmDTO.getAlarmName());
        assertEquals(alarmDTO.getAlarmName(),"Alarm 3");
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void getStartingTime(AlarmDTO alarmDTO) {
        assertNotNull(alarmDTO.getStartingTime());
        assertEquals(alarmDTO.getStartingTime(),localTime);
        assertNotEquals(alarmDTO.getStartingTime(),LocalTime.now());
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void getAlarmSound(AlarmDTO alarmDTO) {
        assertNotNull(alarmDTO.getAlarmSound());
        assertEquals(alarmDTO.getAlarmSound(), "sound.wav");
        assertNotEquals(alarmDTO.getAlarmName(),"anySound.wav");
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void isPlayingSound(AlarmDTO alarmDTO) {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void isEnabled() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setId() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setAlarmName() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setStartingTime() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setAlarmSound() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setPlayingSound() {
    }

    @ParameterizedTest
    @MethodSource("mockDTOs")
    void setEnabled() {
    }
}