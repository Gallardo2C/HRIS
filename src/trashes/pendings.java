package trashes;

import Admin.adminCrud;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import trashes.functions;


public class pendings {

    public static void createRequest(config conf, String employeeUsername) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- Submit Employee Create Request ---");

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

    functions.viewDepartment(conf);  // Show departments
    System.out.print("Enter Department ID: ");
    int deptId = sc.nextInt();
    sc.nextLine(); // consume newline

    System.out.print("Hire Date (YYYY-MM-DD): ");
    String hireDate = sc.nextLine();

    System.out.print("Salary: ");
    int salary = sc.nextInt();
    sc.nextLine(); // consume newline

    String sql = "INSERT INTO requests (original_id, first_name, last_name, date_of_birth, gender, position, department_id, hire_date, salary, action, requested_by, status) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        conf.addRecord(sql, 
                       null,       // original_id null for new employee
                       firstName,
                       lastName,
                       dob,
                       gender,
                       position,
                       deptId,
                       hireDate,
                       salary,
                       "CREATE",   // action
                       employeeUsername,
                       "PENDING"); // default status

        System.out.println(" Request submitted successfully! Waiting for admin approval.");

    } catch (Exception e) {
        System.out.println(" Failed to submit request: " + e.getMessage());
    }
}
    
    public static void requestDelete(config conf, String employeeUsername) {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n--- Submit Delete Request ---");

    // Show all employees
    adminCrud.viewEmployeesWithDepartment(conf);

    System.out.print("Enter Employee ID to delete: ");
    int employeeId = sc.nextInt();
    sc.nextLine();

    // Confirm deletion
    System.out.print("Are you sure you want to request deletion of this employee? (yes/no): ");
    String confirm = sc.nextLine();

    if (!confirm.equalsIgnoreCase("yes")) {
        System.out.println(" Deletion request cancelled.");
        return;
    }

    String sql = "INSERT INTO requests (original_id, action, requested_by, status) VALUES (?, ?, ?, ?)";

    try {
        conf.addRecord(sql,
                       employeeId,  // original_id
                       "DELETE",    // action
                       employeeUsername,
                       "PENDING");  // status

        System.out.println(" Delete request submitted successfully! Waiting for admin approval.");

    } catch (Exception e) {
        System.out.println(" Failed to submit delete request: " + e.getMessage());
    }
}
    
public static void updateRequest(config conf, String employeeUsername) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- Submit Employee Update Request ---");

    // Show existing employees
   adminCrud.viewEmployeesWithDepartment(conf);

    System.out.print("Enter Employee ID to update: ");
    int employeeId = sc.nextInt();
    sc.nextLine(); // consume newline

    // Ask for fields to update (can leave blank)
    System.out.print("New First Name (leave blank to keep current): ");
    String firstName = sc.nextLine();

    System.out.print("New Last Name (leave blank to keep current): ");
    String lastName = sc.nextLine();

    System.out.print("New Date of Birth (leave blank to keep current): ");
    String dob = sc.nextLine();

    System.out.print("New Gender (leave blank to keep current): ");
    String gender = sc.nextLine();

    System.out.print("New Position (leave blank to keep current): ");
    String position = sc.nextLine();

    functions.viewDepartment(conf);
    System.out.print("Enter New Department ID (0 to keep current): ");
    int deptId = sc.nextInt();
    sc.nextLine();

    System.out.print("New Hire Date (leave blank to keep current): ");
    String hireDate = sc.nextLine();

    System.out.print("New Salary (0 to keep current): ");
    int salary = sc.nextInt();
    sc.nextLine();

    String sql = "INSERT INTO requests (original_id, first_name, last_name, date_of_birth, gender, position, department_id, hire_date, salary, action, requested_by, status) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        conf.addRecord(sql,
                       employeeId,
                       firstName.isEmpty() ? null : firstName,
                       lastName.isEmpty() ? null : lastName,
                       dob.isEmpty() ? null : dob,
                       gender.isEmpty() ? null : gender,
                       position.isEmpty() ? null : position,
                       deptId == 0 ? null : deptId,
                       hireDate.isEmpty() ? null : hireDate,
                       salary == 0 ? null : salary,
                       "UPDATE",
                       employeeUsername,
                       "PENDING");

        System.out.println(" Update request submitted successfully! Waiting for admin approval.");

    } catch (Exception e) {
        System.out.println(" Failed to submit update request: " + e.getMessage());
    }
}   
    
public static void viewPending(config conf){




String sqlView = "SELECT request_id, first_name, last_name, action, status, created_at FROM requests WHERE status = 'PENDING'";
    String[] headers = {"Request ID", "First Name", "Last Name", "Action", "Status", "Submitted At"};
    String[] columns = {"request_id", "first_name", "last_name", "action", "status", "created_at"};

    conf.viewRecords(sqlView, headers, columns);



} 
    
    
    
    
}
