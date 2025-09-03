package io.pubmed.database_login_system;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseLogin {

    private static void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters.");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter.");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter.");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one digit.");
        }
        if (!password.matches("[A-Za-z0-9_*$]+")) {
            throw new IllegalArgumentException("Password contains invalid characters.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter database username: ");
        String username = scanner.nextLine();

        System.out.print("Enter database password: ");
        String password = scanner.nextLine();

        try {
            validatePassword(password);
        } catch (IllegalArgumentException e) {
            System.err.println("Password validation failed: " + e.getMessage());
            return;
        }

        // Replace with your actual database connection details
        String host = "localhost";
        String port = "5432";
        String database = "project2";
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to the database successfully as user: " + username);
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}