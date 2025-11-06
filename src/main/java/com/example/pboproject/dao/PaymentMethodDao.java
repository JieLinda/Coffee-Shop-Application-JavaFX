package com.example.pboproject.dao;


import java.sql.*;
import java.util.ArrayList;
import com.example.pboproject.beans.*;
import com.example.pboproject.utils.*;

public class PaymentMethodDao {
    public static PaymentMethod get(int id) throws SQLException {
        //mendapatkan payment method tertentu
        ResultSet rs;
        PaymentMethod method = new PaymentMethod();
        Connection con = ConnectionManager.getConnection();
        java.lang.String query = java.lang.String.format("select * from PaymentMethod where member_id = s%;",id);
        PreparedStatement ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        try {
            method.setPaymentId(rs.getInt("payment_id"));
            method.setPaymentMethod(String.valueOf(rs.getString("payment_method")));
            method.setBank(rs.getString("bank"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps,rs);
        }
        return method;
    }
    public static ArrayList<PaymentMethod> getAll(Connection con){
        //mengeluarkan semua payment method
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.lang.String query = "select * from Payment_Method where status=1 order by payment_id;";
        ArrayList<PaymentMethod> listmethod = new ArrayList<>();
        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while(rs.next()) {
                PaymentMethod method = new PaymentMethod();
                method.setPaymentId(rs.getInt("payment_id"));
                method.setPaymentMethod(rs.getString("payment_method"));
                method.setBank(rs.getString("bank"));
                listmethod.add(method);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listmethod;
    }
    public static void save(PaymentMethod PaymentMethod) throws SQLException {
        //input data baru dan fungsi save button
        Connection con = ConnectionManager.getConnection();
        java.lang.String query = "Insert into Payment_Method (payment_method, bank) values (?,?);";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,PaymentMethod.getPaymentMethod());
            ps.setString(2,PaymentMethod.getBank());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }

    }
    public static void update(PaymentMethod method) throws SQLException {
        //fungsi button update
        Connection con = ConnectionManager.getConnection();
        java.lang.String query = "update Payment_Method set payment_method = ?, bank = ? where payment_id = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setString(1,method.getPaymentMethod());
            ps.setString(2,method.getBank());
            ps.setInt(3,method.getPaymentId());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }
    public static void delete(int id) throws SQLException {
        //delete pake idnya payment method
        Connection con = ConnectionManager.getConnection();
        java.lang.String query = "update payment_method set status=0 where payment_id=?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }


}

