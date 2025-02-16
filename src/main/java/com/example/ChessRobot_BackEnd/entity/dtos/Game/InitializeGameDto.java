package com.example.ChessRobot_BackEnd.entity.dtos.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InitializeGameDto {
    private String userId;

    private String time;

    private String type;  // robot or person

    private String robotChoice;  // which model

    private String colorChoice;
}
