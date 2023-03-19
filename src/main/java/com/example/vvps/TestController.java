package com.example.vvps;

import com.example.vvps.domain.*;
import com.example.vvps.repository.CourseRepository;
import com.example.vvps.repository.TicketRepository;
import com.example.vvps.repository.TrainRepository;
import com.example.vvps.service.TicketReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    private final TicketRepository ticketRepository;

    private final TicketReservationService ticketReservationService;

    private final TrainRepository trainRepository;

    private final CourseRepository courseRepository;

    public TestController(TicketRepository ticketRepository, TicketReservationService ticketReservationService, TrainRepository trainRepository, CourseRepository courseRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketReservationService = ticketReservationService;
        this.trainRepository = trainRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping("/ticket")
    public ResponseEntity<List<Ticket>> getAll() {
        return ResponseEntity.ok(ticketRepository.findAll());
    }

    @PostMapping("/ticket")
    public ResponseEntity<Void> createNew() {
        TravelParameters travelParameters = TravelParameters.builder()
                .departureStation(Station.BURGAS)
                .arrivalStation(Station.SOFIA)
                .departureTime(LocalDateTime.now())
                .passengerName("Aleksandar Obreshkov")
                .priceDependencies(PriceDependencies.builder()
                        .student(true)
                        .build())
                .build();
        ticketReservationService.reserveTicket(travelParameters);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/train")
    public ResponseEntity<Void> createNewTrain() {
        Train train = new Train();
        train.setDepartureStation(Station.BURGAS);
        train.setStationRoute(List.of(Station.PLOVDIV, Station.SOFIA, Station.PLEVEN, Station.DOBRICH));
        train.setPassengerLimit(200);
        train.setDepartureTime(LocalDateTime.now());
        trainRepository.save(train);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/train")
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(trainRepository.findAll());
    }

    @PostMapping("/course")
    public ResponseEntity<Void> createCourses() {
        Course course = new Course(Station.BURGAS, Station.PLOVDIV, 20);
        Course course1 = new Course(Station.PLOVDIV, Station.SOFIA, 15);
        Course course2 = new Course(Station.SOFIA, Station.PLEVEN, 15);
        Course course3 = new Course(Station.PLEVEN, Station.DOBRICH, 15);
        courseRepository.saveAll(List.of(course, course1, course2, course3));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/course")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }
}
