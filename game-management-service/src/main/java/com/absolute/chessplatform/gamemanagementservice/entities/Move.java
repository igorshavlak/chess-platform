package com.absolute.chessplatform.gamemanagementservice.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {

    UUID gameId;
    String move;
}
