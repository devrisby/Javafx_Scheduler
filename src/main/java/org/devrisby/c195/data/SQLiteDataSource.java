package org.devrisby.c195.data;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// src: https://jstobigdata.com/java/singleton-design-pattern-in-java-with-a-real-world-example/
public class SQLiteDataSource {
    private static SQLiteDataSource instance;
    private Connection conn;

    // Connects to database whenever instance == null and getInstance() is invoked
    private SQLiteDataSource() throws SQLException {
        String PROTOCOL = "jdbc";
        String VENDOR = ":sqlite:";
        String PATH = Paths.get(System.getProperty("user.dir"), "db","sqlite.db").toString();
        String DB_URL = PROTOCOL + VENDOR + PATH;

        try {
            // enable driver for SQLITE
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot connect to SQLite database\n" + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public static SQLiteDataSource getInstance() {
        try {
            if(instance == null || instance.getConnection().isClosed()) {
                instance = new SQLiteDataSource();
            }
            return instance;
        } catch (SQLException e) {
            System.out.println("Could not retrieve database instance!\n" + e.getMessage());
            System.exit(1);
        }

        return null;
    }
}
