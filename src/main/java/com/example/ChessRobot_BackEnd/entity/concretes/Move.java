package com.example.ChessRobot_BackEnd.entity.concretes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Move {
    private short row;
    private short col;
    private String message;
}
