package com.absolute.chessplatform.userservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {
    private int rating;
    private int gamesPlayed;
    private int gamesWon;
}
