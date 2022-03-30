package ru.otus.spring.library.job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@Slf4j
public class SqlToMngChunkListener implements ChunkListener {

    private final String entityName;

    @Override
    public void beforeChunk(@NonNull ChunkContext chunkContext) {
        log.info("Начало пачки::{}", entityName);
    }

    @Override
    public void afterChunk(@NonNull ChunkContext chunkContext) {
        log.info("Конец пачки::{}", entityName);
    }

    @Override
    public void afterChunkError(@NonNull ChunkContext chunkContext) {
        log.info("Ошибка пачки");
    }
}
