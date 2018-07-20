package com.pmart.dbconnection;

//Step 1: Use interfaces from java.sql package 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCPostgreConnection {
	
	//static reference to itself
    private static JDBCPostgreConnection instance = new JDBCPostgreConnection();
    public static final String URL = "jdbc:postgresql://localhost:5432/pmart";
    public static final String USER = "namal";
    public static final String PASSWORD = "namal123";
    public static final String DRIVER_CLASS = "org.postgresql.Driver"; 
     
    //private constructor
    private JDBCPostgreConnection() {
        try {
            //Step 2: Load JDBC Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
     
    private Connection createConnection() {
 
        Connection connection = null;
        try {
            //Step 3: Establish Java JDBC connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ERROR: Unable to Connect to Database.");
        }
        return connection;
    }   
     
    public static Connection getConnection() {
        return instance.createConnection();
    }	
	
	

}
