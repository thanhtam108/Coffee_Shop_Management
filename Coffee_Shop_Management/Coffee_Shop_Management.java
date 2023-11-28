package Coffee_Shop_Management;

import java.sql.Connection;
import java.text.ParseException;
import java.util.Scanner;

public class Coffee_Shop_Management {
    public static void main1(String[] args) throws ParseException {
        Scanner sc1 = ScannerManager.getScanner();
        boolean continue_function;
        Connection conn = null;
        String account = new String();
        String password = new String();
        Employee loggedEmp = null;

        do {
            account = GetInput("Enter your account name: ");
            password = GetInput("Enter your password");

            conn = GetDBConnection(account, password);

            loggedEmp = new Employee(conn);
            loggedEmp = loggedEmp.Log_In(account, password);

            if (loggedEmp == null) {
                System.out.println("Invalid account name or password! Please try again!");
            }

        } while (loggedEmp == null);

        if (loggedEmp.GetPosition().equals("manager")) {
            System.out.println("Logged in successfully into Manager's account.");
            pause();
            Employee employee1 = new Employee(conn);
            Drink drink1 = new Drink(conn);
            continue_function = true;

            do {
                ShowManagerMenu();
                int choice = Integer.parseInt(sc1.nextLine());

                switch (choice) {
                    case 1:
                        employee1.AddEmployee();
                        pause();
                        break;
                    case 2:
                        employee1.AlterEmpInformation();
                        pause();
                        break;
                    case 3:
                        employee1.DeleteEmp();
                        pause();
                        break;
                    case 4:
                        employee1.WageSumming();
                        pause();
                        break;
                    case 5:
                        employee1.GetAccountPassword();
                        pause();
                        break;
                    case 6:
                        drink1.AddDrink();
                        pause();
                        break;
                    case 7:
                        drink1.AlterDrinkPrice();
                        pause();
                        break;
                    case 8:
                        drink1.DeleteDrink();
                        pause();
                        break;
                    case 9:
                        continue_function = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a valid function.");
                        break;
                }
            } while (continue_function);
        } else if (loggedEmp.GetPosition().equals("staff")) {
            System.out.println("Logged in successfully into Staff's account.");
            pause();
            Shift shift1 = new Shift(conn);
            continue_function = true;
            do {
                ShowStaffMenu();
                int choice = Integer.parseInt(sc1.nextLine());
                switch (choice) {
                    case 1:
                        shift1.AddShift();
                        pause();
                        break;
                    default:
                        System.out.println("Invalid choice!");
                        break;
                }
            } while (continue_function);
        }
        return;
    }

    private static Connection GetDBConnection(String account, String password) {
        String userName = new String();
        String dbPassword = new String();

        if (Character.isUpperCase(account.charAt(0))) {
            userName = "manager";
            dbPassword = "mysql";
        } else {
            userName = "staff";
            dbPassword = "123456";
        }
        Connection conn = Connector.ConnectionDeploy(
                "localhost", "Coffee_Shop", userName, dbPassword);

        return conn;
    }

    private static String GetInput(String prompt) {
        Scanner sc1 = ScannerManager.getScanner();
        String input = new String();
        do {
            System.out.println(prompt);
            input = sc1.nextLine();
        } while (input.isEmpty());
        return input;
    }

    private static void ShowManagerMenu() {
        String sample = "|\t\tCoffee Shop Functions\t\t\t|";
        System.out.print("+-------------------------------------------------------+");
        System.out.println("\n" + sample);
        System.out.println("|\t\t1. Add employee \t\t\t|");
        System.out.println("|\t\t2. Alter employee information\t\t|");
        System.out.println("|\t\t3. Delete employee \t\t\t|");
        System.out.println("|\t\t4. Employee's wage summary \t\t|");
        System.out.println("|\t\t5. Employee's login information\t\t|");
        System.out.println("|\t\t6. Add drink \t\t\t\t|");
        System.out.println("|\t\t7. Alter drink price \t\t\t|");
        System.out.println("|\t\t8. Delete drink \t\t\t|");
        System.out.println("|\t\t9. Cancel \t\t\t\t|");
        System.out.println("+-------------------------------------------------------+");
        System.out.println("Please choose a function by number: ");
    }

    private static void ShowStaffMenu() {
        String sample = "|\t\tCoffee Shop Functions\t\t\t|";
        System.out.print("+-------------------------------------------------------+");
        System.out.println("\n" + sample);
        System.out.println("|\t\t1. Add shift \t\t\t\t|");
        System.out.println("|\t\t2. Alter employee information\t\t|");
        System.out.println("|\t\t3. Delete employee \t\t\t|");
        System.out.println("|\t\t4. Add drink\t\t\t\t|");
        System.out.println("|\t\t5. Alter drink price \t\t\t|");
        System.out.println("|\t\t6. Delete drink \t\t\t|");
        System.out.println("|\t\t7. Cancel \t\t\t\t|");
        System.out.println("+-------------------------------------------------------+");
        System.out.println("Please choose a function by number: ");
    }

    private static void pause() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
