package ru.otus.spring.butterfly.flow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

@Configuration
public class ButterflyFlow {

    @Bean
    public IntegrationFlow kindBirdFlow() {
        return IntegrationFlows.from("birdChannel")
                .handle("kindBirdService", "interact")
                .channel("butterFlyChannel")
                .get();
    }

    @Bean
    public IntegrationFlow angryBirdFlow() {
        return IntegrationFlows.from("birdChannel")
                .handle("angryBirdService", "interact")
                .channel("butterFlyChannel")
                .get();
    }

    
    @Bean
    public IntegrationFlow caterpillarFlow() {
        return IntegrationFlows.from( "itemsCaterpillarChannel")
                .handle( "caterpillarToCocoonTransformService", "transform")
                .channel("logChannel")
                .handle("cocoonToButterflyTransformService", "transform")
                .channel("birdChannel")
                .get();
    }

    @Bean
    public IntegrationFlow logFlow() {
        return IntegrationFlows.from( "logChannel")
                .log()
                .get();
    }

}
