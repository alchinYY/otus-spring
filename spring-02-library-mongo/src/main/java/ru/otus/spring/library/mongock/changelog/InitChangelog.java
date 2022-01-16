package ru.otus.spring.library.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;

@ChangeLog(order = "000")
@Slf4j
public class InitChangelog {
    @ChangeSet(order = "001", id = "dropDb", author = "alchinYY", runAlways = true)
    public void dropDb(MongoDatabase db) {
        log.info("drop old db");
        db.drop();
    }
}
