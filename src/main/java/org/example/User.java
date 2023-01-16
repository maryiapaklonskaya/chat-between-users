package org.example;

import java.sql.*;
import java.text.DateFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private byte age;
    private String password;
    private String username;
    static int count = 0;

    public User(String firstName, String lastName, String email, String phone, String address, String gender, byte age, String password) throws SQLException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.password = password;
        this.username = createUsername(firstName, lastName, phone);
    }

    public User(String firstName, String lastName, String email, String phone, String address, String gender, byte age, String password, String username) throws SQLException {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.password = password;
        this.username = username;
    }

    public static void main(String[] args) throws SQLException {
            Connection conn = null;
            try{
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "rootroot");
            } catch (SQLException ex){
                ex.printStackTrace();
            }

            //signup(conn);
            login(conn);
            //update(conn);
            //delete(conn);

//        System.out.println(getUserByID(conn, 1).getUsername());
//        System.out.println(getUserID(conn, "maryiapakl20"));

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String createUsername(String firstName, String lastName, String phone) throws SQLException {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/user_management", "root", "rootroot");
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        int i = 0;
        int j = 0;
        String username = firstName.trim().toLowerCase() + lastName.trim().toLowerCase() + phone.trim().substring(phone.length() - 2);


        while((checkUsername(conn, username) > 0) && (j < 10)){
            username = username.substring(0, username.length() - 2) + j + i;
            i++;
            if(i == 10){
                j += 1;
                i = 0;
            }
        }

        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public byte getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }


    public String toString(){
        return "User: " + firstName + lastName + "\nemail: " + email + "\n" +
                "phone " + phone + "\naddress " + address + "\ngender " + gender + "\n" +
                "age: " + age + "\n password " + password + "\nusername " + username + "\n=================\n";
    }

    public static void signup(Connection conn) throws SQLException{
        Scanner scan = new Scanner(System.in);
        System.out.println("THIS IS A SIGN UP FORM. \nPLEASE INSERT YOUR FIRST NAME");
        String firstName = scan.nextLine();
        System.out.println("TYPE YOUR LAST NAME");
        String lastName = scan.nextLine();

        System.out.println("TYPE YOUR EMAIL");
        String email = scan.nextLine();
        int e = 1;
        while(!patternMatches(email)){
            System.out.println("You typed not an email, type real email [" + e + "]");
            email = scan.nextLine();
            e++;
        }

        System.out.println("TYPE YOUR PHONE");
        String phone = scan.nextLine();
        System.out.println("TYPE YOUR ADDRESS");
        String address = scan.nextLine();
        System.out.println("TYPE YOUR GENDER");
        String gender = scan.nextLine();
        System.out.println("TYPE YOUR AGE");
        byte age = scan.nextByte();
        scan.nextLine();  // Consume newline left-over
        System.out.println("TYPE YOUR PASSWORD");
        String password = scan.nextLine();

        User user = new User(firstName.trim(), lastName.trim(), email.trim(), phone.trim(), address.trim(), gender.trim(), age, password.trim());

        System.out.println("Your new unique username is: " + user.getUsername());

        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO `user_management`.`user` (firstName, lastName, email, phone, address, gender, age, password, username) " +
                "VALUES ('" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getEmail() + "','" + user.getPhone() + "','" + user.getAddress() + "" +
                "','" + user.getGender() + "','" + user.getAge() + "','" + user.getPassword() + "','" + user.getUsername() + "');");

        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND phone = ?");
        pStmt.setString(1, user.getUsername());
        pStmt.setString(2, user.getPhone());
        ResultSet resultSet = pStmt.executeQuery();

        while (resultSet.next()) {
            System.out.println("USER WITH USERNAME ==> " + resultSet.getString(10) + " <== WAS CREATED:\nID: " + resultSet.getInt(1) + " || firstname and last name: " + resultSet.getString(2) + " " + resultSet.getString(3) + " || phone is: " + resultSet.getString(5) + "\npassword is: " + resultSet.getString(9));
        }

    }

    public static int checkUsername(Connection conn, String username) throws SQLException {
        CallableStatement cStmt = conn.prepareCall("{CALL getUsernames(?)}");
        cStmt.setString(1, username);
        ResultSet resultSet = cStmt.executeQuery();
        while(resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static void login(Connection conn) throws SQLException {
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
        Scanner scan = new Scanner(System.in);
        String username = "";
        String password = "";
        System.out.println("LOGIN FORM\nType Your Username:");
        username = scan.nextLine();
        System.out.println("Type Your Password:");
        password = scan.nextLine();
        pStmt.setString(1, username);
        pStmt.setString(2, password);

        ResultSet resultSet = pStmt.executeQuery();

        if(resultSet.next()){
            System.out.println("You have successfully logged in, " + resultSet.getString(10) + "!\nSELECT ID OF THE USER WHO YOU WANT TO CHAT WITH:");
                showChatsList(conn, getUserID(conn, username));
                int recipientID = scan.nextInt();
                int senderID = getUserID(conn, username);
                scan.nextLine();

                showMessagesBetween(conn, username, senderID, recipientID);








            System.out.println("Would you like to send a message? Type 'yes' or 'no'");
            String answer = scan.nextLine();

            if(answer.toLowerCase().equals("yes")) {
                System.out.print("Your message: ");
                String message = scan.nextLine();
                Statement stmt = conn.createStatement();
                stmt.execute("INSERT INTO messages (sender, receiver, message_text) VALUES (" + senderID + ", " + recipientID + ", " + "'" + message + "');");
//                pStmt.setInt(1, senderID);
//                pStmt.setInt(2, recipientID);
//                pStmt.setString(3, message);
            }else{System.out.print("OK, BUY :'(");
            }


        }else{
            System.out.println("WRONG CREDENTIALS!");
        }


    }

    public static void update(Connection conn) throws SQLException {
        System.out.println("""
                Choose what do you want to update:
                1 First Name;
                2 Last Name;
                3 Email
                4 Phone
                5 Address
                6 Gender
                7 Age
                8 Password
                """);

        Scanner scan = new Scanner(System.in);
        int answer = scan.nextInt();
        scan.nextLine();
        switch (answer) {
            case 1:
                updateFirstName(conn);
                break;
            case 2:
                updateLastName(conn);
                break;
            case 3:
                updateEmail(conn);
                break;
            case 4:
                updatePhone(conn);
                break;
            case 5:
                updateAddress(conn);
                break;
            case 6:
                updateGender(conn);
                break;
            case 7:
                updateAge(conn);
                break;
            case 8:
                updatePassword(conn);
                break;
            default:
                break;
        }
    }

    public static void updateFirstName(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        User user = getUserByID(conn, id);
        System.out.println("Type new First Name:");
        String input = scan.nextLine();
        user.setFirstName(input);

        stmt.execute("UPDATE user SET firstName = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed First Name, your new username is in progress...");
        }else{
            System.out.println("smth went wrong!");
        }

        user.setUsername(user.createUsername(user.getFirstName(), user.getLastName(), user.getPhone()));

//        int i = 0;
//        int j = 0;
//
//        while((checkUsername(conn, user.getUsername()) > 0) && (j < 10)){
//            user.setUsername(user.getUsername().substring(0, user.getUsername().length() - 2) + j + i);
//            i++;
//            if(i == 10){
//                j += 1;
//                i = 0;
//            }
//        }

        stmt.execute("UPDATE user SET username = '" + user.getUsername() + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("Your new username " + user.getUsername() + " was created!");
        }else{
            System.out.println("smth went wrong!");
        }

    }

    public static void updateLastName(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        User user = getUserByID(conn, id);
        System.out.println("Type new Last Name:");
        String input = scan.nextLine();
        user.setLastName(input);

        stmt.execute("UPDATE user SET lastName = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed Last Name, your new username is in progress...");
        }else{
            System.out.println("smth went wrong!");
        }

        user.setUsername(user.createUsername(user.getFirstName(), user.getLastName(), user.getPhone()));

        stmt.execute("UPDATE user SET username = '" + user.getUsername() + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("Your new username " + user.getUsername() + " was created!");
        }else{
            System.out.println("smth went wrong!");
        }

    }

    public static void updateEmail(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Type new email:");
        String input = scan.nextLine();

        stmt.execute("UPDATE user SET email = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed email");
        }else{
            System.out.println("smth went wrong!");
        }
    }


    public static void updatePhone(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        User user = getUserByID(conn, id);
        System.out.println("Type new Phone:");
        String input = scan.nextLine();
        user.setPhone(input);

        stmt.execute("UPDATE user SET phone = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed Phone, your new username is in progress...");
        }else{
            System.out.println("smth went wrong!");
        }

        user.setUsername(user.createUsername(user.getFirstName(), user.getLastName(), user.getPhone()));

        stmt.execute("UPDATE user SET username = '" + user.getUsername() + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("Your new username " + user.getUsername() + " was created!");
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static void updateAddress(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Type new address:");
        String input = scan.nextLine();

        stmt.execute("UPDATE user SET address = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed address");
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static void updateGender(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Type new gender:");
        String input = scan.nextLine();

        stmt.execute("UPDATE user SET gender = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed gender");
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static void updateAge(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Type new age:");
        int input = scan.nextInt();
        scan.nextLine();

        stmt.execute("UPDATE user SET age = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed age");
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static void updatePassword(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user");
        int id = scan.nextInt();
        scan.nextLine();
        System.out.println("Type new password:");
        String input = scan.nextLine();

        stmt.execute("UPDATE user SET password = '" + input + "' WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully changed password");
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static void delete(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Scanner scan = new Scanner(System.in);
        System.out.println("Insert ID of the user to DELETE");
        int id = scan.nextInt();
        scan.nextLine();

        stmt.execute("DELETE FROM user WHERE id = " + id);

        if(stmt.getUpdateCount() > 0){
            System.out.println("You have successfully deleted user with id " + id);
        }else{
            System.out.println("smth went wrong!");
        }
    }

    public static User getUserByID(Connection conn, int id) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM user WHERE id = ?");
        pStmt.setInt(1, id);
        ResultSet resultSet = pStmt.executeQuery();
        if(resultSet.next()) {
            return new User(resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getByte(8), resultSet.getString(9), resultSet.getString(10));
        }else{
            return null;
        }
    }

    public static int getUserID(Connection conn, String username)throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT id FROM user WHERE username = ?");
        pStmt.setString(1, username);
        ResultSet resultSet = pStmt.executeQuery();
        if(resultSet.next()) {
            return resultSet.getInt(1);
        }else{
            return 0;
        }
    }

    public static boolean patternMatches(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }


    public static void showChatsList(Connection conn, int userId) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT * FROM user WHERE id <> ?");
        pStmt.setInt(1, userId);
        ResultSet resultSet = pStmt.executeQuery();

        while(resultSet.next()){
            System.out.println("User ID: " + resultSet.getString(1) + " || First and last name: " + resultSet.getString(2) + " " + resultSet.getString(3));
        }
    }

    public static void showMessagesBetween(Connection conn, String username, int senderID, int recipientID) throws SQLException{
        PreparedStatement pStmt = conn.prepareStatement("SELECT sender, receiver, time_sent, message_text FROM messages WHERE sender IN (?, ?) AND receiver IN (?, ?) ORDER BY time_sent ASC");
        pStmt.setInt(1, getUserID(conn, username));
        pStmt.setInt(2, recipientID);
        pStmt.setInt(3, getUserID(conn, username));
        pStmt.setInt(4, recipientID);
        ResultSet resultSet = pStmt.executeQuery();

        System.out.println("CHAT BETWEEN " + username + " AND " + getUserByID(conn, recipientID).getUsername());

        while(resultSet.next()){
            System.out.println("From: " + getUserByID(conn, resultSet.getInt(1)).getUsername() + " to " + getUserByID(conn, resultSet.getInt(2)).getUsername() + " at " + resultSet.getTimestamp(3) +": " + resultSet.getString(4));
        }
    }










}

/*
        Create a user profile management system
        User (firstname, lastname, email, phone, address, gender, age, password, USERNAME(firstname + lastname + last 2 digits of phone))
        SIGNUP, LOGIN(username, password), UPDATE(id), DELETE(id) [CRUD]

CLASS EXERCISE
CREATE A MESSAGING SYSTEM WITH YOUR USER PROFILES
TABLE MESSAGES [TO(receiver’s id), FROM(sender’s id), TIME_SENT]
USER CAN SEE LIST OF OTHER USERS AFTER LOGIN AND CAN SELECT WHO TO CHAT WITH
IF USER ALREADY HAS EXISTING CHAT WITH A FRIEND HE SELECTS, DISPLAY OLD MESSAGES BETWEEN BOTH OF THEM IN CONSOLE
AND UPDATE EVERY TIME A NEW MESSAGE IS SENT
E.G
FRIEND AT 12:30: hello there
CURRENT USER AT 12:45: hi how are you
Display all messages on console and after user sends a message ask if they want to continue sending messages.
 */

