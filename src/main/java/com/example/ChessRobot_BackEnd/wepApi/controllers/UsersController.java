package com.example.ChessRobot_BackEnd.wepApi.controllers;

import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.entity.dtos.User.CreateUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("webApi/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    UsersController(UserService userService) {
        super();
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        var result = userService.getAllUsers();

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody CreateUserDto createUserDto) {
        var result = userService.addUser(createUserDto);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "id") Integer userId) {
        var result = userService.getUserById(userId);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable(name = "username") String username) {
        var result = userService.getUserByUsername(username);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@RequestBody CreateUserDto userDto, @PathVariable int id) {
        var result = userService.updateUser(userDto, id);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        var result = userService.deleteUser(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable(name = "email") String email) {
        var result = userService.getUserByEmail(email);

        if (result.isSuccess()) {
            return ResponseEntity.ok().body(result);
        }

        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/addUserToGame")
    public ResponseEntity<?> addUserToGame(@RequestParam(name = "userId") int userId, @RequestParam(name = "gameId") int gameId) {
        // this method will be handled when Game Service is added to the project
        return null;
    }
}
