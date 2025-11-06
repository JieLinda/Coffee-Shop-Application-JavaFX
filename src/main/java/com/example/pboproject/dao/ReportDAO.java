package com.example.pboproject.dao;

import com.example.pboproject.utils.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ReportDAO {
    public ObservableList<ObservableList<String>> getTopSellingDrinks(Date startDate, Date endDate) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String query = "SELECT ml.product_id, ml.product_name, SUM(tpd.product_quantity) AS total_quantity_sold, SUM(tpd.total_price) AS total_sales_amount " +
                "FROM transaction_product_details tpd " +
                "INNER JOIN menu_price_list m ON tpd.price_id = m.price_id " +
                "INNER JOIN menu_list ml ON m.product_id = ml.product_id " +
                "INNER JOIN transaction t ON tpd.transaction_number = t.transaction_number " +
                "WHERE ml.product_type IN ('COFFEE', 'CREAM') " +
                "AND (t.transaction_date BETWEEN ? AND ?) " +
                "GROUP BY tpd.price_id, ml.product_id, ml.product_name " +
                "ORDER BY total_quantity_sold DESC " +
                "LIMIT 5;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getSalesBySizes(Date startDate, Date endDate) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String query = "SELECT mpl.size AS size, SUM(tpd.product_quantity) AS total_sold " +
                "FROM transaction_product_details tpd " +
                "JOIN menu_price_list mpl ON tpd.price_id = mpl.price_id " +
                "JOIN transaction t ON tpd.transaction_number = t.transaction_number " +
                "WHERE (t.transaction_date BETWEEN ? AND ?) " +
                "GROUP BY mpl.size " +
                "ORDER BY total_sold DESC;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getToppingPopularity(Date startDate, Date endDate) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String query = "SELECT ml.product_name AS topping_name, SUM(tpd.product_quantity) AS total_quantity_sold " +
                "FROM transaction_product_details tpd " +
                "INNER JOIN menu_price_list m ON tpd.price_id = m.price_id " +
                "INNER JOIN menu_list ml ON m.product_id = ml.product_id " +
                "INNER JOIN transaction t ON tpd.transaction_number = t.transaction_number " +
                "WHERE ml.product_type = 'ADDONS' " +
                "AND (t.transaction_date BETWEEN ? AND ?) " +
                "GROUP BY ml.product_name " +
                "ORDER BY total_quantity_sold DESC " +
                "LIMIT 5;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getTopPaymentMethods(Date startDate, Date endDate) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String query = "SELECT pm.payment_method, COUNT(t.transaction_number) AS jumlah_transaksi " +
                "FROM transaction t " +
                "JOIN payment_method pm ON t.payment_id = pm.payment_id " +
                "WHERE (t.transaction_date BETWEEN ? AND ?) " +
                "GROUP BY pm.payment_method " +
                "ORDER BY jumlah_transaksi DESC " +
                "LIMIT 5;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getSalesReportByPeriod(Date startDate, Date endDate) {
        String query = "SELECT t.transaction_date, t.transaction_number, t.total_transaction " +
                "FROM transaction t " +
                "WHERE t.transaction_date BETWEEN ? AND ? " +
                "ORDER BY t.transaction_date;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getTopSellingProductsByPeriod(Date startDate, Date endDate) {
        String query = "SELECT ml.product_id, ml.product_name, SUM(tpd.product_quantity) AS total_quantity_sold, SUM(tpd.total_price) AS total_sales_amount " +
                "FROM transaction_product_details tpd " +
                "INNER JOIN menu_price_list mpl ON tpd.price_id = mpl.price_id " +
                "INNER JOIN menu_list ml ON mpl.product_id = ml.product_id " +
                "INNER JOIN transaction t ON tpd.transaction_number = t.transaction_number " +
                "WHERE t.transaction_date BETWEEN ? AND ? " +
                "GROUP BY ml.product_id, ml.product_name " +
                "ORDER BY total_quantity_sold DESC;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getMostActiveMembersByPeriod(Date startDate, Date endDate) {
        String query = "SELECT m.member_id, m.member_name, COUNT(t.transaction_number) AS transaction_count, SUM(t.total_transaction) AS total_spent " +
                "FROM transaction t " +
                "INNER JOIN membership m ON t.member_id = m.member_id " +
                "WHERE t.transaction_date BETWEEN ? AND ? " +
                "GROUP BY m.member_id, m.member_name " +
                "ORDER BY transaction_count DESC;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    public ObservableList<ObservableList<String>> getPromoUsageByPeriod(Date startDate, Date endDate) {
        String query = "SELECT d.discount_id, d.discount_pct, COUNT(tpd.discount_id) AS usage_count " +
                "FROM discount d " +
                "INNER JOIN transaction_product_details tpd ON d.discount_id = tpd.discount_id " +
                "INNER JOIN transaction t ON tpd.transaction_number = t.transaction_number " +
                "WHERE t.transaction_date BETWEEN ? AND ? " +
                "GROUP BY d.discount_id, d.discount_pct " +
                "ORDER BY usage_count DESC;";
        return executeQueryWithDateRange(query, startDate, endDate);
    }

    private ObservableList<ObservableList<String>> executeQueryWithDateRange(String query, Date startDate, Date endDate) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setDate(1, startDate);
            ps.setDate(2, endDate);

            try (ResultSet rs = ps.executeQuery()) {
                int columnCount = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }



    public List<String> getColumnNames(String reportType) {
        switch (reportType) {
            case "Top 5 Selling Drinks":
                return List.of("ID", "Produk", "Jumlah Terjual", "Total Penjualan (Rp)");
            case "Sales by Sizes":
                return List.of("Size", "Total Sold");
            case "Topping Popularity":
                return List.of("Product Name", "Jumlah Terjual");
            case "Top 5 Payment Methods":
                return List.of("Payment Method", "Jumlah Transaksi");
            case "Sales Report by Period":
                return List.of("Transaction Date", "Transaction Number", "Total Transaction");
            case "Top Selling Products by Period":
                return List.of("Product ID", "Product Name", "Total Quantity Sold", "Total Sales Amount");
            case "Most Active Members by Period":
                return List.of("Member ID", "Member Name", "Transaction Count", "Total Spent");
            case "Promo Usage by Period":
                return List.of("Discount ID", "Discount Percentage", "Usage Count");
            default:
                return new ArrayList<>();
        }
    }


    private ObservableList<ObservableList<String>> executeQuery(String query) {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            int columnCount = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
}
