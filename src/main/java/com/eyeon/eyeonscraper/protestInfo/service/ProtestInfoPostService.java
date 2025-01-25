package com.eyeon.eyeonscraper.protestInfo.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

@Service
@Slf4j
public class ProtestInfoPostService {
    private final RestTemplate restTemplate;

    @Value("${api.target.url}")
    private String targetUrl;

    public ProtestInfoPostService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendProtestData(String aiResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(aiResponse, headers);

        log.info("Sending request to {}", targetUrl);
        log.info("Request body: {}", aiResponse);

        String response = restTemplate.postForObject(targetUrl, request, String.class);
        log.info("Response from JSONPlaceholder: {}", response);
    }
}
