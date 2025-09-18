package com.amir.task_manager_api.devices_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeviceApiClient {

    private final RestTemplate restTemplate;
    @Value("${external-api.devices.url}")
    private String apiUrl = "https://api.restful-api.dev/objects";

    public List<Device> getDevices() {
        ResponseEntity<Device[]> response = restTemplate.getForEntity(apiUrl, Device[].class);
        Device[] body = response.getBody();

        if (body == null) {
            log.error("Failed to fetch devices: null body, status={}", response.getStatusCode());
            throw new ExternalApiException("Device API returned null body");
        }

        return Arrays.asList(body);
    }

}

