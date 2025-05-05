package com.absolute.chessplatform.traininglessonsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@Getter
public class SolveResultDTO {
    private boolean success;
    private int newUserRating;
}
