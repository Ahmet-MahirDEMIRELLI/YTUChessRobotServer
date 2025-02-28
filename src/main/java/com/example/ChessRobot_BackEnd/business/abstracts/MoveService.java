package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.GameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.MoveDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;

public interface MoveService {
    DataResult<MoveDto> isMovePossible(GameDto game, SquareDto pieceStartSquare, SquareDto pieceEndSquare);
    DataResult<GameDto> play(GameDto game, MoveDto moveDto);
}
