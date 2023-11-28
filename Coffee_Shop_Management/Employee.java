package Coffee_Shop_Management;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Employee {

    public String emp_id = new String();
    private String emp_name = new String();
    private String emp_birthday = new String();
    private String emp_hire_date = new String();
    private String emp_phone = new String();
    private String emp_position = new String();
    private String emp_account_name = new String();
    private String emp_password = new String();
    public Connection conn;

    public Employee(Connection connection) {
        conn = connection;
    }

    public Employee(Connection connection, String id, String name,
            java.sql.Date birthday, java.sql.Date hire_date, String phone, String position, String account_name,
            String password) {
        conn = connection;
        emp_id = id;
        emp_name = name;
        emp_birthday = birthday.toString();
        emp_hire_date = hire_date.toString();
        emp_phone = phone;
        emp_position = position;
        emp_account_name = account_name;
        emp_password = password;
    }

    private void InputEmpID() {
        Scanner sc = ScannerManager.getScanner();
        do {
            System.out.println("Enter employee's id (consists of 2 letters and 4 digits with no blanks): ");
            emp_id = sc.nextLine();
        } while (emp_id.isEmpty() || emp_id.length() != 6 || !Character.isLetter(emp_id.charAt(0))
                || !Character.isLetter(emp_id.charAt(1)) || !Character.isDigit(emp_id.charAt(2))
                || !Character.isDigit(emp_id.charAt(3)) || !Character.isDigit(emp_id.charAt(4))
                || !Character.isDigit(emp_id.charAt(5)));
    }

    private boolean CheckingInfoExistence(String check_info) {
        String columnName = "";
        String sqlCheck_Emp = "";
        switch (check_info) {
            case "emp_id":
                columnName = "emp_id";
                break;
            case "emp_phone":
                columnName = "emp_phone";
                break;
        }
        sqlCheck_Emp = "SELECT * FROM Employees WHERE " + columnName + " = ?";
        ResultSet resultSet = null;
        try (PreparedStatement checkEmp_Statement = conn.prepareStatement(sqlCheck_Emp)) {
            switch (check_info) {
                case "emp_id":
                    checkEmp_Statement.setString(1, emp_id);
                    break;
                case "emp_phone":
                    checkEmp_Statement.setString(1, emp_phone);
                    break;
            }
            resultSet = checkEmp_Statement.executeQuery();
            return resultSet.next(); // Check if the ResultSet has a record
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String GenerateAccountName() {
        String Name = new String(emp_name.substring(emp_name.lastIndexOf(" ") + 1, emp_name.length()));
        return Name.toLowerCase() + emp_id.toLowerCase();
    }

    private String GeneratePassword() {
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";

        String allowedCharacters = lowercase + uppercase + digits;
        int passwordLength = 8;

        StringBuilder password = new StringBuilder(passwordLength);

        SecureRandom random = new SecureRandom();

        // ensure at least one lowercase, one uppercase, and one digit character
        password.append(lowercase.charAt(random.nextInt(lowercase.length())));
        password.append(uppercase.charAt(random.nextInt(uppercase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));

        // fill the rest of the password with random characters
        for (int i = 3; i < passwordLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            password.append(allowedCharacters.charAt(randomIndex));
        }

        // shuffle the characters to make the order random
        for (int i = 0; i < passwordLength; i++) {
            int randomIndex = i + random.nextInt(passwordLength - i);
            char temp = password.charAt(i);
            password.setCharAt(i, password.charAt(randomIndex));
            password.setCharAt(randomIndex, temp);
        }

        return password.toString();
    }

    private void DBInsertionEmp() {
        String insert_Query = "INSERT INTO Employees (emp_id, emp_name, emp_birthday, emp_hire_date, emp_phone, emp_position, emp_account_name, emp_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insert_SQL = conn.prepareStatement(insert_Query)) {
            insert_SQL.setString(1, emp_id);
            insert_SQL.setString(2, emp_name);
            java.sql.Date sql_BDate = java.sql.Date.valueOf(emp_birthday);
            insert_SQL.setDate(3, sql_BDate);
            java.sql.Date sql_HireDate = java.sql.Date.valueOf(emp_hire_date);
            insert_SQL.setDate(4, sql_HireDate);
            insert_SQL.setString(5, emp_phone);
            insert_SQL.setString(6, emp_position);
            insert_SQL.setString(7, emp_account_name);
            insert_SQL.setString(8, emp_password);
            // Execute the update
            insert_SQL.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Employee Log_In(String account_name, String password) {
        String sqlCheck = "SELECT * FROM Employees WHERE emp_account_name = ? AND emp_password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setString(1, account_name);
            ps.setString(2, password);

            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                Employee employee = new Employee(conn, rSet.getString("emp_id"),
                        rSet.getString("emp_name"), rSet.getDate("emp_birthday"),
                        rSet.getDate("emp_hire_date"), rSet.getString("emp_phone"),
                        rSet.getString("emp_position"), rSet.getString("emp_account_name"),
                        rSet.getString("emp_password"));
                return employee;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void AddEmployee() {
        Scanner sc = ScannerManager.getScanner();
        boolean empIDExists = false;
        boolean phoneExists = false;

        InputEmpID();

        empIDExists = CheckingInfoExistence("emp_id");
        while (empIDExists) {
            System.out.println("Employee's id exists in the database! Please enter again!");
            InputEmpID();
            empIDExists = CheckingInfoExistence("emp_id");
        }

        do {
            System.out.println("Enter employee's full name: ");
            emp_name = sc.nextLine();
        } while (emp_name.isEmpty());

        do {
            System.out.println("Enter employee's birhtday (with format yyyy-mm-dd): ");
            emp_birthday = sc.nextLine();
        } while (emp_birthday.isEmpty());

        do {
            System.out.println("Enter employee's hiredate (with format yyyy-mm-dd): ");
            emp_hire_date = sc.nextLine();
        } while (emp_hire_date.isEmpty());

        do {
            System.out.println("Enter employee's phone number (consists of 10 digits): ");
            emp_phone = sc.nextLine();
        } while (emp_phone.isEmpty() || emp_phone.length() != 10 || emp_phone.charAt(0) != 48);

        phoneExists = CheckingInfoExistence("emp_phone");
        while (phoneExists) {
            System.out.println("Phone number existed in the database! Please enter again!");
            emp_phone = sc.nextLine();
            phoneExists = CheckingInfoExistence("emp_phone");
        }

        System.out.println("Enter employee's postion (position will be staff by default): ");
        emp_position = sc.nextLine();

        emp_account_name = GenerateAccountName();

        emp_password = GeneratePassword();

        DBInsertionEmp();

    }

    public void AlterEmpInformation() {
        Scanner sc = ScannerManager.getScanner();
        int choice = 0;
        boolean empIDExists = false;
        boolean phoneExists = false;

        System.out.println("Choose the employee you want to alter information (by ID of employee): ");
        InputEmpID();

        // Checking existence of the employee
        empIDExists = CheckingInfoExistence("emp_id");

        if (empIDExists) {
            ExtractExistedEmp(conn, emp_id);

            System.out.println("Choose the type of information you want to alter: ");
            System.out.println("-------------------------");
            System.out.println(
                    "|\t1. Name \t|\n|\t2. Birthday\t|\n|\t3. Hire date\t|\n|\t4. Phone\t|\n|\t5. Position\t|");
            System.out.println("-------------------------");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Enter new name: ");
                    emp_name = sc.nextLine();
                    DBUpdateEmpInfo();
                    break;
                case 2:
                    System.out.println("Enter new birthday (yyyy-mm-dd): ");
                    emp_birthday = sc.nextLine();
                    DBUpdateEmpInfo();
                    break;
                case 3:
                    System.out.println("Enter new hiredate (yyyy-mm-dd): ");
                    emp_hire_date = sc.nextLine();
                    DBUpdateEmpInfo();
                    break;
                case 4:
                    do {
                        System.out.println("Enter employee's phone number (consists of 10 digits): ");
                        emp_phone = sc.nextLine();
                    } while (emp_phone.isEmpty() || emp_phone.length() != 10 || emp_phone.charAt(0) == 0);
                    phoneExists = CheckingInfoExistence("emp_phone");
                    while (phoneExists) {
                        System.out.println("Phone number already exists in the database! Please enter again");
                        do {
                            System.out.println("Enter employee's phone number (consists of 10 digits): ");
                            emp_phone = sc.nextLine();
                            phoneExists = CheckingInfoExistence("emp_phone");
                        } while (emp_phone.isEmpty() || emp_phone.length() != 10 || emp_phone.charAt(0) != 48);
                    }
                    DBUpdateEmpInfo();
                    break;
                case 5:
                    System.out.println("Enter new position: ");
                    emp_position = sc.nextLine();
                    DBUpdateEmpInfo();
                    break;
                default:
                    System.out.println("None of the information is chosen!");
                    break;
            }
        } else
            System.out.println("Cannot find the ID of employee!");

    }

    public void DeleteEmp() {
        Scanner sc = ScannerManager.getScanner();
        String emp_id = new String();
        boolean empIDExists = false;

        InputEmpID();

        // Checking existence of the empolyee
        empIDExists = CheckingInfoExistence("emp_id");
        if (empIDExists) { // start deletion if emp exists
            System.out.println(
                    "Are you sure you want to delete the " + emp_id + " employee from the database (y or n) ?");
            String confirm = new String(sc.nextLine());
            if (confirm.equals("y")) {
                String sql = "DELETE FROM Employees where emp_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql);) {
                    ps.setString(1, emp_id);
                    ps.executeUpdate();
                    System.out.println("Employee " + emp_id + " is deleted from database!");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else
                System.out.println("Not deleted!");
        } else
            System.out.println("Employee ID does not exist in the database. Deletion was not performed.");
    }

    public String GetPosition() {
        return emp_position;
    }

    public void GetAccountPassword() {
        boolean empIDExists ;

        System.out.println("Choose the account you want to view:");
        InputEmpID();

        // Checking existence of the employee
        empIDExists = CheckingInfoExistence("emp_id");

        if (empIDExists) {
            ExtractExistedEmp(conn, emp_id);
            System.out.println("Account name: " + emp_account_name + "\nPassword: " + emp_password);
        } else {
            System.out.println("ID not found!");
        }
    }

    public String GetEmpID ()
    {
        return emp_id;
    }

    public void ExtractExistedEmp(Connection conn, String emp_ID) {
        String sqlCheck = "SELECT * FROM Employees WHERE emp_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setString(1, emp_ID);
            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                emp_id = rSet.getString("emp_id");
                emp_name = rSet.getString("emp_name");
                emp_birthday = rSet.getDate("emp_birthday").toString();
                emp_hire_date = rSet.getDate("emp_hire_date").toString();
                emp_phone = rSet.getString("emp_phone");
                emp_position = rSet.getString("emp_position");
                emp_account_name = rSet.getString("emp_account_name");
                emp_password = rSet.getString("emp_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBUpdateEmpInfo() {
        String updateSQL = "UPDATE Employees SET emp_name = ?, emp_birthday = ?, emp_hire_date = ?, emp_phone = ?, emp_position = ? WHERE emp_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
            ps.setString(1, emp_name);
            ps.setString(2, emp_birthday);
            ps.setString(3, emp_hire_date);
            ps.setString(4, emp_phone);
            ps.setString(5, emp_position);
            ps.setString(6, emp_id);
            ps.executeUpdate();
            System.out.println("Information of " + emp_id + " is updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void WageSumming() {
        InputEmpID();
        int sumHours = 0;
        double wagePerHour = 20000;
        double grossWage = 0;
        boolean empIDExists = false;
        ResultSet rSet = null;

        String sqlCheckShift = "SELECT * FROM Shifts WHERE emp_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheckShift)) {
            ps.setString(1, emp_id);
            rSet = ps.executeQuery();
            empIDExists = rSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (!empIDExists) {
            System.out.println("ID not found! Please enter again!");
            InputEmpID();
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckShift)) {
                ps.setString(1, emp_id);
                rSet = ps.executeQuery();
                empIDExists = rSet.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        ExtractExistedEmp(conn, emp_id);
        String sqlExtract = "SELECT total_hours_worked FROM Shifts where emp_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sqlExtract)) {
            ps.setString(1, emp_id);
            rSet = ps.executeQuery();
            try {
                while (rSet.next()) {
                    sumHours += rSet.getInt("total_hours_worked");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        grossWage = sumHours * wagePerHour;
        System.out.printf("Gross wage of Employee %s is: %.0f VND\n", emp_name, grossWage);
    }
}
