package org.example;

import java.sql.*;

public class CallableStatementExample {
    public static void main(String args[]) throws SQLException {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_class_db", "root", "rootroot");
        } catch (SQLException ex){
            ex.printStackTrace();
        }

    }

    public static void getProducer(Connection conn, String regName) throws SQLException{
        CallableStatement cStmt = conn.prepareCall("{call getProducers(?)}");

        cStmt.setString(1, "apple");

        ResultSet resultSet = cStmt.executeQuery();

        while(resultSet.next()){
            System.out.println("Producer name is: " + resultSet.getString(1) + " and the revenue is: " + resultSet.getDouble(2));
        }
    }

    /*
    CREATE A STORED PROCEDURE TO COUNT HOW MANY TIMES AN ACTOR APPEARS IN A MOVIE PRODUCED BY A PROVIDED DIRECTOR
    getAppearanceCount("Tom Cruise", "James Cameron")
 */


}
