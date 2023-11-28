package Coffee_Shop_Management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class man {
    public static void main(String[] args) {
        List<String> orderItems = new ArrayList<String>();
        orderItems.add("Coffee");
        orderItems.add("Espresso");
        orderItems.add("Americano");
        orderItems.add("antatica");
        Collections.sort(orderItems);
        for (int i = 0; i < orderItems.size(); i++)
        {
            System.out.println(orderItems.get(i));
        }
    }
}
