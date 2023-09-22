package com.example.securerise.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Alarms")
@Entity
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private String alarmName;
    @Column()
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startingTime;
    @Column()
    private String alarmSound;
    @Column(nullable = false)
    private boolean isPlayingSound;
    @Column(nullable = false)
    private boolean isEnabled;
}
