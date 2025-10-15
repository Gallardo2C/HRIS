
package functions;
import config.config;
import functions.functions;
import functions.pendings;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class users {
    
    
   public static void admin(config conf){
//ADMIN
       Scanner sc = new Scanner(System.in);
       
 System.out.println("Select Option");
        System.out.println("1. Create (Register)");
        System.out.println("2. Read (View)");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("5. Pendings");
        System.out.println("6. Out");
        System.out.print("Option:");
        
        int option = sc.nextInt();
        
        
        
        switch(option){
        
            case 1: 
                 functions.registerAdmin(conf);
                 functions.viewEmployeesWithDepartment(conf);
                 break;
                
            case 2:
                functions.viewEmployeesWithDepartment(conf);
                //functions.viewData(conf);
                break;
                
            case 3:
                functions.updateEmployee2( conf);
                //functions.updateEmployee(conf);
                break;
            case 4:
                
                 functions.deleteAdmin(conf);
                 break;
                
            case 5:
        
                  admin2(conf);
                  break;
            case 6:
                
                System.out.println("\n👋 Logged out!");
               
                
    return; // exits the main method and closes the program
        
        
        }

       
       
       
       
       



} 
    
   
   
   
   
 public static void employeeMenu(config conf) {
        Scanner sc = new Scanner(System.in);
    int option;

    do {
        System.out.println("\n--- EMPLOYEE MENU ---");
        System.out.println("1. Request Create (Register Employee)");
        System.out.println("2. Request Update");
        System.out.println("3. Request Delete");
        System.out.println("4. Log Out");
        System.out.print("Option: ");
        option = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (option) {
            case 1:
                requestCreateEmployee(conf);
                break;
            case 2:
                requestUpdateEmployee(conf);
                break;
            case 3:
                requestDeleteEmployee(conf);
                break;
            case 4:
                System.out.println("👋 Logged out.");
                break;
            default:
                System.out.println("⚠️ Invalid option.");
        }
    } while (option != 4);
    }
 
 
  public static void admin2(config conf){
  
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
                    pendings.viewPendingRequests(conf);
                    break;
                case 2:
                    System.out.print("Enter Pending ID to approve: ");
                    int approveId = sc.nextInt();
                    
                    break;
                case 3:
                    System.out.print("Enter Pending ID to reject: ");
                    int rejectId = sc.nextInt();
                    pendings.rejectRequest(conf, rejectId);
                    break;
                case 4:
                    System.out.println("👋 Logged out.");
                    break;
                default:
                    System.out.println("⚠️ Invalid option.");
            }

        } while (option != 4);
      
      
  
  }
 
  // 🟢 Employee request to create a new employee
private static void requestCreateEmployee(config conf) {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n--- Create Employee Request ---");
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
    System.out.print("Department ID: ");
    int deptId = sc.nextInt();
    sc.nextLine();
    System.out.print("Hire Date (YYYY-MM-DD): ");
    String hireDate = sc.nextLine();
    System.out.print("Salary: ");
    double salary = sc.nextDouble();
    sc.nextLine();

    String newData = String.join(",",
            firstName, lastName, dob, gender, position,
            String.valueOf(deptId), hireDate, String.valueOf(salary));

   String requestedBy = "Employee"; // or get from login context if you have one
pendings.submitPendingRequest(conf, "CREATE", "employees", newData, requestedBy, firstName + " " + lastName);


}

// 🟡 Employee request to update an employee record
public static void requestUpdateEmployee(config conf) {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n--- Update Employee Request ---");

    System.out.print("Enter Employee ID to update: ");
    int empId = sc.nextInt();
    sc.nextLine(); // consume newline

    System.out.println("\n--- Choose field to update ---");
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
            System.out.println("❌ Invalid option.");
            return;
    }

    System.out.print("Enter new value: ");
    String newValue = sc.nextLine();

    // Prepare the pending data record
    String data = field + "=" + newValue;

    System.out.print("Enter your name (Requester): ");
    String requestedBy = sc.nextLine();

    // Submit to pendings instead of updating immediately
    pendings.submitPendingRequest(conf, "UPDATE", "employees", data, requestedBy, String.valueOf(empId));


    System.out.println("📩 Update request submitted for approval!");
}

// 🔴 Employee request to delete an employee record
private static void requestDeleteEmployee(config conf) {
    Scanner sc = new Scanner(System.in);
    System.out.println("\n--- Delete Employee Request ---");
    System.out.print("Enter Employee ID to delete: ");
    int recordId = sc.nextInt();
    
    String requestedBy = "Employee";// Log in like "User"
    pendings.submitPendingRequest(conf, "DELETE", "employees", null, requestedBy, String.valueOf(recordId));
}

  
  
 
    }

 
    
    
    
    
    
    

