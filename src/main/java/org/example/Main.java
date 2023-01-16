package org.example;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sun.source.tree.StatementTree;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.in;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");
        Connection conn = null;

        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_class_db", "root", "rootroot");

        } catch (SQLException ex){
            ex.printStackTrace();

        }
        ArrayList<String> list = new ArrayList<>();
        createTable(conn, list);

//        try{
//            Connection conn = getConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet resultSet = stmt.executeQuery("SELECT * FROM Product");
//            DecimalFormat fmt = new DecimalFormat("#0.0");
//
//            while(resultSet.next()){
//                System.out.println("id: " + resultSet.getInt(1));
//                System.out.println("name: " + resultSet.getString(2));
//                System.out.println("producerID: " + resultSet.getInt(3));
//                System.out.println("================");
//            }
//
//        }catch (SQLException ex) {
//            ex.printStackTrace();
//        }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/java_class_db", "root", "rootroot");
    }
    public static Connection getSourceConnection() throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/java_class_db");
        dataSource.setUser("root");
        dataSource.setPassword("rootroot");

        return dataSource.getConnection();
    }

    public static void createTable(Connection conn, ArrayList<String> values) throws SQLException{
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        String temp = "";
        String tableName = "";
        int columnNum = 0;

        System.out.println("Please type the table name");
        tableName = scan.nextLine();

        System.out.println("Please type the columns num");
        columnNum = scan.nextInt();

        for(int i = 1; i <= columnNum; i++){
            if(i==1){
                System.out.println("Please type the column " + i + " name");
                temp = scan.nextLine();
                temp = scan.nextLine();
                System.out.println("Please type the column " + i + " parameters");
                temp = temp + " " + scan.nextLine();
            }else {
                System.out.println("Please type the column " + i + " name");
                temp = temp + ", " + scan.nextLine();
                System.out.println("Please type the column " + i + " parameters");
                temp = temp + " " + scan.nextLine();
            }

        }
        System.out.println("CREATE TABLE " + tableName + " (" + temp + ");");

        boolean isResultSetReturned = stmt.execute("CREATE TABLE " + tableName + " (" + temp + ");");


        //boolean isResultSetInserted = stmt.executeUpdate("INSERT INTO " + tableName + " VALUES ");
//        VALUES (value1, value2, value3, ...);



        if(isResultSetReturned){
            System.out.println("Table created");
        } else { System.out.println("Table NOT created");}




//        try{
//            Connection conn = getConnection();
//            Statement stmt = conn.createStatement();
//            ResultSet resultSet = stmt.executeQuery("SELECT * FROM Product");
//            DecimalFormat fmt = new DecimalFormat("#0.0");
//
//            while(resultSet.next()){
//                System.out.println("id: " + resultSet.getInt(1));
//                System.out.println("name: " + resultSet.getString(2));
//                System.out.println("producerID: " + resultSet.getInt(3));
//                System.out.println("================");
//            }
//
//        }catch (SQLException ex) {
//            ex.printStackTrace();
//        }

    }
}