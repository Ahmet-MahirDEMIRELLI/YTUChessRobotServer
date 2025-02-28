package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.GameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.PlayDto;

public interface GameService{
    Result addGame(InitializeGameDto initializeGameDto);
    DataResult<GameDto> play(PlayDto playDto);
}
