package config;

import java.sql.*;
import java.util.*;

public class config {

    //-----------------------------------------------
    // 🔗 CONNECT TO DATABASE
    //-----------------------------------------------
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Jade\\Documents\\NetBeansProjects\\HRIS (Human Resources Information System)\\Data.db");
            System.out.println("✅ Connection Successful!");
        } catch (Exception e) {
            System.out.println("❌ Connection Failed: " + e.getMessage());
        }
        return con;
    }

    //-----------------------------------------------
    // ➕ ADD RECORD
    //-----------------------------------------------
    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("✅ Record added successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error adding record: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // 👁️ VIEW RECORDS (DYNAMIC)
    //-----------------------------------------------
    public void viewRecords(String sqlQuery, String[] headers, String[] columns) {
        if (headers.length != columns.length) {
            System.out.println("⚠️ Headers and columns mismatch!");
            return;
        }

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("-------------------------------------------------------------");
            for (String header : headers) {
                System.out.printf("%-20s", header);
            }
            System.out.println("\n-------------------------------------------------------------");

            while (rs.next()) {
                for (String col : columns) {
                    String value = rs.getString(col);
                    System.out.printf("%-20s", value != null ? value : "");
                }
                System.out.println();
            }

            System.out.println("-------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("❌ Error viewing records: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // ✏️ UPDATE RECORD
    //-----------------------------------------------
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("✅ Record updated successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error updating record: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // ❌ DELETE RECORD
    //-----------------------------------------------
    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, values);
            pstmt.executeUpdate();
            System.out.println("✅ Record deleted successfully!");

        } catch (SQLException e) {
            System.out.println("❌ Error deleting record: " + e.getMessage());
        }
    }

    //-----------------------------------------------
    // 🔍 FETCH RECORDS (Return as List<Map>)
    //-----------------------------------------------
    public List<Map<String, Object>> fetchRecords(String sql, Object... values) {
        List<Map<String, Object>> records = new ArrayList<>();

        try (Connection conn = connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setPreparedStatementValues(pstmt, values);
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    row.put(meta.getColumnName(i), rs.getObject(i));
                }
                records.add(row);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error fetching records: " + e.getMessage());
        }

        return records;
    }

    //-----------------------------------------------
    // 🧩 HELPER — SET PREPARED STATEMENT VALUES
    //-----------------------------------------------
    private void setPreparedStatementValues(PreparedStatement pstmt, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }
    }

    //-----------------------------------------------
    // 🔑 PASSWORD HASHING (SHA-256)
    //-----------------------------------------------
    public static String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            System.out.println("❌ Error hashing password: " + e.getMessage());
            return null;
        }
    }
}
