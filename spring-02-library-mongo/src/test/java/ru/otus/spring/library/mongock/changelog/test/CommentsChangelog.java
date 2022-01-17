package ru.otus.spring.library.mongock.changelog.test;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.util.ChangelogUtil;

import java.util.ArrayList;

@ChangeLog(order = "003")
public class CommentsChangelog {

    private final String collectionName = ChangelogUtil.getCollectionName(Comment.class);


    @ChangeSet(order = "001", id = "insertComments", author = "alchinYY")
    public void insertComments(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection(collectionName);

        var docList = new ArrayList<Document>();

        docList.add(createDocument(1, "2015-12-18T17:53:02.594", 1, "Очень крутая книга"));
        docList.add(createDocument(2, "2015-12-18T17:55:02.594", 1, "Что-то как-то главный персонаж не раскрыт"));
        docList.add(createDocument(3, "2015-12-18T18:56:02.594", 1, "Ты просто ничего не понял"));

        myCollection.insertMany(docList);
    }

    private Document createDocument(Integer id, String date, Integer bookId, String body) {
        return new Document()
                .append("_id", id)
                .append(Comment.Fields.date, date)
                .append(Comment.Fields.book, ChangelogUtil.createDbRef(ChangelogUtil.getCollectionName(Book.class), bookId))
                .append(Comment.Fields.body, body);
    }

}
