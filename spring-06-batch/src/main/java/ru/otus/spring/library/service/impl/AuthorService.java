package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorService implements EntityService<Author> {

    private final JpaRepository<Author, Long> authorRepository;

    @Override
    public Author getById(long id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("author"));
    }

    @Override
    public List<Author> getAll(){
        return authorRepository.findAll();
    }

    @Override
    public Author save(Author author){
        return authorRepository.save(author);
    }

    @Override
    @Transactional
    public Author updateById(Long id, Author author){
        Author authorFromDb = getById(id);
        authorFromDb.setName(author.getName());
        return authorFromDb;
    }

    @Override
    public void deleteById(Long id){
        authorRepository.deleteById(id);
    }
}
