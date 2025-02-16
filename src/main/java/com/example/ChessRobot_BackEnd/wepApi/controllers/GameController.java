package com.example.ChessRobot_BackEnd.wepApi.controllers;

import com.example.ChessRobot_BackEnd.business.abstracts.GameService;
import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.InitializeGameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.PlayDto;
import com.example.ChessRobot_BackEnd.entity.dtos.User.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webApi/games")
public class GameController {
    private final GameService gameService;

    @Autowired
    GameController(GameService gameService) {
        super();
        this.gameService = gameService;
    }

    @PostMapping("/addGame")
    public ResponseEntity<?> addGame(@RequestBody InitializeGameDto initializeGameDto) {
        var result = gameService.addGame(initializeGameDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/play")
    public ResponseEntity<?> play(@RequestBody PlayDto playDto) {
        var result = gameService.play(playDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }
}
