package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;

public interface ThreadCheckService {
    boolean isSquaresUnderThread(byte[][] board, SquareDto[] squaresToCheck, boolean isWhitePlaying);
    int traceToKing(byte[][] board, SquareDto king, SquareDto pieceToCheck);
}
