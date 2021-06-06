//package controller;
//
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.fxml.Initializable;
//
//import module-info.java.io.FileNotFoundException;
//import module-info.java.io.FileReader;
//import module-info.java.io.IOException;
//import module-info.java.io.*;
//import module-info.java.net.URL;
//import module-info.java.util.ResourceBundle;
//import module-info.java.util.Scanner;
//
//public class UserLogInAttemptsController implements Initializable {
//    private int userLoginAttempts = 0;
//    private int successfulUserLoginAttempts = 0;
//    private int unsuccessfulUserLoginAttempts = 0;
//    ObservableList<String> loginTimestamps = FXCollections.observableArrayList();
//    ObservableList<String> loginDates = FXCollections.observableArrayList();
//    public void readLog() throws IOException {
//        System.out.println("Reached read log.");
//        int item = 0;
//        String logFile = "login_activity.txt";
//        File file = new File(logFile);
//        System.out.println("Reached read log - file created.");
//        if (file.exists()) {
//            System.out.println("Reached read log - file exists.");
//            Scanner scanner = new Scanner(file);
//            while(scanner.hasNext()) {
//                System.out.println("Reached read log - file has next.");
//                if (((item) % 4) == 0) {
//                    System.out.println("Reached read log - parsing 1st line.");
//                    System.out.println(scanner.nextLine());
//                    ++item;
//                    ++userLoginAttempts;
//                    System.out.println("User attempted to login. Total attempts: " + userLoginAttempts);
//                }
//                else if (((item) % 4) == 1) {
//                    System.out.println("Reached read log - parsing 2nd line.");
////                    System.out.println(scanner.nextLine());
//                    ++item;
//                    String timestampLine = scanner.nextLine();
//                    System.out.println(timestampLine);
//                    String timestamp = timestampLine.substring(11,30);
//                    loginTimestamps.add(timestamp);
//                }
//                else if (((item) % 4) == 2) {
//                    System.out.println("Reached read log - parsing 3rd line.");
////                    System.out.println(scanner.nextLine());
//                    ++item;
//                    String dateLine = scanner.nextLine();
//                    System.out.println(dateLine);
//                    String date = dateLine.substring(6,16);
////                    System.out.println("Date substring: " + date);
//                    loginDates.add(date);
//                }
//                else if (((item) % 4) == 3) {
//                    System.out.println("Reached read log - parsing 4th line.");
////                    System.out.println(scanner.nextLine());
//                    ++item;
//                    String resultOfLogin = scanner.nextLine();
//                    System.out.println(resultOfLogin);
//                    if (resultOfLogin.equals("Login successful.")) {
//                        ++successfulUserLoginAttempts;
//                        System.out.println("Successful login attempt. Total successful: " + successfulUserLoginAttempts);
//                    } else {
//                        ++unsuccessfulUserLoginAttempts;
//                        System.out.println("Login attempt failed. Total failed: " + unsuccessfulUserLoginAttempts);
//                    }
//                }
//                else {
//                    System.out.println("Reached read log - unknown error.");
//                    System.out.println("Unknown error, math must be broken.");
//                }
//            }
//            System.out.println("Total user login attempts: " + userLoginAttempts);
//            System.out.println("User timestamps: " + loginTimestamps);
//            System.out.println("User login dates: " + loginDates);
//            System.out.println("Total successful login attempts: " + successfulUserLoginAttempts);
//            System.out.println("Total unsuccessful login attempts: " + unsuccessfulUserLoginAttempts);
//        }
//    }
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        try {
//            readLog();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
