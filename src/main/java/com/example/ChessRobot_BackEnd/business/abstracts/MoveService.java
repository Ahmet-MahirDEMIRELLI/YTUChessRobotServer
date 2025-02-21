package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import com.example.ChessRobot_BackEnd.entity.concretes.Match;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.MoveDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;

public interface MoveService {
    DataResult<MoveDto> isMovePossible(Match match, SquareDto pieceStartSquare, SquareDto pieceEndSquare);
    DataResult<Game> play(Game game, MoveDto moveDto);
}
