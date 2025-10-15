
package functions;
import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class login {
    
    public static String login(config conf) {
        
        
         Scanner sc = new Scanner(System.in);
        String role = null;

        do {
            System.out.print("Enter Username: ");
            String username = sc.nextLine();

            System.out.print("Enter Password: ");
            String password = sc.nextLine();

            String sql = "SELECT role FROM users WHERE username = ? AND password = ?";

            try (Connection con = config.connectDB();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, hashPassword(password));
               // ps.setString(2, password);
                ResultSet rs = ps.executeQuery();
                System.out.println("Debug hash: " + hashPassword(password));
                if (rs.next()) {
                    role = rs.getString("role");
                    System.out.println("\n✅ Login successful! Welcome, " + username + " (" + role + ")\n");

                    // Handle roles *inside* this function
                    if ("admin".equalsIgnoreCase(role)) {
                        System.out.println("Opening Admin Menu...");
                        // call your admin functions here
                    } else if ("employee".equalsIgnoreCase(role)) {
                        System.out.println("Opening Employee Menu...");
                        // call your employee functions here
                    }

                } else {
                    System.out.println("\n❌ Invalid username or password. Try again.\n");
                }

            } catch (SQLException e) {
                System.out.println("❌ Database error: " + e.getMessage());
            }

        } while (role == null); // keep looping until valid login

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
    
    
    
    
    
    
    

