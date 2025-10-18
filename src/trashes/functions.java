

package trashes;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class functions {  
  
    
 /*   
public static void viewRequests(config conf, String employeeUsername) {
    String sql = "SELECT request_id, first_name, last_name, action, status, created_at " +
                 "FROM requests " +
                 "WHERE requested_by = '" + employeeUsername + "' " +  // username inserted directly
                 "ORDER BY created_at DESC";

    String[] headers = {"Request ID", "First Name", "Last Name", "Action", "Status", "Submitted At"};
    String[] columns = {"request_id", "first_name", "last_name", "action", "status", "created_at"};

    conf.viewRecords(sql, headers, columns);
}*/

    public static void viewDepartment(config conf){
    
     String sql = "SELECT * FROM departments";

    try (Connection con = config.connectDB();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        System.out.println("=====================================");
        System.out.println("        🏢 Department List");
        System.out.println("=====================================");
        System.out.printf("%-15s %-20s%n", "Department ID", "Department Name");
        System.out.println("-------------------------------------");

        boolean hasData = false;
        while (rs.next()) {
            int id = rs.getInt("department_id");
            String name = rs.getString("department_name");
            System.out.printf("%-15d %-20s%n", id, name);
            hasData = true;
        }

        if (!hasData) {
            System.out.println("No departments found.");
        }

        System.out.println("=====================================");

    } catch (SQLException e) {
        System.out.println("❌ Error fetching departments: " + e.getMessage());
    }
    }

}
