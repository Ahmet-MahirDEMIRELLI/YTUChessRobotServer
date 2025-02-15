package com.example.ChessRobot_BackEnd.dataAccess.abstracts;

import com.example.ChessRobot_BackEnd.entity.concretes.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User getUserByEmail(String email);

    User getUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
