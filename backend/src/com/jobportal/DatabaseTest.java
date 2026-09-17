package com.jobportal;

import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Java is connected to MySQL!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}