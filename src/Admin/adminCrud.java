/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Admin;

import config.config;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import static trashes.functions.viewDepartment;

/**
 *
 * @author Jade
 */
public class adminCrud {
    
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

    int newEmployeeId = conf.addRecordAndReturnId(sql, firstName, lastName, dob, gender, position, deptId, hireDate, salary);

    if (newEmployeeId != -1) {
        System.out.println("✅ Employee registered successfully!");
        System.out.println("🆔 New Employee ID: " + newEmployeeId);

        // Retrieve and display full employee info
        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(
                 "SELECT e.id_num, e.first_name, e.last_name, e.date_of_birth, e.gender, e.position, " +
                 "d.department_name, e.hire_date, e.salary " +
                 "FROM employees e " +
                 "LEFT JOIN departments d ON e.department_id = d.department_id " +
                 "WHERE e.id_num = ?")) {  ///HEEEEEEEEEEEEEEEEREEEEEEEEEEEEE!!!!

            pstmt.setInt(1, newEmployeeId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("=====================================");
                    System.out.println("       🧾 Employee Information       ");
                    System.out.println("=====================================");
                    System.out.println("Employee ID : " + rs.getInt("id_num"));
                    System.out.println("Name        : " + rs.getString("first_name") + " " + rs.getString("last_name"));
                    System.out.println("DOB         : " + rs.getString("date_of_birth"));
                    System.out.println("Gender      : " + rs.getString("gender"));
                    System.out.println("Position    : " + rs.getString("position"));
                    System.out.println("Department  : " + rs.getString("department_name"));
                    System.out.println("Hire Date   : " + rs.getString("hire_date"));
                    System.out.println("Salary      : " + rs.getInt("salary"));
                    System.out.println("=====================================");
                }
            }

        } catch (SQLException e) {
            System.out.println("❌ Error retrieving employee info: " + e.getMessage());
        }

    } else {
        System.out.println("❌ Failed to register Employee.");
    }
}
   
public static void viewEmployeesWithDepartment(config conf) {
    String sql = "SELECT e.id_num, "
           + "e.first_name, "
           + "e.last_name, "
           + "e.date_of_birth, "
           + "e.gender, "
           + "e.position, "
           + "d.department_name, "
           + "e.hire_date, "
           + "e.salary "
           + "FROM employees e "
           + "LEFT JOIN departments d ON e.department_id = d.department_id";

    try (Connection con = config.connectDB();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        System.out.println("---------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-15s %-10s %-15s %-20s %-15s %-10s%n",
                "ID", "First Name", "Last Name", "Birth Date", "Gender", "Position", "Department", "Hire Date", "Salary");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

        boolean hasData = false;
        while (rs.next()) {
            System.out.printf("%-5d %-15s %-15s %-15s %-10s %-15s %-20s %-15s %-10d%n",
                    rs.getInt("id_num"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("date_of_birth"),
                    rs.getString("gender"),
                    rs.getString("position"),
                    rs.getString("department_name") != null ? rs.getString("department_name") : "N/A",
                    rs.getString("hire_date"),
                    rs.getInt("salary"));
            hasData = true;
        }

        if (!hasData) {
            System.out.println("⚠️ No employee records found.");
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------------------");

    } catch (SQLException e) {
        System.out.println("❌ Error viewing employees: " + e.getMessage());
    }
}
   
 public static void updateEmployee2(config conf) {
    Scanner sc = new Scanner(System.in);

    
    viewEmployeesWithDepartment(conf);
    System.out.print("Enter Employee ID to update: ");
    int id = sc.nextInt();
    sc.nextLine(); // consume newline

    // --- Show employee info before update ---
    String viewSql = "SELECT e.id_num, e.first_name, e.last_name, e.date_of_birth, " +
                     "e.gender, e.position, d.department_name, e.hire_date, e.salary " +
                     "FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "WHERE e.id_num = ?";

    try (Connection conn = config.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(viewSql)) {

        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("\n--- Employee Details ---");
            System.out.println("ID: " + rs.getInt("id_num"));
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Date of Birth: " + rs.getString("date_of_birth"));
            System.out.println("Gender: " + rs.getString("gender"));
            System.out.println("Position: " + rs.getString("position"));
            System.out.println("Department: " + rs.getString("department_name"));
            System.out.println("Hire Date: " + rs.getString("hire_date"));
            System.out.println("Salary: " + rs.getDouble("salary"));
            System.out.println("-------------------------");
        } else {
            System.out.println("No employee found with that ID.");
            return;
        }

    } catch (SQLException e) {
        System.out.println("Error retrieving employee: " + e.getMessage());
        return;
    }

    // --- Update Menu ---
    System.out.println("\n--- Update Menu ---");
    System.out.println("1. First Name");
    System.out.println("2. Last Name");
    System.out.println("3. Date of Birth");
    System.out.println("4. Gender");
    System.out.println("5. Position");
    System.out.println("6. Department ID");
    System.out.println("7. Hire Date");
    System.out.println("8. Salary");
    System.out.print("Enter option: ");

    int option = sc.nextInt();
    sc.nextLine(); // consume newline

    // ✅ declare field OUTSIDE switch so it's visible below
    String field = "";//no intial value pleasee ugnore

    switch (option) {
        case 1:
            field = "first_name";
            break;
        case 2:
            field = "last_name";
            break;
        case 3:
            field = "date_of_birth";
            break;
        case 4:
            field = "gender";
            break;
        case 5:
            field = "position";
            break;
        case 6:
            field = "department_id";
            break;
        case 7:
            field = "hire_date";
            break;
        case 8:
            field = "salary";
            break;
        default:
            System.out.println("Invalid option.");
            return;
    }

    System.out.print("Enter new value: ");
    String newValue = sc.nextLine();

    String updateSql = "UPDATE employees SET " + field + " = ? WHERE id_num = ?";

    try (Connection conn = config.connectDB();
         PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

        pstmt.setString(1, newValue);
        pstmt.setInt(2, id);

        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Employee record updated successfully!");
        } else {
            System.out.println("Failed to update employee record.");
        }

    } catch (SQLException e) {
        System.out.println("Error updating employee: " + e.getMessage());
    }
}
   
