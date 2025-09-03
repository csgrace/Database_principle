package io.pubmed.service.impl;
import io.pubmed.dto.Author;
import io.pubmed.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * sort given author's articles by citations, from more to less.
     *
     * @param author the author to be queried
     * @return a sorted list of pmid of author's articles
     */
    @Override
    /**
     * 根据给定作者的文章引用次数，将其文章按引用次数从高到低排序。
     *
     * @param author 要查询的作者
     * @return 按照引用次数排序后的文章 pmid 列表
     */
    public int[] getArticlesByAuthorSortedByCitations(Author author) {
        String query =
                "SELECT aa.article_id AS pmid " +
                        "FROM " +
                        "Article_Authors aa  " +
                        "LEFT JOIN all_Article_Citations ac ON aa.article_id = ac.article_id " + // Changed to LEFT JOIN
                        "JOIN Authors auth ON aa.author_id = auth.author_id " +
                        "WHERE auth.fore_name = ? " +
                        "AND auth.last_name = ? " +
                        "ORDER BY COALESCE(ac.citation_count, 0) DESC"; // Ensure we sort by 0 if null

        return jdbcTemplate.queryForList(query,
                        Integer.class,
                        author.getFore_name(), author.getLast_name())
                .stream().mapToInt(i -> i).toArray();
    }


    /**
     * In which journal has a given author published the most articles ?
     * 查询某个作者在哪本期刊发表的文章最多。
     * @param author the author to be queried
     * @return the title of the required Journal
     */
    /**
     * 查询某个作者在哪本期刊发表的文章最多。
     *
     * @param author 要查询的作者
     * @return 发表最多文章的期刊标题
     */
    @Override
    public String getJournalWithMostArticlesByAuthor(Author author) {
        String query =
                "SELECT j.title " +
                        "FROM Journal j " +
                        "JOIN Article_Journal aj ON j.id = aj.journal_id " +
                        "JOIN Article a ON aj.article_id = a.id " +
                        "JOIN Article_Authors aa ON a.id = aa.article_id " +
                        "JOIN Authors auth ON aa.author_id = auth.author_id " +
                        "WHERE auth.fore_name = ? " +
                        "AND auth.last_name = ? " +
                        "GROUP BY j.title " +        // Keep grouping minimal
                        "ORDER BY COUNT(a.id) DESC " + // Count articles
                        "LIMIT 1";

        try {
            return jdbcTemplate.queryForObject(
                    query,
                    String.class,
                    author.getFore_name(), author.getLast_name()
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return ""; // 如果没有结果，返回null
        }
    }

    /**
     * This is a bonus task, you need find the minimum number of articles
     * that two authors need to be linked by citations, for example author A to E:
     * [article a, authors {A, B, C, D }, references {b, c, d, e}]  ->
     * [article b : authors {B, F, G }, references {q, w, e ,r}]    ->
     * [article q : authors{E, H, ... }]
     * Here, author A need 3 articles to link to author E
     * @return the number of the required articles, if there is no connection, return -1
     * If you don't want impl this task, just return -1
     */
    @Override
    /**
     * 计算连接两位作者所需的最少文章数。
     *
     * @param A 起始作者
     * @param E 目标作者
     * @return 连接所需的最少文章数，如果没有连接，返回 -1
     */
    public int getMinArticlesToLinkAuthors(Author A, Author E) {
        // 假设作者 A 和 E 已经通过某种方法获取到他们的 ID 或其他唯一标识
        String query =
                "SELECT aa.article_id, aa.author_id " +
                        "FROM Article_Authors aa " +
                        "WHERE aa.author_id IN (?, ?)";

        // 获取作者 A 和 E 的文章
        List<Integer> articlesByA = jdbcTemplate.queryForList(query, Integer.class, A.getAuthorId(), E.getAuthorId());

        // 如果作者 A 和 E 没有文章，返回 -1
        if (articlesByA.isEmpty()) {
            return -1;
        }

        // 创建一个队列用于 BFS
        Set<Integer> visitedAuthors = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(A.getAuthorId());
        visitedAuthors.add(A.getAuthorId());

        Map<Integer, Integer> distance = new HashMap<>();
        distance.put(A.getAuthorId(), 0);

        // BFS 遍历
        while (!queue.isEmpty()) {
            int current = queue.poll();

            // 如果找到了目标作者，返回到达目标的步骤数
            if (current == E.getAuthorId()) {
                return distance.get(current);
            }

            // 获取当前作者所参与的所有文章
            List<Integer> connectedArticles = getArticlesByAuthor(current);
            for (int articleId : connectedArticles) {
                // 对每个共同文章，获取文章的所有作者
                List<Integer> authorsInArticle = getAuthorsByArticle(articleId);

                for (int authorId : authorsInArticle) {
                    // 如果这个作者还没有访问过，则加入队列
                    if (!visitedAuthors.contains(authorId)) {
                        visitedAuthors.add(authorId);
                        distance.put(authorId, distance.get(current) + 1);
                        queue.offer(authorId);
                    }
                }
            }
        }

        // 如果无法连接两个作者，返回 -1
        return -1;
    }

    public List<Integer> getAuthorsByArticle(int articleId) {
       String query = "SELECT aa.author_id " +
                    "FROM Article_Authors aa " +
                    "WHERE aa.article_id = ?";

            // 执行查询并返回所有作者的 ID
       return jdbcTemplate.queryForList(query, Integer.class, articleId);
    }
    public List<Integer> getArticlesByAuthor(int authorId) {
        String query =
                "SELECT aa.article_id " +
                        "FROM Article_Authors aa " +
                        "WHERE aa.author_id = ?";

        // 执行查询并返回所有文章的 ID
        return jdbcTemplate.queryForList(query, Integer.class, authorId);
    }
}