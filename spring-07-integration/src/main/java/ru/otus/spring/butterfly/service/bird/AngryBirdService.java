package ru.otus.spring.butterfly.service.bird;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.service.BirdService;

@Service
@Slf4j
public class AngryBirdService implements BirdService {
    @Override
    public Butterfly interact(Butterfly butterfly) {
        System.out.println("Злая птица съела бабочку::" + butterfly);
        butterfly.setAlive(false);
        return butterfly;
    }
}
