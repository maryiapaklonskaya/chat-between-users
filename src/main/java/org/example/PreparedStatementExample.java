package org.example;

import com.mysql.cj.xdevapi.PreparableStatement;

import java.sql.*;
import java.util.Scanner;

public class PreparedStatementExample {

    public static void main(String args[]) throws SQLException {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/java_class_db", "root", "rootroot");
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        getAllMoviesByActorName(conn);
        getAllMoviesCount(conn);

    }

    public static void getProducer(Connection conn, String regName) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM maryia_test WHERE id = ?");

        pStmt.setString(1, regName);

        ResultSet resultSet = pStmt.executeQuery();

        while(resultSet.next()){
            System.out.println("Producer name is: " + resultSet.getString(1) + " and the revenue is: " + resultSet.getDouble(2));
        }
    }

    public static void getAllMoviesByActorName(Connection conn) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM movies WHERE actor_name = ?");
        Scanner scan = new Scanner(System.in);
        String actorName = "";
        System.out.println("Please type the actor name to view their movies");
        actorName = scan.nextLine();
        pStmt.setString(1, actorName);
        ResultSet resultSet = pStmt.executeQuery();

        while(resultSet.next()){
            System.out.println("Actor name is: " + actorName + " and the movie they was in:" + resultSet.getString(2));
        }
    }

    public static void getAllMoviesCount(Connection conn) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT count(*) FROM movies WHERE actor_name = ?");
        Scanner scan = new Scanner(System.in);
        String actorName = "";
        System.out.println("Please type the actor name");
        actorName = scan.nextLine();
        pStmt.setString(1, actorName);
        ResultSet resultSet = pStmt.executeQuery();

        while(resultSet.next()){
            System.out.println("Actor name is: " + actorName + " and the movie they was in:" + resultSet.getInt(1));
        }
    }


}

/*
        getAllMovies("Tom cruise") =>
         getAllMoviesCount("James Cameron")
     */
