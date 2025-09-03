package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

//public class DatabaseCleaner {
//    private static final String URL = "jdbc:postgresql://localhost:5432/project1";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "/PostgreS@1478950+/";
//
//
//    private final Connection connection;
//    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
//    // 构造函数，接收数据库连接
//    public DatabaseCleaner() {
//
//        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//
//    // 清空数据库的主方法
//    public void cleanDatabase() {
//        String[] dropStatements = {
//                "DROP TABLE IF EXISTS pubtypes CASCADE",
//                "DROP TABLE IF EXISTS pubtypes_article CASCADE",
//                "DROP TABLE IF EXISTS reference_article CASCADE",
//                "DROP TABLE IF EXISTS reference CASCADE",
//                "DROP TABLE IF EXISTS grants CASCADE",
//                "DROP TABLE IF EXISTS article_grants CASCADE",
//                "DROP TABLE IF EXISTS article_ids CASCADE",
//                "DROP TABLE IF EXISTS article_author CASCADE",
//                "DROP TABLE IF EXISTS references_article CASCADE",
//                "DROP TABLE IF EXISTS publication_types CASCADE",
//                "DROP TABLE IF EXISTS author CASCADE",
//                "DROP TABLE IF EXISTS affiliation CASCADE",
//                "DROP TABLE IF EXISTS author_affiliation CASCADE",
//                "DROP TABLE IF EXISTS journal_issue CASCADE",
//                "DROP TYPE IF EXISTS journal_issue_type CASCADE",
//                "DROP TABLE IF EXISTS journal CASCADE",
//                "DROP TABLE IF EXISTS keywords CASCADE",
//                "DROP TABLE IF EXISTS article_keywords CASCADE",
//                "DROP TABLE IF EXISTS article CASCADE",
//                "DROP TABLE IF EXISTS ids CASCADE"
//        };
//
//        try (Statement stmt = connection.createStatement()) {
//            // 开始事务
//            connection.setAutoCommit(false);
//
//            try {
//                // 执行所有DROP语句
//                for (String dropStatement : dropStatements) {
//                    stmt.executeUpdate(dropStatement);
//                    System.out.println("Executed: " + dropStatement);
//                }
//
//                // 提交事务
//                connection.commit();
//                System.out.println("Database cleaned successfully!");
//
//            } catch (SQLException e) {
//                // 如果发生错误，回滚事务
//                connection.rollback();
//                System.err.println("Error cleaning database: " + e.getMessage());
//                throw e;
//            }
//
//        } catch (SQLException e) {
//            System.err.println("Database operation failed: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            try {
//                // 恢复自动提交设置
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.err.println("Error resetting auto-commit: " + e.getMessage());
//            }
//        }
//    }
//
//    // 使用示例
//    public static void main(String[] args) {
//        // 假设你已经有了数据库连接实例
//        Connection dbConnection = null; // 这里需要替换为你的实际数据库连接
//
//        try {
//            DatabaseCleaner cleaner = new DatabaseCleaner(dbConnection);
//            cleaner.cleanDatabase();
//        } catch (Exception e) {
//            System.err.println("Failed to clean database: " + e.getMessage());
//        }
//    }
//}

