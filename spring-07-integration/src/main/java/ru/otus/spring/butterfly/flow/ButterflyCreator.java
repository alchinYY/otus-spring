package ru.otus.spring.butterfly.flow;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.butterfly.domain.Butterfly;
import ru.otus.spring.butterfly.domain.Caterpillar;

@MessagingGateway
public interface ButterflyCreator {

    @Gateway(requestChannel = "itemsCaterpillarChannel", replyChannel = "butterFlyChannel")
    Butterfly transformToButterflyProcess(Caterpillar caterpillars);

}
