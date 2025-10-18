/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trashes;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static trashes.functions.viewDepartment;

/**
 *
 * @author Jade
 */
public class trashpunk {
    
    
    public static void createAdmin(config conf) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- Register Admin ---");

    System.out.print("First Name: ");
    String firstName = sc.nextLine();

    System.out.print("Last Name: ");
    String lastName = sc.nextLine();

    System.out.print("Date of Birth (YYYY-MM-DD): ");
    String dob = sc.nextLine();

    System.out.print("Gender: ");
    String gender = sc.nextLine();

    System.out.print("Position: ");
    String position = sc.nextLine();

    // Admins will also need a department ID
    viewDepartment(conf);
    System.out.print("Enter Department ID: ");
    int deptId = sc.nextInt();
    sc.nextLine();

    System.out.print("Hire Date (YYYY-MM-DD): ");
    String hireDate = sc.nextLine();

    System.out.print("Salary: ");
    int salary = sc.nextInt();
    sc.nextLine();

    String sql = "INSERT INTO employees(first_name, last_name, date_of_birth, gender, position, department_id, hire_date, salary) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = config.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, firstName);
        pstmt.setString(2, lastName);
        pstmt.setString(3, dob);
        pstmt.setString(4, gender);
        pstmt.setString(5, position);
        pstmt.setInt(6, deptId);
        pstmt.setString(7, hireDate);
        pstmt.setInt(8, salary);

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            System.out.println("✅ Admin registered successfully!");
        } else {
            System.out.println("❌ Failed to register admin.");
        }

    } catch (SQLException e) {
        System.out.println("❌ Error registering admin: " + e.getMessage());
    }
}

    public static void deleteAdmin(config conf) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- Delete Admin ---");
    System.out.print("Enter Employee ID of admin to delete: ");
    int empId = sc.nextInt();
    sc.nextLine();

    System.out.print("Are you sure you want to delete this admin? (Y/N): ");
    String confirm = sc.nextLine();

    if (confirm.equalsIgnoreCase("Y")) {
        // Check if admin exists before deleting
        String checkSql = "SELECT first_name, last_name, position FROM employees WHERE id_num = ?";
        List<Map<String, Object>> result = conf.fetchRecords(checkSql, empId);

        if (result.isEmpty()) {
            System.out.println("❌ No admin found with that ID.");
            return;
        }

        Map<String, Object> admin = result.get(0);
        System.out.println("\n🧾 Admin Found:");
        System.out.println("Name: " + admin.get("first_name") + " " + admin.get("last_name"));
        System.out.println("Position: " + admin.get("position"));
        System.out.println("-------------------------------------");

        // Proceed with deletion using your config helper
        conf.deleteRecord("DELETE FROM employees WHERE id_num = ?", empId);

    } else {
        System.out.println("❌ Delete canceled.");
    }
}
    
}
