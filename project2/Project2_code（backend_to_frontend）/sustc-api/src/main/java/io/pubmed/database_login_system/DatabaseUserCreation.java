package io.pubmed.database_login_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseUserCreation {


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

        System.out.print("Enter new database username: ");
        String newUsername = scanner.nextLine();

        System.out.print("Enter new database password: ");
        String newPassword = scanner.nextLine();

        try {
            validatePassword(newPassword);
        } catch (IllegalArgumentException e) {
            System.err.println("Password validation failed: " + e.getMessage());
            return;
        }

        System.out.print("Enter user type (1: Admin, 2: Journal Admin, 3: User): ");
        String userTypeInput = scanner.nextLine();
        String role;
        switch(userTypeInput) {
            case "1":
                role = "ADMIN";
                break;
            case "2":
                role = "JOURNAL_ADMIN";
                break;
            case "3":
                role = "USER";
                break;
            default:
                System.out.println("Invalid user type. Defaulting to USER.");
                role = "USER";
        }

        // Replace with your actual database connection details
        String host = "localhost";
        String port = "5432";
        String database = "project2";
        String adminUsername = "postgres"; // Replace with your admin username
        String adminPassword = "/PostgreS@1478950+/"; // Replace with your admin password
        String url = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);

        try (Connection conn = DriverManager.getConnection(url, adminUsername, adminPassword);
             Statement stmt = conn.createStatement()) {

            String sql = String.format("CREATE USER %s WITH PASSWORD '%s'", newUsername, newPassword);
            System.out.println("Executing SQL: " + sql);
            stmt.executeUpdate(sql);
            // Assign roles based on user type
            switch(role) {
                case "ADMIN":
                    stmt.executeUpdate("GRANT ALL PRIVILEGES ON DATABASE project2 TO " + newUsername);
                    break;
                case "JOURNAL_ADMIN":
                    stmt.executeUpdate("GRANT SELECT, INSERT, UPDATE ON TABLE journal TO " + newUsername);
                    stmt.executeUpdate("GRANT SELECT, INSERT, UPDATE ON TABLE article_journal TO " + newUsername);
                    break;
                case "USER":
                    stmt.executeUpdate("GRANT SELECT ON ALL TABLES IN SCHEMA public TO " + newUsername);
                    break;
            }

            /*
            如果为admin权限，则可以访问所有方法，
            如果为journal_admin权限，则不能访问truncate方法，其他方法可以访问，
            如果为user权限，则不能访问truncate和updateJournalName方法，其他方法可以访问
             */            

            System.out.println("User created successfully: " + newUsername + " with role: " + role);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("User creation failed: " + e.getMessage());
        }
    }
}
