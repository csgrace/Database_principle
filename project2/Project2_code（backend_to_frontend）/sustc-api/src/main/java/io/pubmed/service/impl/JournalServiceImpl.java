package io.pubmed.service.impl;

import io.pubmed.dto.Journal;
import io.pubmed.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JournalServiceImpl implements JournalService {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Please calculate the journal's Impact Factor(IF) at the given year.
     * At the given year, IF = the total number of citations of articles in the
     * given year / the total number of articles published in the journal in
     * the previous two years.
     *
     * if year = 2024, you need sum citations of given journal in 2024 /
     * [2022-2023] published articles num in the journal.
     * Example:
     * IF（2024） = A / B
     * A = The number of times all articles in the journal from 2022 to 2023 were cited in 2024.
     * B = Number of articles in the journal from 2022 to 2023.
     *
     * @param journal_id need queried journal id
     * @param year need queried year
     * @return the title of the required Journal
     */
    @Override
    public double getImpactFactor(String journal_id, int year) {
        String citationQuery = 
            "SELECT SUM(ac.citation_count) AS total_citations " +
            "FROM Article_Citations ac " +
            "JOIN Article_Journal aj ON ac.article_id = aj.article_id " +
            "JOIN article a ON aj.article_id = a.id " +
            "WHERE aj.journal_id = ? " +
            "AND ac.citation_year = ? " +
            "AND EXTRACT(YEAR FROM a.date_created)::int BETWEEN ? AND ?";

        String articleQuery = 
            "SELECT COUNT(Article.id) AS total_articles " +
            "FROM Journal " +
            "JOIN Article_Journal ON Journal.id = Article_Journal.journal_id " +
            "JOIN Article ON Article_Journal.article_id = Article.id " +
            "WHERE Journal.id = ? " +
            "AND EXTRACT(YEAR FROM Article.date_created) BETWEEN ? AND ? " +
            "GROUP BY Journal.id";

        // Execute the citation query
        Integer citations;
        try {
            citations = jdbcTemplate.queryForObject(citationQuery, Integer.class, journal_id, year, year - 2, year - 1);
            citations = Objects.requireNonNullElse(citations, 0);
        } catch (Exception e) {
            citations = 0;
        }

        // Execute the article query
        Integer articles;
        try {
            articles = jdbcTemplate.queryForObject(articleQuery, Integer.class, journal_id, year - 2, year - 1);
            articles = Objects.requireNonNullElse(articles, 0);
        } catch (Exception e) {
            articles = 0;
        }

        // Calculate and return the impact factor
        return articles == 0 ? 0.0 : (double) citations / articles;
    }

    /**
     * A journal changed its title from given year, but database data was not update,
     * please update the database, change the article's journal_title from given year
     * (include that year).
     * @param journal need update journal, only contain title and id fields
     * @param year need update from and include year
     * @param new_name need update old journal tile to new_name
     * @param new_id the new journal title's id
     * @return your implement success or not
     * Tips: After testing, you would better delete it from database for next testing.
     */



    @Override
     public boolean updateJournalName(Journal journal, int year, String new_name, String new_id) {
         try (Connection conn = dataSource.getConnection()) {
             conn.setAutoCommit(false);
     
             // 1. First alter the constraint to be deferrable
             try (Statement stmt = conn.createStatement()) {
                 stmt.execute("ALTER TABLE article_journal DROP CONSTRAINT article_journal_journal_id_fkey");
                 stmt.execute("ALTER TABLE article_journal ADD CONSTRAINT article_journal_journal_id_fkey " +
                             "FOREIGN KEY (journal_id) REFERENCES journal(id) DEFERRABLE INITIALLY DEFERRED");
             }
     
             // 2. Update Article_Journal first
             String updateArticleJournalSQL = "UPDATE Article_Journal aj " +
                     "SET journal_id = ? " +
                     "WHERE aj.journal_id = ? " +
                     "AND EXISTS (SELECT 1 FROM Article a WHERE a.id = aj.article_id AND EXTRACT(YEAR FROM a.date_created) >= ?)";
             
             try (PreparedStatement updateArticleJournalStmt = conn.prepareStatement(updateArticleJournalSQL)) {
                 updateArticleJournalStmt.setString(1, new_id);
                 updateArticleJournalStmt.setString(2, journal.getId());
                 updateArticleJournalStmt.setInt(3, year);
                 int articlesUpdated = updateArticleJournalStmt.executeUpdate();
                 
                 // 3. Then update Journal
                 String updateJournalSQL = "UPDATE Journal j SET title = ?, id = ? WHERE j.id = ?";
                 try (PreparedStatement updateJournalStmt = conn.prepareStatement(updateJournalSQL)) {
                     updateJournalStmt.setString(1, new_name);
                     updateJournalStmt.setString(2, new_id);
                     updateJournalStmt.setString(3, journal.getId());
                     int journalUpdated = updateJournalStmt.executeUpdate();
                     
                     conn.commit();
                     return articlesUpdated > 0 && journalUpdated > 0;
                 }
             }
         } catch (SQLException e) {
             log.error("Error updating journal name", e);
             return false;
         }
     }
}
