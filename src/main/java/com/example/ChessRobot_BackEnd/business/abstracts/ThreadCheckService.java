package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.PlayDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;

public interface ThreadCheckService {
    boolean isUnderThread(SquareDto[] squaresToCheck, boolean isWhitePlaying);
}