public static void deleteAdmin(config conf) {
    Scanner sc = new Scanner(System.in);

    
    viewEmployeesWithDepartment(conf);
    
    System.out.println("\n--- Delete Employee ---");
    System.out.print("Enter Employee ID to delete: ");
    int empId = sc.nextInt();
    sc.nextLine();

    System.out.print("Are you sure you want to delete this Employee? (Y/N): ");
    String confirm = sc.nextLine();

    if (!confirm.equalsIgnoreCase("Y")) {
        System.out.println("❌ Delete canceled.");
        return;
    }

   String checkSql = 
    "SELECT e.id_num, e.first_name, e.last_name, e.date_of_birth, e.gender, " +
    "e.position, d.department_name, e.hire_date, e.salary " +
    "FROM employees e " +
    "LEFT JOIN departments d ON e.department_id = d.department_id " +
    "WHERE e.id_num = ?";

    List<Map<String, Object>> result = conf.fetchRecords(checkSql, empId);

    if (result.isEmpty()) {
        System.out.println("❌ No Employee found with that ID.");
        return;
    }

    Map<String, Object> admin = result.get(0);

    // 🧾 Display full info card before deletion
    System.out.println("=====================================");
    System.out.println("        ⚠️ Employee to be Deleted       ");
    System.out.println("=====================================");
    System.out.println("Employee ID : " + admin.get("id_num"));
    System.out.println("Name        : " + admin.get("first_name") + " " + admin.get("last_name"));
    System.out.println("DOB         : " + admin.get("date_of_birth"));
    System.out.println("Gender      : " + admin.get("gender"));
    System.out.println("Position    : " + admin.get("position"));
    System.out.println("Department  : " + admin.get("department_name"));
    System.out.println("Hire Date   : " + admin.get("hire_date"));
    System.out.println("Salary      : " + admin.get("salary"));
    System.out.println("=====================================");

    // ✅ Proceed with deletion using config helper
    conf.deleteRecord("DELETE FROM employees WHERE id_num = ?", empId);
}   
  
 
    
public static void registerUser(config conf) {
    Scanner sc = new Scanner(System.in);

    System.out.print("Enter Username: ");
    String username = sc.nextLine();

    System.out.print("Enter Password: ");
    String password = sc.nextLine();

    System.out.print("Enter Role (admin/employee): ");
    String role = sc.nextLine().toLowerCase();

    // Validate role
    if (!role.equals("admin") && !role.equals("employee")) {
        System.out.println("⚠️ Invalid role. Must be 'admin' or 'employee'.");
        return;
    }

    // Hash password
    String hashedPassword = hashPassword(password);

    String sql = "INSERT INTO users(username, password, role) VALUES (?, ?, ?)";
    try (Connection con = config.connectDB();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, hashedPassword);
        ps.setString(3, role);

        ps.executeUpdate();
        System.out.println("✅ User registered successfully!");

    } catch (SQLException e) {
        System.out.println("❌ Error registering user: " + e.getMessage());
    }
}

// Helper method for hashing
public static String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error hashing password: " + e.getMessage());
    }
}
    
    
    
    
    
    
    
    
}
