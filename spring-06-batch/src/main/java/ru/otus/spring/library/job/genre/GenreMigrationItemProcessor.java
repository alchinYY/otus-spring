package ru.otus.spring.library.job.genre;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.spring.library.domain.mongo.GenreMng;
import ru.otus.spring.library.domain.sql.Genre;

public class GenreMigrationItemProcessor implements ItemProcessor<Genre, GenreMng> {

    @Override
    public GenreMng process(Genre genre) {
        return new GenreMng(genre.getId().toString(), genre.getName());
    }
}