package com.example.Silentdemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.sql.SQLException;

public class SilentModedemo extends JFrame {

    private JTextField mobileNumberField;
    private JTextField targetLatitudeField;
    private JTextField targetLongitudeField;
    private JTextField rangeField;
    private JTextField searchField;
    private JButton checkButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton showButton;
    private JButton searchButton;
    private JLabel statusLabel;
    private JLabel titleLabel;
    private JLabel searchLabel;

    public SilentModedemo() {
        setTitle("Silent Mode Demo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Initialize components
        mobileNumberField = new JTextField(15);
        targetLatitudeField = new JTextField(15);
        targetLongitudeField = new JTextField(15);
        searchField = new JTextField(15);
        rangeField = new JTextField(10);

        checkButton = new JButton("Send data");
        updateButton = new JButton("Update data");
        deleteButton = new JButton("Delete data");
        showButton = new JButton("Show data");
        searchButton= new JButton("Search");
        statusLabel = new JLabel("Status is Normal");
        searchLabel= new JLabel("Enter IMEI");

        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(new Color(0, 128, 128));

        // Title Label
        titleLabel = new JLabel("Device Data");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Make it bold and larger
        titleLabel.setForeground(Color.WHITE); // Set the font color to white
        titleLabel.setBounds(240, 20, 400, 40); // Position at the top center
        panel.add(titleLabel);

        // Mobile Number Label and Field
        JLabel mobileNumberLabel = new JLabel("IMEI Number:");
        mobileNumberLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        mobileNumberLabel.setForeground(Color.WHITE); // Set font color to white
        mobileNumberLabel.setBounds(150, 80, 150, 25);
        panel.add(mobileNumberLabel);

        mobileNumberField.setBounds(320, 80, 200, 25);
        panel.add(mobileNumberField);

        // Latitude Label and Field
        JLabel latitudeLabel = new JLabel("Target Latitude:");
        latitudeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        latitudeLabel.setForeground(Color.WHITE); // Set font color to white
        latitudeLabel.setBounds(150, 120, 150, 25);
        panel.add(latitudeLabel);

        targetLatitudeField.setBounds(320, 120, 200, 25);
        panel.add(targetLatitudeField);

        // Longitude Label and Field
        JLabel longitudeLabel = new JLabel("Target Longitude:");
        longitudeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        longitudeLabel.setForeground(Color.WHITE); // Set font color to white
        longitudeLabel.setBounds(150, 160, 150, 25);
        panel.add(longitudeLabel);

        targetLongitudeField.setBounds(320, 160, 200, 25);
        panel.add(targetLongitudeField);

        // Range Label and Field
        JLabel rangeLabel = new JLabel("Range (km):");
        rangeLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        rangeLabel.setForeground(Color.WHITE); // Set font color to white
        rangeLabel.setBounds(150, 200, 150, 25);
        panel.add(rangeLabel);

        rangeField.setBounds(320, 200, 200, 25);
        panel.add(rangeField);

        // Check Button
        checkButton.setBounds(150, 260, 200, 30);
        panel.add(checkButton);


        updateButton.setBounds(380, 260, 200, 30);
        panel.add(updateButton);

        deleteButton.setBounds(150, 300, 200, 30);
        panel.add(deleteButton);

        showButton.setBounds(380, 300, 200, 30);

        panel.add(showButton);


        // Status Label
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        statusLabel.setForeground(Color.WHITE); // Set font color
        statusLabel.setBounds(150, 390, 400, 25);
        panel.add(statusLabel);

        searchLabel = new JLabel("Enter IMEI:");
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        searchLabel.setForeground(Color.WHITE); // Set font color to white
        searchLabel.setBounds(150, 340, 150, 30);
        panel.add(searchLabel);

        searchField.setBounds(270, 340, 200, 30);
        panel.add(searchField);


        searchButton.setText("Search");
        searchButton.setBounds(530, 340, 200, 30);
        panel.add(searchButton);

        JLabel mobileImageLabel = new JLabel(new ImageIcon("E:\\Java Practice\\Silentdemo\\Silent.jpg"));
        mobileImageLabel.setBounds(530, 80, 180, 145);
        panel.add(mobileImageLabel);
        add(panel);
        setVisible(true);

        checkButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validateData())
                    {
                        sendDataToServer();
                    }
                } catch (IOException ex) {
                    statusLabel.setText("Error sending data: " + ex.getMessage());
                }
            }



            private void sendDataToServer() throws IOException
            {
                String imeiNumber = mobileNumberField.getText();
                String latitude = targetLatitudeField.getText();
                String longitude = targetLongitudeField.getText();
                String rangeKm = rangeField.getText();

                String jsonInputString = String.format("{\"imei\":\"%s\",\"latitude\":%s,\"longitude\":%s,\"range_km\":%s}",
                        imeiNumber, latitude, longitude, rangeKm);

                URL url = new URL("http://localhost:9090/devices");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");

                try (OutputStream os = con.getOutputStream())
                {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK)
                {
                    statusLabel.setText("Data sent successfully!");
                    mobileNumberField.setText("");
                    targetLatitudeField.setText("");
                    targetLongitudeField.setText("");
                    rangeField.setText("");
                } else
                {
                    statusLabel.setText("Failed to send data. Response code: " + responseCode);
                }

                con.disconnect();
            }
        });




        showButton.addActionListener(e -> {


            try {
                Desktop.getDesktop().browse(new URI("http://localhost:9090/showDevices"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (URISyntaxException ex) {
                throw new RuntimeException(ex);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String imei = mobileNumberField.getText();

                try {
                    int responseCode = deleteDevice(imei);
                    if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                        statusLabel.setText("Device with IMEI " + imei + " does not exist.");
                    } else if (responseCode == HttpURLConnection.HTTP_OK) {
                        statusLabel.setText("Device deleted successfully!");
                    } else {
                        statusLabel.setText("Failed to delete device. Response code: " + responseCode);
                    }
                } catch (IOException ex) {
                    statusLabel.setText("Error: " + ex.getMessage());
                }
            }

            private int deleteDevice(String imei) throws IOException {
                URL url = new URL("http://localhost:9090/devices/" + imei);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("DELETE");
                con.setRequestProperty("Accept", "application/json");

                int responseCode = con.getResponseCode();
                con.disconnect();
                return responseCode;
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(validateData())
                    {
                        updateData();
                    }
                } catch (IOException ex) {
                    statusLabel.setText("Error sending data: " + ex.getMessage());
                }
            }

            private void updateData() throws IOException {
                String imeiNumber = mobileNumberField.getText();
                String latitude = targetLatitudeField.getText();
                String longitude = targetLongitudeField.getText();
                String rangeKm = rangeField.getText();

                String jsonInputString = String.format("{\"latitude\":\"%s\",\"longitude\":\"%s\",\"range_km\":\"%s\"}",
                        latitude, longitude, rangeKm);

                URL url = new URL("http://localhost:9090/devices/" + imeiNumber);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    statusLabel.setText("Data updated successfully!");
                    mobileNumberField.setText("");
                    targetLatitudeField.setText("");
                    targetLongitudeField.setText("");
                    rangeField.setText("");
                } else {
                    statusLabel.setText("Failed to update data. Response code: " + responseCode);
                }

                con.disconnect();
            }

        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                        searchByImei();

                }catch (IOException | URISyntaxException ex)
                {
                    statusLabel.setText("Error while searching data "+ ex.getMessage());
                }
            }
            private void searchByImei() throws IOException, URISyntaxException {
                String imei = searchField.getText();

                URL url = new URL("http://localhost:9090/devices/" + imei);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                int responseCode = con.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    statusLabel.setText("Device found! IMEI: " + imei);
                    Desktop.getDesktop().browse(new URI("http://localhost:9090/devices/"+imei));

                } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    statusLabel.setText("Device not found. IMEI: " + imei);
                } else {
                    statusLabel.setText("Failed to search. Response code: " + responseCode);
                }

                con.disconnect();
            }

        });

    }
    private boolean validateData()
    {
        String imeiNumber = mobileNumberField.getText();
        String latitude = targetLatitudeField.getText();
        String longitude = targetLongitudeField.getText();
        String rangeKm = rangeField.getText();

        if(!isValidImei(imeiNumber))
        {
            statusLabel.setText("Invalid Imei Format");
            return false;
        }
        else if (!isValidLatitude(latitude))
        {
            statusLabel.setText("Invalid Latitude(Set Between -90 to 90)");
            return false;
        }
        else if (!isValidLongitude(longitude))
        {
            statusLabel.setText("Invalid Longitude(Set Between -180 to 180)");
            return false;
        }
        else if (!isValidRange(rangeKm))
        {
            statusLabel.setText("Invalid Range");
            return false;
        }
        return true;
    }
    private boolean isValidImei(String imei)
    {
        return imei.matches("\\d{15}");
    }
    private boolean isValidLatitude(String latitude)
    {
        double lat= Double.parseDouble(latitude);
        return lat>=-90 && lat <=90;
    }
    private boolean isValidLongitude(String longitude )
    {
        double lon= Double.parseDouble(longitude);
        return lon>=-180 && lon <=180;
    }
    private boolean isValidRange(String range)
    {
        double r = Double.parseDouble(range);
        return r >= 0;
    }


    public static void main(String[] args) {
        new SilentModedemo();
    }
}
