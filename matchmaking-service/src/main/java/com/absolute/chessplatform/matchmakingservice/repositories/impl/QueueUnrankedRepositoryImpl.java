package com.absolute.chessplatform.matchmakingservice.repositories.impl;

import com.absolute.chessplatform.matchmakingservice.entities.QueueEntry;
import com.absolute.chessplatform.matchmakingservice.exceptions.EmptyQueueException;
import com.absolute.chessplatform.matchmakingservice.repositories.QueueUnrankedRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueueUnrankedRepositoryImpl implements QueueUnrankedRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void addToQueue(String key, QueueEntry entry) {
        redisTemplate.opsForList().rightPush(key, entry);
        log.info("[{}] Enqueued unranked: {}", key, entry);
    }

    @Override
    public Optional<QueueEntry> popFirst(String key) {
        Object obj = redisTemplate.opsForList().leftPop(key);
        if(obj == null) {
            return Optional.empty();
        }
        return Optional.of((QueueEntry)obj);
    }

    @Override
    public void removeFromQueue(String key, UUID uuid) {
        var list = redisTemplate.opsForList();
        var size = list.size(key);
        if(size == null) throw new EmptyQueueException("Empty queue for: " + key);
        var all =  list.range(key, 0, -1);
        if (all == null) throw new EmptyQueueException("Empty queue for: " + key);
        for(Object obj : all){
            QueueEntry entry = (QueueEntry)obj;
            if(entry.getUserId().equals(uuid)){
                list.remove(key,1,entry);
                log.info("[{}] Removed unranked user: {}", key, entry);
                break;
            }
        }
    }
}
