/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trashes;

import Admin.adminCrud;
import config.config;
import static java.awt.Event.INSERT;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import trashes.pendings;
/**
 *
 * @author Jade
 */
public class admin {
    
    
    public static void adminDashboard(config conf){
//ADMIN
       Scanner sc = new Scanner(System.in);
       
       
        System.out.println("Look this is it");
 System.out.println("Select Option");
        System.out.println("1. Create (Register)");
        System.out.println("2. Read (View)");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("5. Pendings");
        System.out.println("6. View All Request");
        System.out.println("7. Out");
        System.out.print("Option:");
        
        int option = sc.nextInt();
        
        
        
        switch(option){
        
            case 1: 
                
                adminCrud.createAdmin(conf);

                 break;
                
            case 2:
                  ////TTHE VIEW
                adminCrud.viewEmployeesWithDepartment(conf);
                
                break;
                
            case 3:
                
                adminCrud.updateEmployee2(conf);

                break;
            case 4:
                
                adminCrud.deleteAdmin(conf);

                 break;
                
            case 5:
                   System.out.println("Pending...");
                   adminPending(conf);
                   
                   
                   
                   
                  break;
            case 6:
                    conf.viewRequests();
                
                    break;
                
            case 7:
                
                System.out.println("\n👋 Logged out!");
                

               
                
    return; // exits the main method and closes the program
        
        
        }

}    
    
