package ru.otus.spring.library.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.exception.GeneratorException;
import ru.otus.spring.library.service.EntityService;
import ru.otus.spring.library.service.Generator;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class GenreService implements EntityService<Genre>, Generator<Genre> {
    private final Random random = new Random();
    private static final String[] RANDOM_LIST_GENRES =
            {"Боевик", "Детектив", "Фантастика", "Мистика", "Авантюрный (приключенческий) роман", "Исторический роман"};

    private static final int MAX_SLEEP_TIME = 10_000;
    private static final String EXCEPTION_SLEEP_TIME = "5000";

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

    @Override
    @HystrixCommand(commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value=EXCEPTION_SLEEP_TIME)
    })
    public Genre generate() {
        int pos = random.nextInt(RANDOM_LIST_GENRES.length);
        Genre genre = new Genre(RANDOM_LIST_GENRES[pos]);

        try {
            Thread.sleep(random.nextInt(MAX_SLEEP_TIME));
        } catch (InterruptedException ex) {
            throw new GeneratorException(ex);
        }

        return genre;
    }
}
