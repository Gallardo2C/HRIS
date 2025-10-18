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
    





    
    
    
    
    
}
