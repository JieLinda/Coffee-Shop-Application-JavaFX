package com.example.pboproject.dao;

import com.example.pboproject.beans.DiscountList;
import com.example.pboproject.beans.PaymentMethod;
import com.example.pboproject.beans.ProductType;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;

public class DiscountDao {
    public static DiscountList get(int id) throws SQLException {
       //ambil 1 diskon spesifik
        ResultSet rs;
        DiscountList disc = new DiscountList();
        Connection con = ConnectionManager.getConnection();
        String query = String.format("select * from discount where discount_id = s% where status = 1;",id);
        PreparedStatement ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        try {
            disc.setDiscId(rs.getInt("discount_id"));
            disc.setDiscPercent(rs.getDouble("discount_pct"));
            disc.setDateStart(rs.getString("date_start"));
            disc.setValidUntilDate(rs.getString("valid_until_date"));
            disc.setRequiredProduct(rs.getString("required_product"));
            disc.setRequiredPaymentMethod(rs.getInt("payment_id"));
            disc.setRequiredType(rs.getString("required_type"));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps,rs);
        }
        return disc;
    }
    public static ArrayList<DiscountList> getAll(Connection con) throws SQLException {
        //ambil semua
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "select * from discount where status = 1 order by discount_id;";
        ArrayList<DiscountList> listDiscount = new ArrayList<>();
        ps = con.prepareStatement(query);
        rs = ps.executeQuery();
        try {
            while(rs.next()) {
                DiscountList disc = new DiscountList();
                disc.setDiscId(rs.getInt("discount_id"));
                disc.setDiscPercent(rs.getDouble("discount_pct"));
                disc.setDateStart(rs.getString("date_start"));
                disc.setValidUntilDate(rs.getString("valid_until_date"));
                disc.setRequiredProduct(rs.getString("required_product"));
                disc.setRequiredPaymentMethod(rs.getInt("payment_id"));
                disc.setRequiredType(rs.getString("required_type"));
                listDiscount.add(disc);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            ConnectionManager.close(ps, rs);
        }
        return listDiscount;
    }
    public static void save(DiscountList disc) throws SQLException {
        //fungsi untuk input data baru dan button save
        Connection con = ConnectionManager.getConnection();
        String query = "Insert into discount (discount_pct, date_start, valid_until_date, payment_id, required_product, required_type) values (?,?::date,?::date,?,?,?);";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setDouble(1,disc.getDiscPercent());
            ps.setString(2,disc.getDateStart());
            ps.setString(3,disc.getValidUntilDate());
            if (disc.getRequiredPaymentMethod()==0) ps.setNull(4, java.sql.Types.INTEGER);
            else ps.setInt(4,disc.getRequiredPaymentMethod());
            if (disc.getRequiredProduct().equalsIgnoreCase("-1")) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, disc.getRequiredProduct());
            if (disc.getRequiredType().equalsIgnoreCase("-1")) ps.setNull(6, Types.VARCHAR);
            else ps.setString(6, disc.getRequiredProduct());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }

    }
    public static void update(DiscountList disc) throws SQLException {
        //fungsi untuk button edit
        Connection con = ConnectionManager.getConnection();
        String query = "update discount set discount_pct= ?, date_start=?::date, valid_until_date=?::date, payment_id = ?, required_product = ?, required_type= ? where discount_id = ?;";
        PreparedStatement ps = con.prepareStatement(query);
        try {
            ps.setDouble(1,disc.getDiscPercent());
            ps.setString(2,disc.getDateStart());
            ps.setString(3,disc.getValidUntilDate());
            if (disc.getRequiredPaymentMethod()==0) ps.setNull(4, java.sql.Types.INTEGER);
            else ps.setInt(4,disc.getRequiredPaymentMethod());
            if (disc.getRequiredProduct().equalsIgnoreCase("-1")) ps.setNull(5, Types.VARCHAR);
            else ps.setString(5, disc.getRequiredProduct());
            if (disc.getRequiredType().equalsIgnoreCase("-1")) ps.setNull(6, Types.VARCHAR);
            else ps.setString(6, disc.getRequiredProduct());
            ps.setInt(7,disc.getDiscId());
            ps.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            ConnectionManager.close(ps);
        }
    }
    public static void delete(int id) throws SQLException {
        //delete menggunakan discount id
        Connection con = ConnectionManager.getConnection();
        String query = "update discount set status=0 where discount_id=?;";
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
    public static <T> ObservableList<T> getAllColumn (Connection con, String columnName, String tableName, Class<T> type) throws SQLException {
        ObservableList<T> dataColumn = FXCollections.observableArrayList();
        Statement st = null;
        ResultSet rs = null;
        String q = "select distinct "+columnName+" from "+tableName+" where status = 1";
        try {
            st = con.createStatement();
            rs = st.executeQuery(q);
            while (rs.next()) {
                dataColumn.add(rs.getObject(columnName, type));
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            assert rs != null;
            rs.close();
            assert st != null;
            st.close();
            ConnectionManager.closeConnection(con);
        }
        return dataColumn;
    }

    public static <T> T getValueByCombination (Connection con, String columnName, String tableName, String toSearchColumnName, String toSearch, String toSearchColumnName2, String toSearch2,  Class<T> type) throws SQLException {
        PreparedStatement ps =null ;
        ResultSet rs =null;
        T result = null;
        String q = "select "+columnName+" from "+tableName+" where "+toSearchColumnName+" = ? and "+toSearchColumnName2+" = ?";
        try {
            ps = con.prepareStatement(q);
            ps.setString(1, toSearch);
            ps.setString(2, toSearch2);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                result = rs.getObject(1,type);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!=null&&ps!=null) ConnectionManager.close(ps,rs);
            ConnectionManager.closeConnection(con);
        }
        return result;
    }

    public static PaymentMethod getPaymentMethod(Connection con, int id) throws SQLException {
        PaymentMethod p = new PaymentMethod();
        PreparedStatement st = null;
        ResultSet rs = null;
        String q = "SELECT * from payment_method where payment_id = ?";
        try {
            st = con.prepareStatement(q);
            st.setInt(1, id);
            rs = st.executeQuery();
            if (rs != null && rs.next()) {
                p.setPaymentId(rs.getInt(id));
                p.setPaymentMethod(rs.getString("payment_method"));
                p.setBank(rs.getString("bank"));
            }
            System.out.println(p.getPaymentMethod()+" - "+p.getBank());
            System.out.println("Get pm dari discount dao");
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            ConnectionManager.closeConnection(con);
        }
        return p;
    }

    public static <T> T getValueById (Connection con, String columnName, String tableName, String toSearchColumnName, String toSearch, Class<T> type) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        String q = "select distinct "+columnName+" from "+tableName+" where "+toSearchColumnName+" = ? and status = 1";
        try {
            ps = con.prepareStatement(q);
            ps.setString(1, toSearch);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) result = rs.getObject(1, type);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!=null&&ps!=null) ConnectionManager.close(ps,rs);
            ConnectionManager.closeConnection(con);
        }
        return result;
    }
}
