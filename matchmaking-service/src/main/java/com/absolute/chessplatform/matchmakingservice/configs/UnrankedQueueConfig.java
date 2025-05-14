package com.absolute.chessplatform.matchmakingservice.configs;

import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import com.absolute.chessplatform.matchmakingservice.workers.IQueueWorker;
import com.absolute.chessplatform.matchmakingservice.workers.impl.UnrankedQueueWorker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnrankedQueueConfig {
    private final MatchmakingService unrankedService;

    public UnrankedQueueConfig(@Qualifier("unrankedService") MatchmakingService unrankedService) {
        this.unrankedService = unrankedService;
    }

    @Bean
    public IQueueWorker classical10plus0UnrankedWorker() {
        return new UnrankedQueueWorker(
                "queue:classic:10+0:unranked",
                unrankedService,
                1000
        );
    }
    @Bean
    public IQueueWorker blitz3plus2UnrankedWorker() {
        return new UnrankedQueueWorker(
                "queue:blitz:3+2:unranked",
                unrankedService,
                1000
        );
    }
    @Bean
    public IQueueWorker blitz3plus0UnrankedWorker() {
        return new UnrankedQueueWorker(
                "queue:blitz:3+0:unranked",
                unrankedService,
                1000
        );
    }
    @Bean
    public IQueueWorker bullet1plus0UnrankedWorker() {
        return new UnrankedQueueWorker(
                "queue:bullet:1+0:unranked",
                unrankedService,
                1000
        );
    }
    @Bean
    public IQueueWorker bullet2plus1UnrankedWorker() {
        return new UnrankedQueueWorker(
                "queue:bullet:2+1:unranked",
                unrankedService,
                1000
        );
    }
}
