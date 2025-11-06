package com.example.pboproject.dao;

import java.sql.*;
import java.util.ArrayList;
import com.example.pboproject.beans.*;
import com.example.pboproject.utils.*;

public class MenuDao {
    public static Menu get(String id) throws SQLException {
        //mengeluarkan menu spesifik
        ResultSet rs;
        Menu menu = new Menu();
        Connection con = ConnectionManager.getConnection();
        String query = "select * from menu_list where product_id = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,id);
            rs = ps.executeQuery();
            if (rs.next()){
                menu.setProductId(rs.getString("product_id"));
                menu.setProductName(rs.getString("product_name").trim());
                menu.setProductType(rs.getString("product_type"));
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return menu;
    }
    public static ArrayList<Menu> getAll(Connection con){
        //mengeluarkan semua menu
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select * from menu_list where status=1 order by product_id ;";
        ArrayList<Menu> listmenu = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                Menu menu = new Menu();
                menu.setProductId(rs.getString("product_id"));
                menu.setProductName(rs.getString("product_name").trim());
                menu.setProductType(rs.getString("product_type"));
                listmenu.add(menu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listmenu;
    }
    public static void save(Menu Menu) throws SQLException {
        //input data baru dan fungsi button save
        try {
            Connection con = ConnectionManager.getConnection();
            String query = "Insert into menu_list (product_id,product_name, product_type) values (?,?,?);";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, Menu.getProductId());
            ps.setString(2, Menu.getProductName());
            ps.setString(3, Menu.getProductType());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public static void update(Menu Menu) throws SQLException {
        //fungsi button edit
        Connection con = ConnectionManager.getConnection();
        String query = "update menu_list set product_name = ?, product_type = ? where product_id = ?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,Menu.getProductName());
            ps.setString(2,Menu.getProductType());
            ps.setString(3,Menu.getProductId());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void delete(String id) throws SQLException {
        Connection con = ConnectionManager.getConnection();
        String query = "update menu_list set status=0 where product_id=?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,id);
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}

