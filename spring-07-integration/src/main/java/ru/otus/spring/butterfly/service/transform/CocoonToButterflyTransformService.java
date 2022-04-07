package ru.otus.spring.butterfly.service.transform;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.butterfly.config.ButterflyConfig;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.domain.Caterpillar;
import ru.otus.spring.butterfly.domain.Cocoon;
import ru.otus.spring.butterfly.exception.TransformException;
import ru.otus.spring.butterfly.service.TransformService;

/**
 * Transform from cocoon to butterfly.
 * 0 -> %
 */
@Service
@RequiredArgsConstructor
public class CocoonToButterflyTransformService implements TransformService<Cocoon, Butterfly> {

    private final ButterflyConfig butterflyConfig;

    @Override
    public Butterfly transform(Cocoon from) {
        try {
            Thread.sleep(butterflyConfig.getTransformTimeMs());
            Butterfly butterfly = new Butterfly(
                    butterflyConfig.getConfigBySize().get(from.getSize()),
                    from.getIndex(),
                    from.getSize(),
                    true
            );
            System.out.println("создание бабочки %:" + butterfly);
            return butterfly;
        } catch (Exception ex) {
            throw new TransformException(Caterpillar.class, from.getIndex());
        }
    }
}