//public class DatabaseCleaner {
//    private final Connection connection;
//
//    public DatabaseCleaner(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void cleanDatabase() {
//        String[] dropStatements = {
//                "DROP TABLE IF EXISTS pubtypes CASCADE",
//                "DROP TABLE IF EXISTS pubtypes_article CASCADE",
//                "DROP TABLE IF EXISTS reference_article CASCADE",
//                "DROP TABLE IF EXISTS reference CASCADE",
//                "DROP TABLE IF EXISTS grants CASCADE",
//                "DROP TABLE IF EXISTS article_grants CASCADE",
//                "DROP TABLE IF EXISTS article_ids CASCADE",
//                "DROP TABLE IF EXISTS article_author CASCADE",
//                "DROP TABLE IF EXISTS references_article CASCADE",
//                "DROP TABLE IF EXISTS publication_types CASCADE",
//                "DROP TABLE IF EXISTS author CASCADE",
//                "DROP TABLE IF EXISTS affiliation CASCADE",
//                "DROP TABLE IF EXISTS author_affiliation CASCADE",
//                "DROP TABLE IF EXISTS journal_issue CASCADE",
//                "DROP TYPE IF EXISTS journal_issue_type CASCADE",
//                "DROP TABLE IF EXISTS journal CASCADE",
//                "DROP TABLE IF EXISTS keywords CASCADE",
//                "DROP TABLE IF EXISTS article_keywords CASCADE",
//                "DROP TABLE IF EXISTS article CASCADE",
//                "DROP TABLE IF EXISTS ids CASCADE"
//        };
//
//        try (Statement stmt = connection.createStatement()) {
//            connection.setAutoCommit(false);
//            try {
//                for (String dropStatement : dropStatements) {
//                    stmt.executeUpdate(dropStatement);
//                    System.out.println("Executed: " + dropStatement);
//                }
//                connection.commit();
//                System.out.println("Database cleaned successfully!");
//            } catch (SQLException e) {
//                connection.rollback();
//                System.err.println("Error cleaning database: " + e.getMessage());
//                throw e;
//            }
//        } catch (SQLException e) {
//            System.err.println("Database operation failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
public class DatabaseCleaner {
    private final Connection connection;

    public DatabaseCleaner(Connection connection) {
        this.connection = connection;
    }

    public void cleanDatabase() {
        String[] dropStatements = {
                "DROP TABLE IF EXISTS pubtypes CASCADE",
                "DROP TABLE IF EXISTS pubtypes_article CASCADE",
                "DROP TABLE IF EXISTS reference_article CASCADE",
                "DROP TABLE IF EXISTS reference CASCADE",
                "DROP TABLE IF EXISTS grants CASCADE",
                "DROP TABLE IF EXISTS article_grants CASCADE",
                "DROP TABLE IF EXISTS article_ids CASCADE",
                "DROP TABLE IF EXISTS article_author CASCADE",
                "DROP TABLE IF EXISTS references_article CASCADE",
                "DROP TABLE IF EXISTS publication_types CASCADE",
                "DROP TABLE IF EXISTS author CASCADE",
                "DROP TABLE IF EXISTS affiliation CASCADE",
                "DROP TABLE IF EXISTS author_affiliation CASCADE",
                "DROP TABLE IF EXISTS journal_issue CASCADE",
                "DROP TYPE IF EXISTS journal_issue_type CASCADE",
                "DROP TABLE IF EXISTS journal CASCADE",
                "DROP TABLE IF EXISTS keywords CASCADE",
                "DROP TABLE IF EXISTS article_keywords CASCADE",
                "DROP TABLE IF EXISTS article CASCADE",
                "DROP TABLE IF EXISTS ids CASCADE"
        };

        String[] createStatements = {
                "CREATE TABLE article(" +
                        "id INT PRIMARY KEY," +
                        "title VARCHAR(1000) NOT NULL," +
                        "pub_model VARCHAR," +
                        "date_created DATE NOT NULL," +
                        "date_completed DATE," +
                        "CONSTRAINT check_pub_model CHECK ( pub_model IN ('Print', 'Print-Electronic', 'Electronic', 'Electronic-Print', 'Electronic-eCollection') )" +
                        ")",

                "CREATE TABLE grants (" +
                        "identity SERIAL PRIMARY KEY," +
                        "id VARCHAR(1000)," +
                        "acronym VARCHAR(1000)," +
                        "country VARCHAR(1000)," +
                        "agency VARCHAR(1000) NOT NULL" +
                        ")",

                "CREATE TABLE article_grants(" +
                        "grant_identity INT," +
                        "article_id INT," +
                        "PRIMARY KEY (grant_identity, article_id)," +
                        "FOREIGN KEY (grant_identity) REFERENCES grants(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")",

                "CREATE TABLE ids (" +
                        "identity SERIAL PRIMARY KEY," +
                        "type VARCHAR," +
                        "id VARCHAR(1000)," +
                        "CONSTRAINT valid_type CHECK (type IN ('pubmed', 'doi'))" +
                        ")",

                "CREATE TABLE article_ids(" +
                        "ids INT," +
                        "article_id INT," +
                        "PRIMARY KEY (ids, article_id)," +
                        "FOREIGN KEY (ids) REFERENCES ids(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")",

