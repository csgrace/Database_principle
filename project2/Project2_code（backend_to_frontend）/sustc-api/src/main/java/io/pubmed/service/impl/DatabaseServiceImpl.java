package io.pubmed.service.impl;

import io.pubmed.service.DatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * It's important to mark your implementation class with {@link Service} annotation.
 * As long as the class is annotated and implements the corresponding interface, you can place it under any package.
 */
/**
 *用{@link Service}注释标记您的实现类非常重要。
 *只要该类被注释并实现了相应的接口，您就可以将其放置在任何包下。
 */
@Service
@Slf4j
public class DatabaseServiceImpl implements DatabaseService {

    /**
     * Getting a {@link DataSource} instance from the framework, whose connections are managed by HikariCP.
     * <p>
     * Marking a field with {@link Autowired} annotation enables our framework to automatically
     * provide you a well-configured instance of {@link DataSource}.
     * Learn more: <a href="https://www.baeldung.com/spring-dependency-injection">Dependency Injection</a>
     */
    /**
     *从框架中获取｛@link DataSource｝实例，其连接由HikariCP管理。
     p
     *使用{@link Autowired}注释标记字段使我们的框架能够自动
     *为您提供配置良好的｛@link DataSource｝实例。
     *了解更多信息：<a href=“https://www.baeldung.com/spring-dependency-injection“>依赖注入</a>
     */
    @Autowired
    private DataSource dataSource;

    @Override
    public List<Integer> getGroupMembers() {
        //TODO: replace this with your own student IDs in your group
        return Arrays.asList(12311004,12311043);
    }

    @Override
    public void importData(String data_path) {
        String sql = "INSERT INTO authors (fore_name, last_name, initials) VALUES ('test', 'test', 'test') ";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * The following code is just a quick example of using jdbc datasource.
     * Practically, the code interacts with database is usually written in a DAO layer.
     *
     * Reference: [Data Access Object pattern](https://www.baeldung.com/java-dao-pattern)
     */
    /*
     *以下代码只是使用jdbc数据源的一个快速示例。
     *实际上，与数据库交互的代码通常是在DAO层编写的。
     *
     *参考：[数据访问对象模式](https://www.baeldung.com/java-dao-pattern)
     */

    @Override
    public void truncate() {
        // You can use the default truncate script provided by us in most cases,
        // but if it doesn't work properly, you may need to modify it.

        String sql = "DO $$\n" +
                "DECLARE\n" +
                "    tables CURSOR FOR\n" +
                "        SELECT tablename\n" +
                "        FROM pg_tables\n" +
                "        WHERE schemaname = 'public';\n" +
                "BEGIN\n" +
                "    FOR t IN tables\n" +
                "    LOOP\n" +
                "        EXECUTE 'TRUNCATE TABLE ' || QUOTE_IDENT(t.tablename) || ' CASCADE;';\n" +
                "    END LOOP;\n" +
                "END $$;\n";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //这个方法通过执行一个 PL/pgSQL 脚本，清空数据库中的所有表。
    //具体来说，它通过查询 pg_tables 获取所有公共模式下的表名，并使用 TRUNCATE 语句清空每个表的数据。
    //CASCADE 选项确保清空表时，所有相关的外键约束也会被处理

    @Override
    public Integer sum(int a, int b) {
        String sql = "SELECT ?+?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a);
            stmt.setInt(2, b);
            //log.info("SQL: {}", stmt);

            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void validatePassword(String username, String password) {
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
        if (password.toLowerCase().contains(username.toLowerCase())) {
            throw new IllegalArgumentException("Password cannot contain the username.");
        }
        if (!password.matches("[A-Za-z0-9_*$]+")) {
            throw new IllegalArgumentException("Password contains invalid characters.");
        }
    }

    @Override
    public void createUser(String username, String password) {
        try {
            validatePassword(username, password);
        } catch (IllegalArgumentException e) {
            log.error("Password validation failed for user '{}': {}", username, e.getMessage());
            return;
        }
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Consider hashing the password
            stmt.setString(3, "USER"); // Default role
            stmt.executeUpdate();
            log.info("User '{}' created successfully.", username);
        } catch (SQLException e) {
            log.error("Error creating user '{}': {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void changePassword(String username, String newPassword) {
        try {
            validatePassword(username, newPassword);
        } catch (IllegalArgumentException e) {
            log.error("Password validation failed for user '{}': {}", username, e.getMessage());
            return;
        }
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Consider hashing the new password
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                log.info("Password for user '{}' updated successfully.", username);
            } else {
                log.warn("User '{}' not found. No password updated.", username);
            }
        } catch (SQLException e) {
            log.error("Error changing password for user '{}': {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assignPermission(String username, String permission) {
        String sql = "UPDATE users SET role = ? WHERE username = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, permission);
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                log.info("Permission '{}' assigned to user '{}'.", permission, username);
                switch(permission.toUpperCase()) {
                    case "ADMIN":
                        stmt.executeUpdate("GRANT ALL PRIVILEGES ON DATABASE project2 TO " + username);
                    break;
                    case "JOURNAL_ADMIN":
                        stmt.executeUpdate("GRANT SELECT, INSERT, UPDATE ON TABLE journal TO " + username);
                        stmt.executeUpdate("GRANT SELECT, INSERT, UPDATE ON TABLE article_journal TO " + username);
                    break;
                    case "USER":
                        stmt.executeUpdate("GRANT SELECT ON ALL TABLES IN SCHEMA public TO " + username);
                        break;
                    default:
                        System.err.println("Unknown permission type: " + permission);
                }
            } else {
                log.warn("User '{}' not found. No permission assigned.", username);
            }
        } catch (SQLException e) {
            log.error("Error assigning permission '{}' to user '{}': {}", permission, username, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String username, String password) {
        String sql = 
            "SELECT COUNT(*) FROM users " +
            "WHERE LOWER(username) = LOWER(?) AND password = ?"; 
        // This helps if username is stored in varying cases

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Consider hashing the password
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            return false;
        } catch (SQLException e) {
            log.error("Error during login for user '{}': {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
