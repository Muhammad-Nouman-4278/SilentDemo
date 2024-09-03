package com.example.Silentdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeviceDao {

    private static final Logger logger = LoggerFactory.getLogger(DeviceDao.class);

    public List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();
        String sql = "SELECT * FROM devices";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Device device = new Device();
                device.setImei(rs.getString("imei"));
                device.setLatitude(rs.getDouble("latitude"));
                device.setLongitude(rs.getDouble("longitude"));
                device.setRange_km(rs.getDouble("range_km"));

                devices.add(device);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while fetching devices from the database: {}", e.getMessage(), e);
            //throw new DataAccessException("Error while fetching devices from the database", e);
        }

        return devices;
    }

    public void saveDevice(Device device) {
        String sql = "INSERT INTO devices (imei, latitude, longitude, range_km) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, device.getImei());
            ps.setDouble(2, device.getLatitude());
            ps.setDouble(3, device.getLongitude());
            ps.setDouble(4, device.getRange_km());
            ps.executeUpdate();
            logger.info("Device saved successfully: {}", device);

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while saving device to the database: {}", e.getMessage(), e);

        }
    }


    public boolean doesDeviceExist(String imei) throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM devices WHERE imei = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, imei); // Set the IMEI parameter
            ResultSet rs = ps.executeQuery(); // Execute the query

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if count > 0
            }
        }
        return false;
    }

    @Transactional
    public void deleteDeviceByImei(String imei) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM devices WHERE imei = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, imei); // Set the IMEI parameter
            int rowsAffected = ps.executeUpdate(); // Execute the delete statement
            if (rowsAffected > 0) {
                logger.info("Device deleted successfully with IMEI: {}", imei);
            } else {
                logger.warn("No device found with IMEI: {}", imei);
            }
        }
    }
    @Transactional
    public void updateDevice(String imei, Device device) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE devices SET latitude = ?, longitude = ?, range_km = ? WHERE imei = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, device.getLatitude());
            ps.setDouble(2, device.getLongitude());
            ps.setDouble(3, device.getRange_km());
            ps.setString(4, imei);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                logger.info("Device updated successfully with IMEI: {}", imei);
            } else
            {
                logger.warn("No device found with IMEI: {}", imei);
            }

        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while updating device in the database: {}", e.getMessage(), e);
        }
    }

    public Device searchDeviceByImei(String imei) {
        String query = "SELECT * FROM devices WHERE imei = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, imei);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Device device = new Device();
                    device.setImei(rs.getString("imei"));
                    device.setLatitude(rs.getDouble("latitude"));
                    device.setLongitude(rs.getDouble("longitude"));
                    device.setRange_km(rs.getDouble("range_km"));
                    return device;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            logger.error("Error while searching device by IMEI: {}", e.getMessage(), e);
            throw new DataAccessException("Error while searching device by IMEI", e) {};
        }
        return null;
    }




}
