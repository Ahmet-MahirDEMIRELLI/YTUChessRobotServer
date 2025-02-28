package com.example.ChessRobot_BackEnd.entity.dtos.Game;

import com.example.ChessRobot_BackEnd.entity.concretes.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class GameDto {
    private int id;
    private boolean rated;
    private String time;
    private UUID whitePlayerId;
    private UUID blackPlayerId;
    private Date startedAt;
    private String whiteMoves = "";
    private String blackMoves = "";
    private GameStatus gameStatus;
    // Match fields
    private short moveCount = 0;
    private short fiftyMoveCounter = 0;
    private ArrayList<String> positionHashes = new ArrayList<>();
    private short whiteRemainingSeconds;
    private short blackRemainingSeconds;
    private Date lastWhiteMoveDateTime;
    private Date lastBlackMoveDateTime;
    private byte[][] board = {
            { 12, 10, 11, 13, 14, 11, 10, 12 },
            {  8,  8,  8,  8,  8,  8,  8,  8 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  0,  0,  0,  0,  0,  0,  0,  0 },
            {  1,  1,  1,  1,  1,  1,  1,  1 },
            {  5,  3,  4,  6,  7,  4,  3,  5 }
    };
    private boolean isCheck = false;
    private boolean isMate = false;
    private boolean isWhiteKingMoved = false;
    private boolean isBlackKingMoved = false;
    private boolean isWhiteShortRookMoved = false;
    private boolean isWhiteLongRookMoved = false;
    private boolean isBlackShortRookMoved = false;
    private boolean isBlackLongRookMoved = false;
    private SquareDto whiteKingPosition = new SquareDto((byte)7, (byte)4);
    private SquareDto blackKingPosition = new SquareDto((byte)0, (byte)4);
    private SquareDto[] whiteCheckers = new SquareDto[]{new SquareDto((byte)-1, (byte)-1), new SquareDto((byte)-1, (byte)-1)};
    private SquareDto[] blackCheckers = new SquareDto[]{new SquareDto((byte)-1, (byte)-1), new SquareDto((byte)-1, (byte)-1)};
    private int whitesPoints = 39;
    private int blacksPoints = 39;
}
