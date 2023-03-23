package com.example.vvps.controller;

import com.example.vvps.repository.TrainRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    private TrainRepository trainRepository;

}
