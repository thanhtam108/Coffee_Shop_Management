package Coffee_Shop_Management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Drink {
    private String drink_id = new String();
    private String drink_name = new String();
    private String drink_category = new String();
    private double drink_price = 0;
    private String drink_description = new String();
    Connection conn = null;

    public Drink(Connection connection) {
        conn = connection;
    }

    public Drink(Connection connection, String name, String category, double price, String description) {
        conn = connection;
        drink_name = name;
        drink_category = category;
        drink_price = price;
        drink_description = description;
    }

    private boolean CheckDrinkExistence(String check_info) {
        String columnName = "";
        String sqlCheck_Drink = "";
        switch (check_info) {
            case "drink_id":
                columnName = "drink_id";
                break;
            case "drink_name":
                columnName = "drink_name";
                break;
        }
        sqlCheck_Drink = "SELECT * FROM Drinks WHERE " + columnName + " = ?";
        ResultSet resultSet = null;
        try (PreparedStatement checkEmp_Statement = conn.prepareStatement(sqlCheck_Drink)) {
            switch (check_info) {
                case "drink_id":
                    checkEmp_Statement.setString(1, drink_id);
                    break;
                case "drink_name":
                    checkEmp_Statement.setString(1, drink_name);
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

    private void InputDrinkID(Scanner sc) {
        do {
            System.out.println(
                    "Enter drink id (consists of 3 uppercase letters that describes drink's category and 3 digits with no blanks): ");
            drink_id = sc.nextLine();
        } while (drink_id.isEmpty() || drink_id.length() != 6 || !Character.isLetter(drink_id.charAt(0))
                || !Character.isLetter(drink_id.charAt(1)) || !Character.isLetter(drink_id.charAt(2))
                || !Character.isUpperCase(drink_id.charAt(0)) || !Character.isUpperCase(drink_id.charAt(1))
                || !Character.isUpperCase(drink_id.charAt(2)) || !Character.isDigit(drink_id.charAt(3))
                || !Character.isDigit(drink_id.charAt(4)) || !Character.isDigit(drink_id.charAt(5)));
    }

    private void InputDrinkName(Scanner sc) {
        do {
            System.out.println("Enter name of drink: ");
            drink_name = sc.nextLine();
        } while (drink_name.isEmpty());
    }

    private void DBInsertionDrink() {
        String sql = "INSERT INTO drinks (drink_id drink_name, drink_category, drink_price, drink_description) VALUES (?, ? , ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, drink_id);
            ps.setString(2, drink_name);
            ps.setString(3, drink_category);
            ps.setDouble(4, drink_price);
            ps.setString(5, drink_description);
            // Execute the update
            ps.executeUpdate();
            System.out.println("Drink " + drink_name + " is added into the database!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void AddDrink() {
        Scanner sc = ScannerManager.getScanner();
        boolean drinkIDExists = false;
        boolean drinkNameExists = false;

        InputDrinkID(sc);
        drinkIDExists = CheckDrinkExistence("drink_id");
        while (drinkIDExists) {
            System.out.println("ID of the drink already exists! Please enter again!");
            InputDrinkID(sc);
            drinkIDExists = CheckDrinkExistence("drink_id");
        }

        InputDrinkName(sc);
        drinkNameExists = CheckDrinkExistence("drink_name");
        while (drinkNameExists) {
            System.out.println("Name of the drink already exists! Please enter again!");
            InputDrinkName(sc);
            drinkNameExists = CheckDrinkExistence("drink_name");
        }
        System.out.println("Enter the category of the drink: ");
        drink_category = sc.nextLine();
        System.out.println("Enter the price of the drink: ");
        drink_price = Double.parseDouble(sc.nextLine());
        System.out.println("Enter the description of the drink: ");
        drink_description = sc.nextLine();

        DBInsertionDrink();
    }

    public void AlterDrinkPrice() {
        Scanner sc = ScannerManager.getScanner();
        boolean drinkIDExists = false;
        double in_price = 0;

        System.out.println("Choose the drink to alter price: ");
        InputDrinkID(sc);
        drinkIDExists = CheckDrinkExistence("drink_name");
        if (drinkIDExists) {
            ExtractExistedDrink(conn, drink_id);
            try {
                System.out.println("Enter the price you want to apply to the drink " + drink_name);
                in_price = Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input is not a valid number. Please enter a valid numeric value.");
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE drinks SET drink_price = ?")) {
                ps.setDouble(1, in_price);
                ps.executeUpdate();
                System.out.println("The price of " + drink_name + " is changed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("Drink name does not exist!");
    }

    public void DeleteDrink() {
        Scanner sc = ScannerManager.getScanner();
        boolean drinkNameExists = false;

        System.out.println("Enter the name of the drink you want to delete: ");
        InputDrinkName(sc);
        drinkNameExists = CheckDrinkExistence("drink_name");
        if (drinkNameExists) {
            System.out.println(
                    "Are you sure you want to delete the " + drink_name + " from the database (y or n) ?");
            String confirm = sc.nextLine();
            if (confirm.equals("y")) {
                String sqlQuerry = "DELETE FROM drinks WHERE drink_name LIKE ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlQuerry)) {
                    ps.setString(1, drink_name);
                    ps.executeUpdate();
                    System.out.println("The drink " + drink_name + " is deleted from database!");
                } catch (SQLException s) {
                    s.printStackTrace();
                }
            } else
                System.out.println("Not deleted!");
        } else
            System.out.println("Drink name does not exist!");
    }

    public void ExtractExistedDrink(Connection conn, String drink_ID) {
        String sqlCheck = "SELECT * FROM Drinks WHERE drink_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setString(1, drink_ID);
            ResultSet rSet = ps.executeQuery();
            if (rSet.next()) {
                drink_id = rSet.getString("drink_id");
                drink_name = rSet.getString("drink_name");
                drink_category = rSet.getString("drink_category");
                drink_price = rSet.getDouble("drink_price");
                drink_description = rSet.getString("drink_description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}