package main;

import config.config;
import java.util.Scanner;

public class main {

   //Useless func
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // fallback
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    public static void deleteEmployee() {
        config db = new config();
        db.connectDB();
        Scanner sc = new Scanner(System.in);

        viewData();

        System.out.print("\nEnter Employee ID to delete: ");
        int empId = sc.nextInt();
        sc.nextLine(); // clear newline

        System.out.print("Are you sure you want to delete this employee? (Y/N): ");
        String confirm = sc.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            String sqlDelete = "DELETE FROM employees WHERE id_num = ?";
            db.updateRecord(sqlDelete, empId);
            System.out.println("\n✅ Employee with ID " + empId + " has been deleted.\n");
        } else {
            System.out.println("\n❌ Delete canceled.\n");
        }
    }

    public static void viewData() {
        config db = new config();
        db.connectDB();
        String sqlView = "SELECT * FROM employees";
        db.viewRecords(sqlView);
    }

    public static void changeUpdate() {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();

        viewData();

        System.out.print("\nEnter Employee ID: ");
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

        String sqlUpdate;
        String newValue;

        switch (option) {
            case 1:
                System.out.print("Enter new First Name: ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET first_name = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 2:
                System.out.print("Enter new Last Name: ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET last_name = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 3:
                System.out.print("Enter new Date of Birth (YYYY-MM-DD): ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET date_of_birth = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 4:
                System.out.print("Enter new Gender: ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET gender = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 5:
                System.out.print("Enter new Position: ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET position = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 6:
                System.out.print("Enter new Department ID: ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET department_id = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 7:
                System.out.print("Enter new Hire Date (YYYY-MM-DD): ");
                newValue = sc.nextLine();
                sqlUpdate = "UPDATE employees SET hire_date = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newValue, empId);
                break;
            case 8:
                System.out.print("Enter new Salary: ");
                int newSalary = sc.nextInt();
                sqlUpdate = "UPDATE employees SET salary = ? WHERE id_num = ?";
                db.updateRecord(sqlUpdate, newSalary, empId);
                break;
            default:
                System.out.println("❌ Invalid option!");
        }

        System.out.println("\n✅ Record Updated!");
        viewData();
    }

    // Main Program Loop
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        config db = new config();
        db.connectDB();

        while (true) {
            clearScreen();
            System.out.println("=====================================");
            System.out.println("   📋 Employee Management System");
            System.out.println("=====================================");
            System.out.println("1. ➕ Register Employee");
            System.out.println("2. 📑 View Employees");
            System.out.println("3. ✏️  Update Employee");
            System.out.println("4. ❌ Delete Employee");
            System.out.println("5. 🚪 Exit");
            System.out.println("=====================================");
            System.out.print("Enter option: ");
            int option = sc.nextInt();
            clearScreen();

            switch (option) {
                case 1:
                    System.out.print("Enter first name: ");
                    String fname = sc.next();
                    System.out.print("Enter last name: ");
                    String lname = sc.next();
                    System.out.print("Enter date of birth: ");
                    String dob = sc.next();
                    System.out.print("Enter gender: ");
                    String gender = sc.next();
                    System.out.print("Enter position: ");
                    String position = sc.next();
                    System.out.print("Enter department ID: ");
                    int depId = sc.nextInt();
                    System.out.print("Enter hire date: ");
                    String hiredate = sc.next();
                    System.out.print("Enter salary: ");
                    int salary = sc.nextInt();

                    String sql = "INSERT INTO employees(first_name, last_name, date_of_birth, gender, position, department_id, hire_date, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    db.addRecord(sql, fname, lname, dob, gender, position, depId, hiredate, salary);
                    System.out.println("\n✅ Record added successfully!\n");
                    break;

                case 2:
                    viewData();
                    break;

                case 3:
                    changeUpdate();
                    break;

                case 4:
                    deleteEmployee();
                    break;

                case 5:
                    System.out.println("\n👋 Goodbye!\n");
                    return;

                default:
                    System.out.println("\n❌ Invalid option!");
            }

            System.out.println("\nPress Enter to continue...");
            sc.nextLine(); // catch leftover newline
            sc.nextLine(); // wait for Enter
        }
    }
}
