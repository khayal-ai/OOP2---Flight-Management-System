/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package flightmanagementsystem;

import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Flightmanagementsystem {
    private static final String URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    public static void main(String[] args) {
        initDatabase();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new startPage();
            }
        });
    }

    private static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement  st   = conn.createStatement()) {

            try {
                st.executeUpdate("CREATE SCHEMA APP");
            } catch (SQLException ignore) {
            }

            try {
                st.executeUpdate(
                  "CREATE TABLE APP.USERS (" +
                    "username    VARCHAR(20)    PRIMARY KEY," +
                    "password    VARCHAR(100)   NOT NULL," +
                    "user_type   VARCHAR(20)    NOT NULL," +
                    "last_login  TIMESTAMP" +
                  ")"
                );
            } catch (SQLException ignore) {}

            try {
                st.executeUpdate(
                  "CREATE TABLE APP.PASSENGERS (" +
                    "name        VARCHAR(100)  PRIMARY KEY," +
                    "email       VARCHAR(100)  NOT NULL DEFAULT ''," +
                    "password    VARCHAR(100)  NOT NULL" +
                  ")"
                );
            } catch (SQLException ignore) {}

            try {
                st.executeUpdate(
                  "CREATE TABLE APP.FLIGHT (" +
                    "flight_number VARCHAR(100) PRIMARY KEY," +
                    "destination   VARCHAR(100) NOT NULL," +
                    "source        VARCHAR(20)  NOT NULL," +
                    "flight_time   VARCHAR(20)  NOT NULL," +
                    "flight_type   VARCHAR(20)  NOT NULL," +
                    "class_type    VARCHAR(20)  NOT NULL" +
                  ")"
                );
            } catch (SQLException ignore) {}

            try {
                st.executeUpdate(
                  "CREATE SEQUENCE APP.BOOKING_SEQ START WITH 1001 INCREMENT BY 1"
                );
            } catch (SQLException ignore) {}

            try {
                st.executeUpdate(
                  "CREATE TABLE APP.BOOKINGS (" +
                    "booking_id           VARCHAR(25)    PRIMARY KEY," +
                    "passenger_passport   VARCHAR(20)    NOT NULL UNIQUE," +
                    "flight_number        VARCHAR(100)   NOT NULL," +
                    "destination          VARCHAR(100)   NOT NULL," +
                    "flight_time          VARCHAR(20)    NOT NULL," +
                    "flight_type          VARCHAR(20)    NOT NULL," +
                    "class_type           VARCHAR(20)    NOT NULL," +
                    "price                INT            NOT NULL," +
                    "booking_date         DATE           DEFAULT CURRENT_DATE NOT NULL," +
                    "FOREIGN KEY(flight_number) REFERENCES APP.FLIGHT(flight_number)" +
                  ")"
                );
            } catch (SQLException ignore) {}

            try {
                st.executeUpdate(
                  "CREATE TABLE APP.EMPLOYEES (" +
                    "employee_id BIGINT    PRIMARY KEY," +
                    "name        VARCHAR(100) NOT NULL," +
                    "password    VARCHAR(100) NOT NULL" +
                  ")"
                );
            } catch (SQLException ignore) {}

            System.out.println("Database initialized.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}