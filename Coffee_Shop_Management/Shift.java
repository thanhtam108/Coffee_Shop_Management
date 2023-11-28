package Coffee_Shop_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Shift {
    private String emp_id = new String();
    private java.sql.Timestamp shift_start_time = null;
    private java.sql.Timestamp shift_end_time = null;
    private int total_hours_worked = 0;
    Connection conn;

    public Shift(Connection connection) {
        conn = connection;
    }

    public void InputEmpID() {
        Scanner sc = ScannerManager.getScanner();
        do {
            System.out.println("Enter employee's id (consists of 2 letters and 4 digits with no blanks): ");
            emp_id = sc.nextLine();
        } while (emp_id.isEmpty() || emp_id.length() != 6 || !Character.isLetter(emp_id.charAt(0))
                || !Character.isLetter(emp_id.charAt(1)) || !Character.isDigit(emp_id.charAt(2))
                || !Character.isDigit(emp_id.charAt(3)) || !Character.isDigit(emp_id.charAt(4))
                || !Character.isDigit(emp_id.charAt(5)));
    }

    private boolean CheckingEmpExistence() {
        String sqlCheck_Emp = "SELECT * FROM Employees WHERE emp_id = ?";
        ResultSet resultSet = null;
        try (PreparedStatement checkEmp_Statement = conn.prepareStatement(sqlCheck_Emp)) {
            checkEmp_Statement.setString(1, emp_id);
            resultSet = checkEmp_Statement.executeQuery();
            return resultSet.next(); // Check if the ResultSet has a record
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static int TotalHour(java.sql.Timestamp startTimestamp, java.sql.Timestamp endTimestamp) {
        int durationInMillis = (int) endTimestamp.getTime() - (int) startTimestamp.getTime();
        int hours_Worked = (int) durationInMillis / (1000 * 60 * 60);
        return hours_Worked;
    }

    private java.sql.Timestamp ParsingTimestamp(String input_date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date Date = sdf.parse(input_date);
        java.sql.Timestamp timestamp = new java.sql.Timestamp(Date.getTime());
        return timestamp;
    }

    private void DBInsertShift() {
        String insert_Querry = "INSERT INTO shifts (emp_id, shift_start_time, shift_end_time, total_hours_worked) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insert_SQL = conn.prepareStatement(insert_Querry)) {
            insert_SQL.setString(1, emp_id);
            insert_SQL.setTimestamp(2, shift_start_time);
            insert_SQL.setTimestamp(3, shift_end_time);
            insert_SQL.setInt(4, total_hours_worked);
            insert_SQL.executeUpdate();
            System.out.println("Shift is added to database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddShift() throws ParseException {
        Scanner sc = ScannerManager.getScanner();
        boolean empIDExists = false;

        InputEmpID();

        empIDExists = CheckingEmpExistence();

        if (empIDExists) {
            System.out.println("Enter start time of the shift (round to yyyy-mm-dd HH:00:00): ");
            String start_Time = new String(sc.nextLine());
            shift_start_time = ParsingTimestamp(start_Time);

            String sqlCheck_Shift = "SELECT * FROM shifts WHERE emp_id = ? AND shift_start_time > ?";
            try (PreparedStatement checkShift_Statement = conn.prepareStatement(sqlCheck_Shift)) {
                checkShift_Statement.setString(1, emp_id);
                checkShift_Statement.setTimestamp(2, shift_start_time);
                ResultSet rSet = checkShift_Statement.executeQuery();
                while (rSet.next()) {
                    System.out.println("Start time must be later than existed shifts! Please try again!");
                    start_Time = sc.nextLine();
                    shift_start_time = ParsingTimestamp(start_Time);
                    checkShift_Statement.setString(1, emp_id);
                    checkShift_Statement.setTimestamp(2, shift_start_time);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("Enter end time of the shift (round to yyyy-mm-dd HH:00:00): ");
            String end_time = new String(sc.nextLine());
            shift_end_time = ParsingTimestamp(end_time);
            total_hours_worked = TotalHour(shift_start_time, shift_end_time);

            DBInsertShift();

        } else
            System.out.println("Employee's ID not found!");
    }
}
