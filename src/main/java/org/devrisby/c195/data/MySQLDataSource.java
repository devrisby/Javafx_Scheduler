package org.devrisby.c195.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Old code based on the course
public class MySQLDataSource {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//localhost/";
    private static final String dbName = "client_schedule";
    private static final String dbURL = protocol + vendorName + ipAddress + dbName + "?connectionTimeZone = SERVER";
    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static final String dbUser = "sqlUser";
    private static final String password = "Passw0rd!";
    private static Connection conn = null;

    /** Initiates connection to MySQL database */
    public static void startConnection() {
        try {
            Class.forName(dbDriver);
            conn = DriverManager.getConnection(dbURL, dbUser, password);

            System.out.println("Database successfully connected");
        }

        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database failed to connect! \n");
            System.out.println(e.getMessage());
        }
    }

    /** Returns database connection object. Used for performing JDBC operations */
    public static Connection getConnection() {
        if(conn == null){
            throw new NullPointerException("Database not connected!");
        }

        return conn;
    }

    /** Closes connection to database */
    public static void closeConnection() {
        try {
            conn.close();
        } catch (Exception ignored) {

        }
    }
}
