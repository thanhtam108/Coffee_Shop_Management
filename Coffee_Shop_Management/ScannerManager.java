package Coffee_Shop_Management;

import java.util.Scanner;

public class ScannerManager {

    private static Scanner scanner = null;

    private ScannerManager() {
        scanner = new Scanner(System.in);
    }

    public static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
}
