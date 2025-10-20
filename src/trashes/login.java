
package trashes;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import trashes.admin;
import debug.debug;

public class login {
    
    
    
    public static String login(config conf) throws InterruptedException {
    Scanner sc = new Scanner(System.in);
    String role = null;
    String username = null;
    
        System.out.println("--------Log In----------");

    do {
        System.out.print("Enter Username: ");
        username = sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        
        System.out.println("-----------------------");
        
        
        String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

        try (Connection con = config.connectDB();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ResultSet rs = ps.executeQuery();

            //System.out.println("Debug hash: " + hashPassword(password));

            if (rs.next()) {
                role = rs.getString("role");
                System.out.println("\n Login successful! Welcome, " + username + " (" + role + ")\n");
                functions.sleepOne();
            } else {
                System.out.println("\n Invalid username or password. Try again.\n");
               functions.sleepOne();
            }

        } catch (SQLException e) {
            System.out.println(" Database error: " + e.getMessage());
        }

    } while (role == null);

   
    if ("admin".equalsIgnoreCase(role)) {
       
        System.out.println("Opening Admin Menu...");
         functions.sleepOnePointFive();
        functions.clearScreen();
        debug.debugadminDashboard(conf);
       //admin.adminDashboard(conf);
        
        
    } else if ("employee".equalsIgnoreCase(role)) {
        
        
        System.out.println("Opening Employee Menu...");
          functions.sleepOnePointFive();
         functions.clearScreen();
        employees.employeeMenu(conf);
    }

    return role;
}

 
       public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // convert to hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password: " + e.getMessage());
        }
    }
    
    
    
    }
    
    
    
    
    
    
    

