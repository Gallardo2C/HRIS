package functions;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class pendings {

    // Represents one pending request
    static class PendingRequest {
        int id;
        String action;       // "CREATE", "UPDATE", "DELETE"
        String table;        // Table name (e.g., employees)
        String newData;      // JSON or String representing new data
        String requestedBy;  // Employee username or ID
        String employee;     // Target employee if relevant
        String status;       // "Pending", "Approved", "Rejected"

        public PendingRequest(int id, String action, String table, String newData, String requestedBy, String employee) {
            this.id = id;
            this.action = action;
            this.table = table;
            this.newData = newData;
            this.requestedBy = requestedBy;
            this.employee = employee;
            this.status = "Pending";
        }
    }

    private static List<PendingRequest> pendingList = new ArrayList<>();
    private static int nextId = 1;

    // Called when an employee submits a CRUD request
   public static void submitPendingRequest(config conf, String action, String table, String data, String requestedBy, String employee) {
        String sql = "INSERT INTO pendings (action, new_data, requested_by, employee, status) VALUES (?, ?, ?, ?, 'PENDING')";

        try (Connection conn = conf.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, action);
            pstmt.setString(2, data);
            pstmt.setString(3, requestedBy);
            pstmt.setString(4, employee);

            pstmt.executeUpdate();
            System.out.println("✅ Pending request submitted successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error submitting request: " + e.getMessage());
        }
    }
   
   

    // Admin views all pending requests
     public static void viewPendingRequests(config conf) {
        String sql = "SELECT * FROM pendings WHERE status = 'Pending'";

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Pending Requests ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println(
                    "ID: " + rs.getInt("pending_id") +
                    " | Action: " + rs.getString("action") +
                    " | Employee: " + rs.getString("employee") +
                    " | Requested By: " + rs.getString("requested_by") +
                    " | Status: " + rs.getString("status")
                );
            }

            if (!hasData)
                System.out.println("📭 No pending requests found.");

        } catch (SQLException e) {
            System.out.println("❌ Error viewing pendings: " + e.getMessage());
        }
    }


    // Admin approves a pending request
 private static void applyUpdate(config conf, int employeeId, String data) {
    // Expecting format like "field=value"
    if (data == null || !data.contains("=")) {
        System.out.println("⚠️ Invalid update data format.");
        return;
    }

    String[] parts = data.split("=", 2);
    String field = parts[0].trim();
    String value = parts[1].trim();

    // Check if numeric (so we don’t quote numbers)
    boolean isNumeric = value.matches("-?\\d+(\\.\\d+)?");
    if (!isNumeric) {
        value = "'" + value + "'";
    }

    String sql = "UPDATE employees SET " + field + " = " + value + " WHERE employee_id = ?";

    try (Connection conn = conf.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, employeeId);
        pstmt.executeUpdate();
        System.out.println("✅ Employee ID " + employeeId + " updated successfully.");

    } catch (SQLException e) {
        System.out.println("❌ Error applying UPDATE: " + e.getMessage());
    }
}

    // Admin rejects a pending request
      public static void rejectRequest(config conf, int pendingId) {
        String sql = "UPDATE pendings SET status = 'Rejected' WHERE pending_id = ?";
        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pendingId);
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("❌ Request ID " + pendingId + " rejected.");
            else
                System.out.println("⚠️ Request not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error rejecting request: " + e.getMessage());
        }
    }
private static void applyCreate(config conf, String data) {
        // expected format: "first_name=John,last_name=Doe,gender=Male,..."
        String[] fields = data.split(",");
        String[] cols = new String[fields.length];
        String[] vals = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            String[] kv = fields[i].split("=");
            cols[i] = kv[0].trim();
            vals[i] = kv[1].trim();
        }

        StringBuilder sql = new StringBuilder("INSERT INTO employees(");
        for (int i = 0; i < cols.length; i++) {
            sql.append(cols[i]);
            if (i < cols.length - 1) sql.append(", ");
        }
        sql.append(") VALUES(");
        for (int i = 0; i < vals.length; i++) {
            sql.append("?");
            if (i < vals.length - 1) sql.append(", ");
        }
        sql.append(")");

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < vals.length; i++) {
                pstmt.setString(i + 1, vals[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error applying CREATE: " + e.getMessage());
        }
    }
 /*
private static void applyUpdate(config conf, int employeeId, String data) {
        String[] fields = data.split(",");
        StringBuilder sql = new StringBuilder("UPDATE employees SET ");
        for (int i = 0; i < fields.length; i++) {
            sql.append(fields[i]);
            if (i < fields.length - 1) sql.append(", ");
        }
        sql.append(" WHERE id_num = ?");

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error applying UPDATE: " + e.getMessage());
        }
    } */

 private static void applyDelete(config conf, int employeeId) {
        String sql = "DELETE FROM employees WHERE id_num = ?";
        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("❌ Error applying DELETE: " + e.getMessage());
        }
    }
    
}
