package com.absolute.chessplatform.traininglessonsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Date;

@Getter
@AllArgsConstructor
public class RatingPointDTO {
    private Instant timestamp;
    private int rating;
}
