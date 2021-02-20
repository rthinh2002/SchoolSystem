package Database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    Connection conn;

    public Connection getConnection(){ //connect to the database server
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/company", "root", "bluesky123");
        } catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }
}
