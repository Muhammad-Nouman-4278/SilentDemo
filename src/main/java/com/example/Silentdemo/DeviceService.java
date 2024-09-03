            package com.example.Silentdemo;

            import org.slf4j.Logger;
            import org.slf4j.LoggerFactory;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.stereotype.Service;
            import org.springframework.transaction.annotation.Transactional;

            import java.sql.SQLException;
            import java.util.List;

            @Service
            public class DeviceService {

                private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

                @Autowired
                private DeviceDao dao;

                public void addDevice(Device device) {
                    try {
                        dao.saveDevice(device);
                        logger.info("Device added successfully: {}", device);
                    } catch (Exception e) {
                        logger.error("Error adding device: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to add device", e); // Custom exception handling
                    }

                }


                public List<Device> getAllDevices() {
                    return dao.getDevices(); //
                }

                public boolean doesDeviceExist(String imei) throws SQLException, ClassNotFoundException {
                    return dao.doesDeviceExist(imei);
                }

                @Transactional
                public void deleteDeviceByImei(String imei)
                {
                    try {
                        dao.deleteDeviceByImei(imei);
                        logger.info("Device deleted successfully with IMEI: {}", imei);
                    } catch (Exception e) {
                        logger.error("Error deleting device: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to delete device", e);
                    }
                }

                @Transactional
                public void updateDeviceByImei(String imei, Device device)
                {
                    try
                    {
                        dao.updateDevice(imei,device);
                        logger.info("Device Updated Successfully with imei: {}" ,imei);
                    }catch( Exception e)
                    {
                        logger.error("Error updating device: {}", e.getMessage(), e);
                        throw new RuntimeException("Failed to update device", e);
                    }
                }

                @Transactional
                public Device searchDeviceByImei(String imei)
                {
                 return dao.searchDeviceByImei(imei);
                }
            }
