package org.example.config;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseInitializer {
    public static void initialize() {

        try (
                Connection conn = DatabaseConfig.getConnection();
                InputStream input = DatabaseInitializer.class
                        .getClassLoader()
                        .getResourceAsStream("db/schema.sql")
        ) {

            if (input == null) {
                throw new RuntimeException("schema.sql not found");
            }

            String sql = new String(
                    input.readAllBytes(),
                    StandardCharsets.UTF_8
            );

            try (Statement stmt = conn.createStatement()) {

                for (String query : sql.split(";")) {
                    if (!query.isBlank()) {
                        stmt.executeUpdate(query);
                    }
                }
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(
                    "Database initialization failed", e
            );
        }

    }
}