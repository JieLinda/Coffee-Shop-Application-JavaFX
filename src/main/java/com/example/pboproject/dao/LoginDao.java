package com.example.pboproject.dao;

import com.example.pboproject.beans.DiscountList;
import com.example.pboproject.beans.ProductType;
import com.example.pboproject.beans.Username;
import com.example.pboproject.utils.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;

public class LoginDao {
    public static Username get(Username username) throws SQLException {
        //ambil 1 diskon spesifik
        ResultSet rs;
        Username user = new Username();
        Connection con = ConnectionManager.getConnection();
        String query = String.format("select * from username where username = ?;");
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,username.getUser());
            rs = ps.executeQuery();
            if (rs.next()){
                user.setUser(rs.getString("username"));
                user.setPass(rs.getString("password"));
                user.setType(rs.getString("accountType"));
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return user;
    }
    public static ArrayList<Username> getAll(Connection con){
        //ambil semua
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select * from username order by username;";
        ArrayList<Username> listUser = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Username user = new Username();
                user.setUser(rs.getString("username"));
                user.setPass(rs.getString("password"));
                user.setType(rs.getString("accountType"));
                listUser.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listUser;
    }
    public static void save(Username username) throws SQLException {
        //fungsi untuk input data baru dan button save
        Connection con = ConnectionManager.getConnection();
        String query = "Insert into username (username,password,accountType) values (?,?,?);";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,username.getUser());
            ps.setString(2,username.getPass());
            ps.setString(3,username.getType());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }

    }
    public static void update(Username user, Username prev) throws SQLException {
        //fungsi untuk button edit
        Connection con = ConnectionManager.getConnection();
        String query = "update username set password=?,accountType=?,username=? where username = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,user.getPass());
            ps.setString(2,user.getType());
            ps.setString(3,user.getUser());
            ps.setString(4,prev.getUser());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }
        public static void delete(Username user) throws SQLException {
        //delete menggunakan discount id
        Connection con = ConnectionManager.getConnection();
        String query = "delete from username where username=?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,user.getUser());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }
}
