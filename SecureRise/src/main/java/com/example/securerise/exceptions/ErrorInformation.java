package com.example.securerise.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInformation {
    private LocalDateTime time;
    private String message;
    private String path;
    private String errorStatus;
}
