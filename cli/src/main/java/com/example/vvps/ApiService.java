package com.example.vvps;

import org.springframework.web.client.RestTemplate;

public abstract class ApiService {
    protected final String SERVER_URL = "http://localhost:8080";
    protected final RestTemplate restTemplate = new RestTemplate();
}
