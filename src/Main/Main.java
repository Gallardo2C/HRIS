import config.config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import trashes.functions;
import trashes.login;
import trashes.admin;
import Admin.adminCrud;
import debug.debug;
import trashes.pendings;
import trashes.admin;
import trashes.employees;

/*


 SOME LOOP, 
Semi-permanent log in until log out, loop
users registration,
OTEN B===D
*/

public class Main {
    
    
    
    
    
    public static void main(String[] args){
    config conf = new config();
    Scanner sc = new Scanner(System.in);
    
    
        System.out.println("oten");
        
        
        //adminCrud.registerUser(conf);
      // debug.debugadminDashboard(conf);
          login.login2(conf);
       
        
    //adminCrud.createAdmin(conf);
    
    //debug.debugcreateAdmin(conf);
    //debug.testAddRecordAndReturnId(conf);
    
    
    //login.login2(conf);
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //conf.viewRequests();
    
   // admin.adminDashboard(conf);
    //employees.employeeMenu(conf);
   // login.login(conf);
    
    //employees.employeeMenu(conf);
    
    
    
    
   // adminCrud.viewEmployeesWithDepartment(conf);
   // pendings.viewPending(conf);
    //admin.requestDelete(conf, null);
    //debug.mahNiga(conf);
    //adminCrud.viewEmployeesWithDepartment(conf);
   // admin.updateRequest(conf, null);
   // debug.mahNiga(conf);
    //adminCrud.viewEmployeesWithDepartment(conf);
    //debug.mahNiga(conf);   
        //pendings.createRequest(conf, null);
        
        //debug.mahNiga(conf);
        
        //admin.approveRequest(conf);
       // adminCrud.viewEmployeesWithDepartment(conf);
        //adminCrud.deleteAdmin(conf);
        
        //pendings.createRequest(conf, null);
        //debug.debugRequest(conf);
        //conf.viewRecords(null, args, args);
      //conf.addStudents();
       // conf.viewRequests();
        //admin.adminPending(conf);
        //login.login(conf);
       // adminCrud.viewEmployeesWithDepartment(conf);
      //  admin.adminDashboard(conf);
 
    }
    
}
    
   

