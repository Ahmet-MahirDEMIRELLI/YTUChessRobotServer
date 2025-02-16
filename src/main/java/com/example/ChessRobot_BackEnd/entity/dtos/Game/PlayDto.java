package com.example.ChessRobot_BackEnd.entity.dtos.Game;

import com.example.ChessRobot_BackEnd.entity.concretes.Square;
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

    private Square pieceStartSquare;

    private Square pieceEndSquare;
}