   public static void adminPending(config conf){
  
  Scanner sc = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1. View Pending Requests");
            System.out.println("2. Approve Request");
            System.out.println("3. Reject Request");
            System.out.println("4. Log Out");
            System.out.print("Option: ");
            option = sc.nextInt();

            switch (option) {
                case 1:
                    
                    System.out.println("Viewing Pending...");
                    pendings.viewPending(conf);
                    
                    break;
                case 2:
                    System.out.println("Approving...");
                    admin.approveRequest(conf);
                    
                    
                    
                    break;
                case 3:
                   
                    System.out.println("Rejecting...");
                    admin.rejectRequest(conf);
                    
                    
                    break;
                case 4:
                   
                    System.out.println("Logging out...");
                    
                    break;
                default:
                    System.out.println("⚠️ Invalid option.");
            }

        } while (option != 4);
      
      
  
  }   
   
  public static void approveRequest(config conf) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- APPROVE REQUEST ---");

    // Step 1: Show all pending requests
    String sqlView = "SELECT request_id, first_name, last_name, action, status, created_at FROM requests WHERE status = 'PENDING'";
    String[] headers = {"Request ID", "First Name", "Last Name", "Action", "Status", "Submitted At"};
    String[] columns = {"request_id", "first_name", "last_name", "action", "status", "created_at"};

    conf.viewRecords(sqlView, headers, columns);

    System.out.print("\nEnter Request ID to approve: ");
    int requestId = sc.nextInt();
    sc.nextLine(); // consume newline

    try (Connection conn = conf.connectDB()) {
        // Step 2: Fetch the request data
        String fetchSQL = "SELECT * FROM requests WHERE request_id = ?";
        PreparedStatement fetchStmt = conn.prepareStatement(fetchSQL);
        fetchStmt.setInt(1, requestId);
        ResultSet rs = fetchStmt.executeQuery();

        if (!rs.next()) {
            System.out.println("⚠️ Request not found!");
            return;
        }

        String action = rs.getString("action");

        if ("CREATE".equalsIgnoreCase(action)) {
            // --- Handle CREATE ---
            String insertEmployee = "INSERT INTO employees (first_name, last_name, date_of_birth, gender, position, department_id, hire_date, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement empStmt = conn.prepareStatement(insertEmployee)) {
                empStmt.setString(1, rs.getString("first_name"));
                empStmt.setString(2, rs.getString("last_name"));
                empStmt.setString(3, rs.getString("date_of_birth"));
                empStmt.setString(4, rs.getString("gender"));
                empStmt.setString(5, rs.getString("position"));
                empStmt.setObject(6, rs.getObject("department_id"));
                empStmt.setString(7, rs.getString("hire_date"));
                empStmt.setInt(8, rs.getInt("salary"));
                empStmt.executeUpdate();
            }
            System.out.println("✅ CREATE request approved and employee added!");
        } 
        
        else if ("DELETE".equalsIgnoreCase(action)) {
    int empId = rs.getInt("original_id");

    String deleteSQL = "DELETE FROM employees WHERE id_num = ?";
    try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL)) {
        deleteStmt.setInt(1, empId);
        int rows = deleteStmt.executeUpdate();

        if (rows > 0) {
            System.out.println("🗑️ Employee record successfully deleted!");
        } else {
            System.out.println("⚠️ Employee not found or already deleted.");
        }
    }

    // Mark request as approved
    String updateReq = "UPDATE requests SET status = 'APPROVED' WHERE request_id = ?";
    try (PreparedStatement updateReqStmt = conn.prepareStatement(updateReq)) {
        updateReqStmt.setInt(1, requestId);
        updateReqStmt.executeUpdate();
    }

    System.out.println("✅ DELETE request approved and executed!");
}

        else if ("UPDATE".equalsIgnoreCase(action)) {
            // --- Handle UPDATE ---
            int empId = rs.getInt("original_id");

            StringBuilder updateQuery = new StringBuilder("UPDATE employees SET ");
            List<Object> params = new ArrayList<>();

            if (rs.getString("first_name") != null && !rs.getString("first_name").isEmpty()) {
                updateQuery.append("first_name = ?, ");
                params.add(rs.getString("first_name"));
            }
            if (rs.getString("last_name") != null && !rs.getString("last_name").isEmpty()) {
                updateQuery.append("last_name = ?, ");
                params.add(rs.getString("last_name"));
            }
            if (rs.getString("date_of_birth") != null && !rs.getString("date_of_birth").isEmpty()) {
                updateQuery.append("date_of_birth = ?, ");
                params.add(rs.getString("date_of_birth"));
            }
            if (rs.getString("gender") != null && !rs.getString("gender").isEmpty()) {
                updateQuery.append("gender = ?, ");
                params.add(rs.getString("gender"));
            }
            if (rs.getString("position") != null && !rs.getString("position").isEmpty()) {
                updateQuery.append("position = ?, ");
                params.add(rs.getString("position"));
            }
            if (rs.getObject("department_id") != null) {
                updateQuery.append("department_id = ?, ");
                params.add(rs.getObject("department_id"));
            }
            if (rs.getString("hire_date") != null && !rs.getString("hire_date").isEmpty()) {
                updateQuery.append("hire_date = ?, ");
                params.add(rs.getString("hire_date"));
            }
            if (rs.getInt("salary") > 0) {
                updateQuery.append("salary = ?, ");
                params.add(rs.getInt("salary"));
            }

            // Remove trailing comma
            if (!params.isEmpty()) {
                updateQuery.setLength(updateQuery.length() - 2);
            }

            // ✅ Corrected primary key column here
            updateQuery.append(" WHERE id_num = ?");
            params.add(empId);

            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery.toString())) {
                for (int i = 0; i < params.size(); i++) {
                    updateStmt.setObject(i + 1, params.get(i));
                }
                updateStmt.executeUpdate();
            }

            System.out.println("✅ UPDATE request approved and employee information updated!");
        } 
        else {
            System.out.println("⚠️ Unsupported action type: " + action);
        }

        // Step 4: Mark request as approved
        String markApproved = "UPDATE requests SET status = 'APPROVED' WHERE request_id = ?";
        try (PreparedStatement markStmt = conn.prepareStatement(markApproved)) {
            markStmt.setInt(1, requestId);
            markStmt.executeUpdate();
        }

    } catch (SQLException e) {
        System.out.println("❌ Error approving request: " + e.getMessage());
    }
}

 
public static void rejectRequest(config conf) {
    try (Connection conn = conf.connectDB()) {
        if (conn == null) {
            System.out.println("❌ Database connection failed.");
            return;
        }

        System.out.println("Connection Successful");

        // --- Display all pending requests
        String fetchQuery = "SELECT request_id, first_name, last_name, action, status, created_at FROM requests WHERE status = 'PENDING'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(fetchQuery)) {

            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("| %-22s | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                    "Request ID", "First Name", "Last Name", "Action", "Status", "Submitted At");
            System.out.println("--------------------------------------------------------------------------------");

            boolean hasPending = false;
            while (rs.next()) {
                hasPending = true;
                System.out.printf("| %-22d | %-20s | %-20s | %-20s | %-20s | %-20s |\n",
                        rs.getInt("request_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("action"),
                        rs.getString("status"),
                        rs.getString("created_at"));
            }
            System.out.println("--------------------------------------------------------------------------------");

            if (!hasPending) {
                System.out.println("No pending requests found.");
                return;
            }
        }

        // --- Ask which request to reject
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Request ID to reject: ");
        int requestId = sc.nextInt();

        // --- Update status to REJECTED
        String rejectQuery = "UPDATE requests SET status = 'REJECTED' WHERE request_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(rejectQuery)) {
            pstmt.setInt(1, requestId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("❌ Request has been rejected successfully!");
            } else {
                System.out.println("⚠️ No matching request found for that ID.");
            }
        }

    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
    }
}
 




   
    
    
}
