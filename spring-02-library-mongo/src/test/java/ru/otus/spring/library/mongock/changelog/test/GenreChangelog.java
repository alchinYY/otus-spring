package ru.otus.spring.library.mongock.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.util.ChangelogUtil;

import java.util.ArrayList;

@ChangeLog(order = "004")
public class GenreChangelog {

    private final String collectionName = ChangelogUtil.getCollectionName(Genre.class);

    @ChangeSet(order = "001", id = "insertGenres", author = "alchinYY")
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        var docList = new ArrayList<Document>();

        docList.add(new Document().append("_id", 1).append(Genre.Fields.name, "роман-эпопея"));
        docList.add(new Document().append("_id", 2).append(Genre.Fields.name, "сказка"));
        docList.add(new Document().append("_id", 3).append(Genre.Fields.name, "Научная фантастика"));

        myCollection.insertMany(docList);
    }

}