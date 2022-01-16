package ru.otus.spring.library.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorService implements EntityService<Author> {

    private final CrudRepository<Author, Integer> authorRepository;
    private final SequenceGeneratorService sequenceGeneratorService;


    @Override
    public Author getById(int id){
        return authorRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("author"));
    }

    @Override
    public List<Author> getAll(){
        return Lists.newArrayList(authorRepository.findAll());
    }

    @Override
    public Author save(Author author){
        author.setId(sequenceGeneratorService.generateSequence(Author.SEQUENCE_NAME));
        return authorRepository.save(author);
    }

    @Override
    public Author updateById(int id, Author author){
        Author authorFromDb = getById(id);
        authorFromDb.setName(author.getName());
        return authorRepository.save(authorFromDb);
    }

    @Override
    public void deleteById(int id){
        authorRepository.deleteById(id);
    }
}
