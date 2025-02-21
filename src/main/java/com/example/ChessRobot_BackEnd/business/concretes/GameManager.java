package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.GameService;
import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.business.constants.GameMessages;
import com.example.ChessRobot_BackEnd.core.utilities.results.*;
import com.example.ChessRobot_BackEnd.dataAccess.abstracts.GameDao;
import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import com.example.ChessRobot_BackEnd.entity.concretes.Match;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.MoveDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.PlayDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GameManager implements GameService {

    private final GameDao gameDao;
    private final UserService userService;
    private final MoveService moveService;

    @Autowired
    public GameManager(GameDao gameDao, UserService userService, MoveService moveService) {
        super();
        this.gameDao = gameDao;
        this.userService = userService;
        this.moveService = moveService;
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

    @Override
    public DataResult<Game> play(PlayDto playDto) {
        if (playDto.getUserId().isEmpty() || playDto.getGameId().isEmpty()) {
            return new ErrorDataResult<>(GameMessages.authorizationFailed);
        }

        if(playDto.getPieceStartSquare().getRow() > 7 || playDto.getPieceStartSquare().getRow() < 0 ||
                playDto.getPieceStartSquare().getCol() > 7 || playDto.getPieceStartSquare().getCol() < 0 ||
                playDto.getPieceEndSquare().getRow() > 7 || playDto.getPieceEndSquare().getRow() < 0 ||
                playDto.getPieceEndSquare().getCol() > 7 || playDto.getPieceEndSquare().getCol() < 0){
            return new ErrorDataResult<>(GameMessages.moveIsNotPossible);
        }

        // kullanıcı ve game id ile bu maç aktif mi ve doğru kullanıcı mı diye bak
        // renge göre kendi taşı mı diye bak

        Game game = null;
        //Game game = gameService.getGameById(playDto.getGameId());
        Match match = game.getMatch();
        DataResult<MoveDto> moveDataResult = this.moveService.isMovePossible(match, playDto.getPieceStartSquare(), playDto.getPieceEndSquare());
        if(!moveDataResult.isSuccess()){
            return new ErrorDataResult<>(GameMessages.moveIsNotPossible);
        }

        MoveDto move = moveDataResult.getData();
        DataResult<Game> gameDataResult = this.moveService.play(game, move);
        if(!gameDataResult.isSuccess()){
            return new ErrorDataResult<>(GameMessages.unexpectedErrorOccurred);
        }

        return new SuccessDataResult<>(game, GameMessages.movePlayed);
    }
}
