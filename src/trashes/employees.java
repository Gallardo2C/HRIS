/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package trashes;

import config.config;
import java.util.Scanner;
import Admin.adminCrud;
/**
 *
 * @author Jade
 */
public class employees {
    
     public static void employeeMenu(config conf) {
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
                
                System.out.println("Viewing....");
                System.out.println("Treat this As an Employee View");
                adminCrud.viewEmployeesWithDepartment(conf);
                
                break;
            
            case 2:
                System.out.println("Registering...");
                pendings.createRequest(conf, null);
                
                
                
                //Register Request
                break;
            case 3:
                System.out.println("Updating...");
                pendings.updateRequest(conf, null);
                //Update Request
                break;
            case 4:
                System.out.println("Deleting...");
                pendings.requestDelete(conf, null);
                //Delete Request
                break;
            case 5:
                System.out.println("👋 Logged out.");
                break;
            default:
                System.out.println("⚠️ Invalid option.");
        }
    } while (option != 4);
    }
    
    
    
    
    
    
    
}
