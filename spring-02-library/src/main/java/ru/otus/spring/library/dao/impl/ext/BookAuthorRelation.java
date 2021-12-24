package ru.otus.spring.library.dao.impl.ext;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class BookAuthorRelation implements Serializable {
    private final long bookId;
    private final long authorId;
}
