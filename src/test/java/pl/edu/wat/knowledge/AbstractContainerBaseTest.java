package pl.edu.wat.knowledge;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import pl.edu.wat.knowledge.entity.Affiliation;
import pl.edu.wat.knowledge.entity.Article;
import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Book;
import pl.edu.wat.knowledge.entity.Chapter;
import pl.edu.wat.knowledge.entity.Journal;
import pl.edu.wat.knowledge.entity.Publisher;
import pl.edu.wat.knowledge.repository.AffiliationRepository;
import pl.edu.wat.knowledge.repository.ArticleRepository;
import pl.edu.wat.knowledge.repository.AuthorRepository;
import pl.edu.wat.knowledge.repository.BookRepository;
import pl.edu.wat.knowledge.repository.ChapterRepository;
import pl.edu.wat.knowledge.repository.JournalRepository;
import pl.edu.wat.knowledge.repository.PublisherRepository;
import pl.edu.wat.knowledge.service.ScoreService;

import java.util.Random;

@SpringBootTest
@Testcontainers
public abstract class AbstractContainerBaseTest {
    private final Random random = new Random();

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.6");

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected ArticleRepository articleRepository;

    @Autowired
    protected BookRepository bookRepository;

    @Autowired
    protected JournalRepository journalRepository;

    @Autowired
    protected PublisherRepository publisherRepository;

    @Autowired
    protected AffiliationRepository affiliationRepository;

    @Autowired
    protected ChapterRepository chapterRepository;



    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setUpDatabase() {
        
      //  var author = new Author();
        //author.setName("Jan");
      //  author.setSurname("Kowalski");
      //  authorRepository.save(
       //        author
       // );
        insertSamplePublishers(5);
        insertSampleAffiliations(10);
        insertSampleAuthors(20);
        insertSampleBooks(20); 
        insertSampleJournals(10);
        insertSampleArticles(20);
        insertSampleChapters(15);

    }


    private String random_string(int length) {
        StringBuilder sb = new StringBuilder(length);

        String characters = "abcdefghijklmnopqrstuvwxyz";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(Character.toUpperCase(randomChar));
        }

        return sb.toString();
    }

    private List<Author> insertSampleAuthors(int count) {
        List<Affiliation> affiliations = affiliationRepository.findAll();
        List<Author> authors = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Author author = new Author();
            author.setName(random_string(5));
            author.setSurname(random_string(6));
            author.setAffiliation(affiliations.get(random.nextInt(affiliations.size()))); 
            authorRepository.save(author);
            authors.add(author);
        }
        return authors;
    }

    private void insertSampleArticles(int count) {
        List<Author> authors = insertSampleAuthors(2);
        List<Journal> journals = journalRepository.findAll();
        for (int i = 0; i < count; i++) {
            Article article = new Article();
            article.setTitle(random_string(10));
            article.setCollection(random_string(8));
            article.setScore(random.nextInt(100));
            article.setVol(random.nextInt(10));
            article.setNo(random.nextInt(5));
            article.setArticleNo(random.nextInt(20));
            article.setJournal(journals.get(random.nextInt(journals.size()))); 
            article.setAuthors(authors);
            articleRepository.save(article);
        }
    }

    private void insertSampleBooks(int count) {
        List<Publisher> publishers = publisherRepository.findAll();
        List<Author> authors = authorRepository.findAll();

        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.setIsbn(random_string(4));
            book.setYear(random.nextInt(50) + 1970); //  1970 -> 2019
            book.setBaseScore(random.nextInt(100));
            book.setTitle(random_string(15));
            book.setEditor(authors.get(random.nextInt(authors.size())));
            book.setPublisher(publishers.get(random.nextInt(publishers.size()))); 
            bookRepository.save(book);
        }
    }

    private void insertSampleJournals(int count) {
        List<Publisher> publishers = publisherRepository.findAll();

        for (int i = 0; i < count; i++) {
            Journal journal = new Journal();
            journal.setBaseScore(random.nextInt(100));
            journal.setTitle(random_string(10));
            journal.setIssn(random_string(4));
            journal.setPublisher(publishers.get(random.nextInt(publishers.size())));
            journalRepository.save(journal);
        }
    }

    private void insertSamplePublishers(int count) {
        List<Publisher> publishers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Publisher publisher = new Publisher();
            publisher.setName(random_string(10));
            publisher.setLocation(random_string(8));
            publisherRepository.save(publisher);
            publishers.add(publisher);
        }
    }

    private List<Affiliation> insertSampleAffiliations(int count) {
        List<Affiliation> affiliations = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Affiliation affiliation = new Affiliation();
            affiliation.setName(random_string(12));
            affiliation.setParent(null);
            affiliationRepository.save(affiliation);
            affiliations.add(affiliation);
        }
        return affiliations;
    }

    private void insertSampleChapters(int count) {
        List<Author> authors = insertSampleAuthors(2);
        List<Book> books = bookRepository.findAll();
        for (int i = 0; i < count; i++) {
            Chapter chapter = new Chapter();
            chapter.setTitle(random_string(15));
            chapter.setScore(random.nextInt(100));
            chapter.setCollection(random_string(8));
            chapter.setAuthors(authors);
            chapter.setBook(books.get(random.nextInt(books.size())));
            chapterRepository.save(chapter);
        }
    }
}