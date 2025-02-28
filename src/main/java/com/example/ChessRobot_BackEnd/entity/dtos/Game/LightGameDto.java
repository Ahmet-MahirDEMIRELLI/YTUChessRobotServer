package com.example.ChessRobot_BackEnd.entity.dtos.Game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class LightGameDto {
    boolean isKingMoved;
    boolean isShortRookMoved;
    boolean isLongRookMoved;
}
