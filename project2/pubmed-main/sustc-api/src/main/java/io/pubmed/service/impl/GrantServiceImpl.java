package io.pubmed.service.impl;
import io.pubmed.service.GrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class GrantServiceImpl implements GrantService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Find all the funded articles by the given country
     *
     * @param country you need query
     * @return the pmid list of funded articles
     */
    @Override
    public int[] getCountryFundPapers(String country) {
        // SQL query to find all funded articles by the given country
        String query = "SELECT article_grants.article_id " +
                "FROM grant_info " +
                "JOIN article_grants ON grant_info.id = article_grants.grant_id " +
                "WHERE LOWER(grant_info.country) = LOWER(?);"; // match case consistently
        // Execute the query and convert the result to a list of integers, then map it to an array of int
        return jdbcTemplate.queryForList(query, Integer.class, country).stream().mapToInt(i -> i).toArray();
    }
}


