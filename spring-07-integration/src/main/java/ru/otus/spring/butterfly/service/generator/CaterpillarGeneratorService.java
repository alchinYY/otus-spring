package ru.otus.spring.butterfly.service.generator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.spring.butterfly.config.CaterpillarConfig;
import ru.otus.spring.butterfly.domain.Caterpillar;
import ru.otus.spring.butterfly.service.GeneratorService;

import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CaterpillarGeneratorService implements GeneratorService<Caterpillar> {

    private final AtomicLong atomicLong = new AtomicLong(0);
    private final CaterpillarConfig config;

    @Override
    public Caterpillar generate() {
        long size = RandomUtils.nextLong(config.getMinSize(),config.getMaxSize());
        return new Caterpillar(atomicLong.incrementAndGet(), size);
    }
}
