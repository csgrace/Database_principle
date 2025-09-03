package io.pubmed.service.impl;
import io.pubmed.dto.Article;
import io.pubmed.dto.Journal;
import io.pubmed.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;

import io.pubmed.dto.Article;
import io.pubmed.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.*;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private DataSource dataSource;
    /**
     * Find the number of citations for an article in a given year
     *
     * @param id the article's id
     * @param year need queried year
     * @return the number of article's citations in given year,
     */
    @Override
    public int getArticleCitationsByYear(int id, int year) {
        String sql = "SELECT citation_count FROM Article_Citations WHERE article_id = ? AND citation_year = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // 设置文章 ID
            stmt.setInt(2, year); // 设置年份
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("citation_count"); // 返回引用次数
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0; // 如果没有查询结果，返回 0
    }

    /**
     * Fist, add one article to your database
     * Second, output the journal IF after adding this article
     * Third, delete the article from your database
     *
     * if year = 2024, you need sum citations of given journal in 2024 /
     * [2022-2023] published articles num in the journal.
     * Example:
     * IF（2024） = A / B
     * A = The number of times all articles in the journal from 2022 to 2023 were cited in 2024.
     * B = Number of articles in the journal from 2022 to 2023.
     *
     * @param article all the article's info
     * @return the updated IF of given article's Journal
     */
    @Override
    public double addArticleAndUpdateIF(Article article) {
        double impactFactor = 0;
        Date createdDate = article.getCreated();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(createdDate);
        int year = calendar.get(Calendar.YEAR);

        try (Connection conn = dataSource.getConnection()) {
            // boolean articleExists = false;
            // String checkArticleSql = "SELECT COUNT(*) FROM article WHERE id = ?";
            // try (PreparedStatement stmt = conn.prepareStatement(checkArticleSql)) {
            //     stmt.setInt(1, article.getId());
            //     ResultSet rs = stmt.executeQuery();
            //     if (rs.next() && rs.getInt(1) > 0) {
            //         articleExists = true;
            //     }
            // }

            // if (!articleExists) {
                String insertArticleSql = "INSERT INTO article (id, title, pub_model, date_created, date_completed) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertArticleSql)) {
                    stmt.setInt(1, article.getId());
                    stmt.setString(2, article.getTitle());
                    stmt.setString(3, article.getPub_model());
                    stmt.setDate(4, new java.sql.Date(article.getCreated().getTime()));
                    stmt.setDate(5, new java.sql.Date(article.getCompleted().getTime()));
                    stmt.executeUpdate();
                }

                String insertArticleJournalSql = "INSERT INTO article_journal (article_id, journal_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertArticleJournalSql)) {
                    stmt.setInt(1, article.getId());
                    stmt.setString(2, article.getJournal().getId());
                    stmt.executeUpdate();
                }
            // }

            String countArticlesSql =
                    "SELECT COUNT(a.id) AS total_articles " +
                    "FROM Article a " +
                    "JOIN Article_Journal aj ON a.id = aj.article_id " +
                    "JOIN Journal j ON aj.journal_id = j.id " +
                    "WHERE j.id = ? " +
                    "AND EXTRACT(YEAR FROM a.date_created) BETWEEN ? AND ? " +
                    "GROUP BY j.id;";

            int totalArticles = 0;
            try (PreparedStatement stmt = conn.prepareStatement(countArticlesSql)) {
                stmt.setString(1, article.getJournal().getId());
                stmt.setInt(2, year - 1);
                stmt.setInt(3, year);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalArticles = rs.getInt(1);
                }
            }

            String citationCountSql =
                    "SELECT SUM(ac.citation_count) AS total_citations " +
                    "FROM Article_Citations ac " +
                    "JOIN Article_Journal aj ON ac.article_id = aj.article_id " +
                    "JOIN article a ON aj.article_id = a.id " +
                    "JOIN Journal j ON aj.journal_id = j.id " +
                    "WHERE j.id = ? " +
                    "AND ac.citation_year = ? " +
                    "AND EXTRACT(YEAR FROM a.date_created) BETWEEN ? AND ?";

            int totalCitations = 0;
            try (PreparedStatement stmt = conn.prepareStatement(citationCountSql)) {
                stmt.setString(1, article.getJournal().getId());
                stmt.setInt(2, year + 1);
                stmt.setInt(3, year - 1);
                stmt.setInt(4, year);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    totalCitations = rs.getInt(1);
                }
            }

            if (totalArticles > 0) {
                impactFactor = (double) totalCitations / totalArticles;
            }

            // if (!articleExists) {
                String deleteArticleSql = "DELETE FROM Article WHERE id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteArticleSql)) {
                    stmt.setInt(1, article.getId());
                    stmt.executeUpdate();
                }

                String deleteArticleJournalSql = "DELETE FROM article_journal WHERE article_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(deleteArticleJournalSql)) {
                    stmt.setInt(1, article.getId());
                    stmt.executeUpdate();
                }
            // }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return impactFactor;
    }

    @Override
    public Journal getJournalByArticleId(int articleId) {
        Journal journal = null;
        String sql = "SELECT j.id, j.title FROM Journal j JOIN Article_Journal aj ON j.id = aj.journal_id WHERE aj.article_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, articleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                journal = new Journal();
                journal.setId(rs.getString("id"));
                journal.setTitle(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return journal;
    }
}
