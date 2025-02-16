package com.example.ChessRobot_BackEnd.wepApi.controllers;

import com.example.ChessRobot_BackEnd.business.abstracts.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webApi/games")
public class KeyController {
    private final KeyService keyService;

    @Autowired
    KeyController(KeyService keyService) {
        super();
        this.keyService = keyService;
    }

    @GetMapping("/getBackendPublicKey")
    public ResponseEntity<?> getBackendPublicKey() {
        var result = keyService.getBackendPublicKey();

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }
}
