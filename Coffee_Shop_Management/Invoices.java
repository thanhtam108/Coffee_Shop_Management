package Coffee_Shop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mysql.cj.x.protobuf.MysqlxCrud.Collection;

public class Invoices {
    private String invoice_id;
    private String cus_phone;
    private String payment_method;
    private double total_amount;
    private String notes;
    Connection conn;
    Scanner sc = new Scanner(System.in);

    public Invoices(Connection conn1) {
        conn = conn1;
        invoice_id = new String();
        cus_phone = new String();
        payment_method = new String();
        total_amount = 0.0;
        notes = new String();
    }

    // public Invoices(String drink_name, String quantity) {
    // this.drink_name = drink_name;
    // }

    // public String getDrinkName() {
    // return drink_name;
    // }

    public String Take_Order(Connection conn) {
        String drink_category = new String();
        String drink_name = new String();
        try {
            System.out.println(
                    "What drink category would you like to order? (1: Coffee, 2: Tea, 3: Smoothie, 4: Milkshake)");
            drink_category = sc.nextLine();
            if (drink_category.equals("1")) {
                System.out.println(
                        "What kind of coffee do you want to order? (1: Espresso, 2: Americano, 3: Cappuccino, 4: Latte, 5: Mocha)");
                drink_name = sc.nextLine();
                if (drink_name.equals("1")) {
                    drink_name = "Espresso";
                } else if (drink_name.equals("2")) {
                    drink_name = "Americano";
                } else if (drink_name.equals("3")) {
                    drink_name = "cappuccino";
                } else if (drink_name.equals("4")) {
                    drink_name = "Latte";
                } else {
                    drink_name = "Mocha";
                }
            } else if (drink_category.equals("2")) {
                System.out.println("What kind of tea do you want to order? (1: Tea, ");
                drink_name = sc.nextLine();
                if (drink_name.equals("1")) {
                    drink_name = "Tea";
                }
            } else if (drink_category.equals("3")) {
                System.out.println("What kind of Smoothie do you want to order? (1: Fruit smoothie, ");
                drink_name = sc.nextLine();
                if (drink_name.equals("1")) {
                    drink_name = "Fruit smoothie";
                }
            } else {
                System.out.println("What kind of Milkshake do you want to order? (1: Chocolate milkshake, ");
                drink_name = sc.nextLine();
                if (drink_name.equals("1")) {
                    drink_name = "Chocolate milkshake";
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid!");
        }

        return drink_name;
    }

    private Map<String, Integer> DrinkCounts(List<String> orderItems) {
        Map<String, Integer> counts = new HashMap<>();

        for (String str : orderItems) {
            counts.put(str, counts.getOrDefault(str, 0) + 1);
        }
        return counts;
    }

    private Map<String, Double> GetDrinkPrice(Map<String, Integer> drink_counts) {
        Map<String, Double> drink_price = new HashMap<>();

        String sql = "SELECT drink_price FROM Drinks WHERE drink_name = ?";

        for (Map.Entry<String, Integer> entry : drink_counts.entrySet()) {
            String drink_name = entry.getKey();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, drink_name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double price = rs.getDouble("drink_price");
                    drink_price.put(drink_name, price);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return drink_price;
    }

    public double Calc_TotalPrice(Map<String, Integer> drink_counts, Map<String, Double> drink_price)
            throws SQLException {
        double totalAmount = 0;

        for (Map.Entry<String, Integer> entry : drink_counts.entrySet()) {
            String drinkName = entry.getKey();
            int quantity = entry.getValue();
            if (drink_price.containsKey(drinkName)) {
                double price = drink_price.get(drinkName);
                totalAmount += quantity * price;
            }
        }
        return totalAmount;
    }

    public void Input_invoice(Connection conn, String emp_id) throws SQLException {
        List<String> orderItems = new ArrayList<String>();
        Customers cus = null;
        String answer = new String();
        do {

            orderItems.add(Take_Order(conn));

            System.out.println("Would you like to order another drink? (y if yes or n if no)");
            answer = sc.nextLine();
        } while (answer.equals("Y") || answer.equals("y"));

        Collections.sort(orderItems);
        Map<String, Integer> drink_counts = DrinkCounts(orderItems);
        Map<String, Double> drink_price = GetDrinkPrice(drink_counts);

        total_amount = Calc_TotalPrice(drink_counts, drink_price);

        System.out.println("Ask if the customer wants to adding points(y if yes, n if no))?");
        String choice = sc.nextLine();
        
        if (choice.equals("y") || choice.equals("Y")) {
            cus = new Customers(conn);
            cus.InputTotalAmount(total_amount);
            cus.Add_Customer(conn);
        }


    }

    // public static void main(String[] args) throws SQLException {
    // Connection conn = Database.Connect("localhost", "Coffee_Shop", "root",
    // "123456");
    // Invoices inv = new Invoices();
    // inv.Input_invoice(conn);
    // }
}
