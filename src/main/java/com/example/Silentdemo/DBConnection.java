package com.example.Silentdemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/devicedata";
    private static final String USER = "root";
    private static final String PASSWORD = "";  // No password


    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName(  "com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
