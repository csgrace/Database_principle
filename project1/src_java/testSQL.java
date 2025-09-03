package org.example;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class testSQL {
    private static final String URL = "jdbc:postgresql://localhost:5432/project1";
    private static final String USER = "postgres";
    private static final String PASSWORD = "huarui66";
    private final Map<String, Integer> journalCache = new ConcurrentHashMap<>();
    public void insertNestedData(Map<String, Object> data) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            try {
                // 开启事务
                conn.setAutoCommit(false);
                // 1. 插入 article 主表数据
                int articleId = insertArticle(conn, data);
                // 2. 插入 journal 和 journal_issue
                Map<String, Object> journalData = (Map<String, Object>) data.get("journal");
                if (journalData != null) {
                    Integer journalIdentity = insertJournal(conn, journalData);
                    insertJournalIssue(conn, journalIdentity, articleId, (Map<String, Object>) journalData.get("journal_issue"));
                }

                // 提交事务
                conn.commit();
                System.out.println("Data inserted successfully.");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Integer insertArticle(Connection conn, Map<String, Object> data) throws SQLException {
        String sql = "INSERT INTO article (id, title, pub_model, date_created, date_completed) " +
                " VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Map<String, Object> dateCreated = (Map<String, Object>) data.get("date_created");
            Map<String, Object> dateCompleted = (Map<String, Object>) data.get("date_completed");

            pstmt.setInt(1, ((Number) data.get("id")).intValue());
            pstmt.setString(2, (String) data.get("title"));
            pstmt.setString(3, (String) data.get("pub_model"));
            pstmt.setDate(4, createDate(dateCreated));
            pstmt.setDate(5, createDate(dateCompleted));
//            pstmt.setObject(6, journalId);
//            pstmt.setString(7, authorLastName);

            ResultSet rs = pstmt.executeQuery();
            conn.commit();
            return rs.next() ? rs.getInt(1) : null;
        }
    }
    private java.sql.Date createDate(Map<String, Object> dateMap) {
        if (dateMap == null) return null;
        return java.sql.Date.valueOf(String.format("%d-%02d-%02d",
                ((Number) dateMap.get("year")).intValue(),
                ((Number) dateMap.get("month")).intValue(),
                ((Number) dateMap.get("day")).intValue()));
    }
    private Integer insertJournal(Connection conn, Map<String, Object> journalData) throws SQLException {
        String sql = "INSERT INTO journal (id, country, title, issn) VALUES (?, ?, ?, ?) RETURNING identity";

        String cacheKey = String.format("%s:%s:%s:%s",
                journalData.get("id"),
                journalData.get("country"),
                journalData.get("title"),
                journalData.get("issn"));

        // 先检查缓存
        Integer cachedIdentity = journalCache.get(cacheKey);
        if (cachedIdentity != null) {
            return cachedIdentity;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, (String) journalData.get("id"));
            pstmt.setString(2, (String) journalData.get("country"));
            pstmt.setString(3, (String) journalData.get("title"));
            pstmt.setString(4, (String) journalData.get("issn"));

            // 可以优化
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int journalIdentity = rs.getInt(1);

            conn.commit();

            journalCache.put(cacheKey, journalIdentity);
            return journalIdentity;
        }
    }
    private void insertJournalIssue(Connection conn, Integer journalIdentity, int articleId, Map<String, Object> journalIssue) throws SQLException {
        String sql = "INSERT INTO journal_issue (journal_id, article_id, issue, volume) VALUES (?, ?, ?, ?)";
        if (journalIdentity == null) {
            throw new SQLException("Invalid journal identity: null");
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, journalIdentity);
            pstmt.setInt(2, articleId);
            pstmt.setString(3, (String) journalIssue.get("issue"));
            pstmt.setString(4, (String) journalIssue.get("volume"));
            pstmt.executeUpdate();
        }
    }

}
