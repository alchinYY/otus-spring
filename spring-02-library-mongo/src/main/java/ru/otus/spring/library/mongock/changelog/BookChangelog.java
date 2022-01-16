package ru.otus.spring.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.util.ChangelogUtil;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.spring.library.util.ChangelogUtil.createDbRef;
import static ru.otus.spring.library.util.ChangelogUtil.createReferenceList;

@ChangeLog(order = "002")
public class BookChangelog {

    private final String collectionName = ChangelogUtil.getCollectionName(Book.class);

    @ChangeSet(order = "001", id = "insertBooks", author = "alchinYY")
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        var docList = new ArrayList<Document>();


        docList.add(createBook(1, "Хоббит", 1, List.of(1, 2, 3), List.of(1)));
        docList.add(createBook(2, "Властелин колец", 1, null, List.of(1)));
        docList.add(createBook(3, "Сказка о царе Салтане", 2, null, List.of(2)));
        docList.add(createBook(4, "За миллиард лет до конца света.(сборник)", 3, null, List.of(3, 4)));

        myCollection.insertMany(docList);
    }

    private Document createBook(int id, String name, int genre, List<Integer> commentId, List<Integer> authorsId) {
        return new Document()
                .append("_id", id)
                .append(Book.Fields.name, name)
                .append(Book.Fields.genre, createDbRef(ChangelogUtil.getCollectionName(Genre.class), genre))
                .append(Book.Fields.authors, createReferenceList(authorsId, Author.class))
                .append(Book.Fields.comments, createReferenceList(commentId, Comment.class));
    }

}
