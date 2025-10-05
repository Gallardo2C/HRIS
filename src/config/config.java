/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author PC 33
 */
public class config {
    
    // Connection Method to SQLITE
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC"); 
            con = DriverManager.getConnection(
                "jdbc:sqlite:C:/Users/Jade/Documents/NetBeansProjects/Human Resources Information System/hris1.db"
            ); // Establish connection
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Connection Failed: " + e);
        }
        return con;
    }   
    
    // Add a record to the database
    public void addRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them in the prepared statement dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]);
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]);
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]);
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]);
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String
                }
            }

            pstmt.executeUpdate();
            System.out.println("Record added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }
    
    // View all records from a table
    public void viewRecords(String sql) {
        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("===================================================================================================================");
            System.out.printf("| %-5s | %-15s | %-15s | %-12s | %-8s | %-15s | %-10s | %-10s | %-10s |\n",
                    "ID", "First Name", "Last Name", "Birth Date", "Gender", "Position", "Dept ID", "Hire Date", "Salary");
            System.out.println("===================================================================================================================");

            while (rs.next()) {
                int id = rs.getInt("id_num");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                String dob = rs.getString("date_of_birth");
                String gender = rs.getString("gender");
                String position = rs.getString("position");
                int deptId = rs.getInt("department_id");
                String hireDate = rs.getString("hire_date");
                double salary = rs.getDouble("salary");

                System.out.printf("| %-5d | %-15s | %-15s | %-12s | %-8s | %-15s | %-10d | %-10s | %-10.2f |\n",
                        id, fname, lname, dob, gender, position, deptId, hireDate, salary);
            }

            System.out.println("===================================================================================================================");

        } catch (SQLException e) {
            System.out.println("❌ Error viewing records: " + e.getMessage());
        }
    }

    // Update record method
    public void updateRecord(String sql, Object... values) {
        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Loop through the values and set them dynamically
            for (int i = 0; i < values.length; i++) {
                if (values[i] instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) values[i]);
                } else if (values[i] instanceof Double) {
                    pstmt.setDouble(i + 1, (Double) values[i]);
                } else if (values[i] instanceof Float) {
                    pstmt.setFloat(i + 1, (Float) values[i]);
                } else if (values[i] instanceof Long) {
                    pstmt.setLong(i + 1, (Long) values[i]);
                } else if (values[i] instanceof Boolean) {
                    pstmt.setBoolean(i + 1, (Boolean) values[i]);
                } else if (values[i] instanceof java.util.Date) {
                    pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) values[i]).getTime()));
                } else if (values[i] instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) values[i]);
                } else if (values[i] instanceof java.sql.Timestamp) {
                    pstmt.setTimestamp(i + 1, (java.sql.Timestamp) values[i]);
                } else {
                    pstmt.setString(i + 1, values[i].toString()); // Default to String
                }
            }

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Record updated successfully!");
            } else {
                System.out.println("No record found to update.");
            }

        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    // 🔐 Authentication method for admins & employees
    public String authenticateUser(String username, String password) {
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = this.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                System.out.println("✅ Login successful. Role: " + role);
                return role;
            } else {
                System.out.println("❌ Invalid username or password.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());
            return null;
        }
    }
}
