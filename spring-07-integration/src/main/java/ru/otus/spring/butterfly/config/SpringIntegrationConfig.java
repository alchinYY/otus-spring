package ru.otus.spring.butterfly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.channel.RendezvousChannel;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

@Configuration
public class SpringIntegrationConfig {

    @Bean
    public QueueChannel itemsCaterpillarChannel(@Value("${transform.queue.size}") int queue) {
        return MessageChannels.queue( queue ).get();
    }

    @Bean
    public PublishSubscribeChannel butterFlyChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel logChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public DirectChannel birdChannel() {
        return MessageChannels.direct().get();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller(
            @Value("${transform.poller.period}") long period,
            @Value("${transform.poller.message-per-poll}") long messagePerPoll

    ) {
        return Pollers.fixedRate( period ).maxMessagesPerPoll( messagePerPoll ).get();
    }

}
