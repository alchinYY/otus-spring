package ru.otus.spring.butterfly.service.bird;

import org.springframework.stereotype.Service;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.service.BirdService;

@Service
public class KindBirdService implements BirdService {
    @Override
    public Butterfly interact(Butterfly butterfly) {
        System.out.println("Заботливая птица пропустила бабочку::" + butterfly);
        return butterfly;
    }
}
