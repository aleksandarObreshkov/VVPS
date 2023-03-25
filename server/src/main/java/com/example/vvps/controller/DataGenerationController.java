package com.example.vvps.controller;

import com.example.vvps.domain.*;
import com.example.vvps.repository.CourseRepository;
import com.example.vvps.repository.TrainRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataGenerationController {

    private final TrainRepository trainRepository;

    private final CourseRepository courseRepository;

    public DataGenerationController(TrainRepository trainRepository, CourseRepository courseRepository) {
        this.trainRepository = trainRepository;
        this.courseRepository = courseRepository;
    }

    @PostConstruct
    public void createNew() {
        Train train = new Train();
        train.setStationRoute(List.of(Station.BURGAS,Station.PLOVDIV, Station.SOFIA, Station.PLEVEN, Station.DOBRICH));
        train.setPassengerLimit(200);
        train.setDepartureTime(LocalDateTime.of(2023, 4, 24, 12, 0));
        trainRepository.save(train);

        Train train1 = new Train();
        train1.setStationRoute(List.of(Station.DOBRICH, Station.SOFIA, Station.PLOVDIV, Station.VARNA));
        train1.setPassengerLimit(250);
        train1.setDepartureTime(LocalDateTime.of(2023, 4, 24, 13, 0));
        trainRepository.save(train1);

        Course course = new Course(Station.BURGAS, Station.PLOVDIV, 20);
        Course course1 = new Course(Station.PLOVDIV, Station.SOFIA, 15);
        Course course2 = new Course(Station.SOFIA, Station.PLEVEN, 15);
        Course course3 = new Course(Station.PLEVEN, Station.DOBRICH, 15);
        courseRepository.saveAll(List.of(course, course1, course2, course3));
    }
}
