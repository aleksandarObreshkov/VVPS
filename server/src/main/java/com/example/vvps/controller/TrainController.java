package com.example.vvps.controller;

import com.example.vvps.domain.Station;
import com.example.vvps.domain.Train;
import com.example.vvps.repository.TrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trains")
public class TrainController {

    private final TrainRepository trainRepository;

    public TrainController(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @GetMapping("/destination/{destination}")
    public ResponseEntity<List<Train>> getByDestination(@PathVariable String destination) {
        List<Train> allTrains = trainRepository.findAll();
        return ResponseEntity.ok(allTrains.stream()
                .filter(t -> t.getStationRoute().contains(Station.parse(destination)))
                .toList());
    }

    @GetMapping("/departure-time/{departureTime}")
    public ResponseEntity<List<Train>> getByDepartureTime(@PathVariable String departureTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy.HH:mm");
        LocalDateTime departureDateTime = LocalDateTime.parse(departureTime, formatter);
        return ResponseEntity.ok(trainRepository.findAllByDepartureTime(departureDateTime));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getById(@PathVariable String id) {
        return ResponseEntity.of(trainRepository.findById(UUID.fromString(id)));
    }

    @GetMapping("")
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(trainRepository.findAll());
    }

}
