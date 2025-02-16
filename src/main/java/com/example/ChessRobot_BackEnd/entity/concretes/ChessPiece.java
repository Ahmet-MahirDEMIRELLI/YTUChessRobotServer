package com.example.ChessRobot_BackEnd.entity.concretes;

import lombok.Getter;

@Getter
public enum ChessPiece {
    WHITE_PAWN(1),
    WHITE_TWO_PAWN(2),
    WHITE_KNIGHT(3),
    WHITE_BISHOP(4),
    WHITE_ROOK(5),
    WHITE_QUEEN(6),
    WHITE_KING(7),
    BLACK_PAWN(8),
    BLACK_TWO_PAWN(9),
    BLACK_KNIGHT(10),
    BLACK_BISHOP(11),
    BLACK_ROOK(12),
    BLACK_QUEEN(13),
    BLACK_KING(14);


    private final int value;

    ChessPiece(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ChessPiece fromValue(int value) {
        for (ChessPiece piece : values()) {
            if (piece.value == value) {
                return piece;
            }
        }
        throw new IllegalArgumentException("Invalid piece value: " + value);
    }
}
