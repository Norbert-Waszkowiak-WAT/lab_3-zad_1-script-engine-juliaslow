package pl.edu.wat.knowledge.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import pl.edu.wat.knowledge.entity.Author;
import pl.edu.wat.knowledge.entity.Chapter;

@RepositoryRestResource(collectionResourceRel = "chapters", path = "chapters")
public interface ChapterRepository extends MongoRepository<Chapter, String> {
    List<Chapter> findByAuthorsBookYear(Author author, Integer year);
}