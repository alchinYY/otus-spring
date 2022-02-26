package ru.otus.spring.library.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.repository.AuthorRepository;
import ru.otus.spring.library.repository.BookRepository;
import ru.otus.spring.library.repository.GenreRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookRepoHandler {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    public Mono<List<BookDto>> getAllDto() {

        return bookRepository.findAll()
                .flatMap(this::toDto)
                .collectList();
    }



    public Mono<Void> delete(String id) {
        return bookRepository.deleteById(id);
    }

    public Mono<BookDto> save(BookDto bookDto) {
        return Mono.just(bookDto)
                .map(b -> toEntity(bookDto))
                .flatMap(bookRepository::save)
                .map(this::toDto)
                .flatMap(b -> b);
    }


    public Book toEntity(BookDto bookDto) {

        Book book = modelMapper.map(bookDto, Book.class);
        book.setAuthors(bookDto.getAuthors().stream().map(AuthorDto::getId).collect(Collectors.toList()));
        book.setGenre(bookDto.getGenre().getId());
        return book;
    }

    private Mono<BookDto> toDto(Book book) {
        return Mono.just(new BookDto(book.getId(), book.getName()))
                .zipWith(
                        authorRepository.findAllById(book.getAuthors()).map(a -> modelMapper.map(a, AuthorDto.class)).collectList(),
                        (b, a) -> {
                            b.setAuthors(a);
                            return b;
                        }
                )
                .zipWith(
                        genreRepository.findById(book.getGenre()).map(g -> modelMapper.map(g, GenreDto.class)),
                        (b, g) -> {
                            b.setGenre(g);
                            return b;
                        }
                );
    }

}
