package Coffee_Shop_Management;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class man {
    public static void main(String[] args) {
        // List<String> orderItems = new ArrayList<String>();
        // orderItems.add("Coffee");
        // orderItems.add("Espresso");
        // orderItems.add("Americano");
        // orderItems.add("antatica");
        // Collections.sort(orderItems);
        // for (int i = 0; i < orderItems.size(); i++)
        // {
        //     System.out.println(orderItems.get(i));
        // }

        // double totalAmount = 0;
        // Map<String, Integer> drink_counts = new HashMap<>();
        // Map<String, Double> drink_price = new HashMap<>();

        // drink_counts.put("A", 3);
        // drink_counts.put("B", 4);
        // drink_counts.put("C", 4);
        
        // drink_price.put("A", 7000.0);
        // drink_price.put("B", 5000.0);
        // drink_price.put("C", 5000.0);
        
        // for (Map.Entry<String, Integer> entry : drink_counts.entrySet()) {
        //     String drinkName = entry.getKey();
        //     int quantity = entry.getValue();
        //     if (drink_price.containsKey(drinkName)) {
        //         double price = drink_price.get(drinkName);
        //         totalAmount += quantity * price;
        //     }
        // }

        // System.out.println(totalAmount);
        // Connection conn = Connector.ConnectionDeploy(
        //         "localhost", "Coffee_Shop", "root", "123456");
        // Invoices i1 = new Invoices(conn);
        // try {
        //     i1.Input_invoice(conn, "AA0002");
        // } catch (SQLException e) {
        //     e.printStackTrace();
        // }
        String lowercase = "abcdefghijklmnopqrstuvwxyz";
        String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Characters = lowercase + uppercase;
        int IDLength = 12;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date currentDate = new Date();

        String datePart = dateFormat.format(currentDate);

        // // Generate a random number part (you can customize this part)
        // String randomPart = String.format("%03d", (int) (Math.random() * 1000));

        StringBuilder ID = new StringBuilder(IDLength);

        SecureRandom random = new SecureRandom();

        String invoice_id = datePart;

        for (int i = invoice_id.length(); i < IDLength; i++) {
            int randomIndex = random.nextInt(Characters.length());
            ID.append(Characters.charAt(randomIndex));
        }
        invoice_id = invoice_id + ID.toString();

        System.out.println(invoice_id);
    }
}
