package pl.edu.wat.knowledge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.wat.knowledge.entity.Article;
import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Book;
import pl.edu.wat.knowledge.entity.Chapter;
import pl.edu.wat.knowledge.repository.ArticleRepository;
import pl.edu.wat.knowledge.repository.BookRepository;
import pl.edu.wat.knowledge.repository.ChapterRepository;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;


@Service
public class ScoreService {
//Uzupełnij `ScoreService` o metodę `getScore(Author)`, która zwróci punkty autora uzyskane w danym roku.
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    public Integer getScore(Author author, Integer year) {
        List<Integer> allScores = new ArrayList<>();

        // punkty z artykułów autora w danym roku
        List<Article> articles = articleRepository.findByAuthorsPublisherYear(author, year);
        List<Integer> articleScores = articles
                .stream()
                .map(article -> calculateScoreForArticle(article, author))
                .collect(Collectors.toList());
        allScores.addAll(articleScores);

        //  punkty z rozdziałów autora w danym roku
        List<Chapter> chapters = chapterRepository.findByAuthorsBookYear(author, year);
        List<Integer> chapterScores = chapters.stream()
                .map(chapter -> calculateScoreForChapter(chapter, author))
                .collect(Collectors.toList());
        allScores.addAll(chapterScores);

        allScores.sort(Comparator.reverseOrder()); //malejąco

        // Wybierz 4 najlepsze wyniki
        int totalScore = allScores
                .stream()
                .limit(4)
                .mapToInt(Integer::intValue)
                .sum();

        return totalScore;
    }

    private int calculateScoreForArticle(Article article, Author author) {
        int score = 0;
        int numAuthors = article.getAuthors().size();

        if (numAuthors == 0) {
            return score;
        }

        int individualScore = article.getScore() / numAuthors;

        if (article.getAuthors().contains(author)) {
            score += individualScore;
        }

        return score;
    }

    private int calculateScoreForChapter(Chapter chapter, Author author) {
        int score = 0;
        int numAuthors = chapter.getAuthors().size();

        if (numAuthors == 0) {
            return score;
        }

        int individualScore = chapter.getScore() / numAuthors;

        if (chapter.getAuthors().contains(author)) {
            score += individualScore;
        }

        return score;
    }
}
