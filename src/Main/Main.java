import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import functions.functions;
import functions.login;
import functions.users;

/*
KULANG ANG GURO AND DEPARTMENT IDK,  SOME LOOP, 
Taronga ang approval og butangi 
Semi-permanent log in until log out, loop
users registration,
OTEN B===D
*/

public class Main {
    
    
    public static void main(String[] args) {
        config conf = new config();
        Scanner sc = new Scanner(System.in);
       
        
        
        String role = login.login(conf);
  
        
        
        
        if (role != null) {
            if (role.equalsIgnoreCase("admin")) {
                System.out.println("🔑 Welcome, Admin! You have full access.");
                // load admin menua
                
                
                
                 users.admin(conf);
                
                
                
            } else if (role.equalsIgnoreCase("employee")) {
                System.out.println("👤 Welcome, Employee! Limited access granted.");
                // load employee menu
                
                users.employeeMenu(conf);
                
            }
        } else {
            System.out.println("🚫 Login failed.");
        }
        
    }
}

