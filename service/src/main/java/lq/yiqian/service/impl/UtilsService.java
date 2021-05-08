package lq.yiqian.service.impl;

import lq.yiqian.service.IUtilsService;
import lq.yiqian.utils.es.pojo.Book;
import lq.yiqian.utils.es.repository.BookRepository;
import lq.yiqian.utils.es.resultMapper.HighlightResultMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author LQ
 * @create 2021-05-06 21:05
 */
@Service
public class UtilsService implements IUtilsService {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Resource
    private BookRepository bookRepository;

    @Override
    public void addAll() {

    }

    @Override
    public void createIndex() {
        try {
            elasticsearchTemplate.createIndex(Book.class);
            elasticsearchTemplate.putMapping(Book.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testES() {
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book(1L, "小米手机7", "234"));
        books.add(new Book(2L, "坚果手机R1", "wer"));
        books.add(new Book(3L, "华为META10", "sdf"));
        books.add(new Book(4L, "小米Mix2S", "xcv"));
        bookRepository.saveAll(books);
    }

    @Override
    public void findAll(String bookName) {
        SearchQuery queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("bookName", bookName))
                .withHighlightFields(new HighlightBuilder
                        .Field("bookName")
                        .preTags("<font>")
                        .postTags("</font>"))
                .build();
        Page<Book> books = this.elasticsearchTemplate.queryForPage(queryBuilder, Book.class, new HighlightResultMapper());
        System.out.println("books = " + books);
        books.forEach(System.out::println);
    }
}
