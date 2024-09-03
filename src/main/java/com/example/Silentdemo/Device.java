package com.example.Silentdemo;

import lombok.Data;

@Data
public class Device{
    private String imei;
    private double latitude;
    private double longitude;
    private double range_km;
}