                "CREATE TABLE reference(" +
                        "identity SERIAL PRIMARY KEY," +
                        "references_id varchar(1000)" +
                        ")",

                "CREATE TABLE reference_article(" +
                        "article_id INT," +
                        "references_identity INT," +
                        "PRIMARY KEY (article_id, references_identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)," +
                        "FOREIGN KEY (references_identity) REFERENCES reference(identity)" +
                        ")",

                "CREATE TABLE pubtypes(" +
                        "identity SERIAL PRIMARY KEY," +
                        "id VARCHAR(1000)," +
                        "name VARCHAR(1000)" +
                        ")",

                "CREATE TABLE pubtypes_article(" +
                        "pubtypes_identity INT," +
                        "article_id INT," +
                        "PRIMARY KEY (pubtypes_identity, article_id)," +
                        "FOREIGN KEY (pubtypes_identity) REFERENCES pubtypes(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")",

                "CREATE TABLE author (" +
                        "identity SERIAL PRIMARY KEY," +
                        "fore_name VARCHAR DEFAULT ''," +
                        "last_name VARCHAR NOT NULL," +
                        "initials VARCHAR DEFAULT ''," +
                        "is_collective BOOLEAN DEFAULT FALSE," +
                        "CONSTRAINT unique_name_quadruple UNIQUE (fore_name, last_name, initials, is_collective)" +
                        ")",

                "CREATE TABLE article_author(" +
                        "author_id INT," +
                        "article_id INT," +
                        "PRIMARY KEY (author_id, article_id)," +
                        "FOREIGN KEY (author_id) REFERENCES author(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")",

                "CREATE TABLE affiliation (" +
                        "identity SERIAL PRIMARY KEY," +
                        "name VARCHAR(1000) NOT NULL" +
                        ")",

                "CREATE TABLE author_affiliation (" +
                        "author_id INT," +
                        "affiliation_id INT," +
                        "PRIMARY KEY (author_id, affiliation_id)," +
                        "FOREIGN KEY (author_id) REFERENCES author(identity)," +
                        "FOREIGN KEY (affiliation_id) REFERENCES affiliation(identity)" +
                        ")",

                "CREATE TABLE journal (" +
                        "identity SERIAL PRIMARY KEY," +
                        "id VARCHAR(1000)," +
                        "country VARCHAR(1000)," +
                        "title VARCHAR(1000)," +
                        "issn VARCHAR(1000)" +
                        ")",

                "CREATE TABLE journal_issue(" +
                        "journal_id INT," +
                        "article_id INT," +
                        "issue VARCHAR(1000) NULL," +
                        "volume VARCHAR(1000) NULL," +
                        "PRIMARY KEY (journal_id, article_id)," +
                        "FOREIGN KEY (journal_id) REFERENCES journal(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")",

                "CREATE TABLE keywords (" +
                        "identity SERIAL PRIMARY KEY," +
                        "keyword VARCHAR(1000) UNIQUE" +
                        ")",

                "CREATE TABLE article_keywords (" +
                        "keywords_identity INT," +
                        "article_id INT," +
                        "PRIMARY KEY (article_id, keywords_identity)," +
                        "FOREIGN KEY (keywords_identity) REFERENCES keywords(identity)," +
                        "FOREIGN KEY (article_id) REFERENCES article(id)" +
                        ")"
        };

        try (Statement stmt = connection.createStatement()) {
            connection.setAutoCommit(false);
            try {
                // 执行所有DROP语句
                for (String dropStatement : dropStatements) {
                    stmt.executeUpdate(dropStatement);
                    System.out.println("Executed DROP: " + dropStatement);
                }

                // 执行所有CREATE语句
                for (String createStatement : createStatements) {
                    stmt.executeUpdate(createStatement);
                    System.out.println("Executed CREATE: " + createStatement);
                }

                connection.commit();
                System.out.println("Database cleaned and recreated successfully!");
            } catch (SQLException e) {
                connection.rollback();
                System.err.println("Error cleaning/recreating database: " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
