/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package debug;

import Admin.adminCrud;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import trashes.admin;
import static trashes.functions.viewDepartment;
import trashes.pendings;



/**
 *
 * @author Jade
 */
public class debug {
    
    // In config class
public static void debugRequest(config conf) {
    Scanner sc = new Scanner(System.in);

    System.out.println("\n--- Debug Request Creation ---");

    System.out.print("First Name: ");
    String fname = sc.nextLine();

    System.out.print("Last Name: ");
    String lname = sc.nextLine();

    System.out.print("Action (Create/Update/Delete): ");
    String action = sc.nextLine();

    System.out.print("Requested By (username): ");
    String requestedBy = sc.nextLine();

    String sql = "INSERT INTO requests (first_name, last_name, action, requested_by) VALUES (?, ?, ?, ?)";
    conf.addRecord(sql, fname, lname, action, requestedBy);

    System.out.println("✅ Debug request added to the requests table.");
}

public static void mahNiga(config conf){

    Scanner sc = new Scanner(System.in);
    
    
    
 System.out.println("Choose ADMIN CHOOSE!");
        System.out.println("1. Reject");
        System.out.println("2. Approve");
        System.out.print("Cjosee:");
         int option = sc.nextInt();
        
        
         
        switch(option){
        
            case 1:
                
                admin.rejectRequest(conf);
                 
                break;
        
            case 2:
                admin.approveRequest(conf);
                break;
                
               
        }

}

public static void testAddRecordAndReturnId(config conf) {
    // Example test insert
    String sql = "INSERT INTO test_table (name) VALUES (?)";

    int newId = conf.addRecordAndReturnId(sql, "Svetlana");

    if (newId != -1) {
        System.out.println("✨ Insert successful! New record ID: " + newId);
    } else {
        System.out.println("❌ Insert failed or no ID returned.");
    }
}
    
public static void debugadminDashboard(config conf) {
    Scanner sc = new Scanner(System.in);

    while (true) { // loop until logout
        System.out.println("\n--- Admin Dashboard ---");
        System.out.println("Select Option");
        System.out.println("1. Create (Register)");
        System.out.println("2. Read (View)");
        System.out.println("3. Update");
        System.out.println("4. Delete");
        System.out.println("5. Pendings");
        System.out.println("6. View All Request");
        System.out.println("7. Register User");
        System.out.println("8. Log Out");
        System.out.print("Option: ");

        int option = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (option) {
            case 1:
                adminCrud.createAdmin(conf);
                break;

            case 2:
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
                admin.adminPending(conf);
                break;

            case 6:
                conf.viewRequests();
                break;

            case 7:
                adminCrud.registerUser(conf);
                break;
                
            case 8:    
                
                System.out.println("\n👋 Logged out!");
                return; // exit loop and method

            default:
                System.out.println("⚠️ Invalid option. Try again.");
        }
    }
}

public static void debugemployeeMenu(config conf) {
    Scanner sc = new Scanner(System.in);
    int option;

    do {
        System.out.println("\n--- EMPLOYEE MENU ---");
        System.out.println("1. View");
        System.out.println("2. Request Create (Register Employee)");
        System.out.println("3. Request Update");
        System.out.println("4. Request Delete");
        System.out.println("5. Log Out");
        System.out.print("Option: ");
        option = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (option) {
            case 1:
                adminCrud.viewEmployeesWithDepartment(conf);
                break;

            case 2:
                pendings.createRequest(conf, null); // replace null with actual input data
                break;

            case 3:
                System.out.print("Enter Employee ID to update: ");
                int updateId = sc.nextInt();
                sc.nextLine();
                pendings.updateRequest(conf, updateId);
                break;

            case 4:
                System.out.print("Enter Employee ID to delete: ");
                int deleteId = sc.nextInt();
                sc.nextLine();
                pendings.requestDelete(conf, deleteId);
                break;

            case 5:
                System.out.println("👋 Logged out.");
                break;

            default:
                System.out.println("⚠️ Invalid option.");
        }
    } while (option != 5);
}




    
    
    
    
    
}
