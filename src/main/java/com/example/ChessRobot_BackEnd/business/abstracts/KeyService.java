package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;

public interface KeyService {
    Result getBackendPublicKey();
}
