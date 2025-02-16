package com.example.ChessRobot_BackEnd.entity.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "move_count")
    private int moveCount = 0;

    @Column(name = "fifty_move_counter")
    private int fiftyMoveCounter = 0;

    @Column(name = "position_hashes", length = 199)
    private String positionHashes = "0-0-0-0-0-0-0-0-0-0";

    @Column(name = "white_remaining_seconds")
    private int whiteRemainingSeconds;

    @Column(name = "black_remaining_seconds")
    private int blackRemainingSeconds;

    @Column(name = "last_white_move_date_time")
    private int lastWhiteMoveDateTime;

    @Column(name = "last_black_move_date_time")
    private int lastBlackMoveDateTime;

    @Column(name = "board_matrix", length = 191)
    private String boardMatrix = "12-10-11-13-14-11-10-12-" +
                                 " 8- 8- 8- 8- 8- 8- 8- 8-" +
                                 " 0- 0- 0- 0- 0- 0- 0- 0-" +
                                 " 0- 0- 0- 0- 0- 0- 0- 0-" +
                                 " 0- 0- 0- 0- 0- 0- 0- 0-" +
                                 " 0- 0- 0- 0- 0- 0- 0- 0-" +
                                 " 1- 1- 1- 1- 1- 1- 1- 1-" +
                                 " 5- 3- 4- 6- 7- 4- 3- 5";

    @Column(name = "is_check")
    private boolean isCheck = false;

    @Column(name = "is_mate")
    private boolean isMate = false;

    @Column(name = "is_white_king_moved")
    private boolean isWhiteKingMoved = false;

    @Column(name = "is_black_king_moved")
    private boolean isBlackKingMoved = false;

    @Column(name = "is_white_short_rook_moved")
    private boolean isWhiteShortRookMoved = false;

    @Column(name = "is_white_long_rook_moved")
    private boolean isWhiteLongRookMoved = false;

    @Column(name = "is_black_short_rook_moved")
    private boolean isBlackShortRookMoved = false;

    @Column(name = "is_black_long_rook_moved")
    private boolean isBlackLongRookMoved = false;

    @Column(name = "white_king_position", length = 3)
    private String whiteKingPosition = "7-4";

    @Column(name = "black_king_position", length = 3)
    private String blackKingPosition = "0-4";

    @Column(name = "white_checkers", length = 15)
    private String whiteCheckers = "[-1,-1],[-1,-1]";

    @Column(name = "black_checkers", length = 15)
    private String blackCheckers = "[-1,-1],[-1,-1]";

    @Column(name = "whites_points")
    private int whitesPoints = 39;

    @Column(name = "blacks_points")
    private int blacksPoints = 39;
}
