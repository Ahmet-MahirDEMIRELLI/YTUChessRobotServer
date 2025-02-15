package com.example.ChessRobot_BackEnd.business.abstracts;

import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.Result;
import com.example.ChessRobot_BackEnd.entity.dtos.User.CreateUserDto;
import com.example.ChessRobot_BackEnd.entity.dtos.User.GetUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    DataResult<List<GetUserDto>> getAllUsers();

    DataResult<GetUserDto> getUserById(int id);

    DataResult<GetUserDto> getUserByEmail(String email);

    DataResult<GetUserDto> getUserByUsername(String username);

    Result addUser(CreateUserDto userDto);

    Result deleteUser(int id);

    Result updateUser(CreateUserDto userDto, int id);

    Result addUserToGame(int userId, int gameId);

    Result addModerator(int id);

    Result removeModerator(int id);
}
