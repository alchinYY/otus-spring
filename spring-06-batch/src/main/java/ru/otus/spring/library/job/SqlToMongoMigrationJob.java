package ru.otus.spring.library.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.sql.Book;
import ru.otus.spring.library.job.processor.BookMigrationItemProcessor;

import java.util.HashMap;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SqlToMongoMigrationJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int CHUNK_SIZE = 5;
    private final ItemReadListener<Book> bookItemReadListener;
    private final CleanUpService cleanUpService;


    @StepScope
    @Bean
    public ItemReader<Book> sqlReader(JpaRepository<Book, Long> bookRepo) {
        return new RepositoryItemReaderBuilder<Book>()
                .name("bookRepositoryItemReader")
                .repository(bookRepo)
                .methodName("findAll")
                .sorts(new HashMap<>())
                .build();
    }

    @StepScope
    @Bean
    public BookMigrationItemProcessor processor(ModelMapper modelMapper) {
        return new BookMigrationItemProcessor(modelMapper);
    }

    @StepScope
    @Bean
    public ItemWriter<BookMng> mongoWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<BookMng>()
                .collection("books")
                .template(mongoOperations)
                .delete(false)
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Job importUserJob(Step transformPersonsStep, Step cleanUpStep) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(transformPersonsStep)
                .next(cleanUpStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        log.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        log.info("Конец job");
                    }
                })
                .build();
    }


    @Bean
    public Step transformPersonsStep(ItemReader<Book> reader, ItemWriter<BookMng> writer,
                                     ItemProcessor<Book, BookMng> itemProcessor) {
        return stepBuilderFactory.get("step1")
                .<Book, BookMng>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<>() {
                    public void beforeRead() {
                        log.info("Начало чтения");
                    }

                    public void afterRead(@NonNull Book o) {
                        log.info("Конец чтения");
                    }

                    public void onReadError(@NonNull Exception e) {
                        log.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<>() {
                    public void beforeWrite(@NonNull List list) {
                        log.info("Начало записи");
                    }

                    public void afterWrite(@NonNull List list) {
                        log.info("Конец записи");
                    }

                    public void onWriteError(@NonNull Exception e, @NonNull List list) {
                        log.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<>() {
                    public void beforeProcess(Book o) {
                        log.info("Начало обработки");
                    }

                    public void afterProcess(@NonNull Book o, BookMng o2) {
                        log.info("Конец обработки");
                    }

                    public void onProcessError(@NonNull Book o, @NonNull Exception e) {
                        log.info("Ошибка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(@NonNull ChunkContext chunkContext) {
                        log.info("Начало пачки");
                    }

                    public void afterChunk(@NonNull ChunkContext chunkContext) {
                        log.info("Конец пачки");
                    }

                    public void afterChunkError(@NonNull ChunkContext chunkContext) {
                        log.info("Ошибка пачки");
                    }
                })
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return this.stepBuilderFactory.get("cleanUpStep")
                .tasklet(cleanUpTasklet())
                .build();
    }

}
