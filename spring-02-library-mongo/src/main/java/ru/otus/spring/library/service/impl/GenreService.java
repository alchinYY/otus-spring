package ru.otus.spring.library.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.repo.BookRepository;
import ru.otus.spring.library.service.EntityService;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService implements EntityService<Genre> {

    private final CrudRepository<Genre, Integer> genreRepository;
    private final BookRepository bookRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    @Override
    public Genre getById(int id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("genre"));
    }

    @Override
    public List<Genre> getAll() {
        return Lists.newArrayList(genreRepository.findAll());
    }

    @Override
    public Genre save(Genre genre) {
        genre.setId(sequenceGeneratorService.generateSequence(Genre.SEQUENCE_NAME));
        return genreRepository.save(genre);
    }

    @Override
    public Genre updateById(int id, Genre genre) {
        Genre genreFromDb = getById(id);
        genreFromDb.setName(genre.getName());
        return genreRepository.save(genreFromDb);
    }

    @Override
    public void deleteById(int id) {
        genreRepository.deleteById(id);
    }
}
