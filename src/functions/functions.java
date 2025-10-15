

package functions;

import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class functions {  
   
   public static void updateEmployee2(config conf) {
    Scanner sc = new Scanner(System.in);

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
    
    
    
    
    public static void viewData(config conf) {
    String sqlView = "SELECT * FROM employees";

    // Headers for display (matching your table columns)
    String[] headers = {
        "ID", "First Name", "Last Name", "Birth Date", "Gender",
        "Position", "Dept ID", "Hire Date", "Salary"
    };

    // Actual column names in the table
    String[] columns = {
        "id_num", "first_name", "last_name", "date_of_birth", "gender",
        "position", "department_id", "hire_date", "salary"
    };

    // Use the config's viewRecords method to display
    conf.viewRecords(sqlView, headers, columns);
}
    
    // --- EMPLOYEE SIDE FUNCTIONS (submit pending requests) ---

    public static void registerEmployee(config conf, String requestedBy) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter First Name: ");
        String firstName = sc.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = sc.nextLine();

        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dob = sc.nextLine();

        System.out.print("Enter Gender: ");
        String gender = sc.nextLine();

        System.out.print("Enter Position: ");
        String position = sc.nextLine();

        viewDepartment(conf);
        System.out.print("Enter Department ID: ");
        int deptId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter Hire Date (YYYY-MM-DD): ");
        String hireDate = sc.nextLine();

        System.out.print("Enter Salary: ");
        int salary = sc.nextInt();

        // 📝 Build simple data string (can be JSON later)
        String data = String.format(
            "FirstName=%s, LastName=%s, DOB=%s, Gender=%s, Position=%s, DeptID=%d, HireDate=%s, Salary=%d",
            firstName, lastName, dob, gender, position, deptId, hireDate, salary
        );

        // 📨 Submit request instead of direct insert
        pendings.submitPendingRequest(conf, "CREATE", "employees", data, requestedBy, null);
    }

    public static void updateEmployee(config conf, String requestedBy) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Employee ID to update: ");
        int empId = sc.nextInt();
        sc.nextLine();

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
        sc.nextLine();

        String field = "";

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
                System.out.println("❌ Invalid option!");
                return;
        }

        System.out.print("Enter new value: ");
        String newValue = sc.nextLine();

        // 📝 Build update request string
        String data = String.format("Field=%s, NewValue=%s", field, newValue);

       pendings.submitPendingRequest(conf, "UPDATE", "employees", data, requestedBy, null);

    }

    public static void deleteEmployee(config conf, String requestedBy) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Employee ID to delete: ");
        int empId = sc.nextInt();
        sc.nextLine();

        System.out.print("Are you sure you want to delete this employee? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            pendings.submitPendingRequest(conf, "DELETE", "employees", "Delete employee record", requestedBy, null);

        } else {
            System.out.println("❌ Delete canceled.");
        }
    }

    // --- ADMIN SIDE (direct CRUD stays as-is) ---
    // viewEmployeesWithDepartment, viewDepartment, updateEmployee2, etc. stay unchanged.
    
    // --- ADMIN-ONLY FUNCTIONS ---

public static void registerAdmin(config conf) {
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
        String sql = "DELETE FROM employees WHERE id_num = ?";

        try (Connection conn = config.connectDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, empId);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Admin deleted successfully!");
            } else {
                System.out.println("❌ No admin found with that ID.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error deleting admin: " + e.getMessage());
        }

    } else {
        System.out.println("❌ Delete canceled.");
    }
}
}
