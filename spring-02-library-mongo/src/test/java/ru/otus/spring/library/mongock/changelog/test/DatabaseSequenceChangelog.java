package ru.otus.spring.library.mongock.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.spring.library.domain.*;
import ru.otus.spring.library.util.ChangelogUtil;

import java.util.ArrayList;

@ChangeLog(order = "005")
public class DatabaseSequenceChangelog {

    private final String collectionName = ChangelogUtil.getCollectionName(DatabaseSequence.class);

    @ChangeSet(order = "001", id = "insertSeq", author = "alchinYY")
    public void insertSeq(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);
        var docList = new ArrayList<Document>();

        docList.add(createSequenceDocument(Author.class, Author.SEQUENCE_NAME, db));
        docList.add(createSequenceDocument(Comment.class, Comment.SEQUENCE_NAME, db));
        docList.add(createSequenceDocument(Book.class, Book.SEQUENCE_NAME, db));
        docList.add(createSequenceDocument(Genre.class, Genre.SEQUENCE_NAME, db));

        myCollection.insertMany(docList);
    }


    private Document createSequenceDocument(Class<? extends AbstractDomain> aClass, String sequence, MongoDatabase db) {
        String collectionNameForUpdate = ChangelogUtil.getCollectionName(aClass);
        return new Document().append("_id", sequence).append("seq", db.getCollection(collectionNameForUpdate).countDocuments());
    }
}
