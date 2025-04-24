package com.absolute.chessplatform.userservice.dtos;

import com.absolute.chessplatform.userservice.entities.GameMode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class GameResultDTO {
    public GameMode gameMode;
    public boolean isRating;
    public int rating;
    public boolean isWinner;
    public boolean isDrawn;
}
