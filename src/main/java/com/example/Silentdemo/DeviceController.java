package com.example.Silentdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
public class DeviceController {
    @Autowired
DeviceService service;
    private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

    @PostMapping("/devices")
    public ResponseEntity<String> addDevice(@RequestBody Device device) {
        try {
            service.addDevice(device);
            return ResponseEntity.ok("Device added successfully");
        } catch (Exception e) {
            logger.error("Error adding device: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getDevices() {
        List<Device> devices = service.getAllDevices();
        return ResponseEntity.ok(devices);
    }



    @GetMapping("/showDevices")
    public String showDevices(Model model) {
        try {
            List<Device> devices = service.getAllDevices();
            model.addAttribute("devices", devices);
            return "devices"; // This matches the name of your Thymeleaf template
        } catch (Exception e) {
            logger.error("Error retrieving devices: {}", e.getMessage(), e);
            model.addAttribute("error", "Error retrieving devices. Please try again later.");
            return "error"; // Redirect to an error page
        }
    }


    @DeleteMapping("/devices/{imei}")
    public ResponseEntity<String> deleteDevice(@PathVariable String imei) {
        try {
            if (!service.doesDeviceExist(imei)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found");
            }
            service.deleteDeviceByImei(imei);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }


    @PutMapping("/devices/{imei}")
    public ResponseEntity <String> updateDevice(@PathVariable("imei") String imei,@RequestBody Device device)
    {
        try {
            if (!service.doesDeviceExist(imei)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Device not found");
            }
            service.updateDeviceByImei(imei, device);
            return ResponseEntity.ok("Device updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
}


    @GetMapping("/devices/{imei}")
    public String getDeviceByImei(@PathVariable String imei,Model model) {
        try {
            Device device = service.searchDeviceByImei(imei);
            if (device != null) {
                model.addAttribute("device1", device);
                return "singledata";
            } else {

                model.addAttribute("Error ", "Device not found");
                return "error";
            }
        } catch (Exception e) {
            logger.error("Error fetching device by IMEI: {}", e.getMessage(), e);
            return "Error";
        }
    }
}
