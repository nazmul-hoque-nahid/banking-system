package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig{
    private static final   String url = "jdbc:mysql://localhost:3306/bank";
    private  static final   String username = "spring";
    private static final   String password = "Spring!48";
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }
}
