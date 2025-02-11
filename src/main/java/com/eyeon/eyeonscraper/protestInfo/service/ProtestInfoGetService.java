package com.eyeon.eyeonscraper.protestInfo.service;

import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class ProtestInfoGetService {
    private final RestTemplate restTemplate;

    @Value("${api.spatic.url}")
    private String targetUrl;

    public ProtestInfoGetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getProtestData() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/xml");
        headers.set("Content-Type", "application/xml");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(targetUrl)
                .queryParam("service", "WFS")
                .queryParam("version", "1.0.0")
                .queryParam("request", "GetFeature");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        log.info("Sending GET request to {}", builder.toUriString());
        log.info("Request headers: {}", headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            log.info("Response received: {}", response.getBody());
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error while sending GET request: {}", e.getMessage());
            throw e;
        }
    }
}