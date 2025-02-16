package com.example.ChessRobot_BackEnd.dataAccess.abstracts;

import com.example.ChessRobot_BackEnd.entity.concretes.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameDao extends JpaRepository<Game, Integer> {

}
