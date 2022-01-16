package ru.otus.spring.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.util.ChangelogUtil;

import java.util.ArrayList;

@ChangeLog(order = "001")
public class AuthorsChangelog {

    private final String collectionName = ChangelogUtil.getCollectionName(Author.class);

    @ChangeSet(order = "001", id = "insertAuthors", author = "alchinYY")
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        var docList = new ArrayList<Document>();

        docList.add(new Document().append("_id", 1).append(Author.Fields.name, "Толкин, Джон Рональд Руэл"));
        docList.add(new Document().append("_id", 2).append(Author.Fields.name, "Пушкин Александр Сергеевич"));
        docList.add(new Document().append("_id", 3).append(Author.Fields.name, "Стругацкий Аркадий"));
        docList.add(new Document().append("_id", 4).append(Author.Fields.name, "Стругацкий Илья"));

        myCollection.insertMany(docList);
    }
}
