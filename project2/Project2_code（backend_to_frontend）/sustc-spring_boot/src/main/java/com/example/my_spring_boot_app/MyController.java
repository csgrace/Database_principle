package com.example.my_spring_boot_app;
import io.pubmed.dto.Article;
import io.pubmed.dto.Author;
import io.pubmed.dto.Journal;
import io.pubmed.service.DatabaseService;
import io.pubmed.service.KeywordService;
import io.pubmed.service.ArticleService;
import io.pubmed.service.AuthorService;
import io.pubmed.service.JournalService;
import io.pubmed.service.GrantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private KeywordService keywordService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private GrantService grantService;
    @GetMapping("/getGroupMembers")
    public String getGroupMembers() {
        logger.info("Received request for getGroupMembers");
        List<Integer> groupMembers = databaseService.getGroupMembers();
        System.out.println("groupMembers "+ groupMembers);
        return groupMembers.toString();
        //return "Request for getGroupMembers received";
    }

    @GetMapping("/importData")
    public String importData(@RequestParam String dataPath) {
        logger.info("Received request for importData");
        databaseService.importData(dataPath);
        System.out.println("dataPath "+dataPath);
        //return dataPath;
        return "Request for importData received";
    }

    @GetMapping("/truncate")
    public String truncate() {
        logger.info("Received request for truncate");
        databaseService.truncate();
        return "Request for truncate received";
    }

    @GetMapping("/sum")
    public String sum(@RequestParam int a, @RequestParam int b) {
        logger.info("Received request for sum");
        System.out.println("a "+a+" b "+b);
        int result = databaseService.sum(a, b);
        System.out.println("result " +result);
        return "The sum is: " + result;
        //return "Request for sum received";
    }

    @GetMapping("/getImpactFactor")
    public String getImpactFactor(@RequestParam String journal_title, @RequestParam int year) {
        logger.info("Received request for getImpactFactor");
        double result = journalService.getImpactFactor(journal_title, year);
        System.out.println("journal_title "+journal_title+" year "+year);
        System.out.println("result "+result);
        return "The Impact Factor is: " + result;
        //return "Request for getImpactFactor received";
    }


    @GetMapping("/getArticleCountByKeywordInPastYears")
    public String getArticleCountByKeywordInPastYears(@RequestParam String keyword) {
        logger.info("Received request for getArticleCountByKeywordInPastYears");
        int[] result = keywordService.getArticleCountByKeywordInPastYears(keyword);
        System.out.println("keyword "+keyword);
        System.out.println("result " + Arrays.toString(result));
        return Arrays.toString(result);
        //return "Request for getArticleCountByKeywordInPastYears received";
    }

    @GetMapping("/getArticlesByAuthorSortedByCitations")
    public String getArticlesByAuthorSortedByCitations(
            @RequestParam String last_name,
            @RequestParam String fore_name,
            @RequestParam String initials,
            @RequestParam String collective_name) {
        logger.info("Received request for getArticlesByAuthorSortedByCitations");

        Author author = new Author();
        author.setLast_name(last_name);
        author.setFore_name(fore_name);
        author.setInitials(initials);
        author.setCollective_name(collective_name);

        int[] result = authorService.getArticlesByAuthorSortedByCitations(author);
        System.out.println("authorName " + author);
        System.out.println("result " + Arrays.toString(result));
        return Arrays.toString(result);
    }

    @GetMapping("/getJournalWithMostArticlesByAuthor")
    public String getJournalWithMostArticlesByAuthor(
            @RequestParam String last_name,
            @RequestParam String fore_name,
            @RequestParam String initials,
            @RequestParam String collective_name) {
        logger.info("Received request for getJournalWithMostArticlesByAuthor");

        Author author = new Author();
        author.setLast_name(last_name);
        author.setFore_name(fore_name);
        author.setInitials(initials);
        author.setCollective_name(collective_name);
        String result = authorService.getJournalWithMostArticlesByAuthor(author);
        System.out.println("authorName "+ author);
        System.out.println("result "+result);
        return result;
        //return "Request for getJournalWithMostArticlesByAuthor received";
    }

    @GetMapping("/getMinArticlesToLinkAuthors")
    public String getMinArticlesToLinkAuthors(
            @RequestParam String last_name_A,
            @RequestParam String fore_name_A,
            @RequestParam String initials_A,
            @RequestParam String collective_name_A,
            @RequestParam String last_name_E,
            @RequestParam String fore_name_E,
            @RequestParam String initials_E,
            @RequestParam String collective_name_E) {
        logger.info("Received request for getMinArticlesToLinkAuthors");

        Author authorA = new Author();
        authorA.setLast_name(last_name_A);
        authorA.setFore_name(fore_name_A);
        authorA.setInitials(initials_A);
        authorA.setCollective_name(collective_name_A);

        Author authorE = new Author();
        authorE.setLast_name(last_name_E);
        authorE.setFore_name(fore_name_E);
        authorE.setInitials(initials_E);
        authorE.setCollective_name(collective_name_E);

        int result = authorService.getMinArticlesToLinkAuthors(authorA, authorE);
        System.out.println("authorA " + authorA + " authorE " + authorE);
        System.out.println("result " + result);
        return "The number of the required articles is: " + result;
    }

    @GetMapping("/getCountryFundPapers")
    public String getCountryFundPapers(@RequestParam String country) {
        logger.info("Received request for getCountryFundPapers");
        int[] result = grantService.getCountryFundPapers(country);
        System.out.println("country "+country);
        System.out.println("result "+ Arrays.toString(result));
        if (result.length == 0) {
            return "No fund papers found for the specified country.";
        }
        return Arrays.toString(result);
        //return "Request for getCountryFundPapers received";
    }

    @GetMapping("/getArticleCitationsByYear")
    public String getArticleCitationsByYear(@RequestParam int articleId, @RequestParam int year) {
        logger.info("Received request for getArticleCitationsByYear with articleId: {} and year: {}", articleId, year);
        System.out.println("Received request for getArticleCitationsByYear with articleId:"+articleId+ "year: "+year);
        int result = articleService.getArticleCitationsByYear(articleId, year);
        System.out.println("result "+result);
        return "The number of article's citations in given year is: " + result;
        //return "Request for getArticleCitationsByYear received";
    }


    @GetMapping("/updateJournalName")
    public String updateJournalName(@RequestParam String journalId, @RequestParam String journalTitle, @RequestParam int year, @RequestParam String newName, @RequestParam String newId) {
        logger.info("Received request for updateJournalName");

        Journal journal = new Journal();
        journal.setId(journalId);
        journal.setTitle(journalTitle);

        boolean result = journalService.updateJournalName(journal, year, newName, newId);

        System.out.println("journal " + journal + " year " + year + " newName " + newName + " newId " + newId);
        System.out.println("result " + result);
        return "Request for updateJournalName received";
    }

    @GetMapping("/addArticleAndUpdateIF")
    public String addArticleAndUpdateIF(
            @RequestParam int id,
            @RequestParam String title,
            @RequestParam String pubModel,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date created,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date completed) {
        logger.info("Received request for addArticleAndUpdateIF");

        Article article = new Article();
        article.setId(id);
        article.setTitle(title);
        article.setPub_model(pubModel);
        article.setCreated(created);
        article.setCompleted(completed);

        // Retrieve the Journal details based on the Article ID

        Journal journal = articleService.getJournalByArticleId(id);
        article.setJournal(journal);

        double result = articleService.addArticleAndUpdateIF(article);
        System.out.println("id " + id + " title " + title + " pubModel " + pubModel + " created " + created + " completed " + completed + " journalTitle " + journal.getTitle());
        System.out.println("result " + result);
        return "result" + result;
        //return "Request for addArticleAndUpdateIF received";
    }
    //@RequestParam 是 Spring MVC 中用于处理 HTTP 请求参数的注解。
    //它可以将请求中的参数绑定到控制器方法的参数上。
    //具体来说，当客户端发送一个 HTTP 请求时，URL 中的查询参数会被提取并传递给控制器方法中带有 @RequestParam 注解的参数。
}