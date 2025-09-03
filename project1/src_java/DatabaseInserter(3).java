package org.example;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseInserter {
//    private static final String URL = "jdbc:postgresql://localhost:5432/project1";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "/PostgreS@1478950+/";

    private final Connection conn;

    public DatabaseInserter(Connection connection) throws SQLException {
        this.conn = connection;
        this.articleStmt = conn.prepareStatement(ARTICLE_SQL);
        this.journalStmt = conn.prepareStatement(JOURNAL_SQL);
        this.journalIssueStmt = conn.prepareStatement(JOURNAL_ISSUE_SQL);
        this.authorStmt = conn.prepareStatement(AUTHOR_SQL);
        this.affiliationStmt = conn.prepareStatement(AFFILIATION_SQL);
        this.authorAffiliationStmt = conn.prepareStatement(AUTHOR_AFFILIATION_SQL);
        this.pubtypeStmt = conn.prepareStatement(PUBTYPE_SQL);
        this.pubtypeArticleStmt = conn.prepareStatement(PUBTYPE_ARTICLE_SQL);
        this.grantStmt = conn.prepareStatement(GRANT_SQL);
        this.grantArticleStmt = conn.prepareStatement(GRANT_ARTICLE_SQL);
// IDS 表和关联表的 PreparedStatement
        this.idsStmt = conn.prepareStatement(IDS_SQL);
        this.articleIdsStmt = conn.prepareStatement(ARTICLE_IDS_SQL);

// REFERENCE 表和关联表的 PreparedStatement
        this.referenceStmt = conn.prepareStatement(REFERENCE_SQL);
        this.referenceArticleStmt = conn.prepareStatement(REFERENCE_ARTICLE_SQL);

// KEYWORD 表和关联表的 PreparedStatement
        this.keywordsStmt = conn.prepareStatement(KEYWORD_SQL);
        this.articleKeywordsStmt = conn.prepareStatement(KEYWORD_ARTICLE_SQL);

//        DatabaseInserter d = new DatabaseInserter(connection);
    }
    // Article相关SQL
    private final String articleSQL = "INSERT INTO article (id, title, pub_model, date_created, date_completed) " +
            "VALUES (?, ?, ?, ?, ?) RETURNING id";

        // SQL语句常量
        private static final String ARTICLE_SQL =
                "INSERT INTO article (id, title, pub_model, date_created, date_completed) VALUES (?, ?, ?, ?, ?) RETURNING id";

        private static final String JOURNAL_SQL =
                "INSERT INTO journal (id, country, title, issn) VALUES (?, ?, ?, ?) RETURNING identity";

        private static final String JOURNAL_ISSUE_SQL =
                "INSERT INTO journal_issue (journal_id, article_id, issue, volume) VALUES (?, ?, ?, ?)";

        private static final String AUTHOR_SQL =
                "INSERT INTO author (fore_name, last_name, initials, is_collective) VALUES (?, ?, ?, ?)";

        private static final String AFFILIATION_SQL =
                "INSERT INTO affiliation (name) VALUES (?) RETURNING identity";

        private static final String AUTHOR_AFFILIATION_SQL =
                "INSERT INTO author_affiliation (author_id, affiliation_id) VALUES (?, ?)";

        private static final String PUBTYPE_SQL =
                "INSERT INTO pubtypes (id, name) VALUES (?, ?) RETURNING identity";

        private static final String PUBTYPE_ARTICLE_SQL =
                "INSERT INTO pubtypes_article (pubtypes_identity, article_id) VALUES (?, ?)";

        private static final String GRANT_SQL =
                "INSERT INTO grants (id, acronym, country, agency) VALUES (?, ?, ?, ?) RETURNING identity";

        private static final String GRANT_ARTICLE_SQL =
                "INSERT INTO article_grants (grant_identity, article_id) VALUES (?, ?)";

    private static final String IDS_SQL =
            "INSERT INTO ids (type, id) VALUES (?, ?) RETURNING identity";

    private static final String ARTICLE_IDS_SQL =
            "INSERT INTO article_ids (ids, article_id) VALUES (?, ?)";

    private static final String REFERENCE_SQL =
            "INSERT INTO reference (references_id) VALUES (?) RETURNING identity";

    private static final String REFERENCE_ARTICLE_SQL =
            "INSERT INTO reference_article (references_identity, article_id) VALUES (?, ?)";

    private static final String KEYWORD_SQL =
            "INSERT INTO keywords (keyword) VALUES (?) RETURNING identity";

    private static final String KEYWORD_ARTICLE_SQL =
            "INSERT INTO article_keywords (keywords_identity, article_id) VALUES (?, ?)";



    private final Map<String, Integer> journalCache = new ConcurrentHashMap<>();
    private final Map<String, Boolean> collectiveAuthors = new ConcurrentHashMap<>();  // 集体作者去重
    private final Map<String, Boolean> lastNameOnlyAuthors = new ConcurrentHashMap<>();  // 仅有last_name的作者去重
    private final Map<String, Boolean> fullAuthors = new ConcurrentHashMap<>();  // 完整信息作者去重
    private final Map<String, Integer> pubtypesCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> grantsCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> idsCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> referencesCache = new ConcurrentHashMap<>();
    private final Map<String, Integer> keywordsCache = new ConcurrentHashMap<>();

    // PreparedStatement声明
    private final PreparedStatement articleStmt;
    private final PreparedStatement journalStmt;
    private final PreparedStatement journalIssueStmt;
    private final PreparedStatement authorStmt;
    private final PreparedStatement affiliationStmt;
    private final PreparedStatement authorAffiliationStmt;
    private final PreparedStatement pubtypeStmt;
    private final PreparedStatement pubtypeArticleStmt;
    private final PreparedStatement grantStmt;
    private final PreparedStatement grantArticleStmt;

    private final PreparedStatement idsStmt;
    private final PreparedStatement articleIdsStmt;
    private final PreparedStatement referenceStmt;
    private final PreparedStatement referenceArticleStmt;
    private final PreparedStatement keywordsStmt;
    private final PreparedStatement articleKeywordsStmt;



    private int affiliationCount = 0;
    private int authorCount = 0;
    private int pubtypesCount = 0;
    private int idsCount = 0;
    private int referenceCount = 0;
    private int keywordsCount = 0;
    private int grantsCount = 0;
    private int journalCount = 0;



    int batchSize = 1000;








    public void insertNestedData (Map<String, Object> data, int count) throws SQLException {
        if (count % batchSize == 0) {
            try {
                // 1. 首先插入主表article
                articleStmt.executeBatch();
                articleStmt.clearBatch();

                // 2. 插入独立的实体表
                journalStmt.executeBatch();
                journalStmt.clearBatch();

                affiliationStmt.executeBatch();
                affiliationStmt.clearBatch();

                pubtypeStmt.executeBatch();
                pubtypeStmt.clearBatch();

                grantStmt.executeBatch();
                grantStmt.clearBatch();

                idsStmt.executeBatch();
                idsStmt.clearBatch();

                referenceStmt.executeBatch();
                referenceStmt.clearBatch();

                keywordsStmt.executeBatch();
                keywordsStmt.clearBatch();

                // 3. 插入依赖于journal的表
                journalIssueStmt.executeBatch();
                journalIssueStmt.clearBatch();

                // 4. 插入作者表
                authorStmt.executeBatch();
                authorStmt.clearBatch();

                // 5. 插入作者关联表
                authorAffiliationStmt.executeBatch();
                authorAffiliationStmt.clearBatch();

                // 6. 插入其他所有关联表
                pubtypeArticleStmt.executeBatch();
                pubtypeArticleStmt.clearBatch();

                grantArticleStmt.executeBatch();
                grantArticleStmt.clearBatch();

                articleIdsStmt.executeBatch();
                articleIdsStmt.clearBatch();

                referenceArticleStmt.executeBatch();
                referenceArticleStmt.clearBatch();

                articleKeywordsStmt.executeBatch();
                articleKeywordsStmt.clearBatch();

                System.out.println(count + " records inserted.");

            } catch (BatchUpdateException e) {
                // 处理批处理特定的异常
                conn.rollback();
                System.err.println("批处理执行失败: " + e.getMessage());
                System.err.println("失败的更新计数: " + Arrays.toString(e.getUpdateCounts()));
                throw e;
            } catch (SQLException e) {
                // 处理一般SQL异常
                conn.rollback();
                System.err.println("数据库操作失败: " + e.getMessage());
                throw e;
            } catch (Exception e) {
                // 处理其他未预期的异常
                conn.rollback();
                System.err.println("未预期的错误: " + e.getMessage());
                throw e;
            }
        }


//        if(count % batchSize == 0){
//// 1. 首先插入主表article
//            articleStmt.executeBatch();
////            conn.commit();
//            articleStmt.clearBatch();
//
//// 2. 插入独立的实体表（它们只依赖article）
//            journalStmt.executeBatch();
////            conn.commit();
//            journalStmt.clearBatch();
//
//            affiliationStmt.executeBatch();
//            affiliationStmt.clearBatch();
//
//            pubtypeStmt.executeBatch();
//            pubtypeStmt.clearBatch();
//
//            grantStmt.executeBatch();
//            grantStmt.clearBatch();
//
//            idsStmt.executeBatch();
//            idsStmt.clearBatch();
//
//            referenceStmt.executeBatch();
//            referenceStmt.clearBatch();
//
//            keywordsStmt.executeBatch();
//            keywordsStmt.clearBatch();
//
//// 3. 插入依赖于journal的表
//            journalIssueStmt.executeBatch();
//            journalIssueStmt.clearBatch();
//
//// 4. 插入作者表
//            authorStmt.executeBatch();
//            authorStmt.clearBatch();
//
//// 5. 插入作者关联表（依赖author和affiliation）
//            authorAffiliationStmt.executeBatch();
//            authorAffiliationStmt.clearBatch();
//
//// 6. 插入其他所有关联表（它们都依赖article和各自的主表）
//            pubtypeArticleStmt.executeBatch();
//            pubtypeArticleStmt.clearBatch();
//
//            grantArticleStmt.executeBatch();
//            grantArticleStmt.clearBatch();
//
//            articleIdsStmt.executeBatch();
//            articleIdsStmt.clearBatch();
//
//            referenceArticleStmt.executeBatch();
//            referenceArticleStmt.clearBatch();
//
//            articleKeywordsStmt.executeBatch();
//            articleKeywordsStmt.clearBatch();
//
//            System.out.println(count + " records inserted.");
//        }
        try {
            // 开启事务
            conn.setAutoCommit(false);

            // 1. 插入 article 主表数据
            int articleId = insertArticle(conn, data, articleStmt);

            // 2. 插入 journal 和 journal_issue
            Map<String, Object> journalData = (Map<String, Object>) data.get("journal");
            if (journalData != null) {
                Integer journalIdentity = insertJournal(conn, journalData, journalStmt);
                insertJournalIssue(conn, journalIdentity, articleId,
                        (Map<String, Object>) journalData.get("journal_issue"), journalIssueStmt);
            }

            // 3. 插入 author 及其 affiliation
            List<Map<String, Object>> authors = (List<Map<String, Object>>) data.get("author");
            if (authors != null) {
                insertAuthorAndAffiliation(conn, authors, articleId, authorStmt);
            }

            // 4. 插入 publication types
            List<Map<String, Object>> pubTypes = (List<Map<String, Object>>) data.get("publication_types");
            if (pubTypes != null) {
                insertPublicationTypes(conn, pubTypes, articleId, pubtypeStmt, pubtypeArticleStmt);
            }

            // 5. 插入 grants
            List<Map<String, Object>> grants = (List<Map<String, Object>>) data.get("grant");
            if (grants != null) {
                insertGrants(conn, grants, articleId, grantStmt, grantArticleStmt);
            }

            // 6. 插入 article IDs
            List<Map<String, Object>> articleIds = (List<Map<String, Object>>) data.get("article_ids");
            if (articleIds != null) {
                insertArticleIds(conn, articleId, articleIds, idsStmt, articleIdsStmt);
            }

            // 7. 插入 references
            List<String> references = (List<String>) data.get("references");
            if (references != null) {
                insertReferences(conn, articleId, references, referenceStmt, referenceArticleStmt);
            }

            // 8. 插入 keywords
            List<String> keywords = (List<String>) data.get("keywords");
            if (keywords != null) {
                insertKeywords(conn, articleId, keywords, keywordsStmt, articleKeywordsStmt);
            }

//            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex.getMessage());
            }
            System.err.println("Error inserting data: " + e.getMessage());
        }


    }
    private Integer insertArticle(Connection conn, Map<String, Object> data, PreparedStatement pstmt) throws SQLException {

            Map<String, Object> dateCreated = (Map<String, Object>) data.get("date_created");
            Map<String, Object> dateCompleted = (Map<String, Object>) data.get("date_completed");

            pstmt.setInt(1, ((Number) data.get("id")).intValue());
            pstmt.setString(2, (String) data.get("title"));
            pstmt.setString(3, (String) data.get("pub_model"));
            pstmt.setDate(4, createDate(dateCreated));
            pstmt.setDate(5, createDate(dateCompleted));
//            pstmt.setObject(6, journalId);
//            pstmt.setString(7, authorLastName);

            pstmt.addBatch();
//            ResultSet rs = pstmt.getGeneratedKeys();
//            conn.commit();
            return ((Number) data.get("id")).intValue();

    }
    private java.sql.Date createDate(Map<String, Object> dateMap) {
        if (dateMap == null) return null;
        return java.sql.Date.valueOf(String.format("%d-%02d-%02d",
                ((Number) dateMap.get("year")).intValue(),
                ((Number) dateMap.get("month")).intValue(),
                ((Number) dateMap.get("day")).intValue()));
    }

    private Integer insertJournal(Connection conn, Map<String, Object> journalData, PreparedStatement pstmt) throws SQLException {
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
            pstmt.setString(1, (String) journalData.get("id"));
            pstmt.setString(2, (String) journalData.get("country"));
            pstmt.setString(3, (String) journalData.get("title"));
            pstmt.setString(4, (String) journalData.get("issn"));
            pstmt.addBatch();

            // 可以优化

            int journalIdentity = ++journalCount;
//            conn.commit();

            journalCache.put(cacheKey, journalIdentity);
            return journalIdentity;
    }
    private void insertJournalIssue(Connection conn, Integer journalIdentity, int articleId, Map<String, Object> journalIssue, PreparedStatement pstmt) throws SQLException {
        String sql = "INSERT INTO journal_issue (journal_id, article_id, issue, volume) VALUES (?, ?, ?, ?)";
        if (journalIdentity == null) {
            throw new SQLException("Invalid journal identity: null");
        }
            pstmt.setInt(1, journalIdentity);
            pstmt.setInt(2, articleId);
            pstmt.setString(3, (String) journalIssue.get("issue"));
            pstmt.setString(4, (String) journalIssue.get("volume"));
            pstmt.addBatch();
    }



    public void insertAuthorAndAffiliation(Connection conn, List<Map<String, Object>> authors, int articleId, PreparedStatement authorStmt) throws SQLException {
        String authorSql = "INSERT INTO author (fore_name, last_name, initials, is_collective) " +
                "VALUES (?, ?, ?, ?) "; // 数据库级别的去重保护

        String affiliationSql = "INSERT INTO affiliation (name) " +
                "VALUES (?)  " +
                "RETURNING identity";

        String authorAffiliationSql = "INSERT INTO author_affiliation (author_id, affiliation_id) " +
                "VALUES (?, ?) ";

        // Step 1: 插入作者信息
            for (Map<String, Object> author : authors) {
                String foreName = (String) author.get("fore_name");
                String lastName = (String) author.get("last_name");
                String initials = (String) author.get("initials");
                String collectiveName = (String) author.get("collective_name");
                boolean isCollective = collectiveName != null;

                // 构建用于去重的键
                String deduplicationKey;
                boolean shouldInsert = false;

                if (isCollective) {
                    deduplicationKey = collectiveName;
                    if (!collectiveAuthors.containsKey(deduplicationKey)) {
                        collectiveAuthors.put(deduplicationKey, true);
                        shouldInsert = true;
                    }
                } else if (foreName == null && initials == null) {
                    deduplicationKey = lastName;
                    if (!lastNameOnlyAuthors.containsKey(deduplicationKey)) {
                        lastNameOnlyAuthors.put(deduplicationKey, true);
                        shouldInsert = true;
                    }
                } else {
                    deduplicationKey = String.format("%s_%s_%s",
                            foreName == null ? "" : foreName,
                            lastName,
                            initials == null ? "" : initials);
                    if (!fullAuthors.containsKey(deduplicationKey)) {
                        fullAuthors.put(deduplicationKey, true);
                        shouldInsert = true;
                    }
                }

                if (shouldInsert) {
                    // 处理NULL值
                    foreName = foreName == null ? "" : foreName;
                    lastName = lastName == null ? "" : lastName;
                    initials = initials == null ? "" : initials;

                    // 设置参数并插入作者
                    authorStmt.setString(1, foreName);
                    authorStmt.setString(2, isCollective ? collectiveName : lastName);
                    authorStmt.setString(3, initials);
                    authorStmt.setBoolean(4, isCollective);
//                    authorStmt.setInt(5, articleId);
//                    authorStmt.executeUpdate();

                    authorStmt.addBatch();
                    // 获取生成的作者 ID
                    int authorId;
                    // 存疑，executeQuery?
//                    try (ResultSet rs = authorStmt.getGeneratedKeys()) {
//                        if (rs.next()) {
                            authorId = ++authorCount;

                            // Step 2: 插入作者的机构信息并建立关联
//                            String[] affiliations = (String[]) author.get("affiliation");

                            Object affiliationObj = author.get("affiliation");
                            String[] affiliations;
                            if (affiliationObj instanceof String[]) {
                                affiliations = (String[]) affiliationObj;
                            } else if (affiliationObj instanceof List) {
                                List<?> affiliationList = (List<?>) affiliationObj;
                                affiliations = affiliationList.toArray(new String[0]);
                            } else if (affiliationObj == null) {
                                affiliations = null;
                            } else {
                                // 处理其他情况或记录错误
                                throw new IllegalArgumentException("Unexpected affiliation type: " + affiliationObj.getClass());
                            }

                            if (affiliations != null && affiliations.length > 0) {
                                for (String affiliation : affiliations) {
                                    int affiliationIdentity;

                                    // 2.1 插入机构并获取ID
                                    try (PreparedStatement affiliationStmt = conn.prepareStatement(affiliationSql)) {
                                        affiliationStmt.setString(1, affiliation);
//                                        try (ResultSet affRs = affiliationStmt.executeQuery()) {
//                                            if (affRs.next()) {
                                                affiliationIdentity = ++affiliationCount;

                                                // 2.2 创建作者-机构关联
                                                try (PreparedStatement authorAffStmt = conn.prepareStatement(authorAffiliationSql)) {
                                                    authorAffStmt.setInt(1, authorId);
                                                    authorAffStmt.setInt(2, affiliationIdentity);
                                                    authorAffStmt.executeUpdate();
                                                }


                                    }
                                }
                            }


                }
            }

        return;
    }



    private void insertPublicationTypes(Connection conn, List<Map<String, Object>> publicationTypes, int articleId, PreparedStatement insertStmt, PreparedStatement relationStmt) throws SQLException {
        if (publicationTypes == null || publicationTypes.isEmpty()) return;

        String insertSql = "INSERT INTO pubtypes (id, name) VALUES (?, ?) RETURNING identity";
        String relationSql = "INSERT INTO pubtypes_article (pubtypes_identity, article_id) VALUES (?, ?)";

//        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//             PreparedStatement relationStmt = conn.prepareStatement(relationSql)) {

            for (Map<String, Object> pubType : publicationTypes) {
                String id = (String) pubType.get("id");
                String name = (String) pubType.get("name");

                // 构建缓存键
                String cacheKey = String.format("%s:%s", id, name);

                // 获取或插入pubtypes记录
                Integer pubtypesIdentity = pubtypesCache.get(cacheKey);

                if (pubtypesIdentity == null) {
                    // 如果缓存中不存在，执行插入操作
                    insertStmt.setString(1, id);
                    insertStmt.setString(2, name);

                    insertStmt.addBatch();
//                    ResultSet rs = insertStmt.getGeneratedKeys();
//                    if (rs.next()) {
                        pubtypesIdentity = ++pubtypesCount;
                        pubtypesCache.put(cacheKey, pubtypesIdentity);

                }

//                conn.commit();

                // 插入关联关系
                if (pubtypesIdentity != null) {
                    relationStmt.setInt(1, pubtypesIdentity);
                    relationStmt.setInt(2, articleId);
                    relationStmt.addBatch();
                }
            }

    }



    private void insertGrants(Connection conn, List<Map<String, Object>> grants, int articleId, PreparedStatement insertStmt, PreparedStatement relationStmt) throws SQLException {
        if (grants == null || grants.isEmpty()) return;

        String insertSql = "INSERT INTO grants (id, acronym, country, agency) VALUES (?, ?, ?, ?) RETURNING identity";
        String relationSql = "INSERT INTO article_grants (grant_identity, article_id) VALUES (?, ?)";

//        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//             PreparedStatement relationStmt = conn.prepareStatement(relationSql)) {

            for (Map<String, Object> grant : grants) {
                String id = (String) grant.get("id");
                String acronym = (String) grant.get("acronym");
                String country = (String) grant.get("country");
                String agency = (String) grant.get("agency");

                // 构建缓存键
                String cacheKey = String.format("%s:%s:%s:%s", id, acronym, country, agency);

                // 获取或插入grant记录
                Integer grantIdentity = grantsCache.get(cacheKey);

                if (grantIdentity == null) {
                    // 如果缓存中不存在，执行插入操作
                    insertStmt.setString(1, id);
                    insertStmt.setString(2, acronym);
                    insertStmt.setString(3, country);
                    insertStmt.setString(4, agency);

                    insertStmt.addBatch();
//                    ResultSet rs = insertStmt.getGeneratedKeys();
//                    if (rs.next()) {
                        grantIdentity = ++grantsCount;
                        grantsCache.put(cacheKey, grantIdentity);

                }

//                conn.commit();

                // 插入关联关系
                if (grantIdentity != null) {
                    relationStmt.setInt(1, grantIdentity);
                    relationStmt.setInt(2, articleId);
                    relationStmt.addBatch();
                }
            }

    }

    private void insertArticleIds(Connection conn, int articleId, List<Map<String, Object>> articleIds, PreparedStatement insertStmt, PreparedStatement relationStmt) throws SQLException {
        if (articleIds == null || articleIds.isEmpty()) return;

        String insertSql = "INSERT INTO ids (type, id) VALUES (?, ?) RETURNING identity";
        String relationSql = "INSERT INTO article_ids (ids, article_id) VALUES (?, ?)";

//        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//             PreparedStatement relationStmt = conn.prepareStatement(relationSql)) {

            for (Map<String, Object> idData : articleIds) {
                String type = (String) idData.get("type");
                String id = (String) idData.get("id");

                // 构建缓存键
                String cacheKey = String.format("%s:%s", type, id);

                // 获取或插入记录
                Integer idsIdentity = idsCache.get(cacheKey);
                if (idsIdentity == null) {
                    // 如果缓存中不存在，执行插入操作
                    insertStmt.setString(1, type);
                    insertStmt.setString(2, id);
                    insertStmt.addBatch();

//                    ResultSet rs = insertStmt.getGeneratedKeys();
//                    if (rs.next()) {
                        idsIdentity = ++idsCount;
                        idsCache.put(cacheKey, idsIdentity);

                }

//                conn.commit();

                // 插入关联关系
                if (idsIdentity != null) {
                    relationStmt.setInt(1, idsIdentity);
                    relationStmt.setInt(2, articleId);
                    relationStmt.addBatch();
                }
            }

    }

    private void insertReferences(Connection conn, int articleId, List<String> references, PreparedStatement insertStmt, PreparedStatement relationStmt) throws SQLException {
        if (references == null || references.isEmpty()) return;

        String insertSql = "INSERT INTO reference (references_id) VALUES (?) RETURNING identity";
        String relationSql = "INSERT INTO reference_article (references_identity, article_id) VALUES (?, ?)";

//        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//             PreparedStatement relationStmt = conn.prepareStatement(relationSql)) {

            for (String referenceId : references) {
                // 使用referenceId作为缓存键
                Integer referenceIdentity = referencesCache.get(referenceId);

                if (referenceIdentity == null) {
                    // 如果缓存中不存在，执行插入操作
                    insertStmt.setString(1, referenceId);
                    insertStmt.addBatch();

//                    ResultSet rs = insertStmt.getGeneratedKeys();
//                    if (rs.next()) {
                        referenceIdentity = ++referenceCount;
                        referencesCache.put(referenceId, referenceIdentity);

                }

//                conn.commit();

                // 插入关联关系
                if (referenceIdentity != null) {
                    relationStmt.setInt(1, referenceIdentity);
                    relationStmt.setInt(2, articleId);
                    relationStmt.addBatch();
                }
            }

    }

    private void insertKeywords(Connection conn, int articleId, List<String> keywords, PreparedStatement insertStmt, PreparedStatement relationStmt) throws SQLException {
        if (keywords == null || keywords.isEmpty()) return;

        String insertSql = "INSERT INTO keywords (keyword) VALUES (?) RETURNING identity";
        String relationSql = "INSERT INTO article_keywords (keywords_identity, article_id) VALUES (?, ?)";

//        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql);
//             PreparedStatement relationStmt = conn.prepareStatement(relationSql)) {

            for (String keyword : keywords) {
                if (keyword == null || keyword.trim().isEmpty()) continue;

                // 规范化关键词
                keyword = keyword.trim();

                // 先检查缓存
                Integer keywordIdentity = keywordsCache.get(keyword);
                if (keywordIdentity == null) {
                    // 不存在，插入新记录
                    insertStmt.setString(1, keyword);
                    insertStmt.addBatch();

//                    ResultSet insertRs = insertStmt.getGeneratedKeys();
//                    if (insertRs.next()) {
                        keywordIdentity = ++keywordsCount;

                        keywordsCache.put(keyword, keywordIdentity);

                }

//                conn.commit();

                // 插入关联关系
                if (keywordIdentity != null) {
                    relationStmt.setInt(1, keywordIdentity);
                    relationStmt.setInt(2, articleId);
                    relationStmt.addBatch();
                }
            }

    }









}




