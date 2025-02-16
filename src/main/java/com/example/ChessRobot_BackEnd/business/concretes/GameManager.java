package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.GameService;
import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.business.constants.GameMessages;
import com.example.ChessRobot_BackEnd.core.utilities.results.*;
import com.example.ChessRobot_BackEnd.dataAccess.abstracts.GameDao;
import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GameManager implements GameService {

    private final GameDao gameDao;
    private final UserService userService;

    @Autowired
    public GameManager(GameDao gameDao, UserService userService) {
        super();
        this.gameDao = gameDao;
        this.userService = userService;
    }


    @Override
    public Result addGame(InitializeGameDto gameDto) {
        if (gameDto.getUserId().isEmpty() || gameDto.getTime().isEmpty() || gameDto.getType().isEmpty()) {
            return new ErrorResult(GameMessages.gameCannotBeInitialized);
        }

        // kullanıcı kayıtlı mı ve başka maçi var mı kontrolü yap

        if(gameDto.getType().equals("player")){
            return new ErrorResult(GameMessages.currentlyNotAvailable);
        }
        else if(!gameDto.getType().equals("robot")){
            return new ErrorResult(GameMessages.gameCannotBeInitialized);
        }

        // var yerine User'a cast et
        var user = this.userService.getUserById(Integer.parseInt(gameDto.getUserId()));
        var robot = "Robot Number 1";  // robotService yap model numarasına göre robotu al

        boolean isUserWhite = true;
        if(gameDto.getColorChoice().equals("random")){
            isUserWhite = new Random().nextBoolean();
        }
        else if(gameDto.getColorChoice().equals("black")){
            isUserWhite = false;
        }

        Game game = Game.builder()
                .rated(true)
                .time(gameDto.getTime())
                //.whitePlayer(isUserWhite ? user : robot)
                //.blackPlayer(isUserWhite ? robot : user)
                .createdAt(new Date())
                .build();

        gameDao.save(game);
        return new SuccessResult(GameMessages.gameInitializedSuccessfully);
    }
}
