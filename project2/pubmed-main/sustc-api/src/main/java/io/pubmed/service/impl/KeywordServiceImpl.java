package io.pubmed.service.impl;

import io.pubmed.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordServiceImpl implements KeywordService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Find the number of published articles containing a keyword over the past year, in descending order by year.
     *
     * @param keyword the keyword need queried
     * @return article's numbers in past years which contains given keyword.
     */
    @Override
    public int[] getArticleCountByKeywordInPastYears(String keyword) {
        String query = "SELECT COUNT(Article_Keywords.article_id) AS article_count " +
                "FROM Article_Keywords " + 
                "JOIN Keywords ON Article_Keywords.keyword_id = Keywords.id " + 
                "JOIN Article ON Article_Keywords.article_id = Article.id " + 
                "WHERE Keywords.keyword LIKE ? " + 
                "GROUP BY EXTRACT(YEAR FROM Article.date_created) " + 
                "ORDER BY EXTRACT(YEAR FROM Article.date_created) DESC;";

        // List<Integer> counts = jdbcTemplate.query(query, new Object[]{"%" + keyword + "%"},
        //     (rs, rowNum) -> rs.getInt("article_count"));
        List<Integer> counts = jdbcTemplate.queryForList(query, Integer.class, keyword);
        return counts.stream().mapToInt(i -> i).toArray();
    }
}
