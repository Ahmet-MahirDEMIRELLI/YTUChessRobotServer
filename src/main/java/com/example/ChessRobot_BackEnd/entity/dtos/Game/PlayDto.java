package com.example.ChessRobot_BackEnd.entity.dtos.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlayDto {
    private String userId;

    private String gameId;

    private SquareDto pieceStartSquare;

    private SquareDto pieceEndSquare;
}