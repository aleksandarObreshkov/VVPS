package com.example.vvps.repository;

import com.example.vvps.domain.Station;
import com.example.vvps.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TrainRepository extends JpaRepository<Train, UUID> {

    List<Train> findAllByDepartureTime(LocalDateTime departureTime);
}
