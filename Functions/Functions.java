package Functions;

import add_Emp.Add_Emp;
import Add_Drinks.Add_Drinks;
import Alter_Emp_Info.Alter_Emp_Info;
import Alter_Drink_Price.Alter_Drink_Price;
import Delete_Employee.Delete_Employee;
import Delete_Drinks.Delete_Drinks;
import java.util.Scanner;

public class Functions {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String sample = "|\t\t\tCoffee Shop Functions\t\t\t|";
        System.out.print("+---------------------------------------------------------------+");
        System.out.println("\n" + sample);
        System.out.println("|\t\t\t1. Add employee \t\t\t|");
        System.out.println("|\t\t\t2. Alter employee information\t\t|");
        System.out.println("|\t\t\t3. Delete employee \t\t\t|");
        System.out.println("|\t\t\t4. Add drink\t\t\t\t|");
        System.out.println("|\t\t\t5. Alter drink price \t\t\t|");
        System.out.println("|\t\t\t6. Delete drink \t\t\t|");
        System.out.println("+---------------------------------------------------------------+");
        int choice = Integer.parseInt(sc.nextLine());
        switch (choice) {
            case 1:
                Add_Emp add_emp = new Add_Emp();
                add_emp.Add_Employee();
                break;
            case 2:
                Alter_Emp_Info alter_emp = new Alter_Emp_Info();
                alter_emp.Alter_Emp_Infor();
                break;
            case 3:
                Delete_Employee del_emp = new Delete_Employee();
                del_emp.delete_Emp();
                break;
            case 4:
                Add_Drinks add_dr = new Add_Drinks();
                add_dr.add_Drink();
                break;
            case 5:
                Alter_Drink_Price alter_price = new Alter_Drink_Price();
                alter_price.alter_Drink_Price();
                break;
            case 6:
                Delete_Drinks del_dr = new Delete_Drinks();
                del_dr.delete_Drink();
                break;
        }
        sc.close();
    }
}
