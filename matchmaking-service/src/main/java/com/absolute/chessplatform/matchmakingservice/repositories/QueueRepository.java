package com.absolute.chessplatform.matchmakingservice.repositories;

import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
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
public class QueueRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String QUEUE_KEY = "chess_queue";
    private static final String TIME_HASH_KEY = "chess_time_hash";

    public void addToQueue(QueueEntry queue) {
        redisTemplate.opsForZSet().add(QUEUE_KEY, queue.getUserId(), queue.getRating());
        redisTemplate.opsForHash().put(TIME_HASH_KEY, queue.getUserId().toString(), queue.getTimestamp());
        log.info("Added queue entry: {}", queue.toString());
    }
    public void removeFromQueue(UUID userId) {
        redisTemplate.opsForZSet().remove(QUEUE_KEY, userId);
        redisTemplate.opsForHash().delete(TIME_HASH_KEY, userId.toString());
        log.info("Removed queue entry: {}", userId);
    }
    public Optional<QueueEntry> getQueueEntry(UUID userId) {
        Double score = redisTemplate.opsForZSet().score(QUEUE_KEY, userId);
        Object enqueueTimeObj = redisTemplate.opsForHash().get(TIME_HASH_KEY, userId.toString());
        if (score != null && enqueueTimeObj != null) {
            long enqueueTime = Long.parseLong(enqueueTimeObj.toString());
            return Optional.of(new QueueEntry(userId, score.intValue(), enqueueTime));
        }
        return Optional.empty();
    }
    public Set<Object> getPlayersInRange(double minRating, double maxRating) {
        return redisTemplate.opsForZSet().rangeByScore(QUEUE_KEY, minRating, maxRating);
    }
    public Set<Object> getAllPlayers() {
        return redisTemplate.opsForZSet().range(QUEUE_KEY, 0, -1);
    }


}
