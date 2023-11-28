package Coffee_Shop_Management;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {
    public static Connection ConnectionDeploy(String hostName, String dbName, String userName, String password) {
		Connection conn = null;
        String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            conn = DriverManager.getConnection(connectionURL, userName, password);
            return conn;
        } catch (Exception ex) { 
            ex.printStackTrace();
            return null;
		}
    }
}
