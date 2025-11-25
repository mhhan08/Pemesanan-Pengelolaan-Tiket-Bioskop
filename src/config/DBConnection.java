package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/cinema_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "mhhan-wsl";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Gagal koneksi: " + e.getMessage());
            return null;
        }
    }
}