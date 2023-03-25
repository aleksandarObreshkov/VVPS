package com.example.vvps;

import com.example.vvps.domain.Train;

public class TrainService extends ApiService{

    public Train getTrainById(String id) {
        return restTemplate.getForEntity(SERVER_URL+"/trains/"+id, Train.class).getBody();
    }
}
