package com.example.securerise;

import com.example.securerise.dto.AlarmDTO;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.LinkedList;
import java.util.Queue;

@EnableScheduling
@SpringBootApplication
public class SecureRise {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
    @Bean
    public Queue<AlarmDTO> queue(){
        return new LinkedList<>();
    }

    public static void main(String[] args) {
        SpringApplication.run(SecureRise.class, args);
    }
}
