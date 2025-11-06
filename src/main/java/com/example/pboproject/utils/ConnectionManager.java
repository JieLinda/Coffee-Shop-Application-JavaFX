package com.example.pboproject.utils;

import java.sql.*;

public class ConnectionManager {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/lindaFinal?user=postgres&password=123";
        Connection con = DriverManager.getConnection(url);
        return con;
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
