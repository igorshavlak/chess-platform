package com.absolute.chessplatform.matchmakingservice.workers.impl;

import com.absolute.chessplatform.matchmakingservice.entities.Match;
import com.absolute.chessplatform.matchmakingservice.services.MatchmakingService;
import com.absolute.chessplatform.matchmakingservice.workers.IQueueWorker;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnrankedQueueWorker implements Runnable, IQueueWorker {
    private final String queueKey;
    private final MatchmakingService matchmakingService;
    private final long sleepNoMatchMs;

    private Thread workerThread;
    private volatile boolean running = true;

    public UnrankedQueueWorker(String queueKey,
                               MatchmakingService matchmakingService,
                               long sleepNoMatchMs) {
        this.queueKey = queueKey;
        this.matchmakingService = matchmakingService;
        this.sleepNoMatchMs = sleepNoMatchMs;
    }


    @PostConstruct
    @Override
    public void startWorker() {
        workerThread = new Thread(this, "UnrankedWorker-" + queueKey);
        workerThread.setDaemon(true);
        workerThread.start();
        log.info("Started UnrankedQueueWorker for {}", queueKey);
    }

    @Override
    public void stopWorker() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                var matchOpt = matchmakingService.findMatchInQueue(queueKey);
                if (matchOpt.isEmpty()) {
                    Thread.sleep(sleepNoMatchMs);
                } else {
                    log.info("Unranked Match found: {}");
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                log.warn("Unranked worker {} interrupted", queueKey);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error in unranked worker {}: {}", queueKey, e.getMessage(), e);
            }
        }
        log.info("Unranked worker {} stopped.", queueKey);
    }
}
