package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.business.constants.UserMessages;
import com.example.ChessRobot_BackEnd.core.utilities.results.*;
import com.example.ChessRobot_BackEnd.dataAccess.abstracts.UserDao;
import com.example.ChessRobot_BackEnd.entity.concretes.Role;
import com.example.ChessRobot_BackEnd.entity.concretes.User;
import com.example.ChessRobot_BackEnd.entity.dtos.User.CreateUserDto;
import com.example.ChessRobot_BackEnd.entity.dtos.User.GetUserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserManager implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserManager(UserDao userDao) {
        super();
        this.userDao = userDao;
    }

    @Override
    public DataResult<List<GetUserDto>> getAllUsers() {
        List<User> userList = userDao.findAll();

        if (userList.isEmpty()) {
            return new ErrorDataResult<>(UserMessages.usersNotFound);
        }

        List<GetUserDto> userDtoList = new ArrayList<>();

        for (User user : userList) {
            GetUserDto userDto = new GetUserDto();
            BeanUtils.copyProperties(user, userDto);
            userDtoList.add(userDto);
        }

        return new SuccessDataResult<>(userDtoList, UserMessages.usersBroughtSuccessfully);
    }

    @Override
    public DataResult<GetUserDto> getUserById(int id) {
        var result = userDao.findById(id);

        if (result.isEmpty()) {
            return new ErrorDataResult<>(UserMessages.userNotFound);
        }

        GetUserDto userDto = new GetUserDto();
        BeanUtils.copyProperties(result.get(), userDto);

        return new SuccessDataResult<>(userDto, UserMessages.userBroughtSuccessfully);
    }

    @Override
    public DataResult<GetUserDto> getUserByEmail(String email) {
        var result = userDao.getUserByEmail(email);

        if (result == null) {
            return new ErrorDataResult<>(UserMessages.userNotFound);
        }

        GetUserDto userDto = new GetUserDto();
        BeanUtils.copyProperties(result, userDto);

        return new SuccessDataResult<>(userDto, UserMessages.userBroughtSuccessfully);
    }

    @Override
    public DataResult<GetUserDto> getUserByUsername(String username) {
        var result = userDao.getUserByUsername(username);

        if (result == null) {
            return new ErrorDataResult<>(UserMessages.userNotFound);
        }

        GetUserDto userDto = new GetUserDto();
        BeanUtils.copyProperties(result, userDto);

        return new SuccessDataResult<>(userDto, UserMessages.userBroughtSuccessfully);
    }

    @Override
    public Result addUser(CreateUserDto userDto) {
        if (userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty() || userDto.getUsername().isEmpty()) {
            return new ErrorResult(UserMessages.userCouldNotBeAdded);
        }

        if (userDao.existsByUsername(userDto.getUsername())) {
            return new ErrorResult(UserMessages.usernameAlreadyExists);
        }

        if (userDao.existsByEmail(userDto.getEmail())) {
            return new ErrorResult(UserMessages.emailAlreadyExists);
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .authorities(Set.of(Role.ROLE_USER))
                .password(userDto.getPassword())
                .build();

        userDao.save(user);
        return new SuccessResult(UserMessages.userAddedSuccessfully);
    }

    @Override
    public Result deleteUser(int id) {
        var result = userDao.findById(id);

        if (result.isEmpty()) {
            return new ErrorResult(UserMessages.userNotFound);
        }

        userDao.delete(result.get());
        return new SuccessResult(UserMessages.userDeletedSuccessfully);
    }

    @Override
    public Result updateUser(CreateUserDto userDto, int id) {
        var result = userDao.findById(id);

        if (result.isEmpty()) {
            return new ErrorResult(UserMessages.userNotFound);
        }

        if (userDto.getEmail().isEmpty() || userDto.getPassword().isEmpty() || userDto.getUsername().isEmpty()) {
            return new ErrorResult(UserMessages.userCouldNotBeUpdated);
        }

        if (userDao.existsByUsername(userDto.getUsername())) {
            return new ErrorResult(UserMessages.usernameAlreadyExists);
        }

        if (userDao.existsByEmail(userDto.getEmail())) {
            return new ErrorResult(UserMessages.emailAlreadyExists);
        }

        result.get().setEmail(userDto.getEmail());
        result.get().setPassword(userDto.getPassword());
        result.get().setUsername(userDto.getUsername());

        return new SuccessResult(UserMessages.userUpdatedSuccessfully);
    }

    @Override
    public Result addUserToGame(int userId, int gameId) {
        // this method will be handled when Game Service is added to the project
        return null;
    }

    @Override
    public Result addModerator(int id) {
        return null;
    }

    @Override
    public Result removeModerator(int id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByUsername(username);
    }
}
