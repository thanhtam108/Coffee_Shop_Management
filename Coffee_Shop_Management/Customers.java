package Coffee_Shop_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Customers {
    private Connection conn;
    private String cus_name;
    private String cus_phone;
    private double purchaseAmount;
    private double point;
    private String rank;
    private double total_amount;

    public Customers(Connection connection) {
        conn = connection;
        cus_name = new String();
        cus_phone = new String();
        purchaseAmount = 0.0;
        rank = new String();
    }

    private static boolean Check_Exists(Connection conn, String phone) {
        String sqlCheck = " SELECT cus_id FROM Customers where cus_phone = ?";
        try (PreparedStatement checkStatement = conn.prepareStatement(sqlCheck)) {
            checkStatement.setString(1, phone);
            ResultSet resultSet = checkStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private double calc_point(double purchaseAmount) {
        point = purchaseAmount * 0.0001;
        return point;
    }

    public void InputTotalAmount(Double totalamount){
        total_amount = totalamount;
    }

    public String GetRank() {
        return rank;
    }

    public void GenerateRank() {
        if (point < 200) {
            rank = "Bronze";
        } else if (point < 500) {
            rank = "Silver";
        } else if (point < 700) {
            rank = "Gold";
        } else if (point < 1000) {
            rank = "Platinum";
        } else {
            rank = "Diamond";
        }
    }

    public void Add_Customer(Connection conn) throws SQLException {
        Scanner sc = ScannerManager.getScanner();

        do {
            System.out.println("Enter customer's phone number(consists of 10 digits): ");
            cus_phone = sc.nextLine();
        } while (cus_phone.isEmpty() || cus_phone.length() != 10 || cus_phone.charAt(0) != 48);

        boolean cus_phone_check = Check_Exists(conn, cus_phone);

        while (cus_phone_check) {
            System.out.println("Customer already exists!");
            do {
                System.out.println("Enter customer's phone number(consists of 10 digits): ");
                cus_phone = sc.nextLine();
            } while (cus_phone.isEmpty() || cus_phone.length() != 10 || cus_phone.charAt(0) != 48);
            cus_phone_check = Check_Exists(conn, cus_phone);
        }

        do {
            System.out.println("Enter Customer's full name: ");
            cus_name = sc.nextLine();
        } while (cus_name.isEmpty());

        // do {
        //     System.out.println("Enter Customer's purchase Amount: ");
        //     purchaseAmount = Double.parseDouble(sc.nextLine());
        // } while (purchaseAmount <= 0);

        // InputTotalAmount(total);

        point = calc_point(purchaseAmount);

        GenerateRank();

        DBInsertionCustomer();

    }

    private void DBInsertionCustomer() {
        String sql = "INSERT INTO Customers (cus_name, cus_rank, cus_point, cus_phone) VALUES (? , ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cus_name);
            pstmt.setString(2, rank);
            pstmt.setDouble(3, point);
            pstmt.setString(4, cus_phone);
            pstmt.executeUpdate();
            System.out.println("Customer " + cus_name + " is added into the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}