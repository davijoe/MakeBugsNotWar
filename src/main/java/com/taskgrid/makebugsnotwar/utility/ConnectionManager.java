package com.taskgrid.makebugsnotwar.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static Connection connection = null;

    public static Connection getConnection(String DB_URL, String DB_UID, String DB_PWD){
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, DB_UID, DB_PWD);
            }catch(SQLException e){
                System.out.println("Could not connect to database");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
