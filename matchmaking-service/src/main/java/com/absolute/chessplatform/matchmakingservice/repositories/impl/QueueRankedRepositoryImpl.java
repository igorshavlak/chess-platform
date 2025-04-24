package com.absolute.chessplatform.matchmakingservice.repositories.impl;

import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
import com.absolute.chessplatform.matchmakingservice.repositories.QueueRankedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueueRankedRepositoryImpl implements QueueRankedRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addToQueue(String key, QueueEntry queue) {
        String timeHashKey = key + ":time";
        redisTemplate.opsForZSet().add(key, queue.getUserId().toString(), queue.getRating());
        redisTemplate.opsForHash().put(timeHashKey, queue.getUserId().toString(), String.valueOf(queue.getTimestamp()));
        log.info("[{}] Added ranked entry: {}", key, queue);
    }

    @Override
    public void removeFromQueue(String key, UUID userId) {
        String timeHashKey = key + ":time";
        redisTemplate.opsForZSet().remove(key, userId.toString());
        redisTemplate.opsForHash().delete(timeHashKey, userId);
        log.info("[{}] Removed user {} from ranked queue", key, userId);
    }

    @Override
    public Optional<QueueEntry> getQueueEntry(String key, UUID userId) {
        String timeHashKey = key + ":time";
        Double score = redisTemplate.opsForZSet().score(timeHashKey, userId.toString());
        Object enqueueTimeObj = redisTemplate.opsForHash().get(timeHashKey, userId.toString());
        if (score != null && enqueueTimeObj != null) {
            long enqueueTime = Long.parseLong(enqueueTimeObj.toString());
            return Optional.of(new QueueEntry(userId, score.intValue(), enqueueTime));
        }
        return Optional.empty();
    }

    @Override
    public Set<Object> getPlayersInRange(String key, double minRating, double maxRating) {
        return redisTemplate.opsForZSet().rangeByScore(key, minRating, maxRating);
    }

    @Override
    public Set<Object> getAllPlayers(String key) {
        return redisTemplate.opsForZSet().range(key, 0, -1);
    }


}
