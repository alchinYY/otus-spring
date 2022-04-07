package ru.otus.spring.butterfly.service.transform;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.butterfly.config.CaterpillarConfig;
import ru.otus.spring.butterfly.domain.Caterpillar;
import ru.otus.spring.butterfly.domain.Cocoon;
import ru.otus.spring.butterfly.exception.TransformException;
import ru.otus.spring.butterfly.service.TransformService;

/**
 * Transform from caterpillar to cocoon.
 * ooooooO -> 0
 */
@Service
@RequiredArgsConstructor
public class CaterpillarToCocoonTransformService implements TransformService<Caterpillar, Cocoon> {

    private final CaterpillarConfig config;

    @Override
    public Cocoon transform(Caterpillar from) {
        try {
            Thread.sleep(config.getTransformTimeMs());
            Cocoon cocoon = new Cocoon(from.getIndex(), from.getSize());
            System.out.println("создание кокона 0:" + cocoon);
            return cocoon;
        } catch (Exception ex) {
            throw new TransformException(Caterpillar.class, from.getIndex());
        }
    }
}
