package com.absolute.chessplatform.traininglessonsservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ThemePerformanceDTO {
    private String theme;
    private int attempts;
    private int successes;
    private double successRate;
}
