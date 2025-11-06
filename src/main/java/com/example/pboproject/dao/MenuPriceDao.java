package com.example.pboproject.dao;

import com.example.pboproject.beans.Menu;
import com.example.pboproject.beans.MenuPrice;
import com.example.pboproject.beans.Sizes;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MenuPriceDao {
    public static ObservableList<MenuPrice> get(String id) throws SQLException {
        //mencari price spesifik
        ResultSet rs;
        MenuPrice menu = new MenuPrice();
        Connection con = ConnectionManager.getConnection();
        ObservableList<MenuPrice> listPrice = FXCollections.observableArrayList();
        String query = "select * from menu_price_list where price_id = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,id);
            rs = ps.executeQuery();
            while(rs.next()) {
                MenuPrice mp = new MenuPrice();
                menu.setSizePrizeId(rs.getString("price_id"));
                menu.setSize(Sizes.valueOf(rs.getString("size").trim()));
                menu.setPrice(rs.getDouble("price"));
                listPrice.add(menu);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            ConnectionManager.close(ps);
        }
        return listPrice;
    }
    public static ObservableList<MenuPrice> getAll(Connection con, String id){
        //mencari semua price berdasarkan menu_id yang diberi
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select * from menu_price_list where product_id = ? and status=1 order by price_id ;";
        ObservableList<MenuPrice> listmenu = FXCollections.observableArrayList();
        try {
            ps = con.prepareStatement(query);
            ps.setString(1,id);
            rs = ps.executeQuery();
            while(rs.next()) {
                MenuPrice menu = new MenuPrice();
                menu.setSizePrizeId(rs.getString("price_id"));
                menu.setSize(Sizes.valueOf(rs.getString("size").trim()));
                menu.setPrice(rs.getDouble("price"));
                listmenu.add(menu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listmenu;
    }


    public static void save(MenuPrice MenuPrice, Menu menu) throws SQLException {
        //input data baru dan fungsi button save
        Connection con = ConnectionManager.getConnection();
        String query = "Insert into menu_price_list (price, size, product_id, price_id) values (?,?,?,?);";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setDouble(1, MenuPrice.getPrice());
            ps.setString(2, String.valueOf(MenuPrice.getSize()));
            ps.setString(3,menu.getProductId());
            ps.setString(4,MenuPrice.getSizePrizeId());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }

    }
    public static void update(MenuPrice MenuPrice) throws SQLException {
        //fungsi button edit
        Connection con = ConnectionManager.getConnection();
        String query = "update menu_price_list set size = ?, price = ? where price_id = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,String.valueOf(MenuPrice.getSize()));
            ps.setDouble(2,MenuPrice.getPrice());
            ps.setString(3,MenuPrice.getSizePrizeId());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }
    public static void delete(String id) throws SQLException {
        //delete pake id price
        MenuPrice menu = new MenuPrice();
        Connection con = ConnectionManager.getConnection();
        String query = "update menu_price_list set status=0 where price_id = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,id);
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }


}

