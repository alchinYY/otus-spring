package ru.otus.spring.butterfly;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.domain.Caterpillar;
import ru.otus.spring.butterfly.flow.ButterflyCreator;
import ru.otus.spring.butterfly.service.GeneratorService;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRunner implements ApplicationRunner {

    private final ButterflyCreator butterflyCreator;
    private final GeneratorService<Caterpillar> caterpillarGeneratorService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {

            Thread.sleep(1000);

            Caterpillar caterpillar = caterpillarGeneratorService.generate();
            System.out.println(caterpillar);
            Butterfly butterfly = butterflyCreator.transformToButterflyProcess(caterpillar);
            System.out.println(butterfly);

        }
    }


}
