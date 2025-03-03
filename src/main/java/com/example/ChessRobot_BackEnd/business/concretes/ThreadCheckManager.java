package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.ThreadCheckService;
import com.example.ChessRobot_BackEnd.entity.concretes.ChessPiece;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;
import org.springframework.beans.factory.annotation.Autowired;

public class ThreadCheckManager implements ThreadCheckService {

    @Autowired
    public ThreadCheckManager() {
        super();
    }

    @Override
    public boolean isSquaresUnderThread(byte[][] board, SquareDto[] squaresToCheck, boolean isWhitePlaying) {
        int row = 0;
        int col = 0;
        if(isWhitePlaying){
            for (SquareDto square : squaresToCheck) {
                row = square.getRow() - 1;  // going up
                col = square.getCol();
                if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                    return true;
                }
                while (row >= 0) {
                    if (board[row][col] == 12 || board[row][col] == 13) {  // rook or queen
                        return true;
                    } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                        return false;
                    }
                    row--;
                }

                row = square.getRow() + 1;  // going down
                if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                    return true;
                }
                while (row <= 7) {
                    if (board[row][col] == 12 || board[row][col] == 13) {  // rook or queen
                        return true;
                    } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                        return false;
                    }
                    row++;
                }

                row = square.getRow();  // going right
                col = square.getCol() + 1;
                if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                    return true;
                }
                while (col <= 7) {
                    if (board[row][col] == 12 || board[row][col] == 13) {  // rook or queen
                        return true;
                    } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                        return false;
                    }
                    col++;
                }

                col = square.getCol() - 1; // going left
                if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                    return true;
                }
                while (col >= 0) {
                    if (board[row][col] == 12 || board[row][col] == 13) {  // rook or queen
                        return true;
                    } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                        return false;
                    }
                    col--;
                }

                row = square.getRow() - 1;  // going up-right
                col = square.getCol() + 1;
                if (board[row][col] == ChessPiece.BLACK_KING.getValue() ||          // first check king
                        board[row][col] == ChessPiece.BLACK_PAWN.getValue() ||      // or pawn
                        board[row][col] == ChessPiece.BLACK_TWO_PAWN.getValue()) {
                    return true;
                }
                while (row >= 0 && col <= 7) {
                    if (board[row][col] == 11 || board[row][col] == 13) {  // bishop or queen
                        return true;
                    } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                        return false;
                    }
                    row--;
                    col++;
                }

                row = square.getRow() - 1;  // going up-left
                col = square.getCol() - 1;
                if (board[row][col] == ChessPiece.BLACK_KING.getValue() ||          // first check king
                        board[row][col] == ChessPiece.BLACK_PAWN.getValue() ||      // or pawn
                        board[row][col] == ChessPiece.BLACK_TWO_PAWN.getValue()) {
                    while (row >= 0 && col >= 0) {
                        if (board[row][col] == 11 || board[row][col] == 13) {  // bishop or queen
                            return true;
                        } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                            return false;
                        }
                        row--;
                        col--;
                    }

                    row = square.getRow() + 1;  // going down-left
                    col = square.getCol() - 1;
                    if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                        return true;
                    }
                    while (row <= 7 && col >= 0) {
                        if (board[row][col] == 11 || board[row][col] == 13) {  // bishop or queen
                            return true;
                        } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                            return false;
                        }
                        row++;
                        col--;
                    }

                    row = square.getRow() + 1;  // going down-right
                    col = square.getCol() + 1;
                    if (board[row][col] == ChessPiece.BLACK_KING.getValue()) {  // first check king
                        return true;
                    }
                    while (row <= 7 && col <= 7) {
                        if (board[row][col] == 11 || board[row][col] == 13) {  // bishop or queen
                            return true;
                        } else if (board[row][col] <= 7 && board[row][col] != 0) {  // white piece
                            return false;
                        }
                        row++;
                        col++;
                    }

                    // check knight
                    row = square.getRow() - 2;
                    col = square.getCol() - 1;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() - 2;
                    col = square.getCol() + 1;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() + 2;
                    col = square.getCol() - 1;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() + 2;
                    col = square.getCol() + 1;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() - 1;
                    col = square.getCol() - 2;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() - 1;
                    col = square.getCol() + 2;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() + 1;
                    col = square.getCol() - 2;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                    row = square.getRow() + 1;
                    col = square.getCol() + 2;
                    if(isSquareValid(row, col) && board[row][col] == ChessPiece.BLACK_KNIGHT.getValue()){
                        return true;
                    }
                }
            }
        }
        else{
            for (SquareDto square : squaresToCheck) {
                row = square.getRow() - 1;  // going up
                col = square.getCol();
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (row >= 0) {
                    if (board[row][col] == 5 || board[row][col] == 6) {  // rook or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row--;
                }

                row = square.getRow() + 1;  // going down
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (row <= 7) {
                    if (board[row][col] == 5 || board[row][col] == 6) {  // rook or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row++;
                }

                row = square.getRow();  // going right
                col = square.getCol() + 1;
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (col <= 7) {
                    if (board[row][col] == 5 || board[row][col] == 6) {  // rook or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    col++;
                }

                col = square.getCol() - 1; // going left
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (col >= 0) {
                    if (board[row][col] == 5 || board[row][col] == 6) {  // rook or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    col--;
                }

                row = square.getRow() - 1;  // going up-right
                col = square.getCol() + 1;
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (row >= 0 && col <= 7) {
                    if (board[row][col] == 4 || board[row][col] == 6) {  // bishop or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row--;
                    col++;
                }

                row = square.getRow() - 1;  // going up-left
                col = square.getCol() - 1;
                if (board[row][col] == ChessPiece.WHITE_KING.getValue()) {  // first check king
                    return true;
                }
                while (row >= 0 && col >= 0) {
                    if (board[row][col] == 4 || board[row][col] == 6) {  // bishop or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row--;
                    col--;
                }

                row = square.getRow() + 1;  // going down-left
                col = square.getCol() - 1;
                if (board[row][col] == ChessPiece.WHITE_KING.getValue() ||         // first check king
                        board[row][col] == ChessPiece.WHITE_PAWN.getValue() ||     // or pawn
                        board[row][col] == ChessPiece.WHITE_TWO_PAWN.getValue()) {
                    return true;
                }
                while (row <= 7 && col >= 0) {
                    if (board[row][col] == 4 || board[row][col] == 6) {  // bishop or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row++;
                    col--;
                }

                row = square.getRow() + 1;  // going down-right
                col = square.getCol() + 1;
                if (board[row][col] == ChessPiece.WHITE_KING.getValue() ||         // first check king
                        board[row][col] == ChessPiece.WHITE_PAWN.getValue() ||     // or pawn
                        board[row][col] == ChessPiece.WHITE_TWO_PAWN.getValue()) {
                    return true;
                }
                while (row <= 7 && col <= 7) {
                    if (board[row][col] == 4 || board[row][col] == 6) {  // bishop or queen
                        return true;
                    } else if (board[row][col] >= 8) {  // black piece
                        return false;
                    }
                    row++;
                    col++;
                }

                // check knight
                row = square.getRow() - 2;
                col = square.getCol() - 1;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() - 2;
                col = square.getCol() + 1;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() + 2;
                col = square.getCol() - 1;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() + 2;
                col = square.getCol() + 1;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() - 1;
                col = square.getCol() - 2;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() - 1;
                col = square.getCol() + 2;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() + 1;
                col = square.getCol() - 2;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
                row = square.getRow() + 1;
                col = square.getCol() + 2;
                if(isSquareValid(row, col) && board[row][col] == ChessPiece.WHITE_KNIGHT.getValue()){
                    return true;
                }
            }
        }
        return false;
    }

    /*
    0 -> no trace from piece to king
    1 -> king is on the right side of the piece
    2 -> king is on the left side of the piece
    3 -> king is on the lower side of the piece
    4 -> king is on the upper side of the piece
    5 -> king is on the up-left side of the piece
    6 -> king is on the lower-left side of the piece
    7 -> king is on the lower-right side of the piece
    8 -> king is on the up-right side of the piece
    */
    @Override
    public int traceToKing(byte[][] board, SquareDto king, SquareDto pieceToCheck) {
        int rowDiff = king.getRow() - pieceToCheck.getRow();
        int colDiff = king.getCol() - pieceToCheck.getCol();
        int row = pieceToCheck.getRow();
        int col = pieceToCheck.getCol();
        if(rowDiff == 0 && colDiff != 0){  // same row
            if(colDiff > 0){  // king is on the right side
                col++;
                while (col < king.getCol()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    col++;
                }
                return 1;
            }
            else{             // king is on the left side
                col--;
                while (col > king.getCol()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    col--;
                }
                return 2;
            }
        }
        else if(colDiff == 0 && rowDiff != 0){  // same col
            if(rowDiff > 0){  // king is on the lower side
                row++;
                while (row < king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row++;
                }
                return 3;
            }
            else{  // king is on the upper side
                row--;
                while (row > king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row--;
                }
                return 4;
            }
        }
        else if(rowDiff == colDiff || rowDiff == -colDiff){  // same diagonal
            if(rowDiff > 0 && colDiff > 0){       // king is on the up-left
                row++;
                col++;
                while (row < king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row++;
                    col++;
                }
                return 5;
            }
            else if(rowDiff < 0 && colDiff > 0){  // king is on the lower-left
                row--;
                col++;
                while (row > king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row--;
                    col++;
                }
                return 6;
            }
            else if(rowDiff < 0){    // king is on the lower-right
                row--;
                col--;
                while (row > king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row--;
                    col--;
                }
                return 7;
            }
            else if(rowDiff > 0){    // king is on the up-right
                row++;
                col--;
                while (row < king.getRow()){
                    if(board[row][col] != 0){  // piece in between
                        return 0;
                    }
                    row++;
                    col--;
                }
                return 8;
            }
        }

        return 0;
    }

    private boolean isSquareValid(int row, int col){
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }
}
