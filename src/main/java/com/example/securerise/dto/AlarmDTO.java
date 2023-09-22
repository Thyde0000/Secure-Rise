package com.example.securerise.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDTO {
    private Long id;
    @NotEmpty(message = "Alarm must have a name.")
    private String alarmName;
    @NotNull(message = "Alarm must have a starting time.")
    private LocalTime startingTime;
    @NotEmpty(message = "Alarm must have a sound.")
    private String alarmSound;
    private boolean isPlayingSound;
    private boolean isEnabled;
}
