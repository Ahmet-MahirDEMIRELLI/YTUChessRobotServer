package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.KeyService;
import com.example.ChessRobot_BackEnd.business.abstracts.ThreadCheckService;
import com.example.ChessRobot_BackEnd.business.constants.KeyMessages;
import com.example.ChessRobot_BackEnd.core.utilities.results.ErrorResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.core.utilities.results.SuccessResult;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;
import org.springframework.beans.factory.annotation.Autowired;

public class ThreadCheckManager implements ThreadCheckService {

    @Autowired
    public ThreadCheckManager() {
        super();
    }

    @Override
    public boolean isUnderThread(SquareDto[] squaresToCheck) {
        return false;
    }
}
