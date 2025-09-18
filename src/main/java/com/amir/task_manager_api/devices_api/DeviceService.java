package com.amir.task_manager_api.devices_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceApiClient deviceApiClient;

    public List<Device> fetchDevices() {
        List<Device> devices = deviceApiClient.getDevices();
        log.info("Fetched {} devices", devices.size());
        return devices;
    }
}
