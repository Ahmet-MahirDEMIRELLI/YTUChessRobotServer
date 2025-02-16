package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import com.example.ChessRobot_BackEnd.entity.concretes.Match;
import com.example.ChessRobot_BackEnd.entity.concretes.Move;
import com.example.ChessRobot_BackEnd.entity.concretes.Square;

public interface MoveService {
    DataResult<Move> isMovePossible(Match match, Square pieceStartSquare, Square pieceEndSquare);
    DataResult<Game> play(Game game, Move move);
}
