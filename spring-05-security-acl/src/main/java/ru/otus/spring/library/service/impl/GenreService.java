package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService implements EntityService<Genre> {

    private final JpaRepository<Genre, Long> genreRepository;

    @Override
    public Genre getById(long id){
        return genreRepository.findById(id).orElseThrow(() -> new DomainNotFound("genre"));
    }

    @Override
    public List<Genre> getAll(){
        return genreRepository.findAll();
    }

    @Override
    public Genre save(Genre genre){
        return genreRepository.save(genre);
    }

    @Override
    @Transactional
    public Genre updateById(Long id, Genre genre){
        Genre genreFromDb = getById(id);
        genreFromDb.setName(genre.getName());
        return genreFromDb;
    }

    @Override
    public void deleteById(Long id){
        genreRepository.deleteById(id);
    }

}
