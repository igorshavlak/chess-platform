package com.absolute.chessplatform.traininglessonsservice.services;

import com.absolute.chessplatform.traininglessonsservice.dtos.RatingPointDTO;
import com.absolute.chessplatform.traininglessonsservice.dtos.SolveResultDTO;
import com.absolute.chessplatform.traininglessonsservice.dtos.ThemePerformanceDTO;
import com.absolute.chessplatform.traininglessonsservice.entities.Puzzle;
import com.absolute.chessplatform.traininglessonsservice.entities.UserPuzzleHistory;
import com.absolute.chessplatform.traininglessonsservice.entities.UserPuzzleStats;
import com.absolute.chessplatform.traininglessonsservice.entities.UserRatingHistory;
import com.absolute.chessplatform.traininglessonsservice.repositories.PuzzleRepository;
import com.absolute.chessplatform.traininglessonsservice.repositories.UserPuzzleHistoryRepository;
import com.absolute.chessplatform.traininglessonsservice.repositories.UserPuzzleStatsRepository;
import com.absolute.chessplatform.traininglessonsservice.repositories.UserRatingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PuzzleService {
    private final PuzzleRepository puzzleRepo;
    private final UserPuzzleStatsRepository statsRepo;
    private final UserPuzzleHistoryRepository historyRepo;
    private final UserRatingHistoryRepository ratingHistoryRepo;


    @Transactional
    public SolveResultDTO solvePuzzle(UUID userId, String puzzleId, List<String> userMoves) {
        Puzzle puzzle = puzzleRepo.findById(puzzleId).orElseThrow();
        UserPuzzleStats stats = statsRepo.findById(userId)
                .orElseGet(() -> statsRepo.save(new UserPuzzleStats(userId)));

        boolean success = puzzle.getMoves().equals(String.join(" ", userMoves));

        double R_u = stats.getRating();
        double R_p = puzzle.getRating();
        double E_u = 1.0 / (1 + Math.pow(10, (R_p - R_u) / 400));
        double S_u = success ? 1.0 : 0.0;
        int kUser = getKForUser(stats);
        int newR_u = (int) Math.round(R_u + kUser * (S_u - E_u));
        stats.setRating(newR_u);
        stats.setNbPlays(stats.getNbPlays() + 1);
        if (success) stats.setNbSuccess(stats.getNbSuccess() + 1);
        statsRepo.save(stats);
        UserPuzzleHistory userPuzzleHistory = new UserPuzzleHistory();
        userPuzzleHistory.setUserId(userId);
        userPuzzleHistory.setPuzzleId(puzzleId);
        userPuzzleHistory.setSuccess(success);
        userPuzzleHistory.setTries(1);
        historyRepo.save(userPuzzleHistory);
        UserRatingHistory userRatingHistory = new UserRatingHistory();
        userRatingHistory.setId(userId);
        userRatingHistory.setRating(newR_u);
        ratingHistoryRepo.save(userRatingHistory);

        return new SolveResultDTO(success, newR_u);
    }
    @Transactional(readOnly = true)
    public List<ThemePerformanceDTO> getPerformanceByTheme(UUID userId) {
        List<UserPuzzleHistory> entries = historyRepo.findByUserId(userId);
        Map<String, List<UserPuzzleHistory>> byTheme = new HashMap<>();
        for(var h : entries) {
            puzzleRepo.findById(h.getPuzzleId()).ifPresent(puzzle -> {
                if(puzzle.getThemes() != null) {
                    for (String theme : puzzle.getThemes().split(" ")) {
                        byTheme.computeIfAbsent(theme.trim(), k -> new ArrayList<>()).add(h);
                    }
                }
            });
        }
        return byTheme.entrySet().stream()
                .map(e -> new ThemePerformanceDTO(
                        e.getKey(),
                        e.getValue().size(),
                        (int)e.getValue().stream().filter(UserPuzzleHistory::getSuccess).count(),
                        e.getValue().stream().filter(UserPuzzleHistory::getSuccess).count() * 100.0 / e.getValue().size()
                ))
                .sorted(Comparator.comparingDouble(ThemePerformanceDTO::getSuccessRate).reversed())
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public int getMaxUserRating(UUID userId) {
        return ratingHistoryRepo.findByUserIdOrderByChangedAt(userId).stream()
                .mapToInt(UserRatingHistory::getRating)
                .max()
                .orElseGet(() -> statsRepo.findById(userId)
                        .map(UserPuzzleStats::getRating).orElse(1500));
    }
    @Transactional(readOnly = true)
    public List<RatingPointDTO> getRatingHistory(UUID userId) {
        return ratingHistoryRepo.findByUserIdOrderByChangedAt(userId).stream()
                .map(rh -> new RatingPointDTO(rh.getChangedAt(), rh.getRating()))
                .collect(Collectors.toList());
    }

    private int getKForUser(UserPuzzleStats stats) {
        int plays = stats.getNbPlays();
        int rating = stats.getRating();
        if (plays < 30) {
            return 40;
        } else if (rating < 2400) {
            return 20;
        } else {
            return 10;
        }
    }
}
