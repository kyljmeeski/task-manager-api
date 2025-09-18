package com.amir.task_manager_api.devices_api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String id;
    private String name;
    private Map<String, Object> data;
}