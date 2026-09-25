package com.example.pboproject.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Properties;

public class ConnectionManager {
    private static final String CONFIG_FILE = "db.properties";

    private static final Properties CONFIG = loadConfig();

    private static final String URL = resolve("db.url", "COFFEE_DB_URL", "jdbc:postgresql://localhost:5432/CoffeeDB");
    private static final String USER = resolve("db.user", "COFFEE_DB_USER", "postgres");
    private static final String PASSWORD = resolve("db.password", "COFFEE_DB_PASSWORD", "postgres");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Reads db.properties from the working directory first so the settings can be changed
     * without a rebuild, and falls back to the copy bundled in the jar.
     */
    private static Properties loadConfig() {
        Properties props = new Properties();

        Path external = Path.of(CONFIG_FILE);
        if (Files.isReadable(external)) {
            try (InputStream in = Files.newInputStream(external)) {
                props.load(in);
                return props;
            } catch (IOException e) {
                throw new UncheckedIOException("Could not read " + external.toAbsolutePath(), e);
            }
        }

        try (InputStream in = ConnectionManager.class.getResourceAsStream("/" + CONFIG_FILE)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read bundled " + CONFIG_FILE, e);
        }
        return props;
    }

    /** System property wins over environment variable, which wins over db.properties. */
    private static String resolve(String key, String envVar, String fallback) {
        String value = System.getProperty(key);
        if (value == null) {
            value = System.getenv(envVar);
        }
        if (value == null) {
            value = CONFIG.getProperty(key);
        }
        return value == null ? fallback : value;
    }

    public static void closeConnection(Connection con){
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
            }
            if(ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(PreparedStatement ps) {
        try {
            if(ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
