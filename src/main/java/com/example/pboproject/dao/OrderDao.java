package com.example.pboproject.dao;

import com.example.pboproject.beans.*;
import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;


public class OrderDao {
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
    public static ObservableList<PaymentMethod> reachPaymentMethod(Connection con, String id, String method, String bank, String tableName) throws SQLException {
        ObservableList<PaymentMethod> dataColumn = FXCollections.observableArrayList();
        Statement st = null;
        ResultSet rs = null;
        String columns = id+","+method+","+bank;
        String q = "SELECT DISTINCT "+columns+" from "+tableName+" where status = 1";
        try {
            st = con.createStatement();
            rs = st.executeQuery(q);
            while (rs.next()) {
                PaymentMethod p = new PaymentMethod();
                p.setPaymentId(rs.getInt(id));
                p.setPaymentMethod(rs.getString(method));
                p.setBank(rs.getString(bank));
                dataColumn.add(p);
            }
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
        return dataColumn;
    }




        public static <T> ObservableList<T> getWhere (Connection con, String toSelectColumnName, String tableName, String toSearchColumnName, String toSearch, Class<T> type) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs =null;
        ObservableList<T> data = FXCollections.observableArrayList();
        String q = "SELECT " + toSelectColumnName + " FROM " + tableName + " WHERE " + toSearchColumnName + " = ? AND status = 1";
        try{
            ps = con.prepareStatement(q);
            ps.setString(1, toSearch);

            rs = ps.executeQuery();
            while (rs.next()) {
                data.add(rs.getObject(toSelectColumnName, type));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (rs!=null&&ps!=null) ConnectionManager.close(ps,rs);
            ConnectionManager.closeConnection(con);
        }
        return data;
    }

        public static <T> T getValueById (Connection con, String columnName, String tableName, String toSearchColumnName, String toSearch, Class<T> type) throws SQLException {
            PreparedStatement ps = null;
            ResultSet rs = null;
            T result = null;
            String q = "select "+columnName+" from "+tableName+" where "+toSearchColumnName+" = ? and status = 1";
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


    public static <T> T getValueByIdInt (Connection con, String columnName, String tableName, String toSearchColumnName, int toSearch, Class<T> type) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        T result = null;
        String q = "select "+columnName+" from "+tableName+" where "+toSearchColumnName+" = ? and status = 1";
        try {
            ps = con.prepareStatement(q);
            ps.setInt(1, toSearch);
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





    public static <T> T getValueByCombination (Connection con, String columnName, String tableName, String toSearchColumnName, String toSearch, String toSearchColumnName2, String toSearch2,  Class<T> type) throws SQLException {
        PreparedStatement ps =null ;
        ResultSet rs =null;
        T result = null;
        String q = "select "+columnName+" from "+tableName+" where "+toSearchColumnName+" = ? and "+toSearchColumnName2+" = ? and status = 1";
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

    public static int getDateID(Connection con, String formattedDate) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<String> number= FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM transaction WHERE transaction_number LIKE ?";
            ps = con.prepareStatement(query);
            ps.setString(1, formattedDate + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                String transNumber = rs.getString("transaction_number");
                number.add(transNumber);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!=null&&ps!=null) ConnectionManager.close(ps,rs);
            ConnectionManager.closeConnection(con);
        }
        return number.size() +1;
    }


    public static ObservableList<Discount> getDiscounts (Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Discount d ;
        ObservableList<Discount> discounts= FXCollections.observableArrayList();
        try {
            String query = "select d.discount_id, d.discount_pct, \n" +
                    "                   (select distinct product_type from menu_list  m \n" +
                    "                    where d.required_type = m.product_id), \n" +
                    "                    ml.product_name, d.payment_id, \n" +
                    "                                        p.payment_method, p.bank from discount d \n" +
                    "                                        left join payment_method p on d.payment_id = p.payment_id \n" +
                    "                    left join menu_list ml on ml.product_id = d.required_product \n" +
                    "                                        where d.date_start < current_date  \n" +
                    "                                        and valid_until_date > current_date and d.status= 1;";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                d = new Discount();
                d.setId(rs.getInt("discount_id"));
                d.setDisc(rs.getDouble("discount_pct"));
                String productType = rs.getString("product_type");
                d.setRequiredType(productType != null ? productType : "");

                String productName = rs.getString("product_name");
                d.setRequiredProduct(productName != null ? productName : "");

                String paymentMethod = rs.getString("payment_method");
                d.setPmethod(paymentMethod != null ? paymentMethod : "");

                String bank = rs.getString("bank");
                d.setBank(bank != null ? bank : "");

                // For integer values, set a default if the value is null (or 0)
                int paymentId = rs.getInt("payment_id");
                if (rs.wasNull()) {
                    d.setPayment_id(-1); // Set default value if null
                } else {
                    d.setPayment_id(paymentId);
                }
                discounts.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (rs!=null&&ps!=null) ConnectionManager.close(ps,rs);
            ConnectionManager.closeConnection(con);
        }
        return discounts;
    }

    public static void EndTransac (Connection con, Transaction transaction) throws SQLException {
        PreparedStatement ps = null;
        String query = "INSERT INTO transaction (transaction_number, transaction_date, transaction_time, customer_name, total_item, " +
                "total_transaction, payment, change, point_disc, point_gain, payment_id, member_id, total_disc, cashier_name, total_price) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try{
            ps = con.prepareStatement(query);
            ps.setString(1, transaction.getNumber());
            ps.setDate(2, Date.valueOf(LocalDate.now()));
            ps.setTime(3, Time.valueOf(LocalTime.now()));
            ps.setString(4, transaction.getCustname());
            ps.setInt(5, transaction.getTotalitem());
            ps.setDouble(6, transaction.getTotalTransaction());
            ps.setDouble(7, transaction.getPayment());
            ps.setDouble(8, transaction.getChange());
            ps.setInt(9, transaction.getPoint_disc());
            ps.setInt(10, transaction.getPointgain());
            ps.setInt(11, transaction.getPayment_method());
            ;
            if (transaction.getMember_id()==-1){
                ps.setNull(12, Types.INTEGER);
            }else {
                ps.setInt(12, transaction.getMember_id());
            }
            ps.setDouble(13, transaction.getTotalDisc());
            ps.setString(14, transaction.getCashier());
            ps.setDouble(15, transaction.getTotalPrice());
            ps.executeUpdate();
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (ps!=null) ConnectionManager.close(ps);
            ConnectionManager.closeConnection(con);
        }
    }

    public static void saveOrders(Connection con, ObservableList<order> data) throws SQLException {
        PreparedStatement ps = null;
        String query = "INSERT INTO transaction_product_details (product_quantity, total_price, total_disc, special_request, price_id, transaction_number, discount_id) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        try{
            ps = con.prepareStatement(query);
            for (order o : data) {
                ps.setInt(1, o.getQty());
                ps.setDouble(2, o.getTotalprice());
                ps.setDouble(3, o.getTotaldisc());
                ps.setString(4, o.getRequest());
                ps.setString(5, o.getPriceid());
                ps.setString(6, o.getTransactionNum());
                if (o.getDicount_id()==-1){
                    ps.setNull(7, Types.INTEGER);
                } else {
                    ps.setInt(7, o.getDicount_id());
                }
                ps.executeUpdate();
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (ps!=null) ConnectionManager.close(ps);
            ConnectionManager.closeConnection(con);
        }
    }




    public static void updatePoints(int id, int points) {
        Connection con = null;
        PreparedStatement ps = null;
        String query = "update membership set total_points = ? where member_id = ?";
        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1,points);
            ps.setInt(2,id);
            ps.executeUpdate();
            ps.close();
        }catch (SQLException e){
            throw new RuntimeException(e);
        } finally {
            if (ps!=null) ConnectionManager.close(ps);
            ConnectionManager.closeConnection(con);
        }
    }



}