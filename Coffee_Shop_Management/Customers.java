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
    private double cus_point;
    private String cus_rank;
    private double total_amount;

    public Customers(Connection connection) {
        conn = connection;
        cus_name = new String();
        cus_phone = new String();
        purchaseAmount = 0.0;
        cus_rank = new String();
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

    private void calc_point(double purchaseAmount) {
        cus_point = purchaseAmount * 0.0001;
    }

    public void InputTotalAmount(Double totalamount){
        total_amount = totalamount;
    }

    public String GetRank() {
        return cus_rank;
    }

    public void GenerateRank() {
        if (cus_point < 200) {
            cus_rank = "Bronze";
        } else if (cus_point < 500) {
            cus_rank = "Silver";
        } else if (cus_point < 700) {
            cus_rank = "Gold";
        } else if (cus_point < 1000) {
            cus_rank = "Platinum";
        } else {
            cus_rank = "Diamond";
        }
    }

    public void ExtractExistedCustomer(String phone) {
        String sqlCheck = "SELECT * FROM Customers WHERE cus_phone = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setString(1, phone);
            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                cus_name = rSet.getString("cus_name");
                cus_phone = rSet.getString("cus_phone");
                cus_rank = rSet.getString("cus_rank");
                cus_point = rSet.getDouble("cus_point");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double GetDiscount () {
        double disc = 0.0;
        switch (cus_rank) {
            case "Bronze" :
                disc = 0.0;
                break;
            case "Silver" :
                disc = 0.05;
                break;
            case "Gold" :
                disc = 0.1;
                break;
            case "Platinum" :
                disc = 0.15;
                break;
            case "Diamond" :
                disc = 0.2;
                break;
        }
        return disc;
    }
    
    public void Add_Customer(String phone) throws SQLException {
        Scanner sc = ScannerManager.getScanner();

        // do {
        //     System.out.println("Enter customer's phone number(consists of 10 digits): ");
        //     cus_phone = sc.nextLine();
        // } while (cus_phone.isEmpty() || cus_phone.length() != 10 || cus_phone.charAt(0) != 48);

        boolean cus_phone_check = Check_Exists(conn, cus_phone);

        while (cus_phone_check) {
            System.out.println("Customer already exists!");
            do {
                System.out.println("Enter customer's phone number(consists of 10 digits): ");
                cus_phone = sc.nextLine();
            } while (cus_phone.isEmpty() || cus_phone.length() != 10 || cus_phone.charAt(0) != 48);
            cus_phone_check = Check_Exists(conn, cus_phone);
        }

        cus_phone = phone;

        do {
            System.out.println("Enter Customer's full name: ");
            cus_name = sc.nextLine();
        } while (cus_name.isEmpty());

        // do {
        //     System.out.println("Enter Customer's purchase Amount: ");
        //     purchaseAmount = Double.parseDouble(sc.nextLine());
        // } while (purchaseAmount <= 0);

        // InputTotalAmount(total);

        calc_point(purchaseAmount);

        GenerateRank();

        DBInsertionCustomer();

    }

    private void DBInsertionCustomer() {
        String sql = "INSERT INTO Customers (cus_name, cus_rank, cus_point, cus_phone) VALUES (? , ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cus_name);
            pstmt.setString(2, cus_rank);
            pstmt.setDouble(3, cus_point);
            pstmt.setString(4, cus_phone);
            pstmt.executeUpdate();
            System.out.println("Customer " + cus_name + " is added into the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}