package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.ErrorDataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.SuccessDataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

public class MoveManager implements MoveService {

    @Autowired
    public MoveManager() {
        super();
    }

    @Override
    public DataResult<Move> isMovePossible(Match match, Square pieceStartSquare, Square pieceEndSquare) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        byte[][] board = mapBoardMatrix(match.getBoardMatrix());
        if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == 0){
            return new ErrorDataResult<>();
        }

        DataResult<Move> result = getMove(board, pieceStartSquare, pieceEndSquare);
        if(!result.isSuccess()){
            return new ErrorDataResult<>();
        }

        boolean isWhitePlaying = board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] <= 7;
        if(isWhitePlaying){
            if(match.isCheck()){
                if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.WHITE_KING.getValue()){  // playing with the king

                }
                else{
                    if(match.getBlackCheckers().startsWith("-1", 9)){  // not double-check
                        if(doesItBlockCheck() || doesItTakeChecker()){
                            return new SuccessDataResult<>(result.getData());
                        }
                        return new ErrorDataResult<>();
                    }
                    else{
                        return new ErrorDataResult<>();
                    }
                }
            }
            else{
                if(isPinned()){
                    return new ErrorDataResult<>();
                }
                else{
                    return new SuccessDataResult<>(result.getData());
                }
            }
        }
        else{
            if(match.isCheck()){
                if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.BLACK_KING.getValue()){  // playing with the king

                }
                else{
                    if(match.getWhiteCheckers().startsWith("-1", 9)){  // not double-check
                        if(doesItBlockCheck() || doesItTakeChecker()){
                            return new SuccessDataResult<>(result.getData());
                        }
                        return new ErrorDataResult<>();
                    }
                    else{
                        return new ErrorDataResult<>();
                    }
                }
            }
            else{

            }
        }

        return new ErrorDataResult<>();
    }

    @Override
    public DataResult<Game> play(Game game, Move move) {
        return null;
    }

    private DataResult<Move> getMove(byte[][] board, Square start, Square end){
        boolean isValid = false;
        Move move = new Move();
        move.setRow(end.getRow());
        move.setCol(end.getCol());
        move.setMessage("");
        int rowDiff;
        int colDiff;
        int opponentKingValue = board[start.getRow()][start.getCol()] <= ChessPiece.WHITE_KING.getValue() ? ChessPiece.BLACK_KING.getValue() : ChessPiece.WHITE_KING.getValue();
        switch (board[start.getRow()][start.getCol()]){
            case 1:
                if(start.getCol() == end.getCol() && start.getRow() - 1 == end.getRow()){  // 1 square push
                    if(end.getRow() == 0){  // upgrade
                        move.setMessage("Upgrade");
                    }
                    isValid = true;
                }
                else if(start.getCol() == end.getCol() && start.getRow() - 2 == end.getRow() &&
                        start.getRow() == 6 && board[start.getRow()-1][start.getCol()] == 0 &&
                        board[end.getRow()][end.getCol()] == 0){  // two square push
                    move.setMessage("Two square push");
                    isValid = true;
                }
                else if(start.getRow() - 1 == end.getRow() && board[end.getRow()][end.getCol()] >= ChessPiece.BLACK_PAWN.getValue() &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){    // eating piece to left or right
                    isValid = true;
                }
                else if(start.getRow() - 1 == end.getRow() && board[end.getRow()+1][end.getCol()] == ChessPiece.BLACK_TWO_PAWN.getValue() &&
                        (start.getCol() - 1 == end.getCol()  || start.getCol() + 1 == end.getCol())){    //  en passant to left or right
                    move.setMessage("En passant " + end.getRow() + 1 + "," + end.getCol());
                    isValid = true;
                }
                break;
            case 2:
                if(start.getCol() == end.getCol() && start.getRow() - 1 == end.getRow()){  // 1 square push
                    isValid = true;
                }
                else if(start.getRow() - 1 == end.getRow() && board[end.getRow()][end.getCol()] >= ChessPiece.BLACK_PAWN.getValue() &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){    // eating piece to left or right
                    isValid = true;
                }
                break;
            case 3:
                break;
            case 4:
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if((rowDiff == colDiff || rowDiff == -colDiff) &&                                    // in a diagonal and
                        (board[end.getRow()][end.getCol()] == 0 ||                                   // empty or
                                (board[end.getRow()][end.getCol()] >= 8 &&                           // has black piece
                                        board[end.getRow()][end.getCol()] != opponentKingValue))){   // other than king
                    int row = start.getRow();
                    int col = start.getCol();
                    if(rowDiff > 0 && colDiff > 0){  // end is on the upper left diagonal of start
                        row--;
                        col--;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row--;
                            col--;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff > 0 && colDiff < 0) {  // end is on the upper right diagonal of start
                        row--;
                        col++;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row--;
                            col++;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff < 0 && colDiff > 0) {  // end is on the lower left diagonal of start
                        row++;
                        col--;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row++;
                            col--;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff < 0 && colDiff < 0) {  // end is on the lower right diagonal of start
                        row++;
                        col++;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row++;
                            col++;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                }
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                if(start.getCol() == end.getCol() && start.getRow() + 1 == end.getRow()){  // 1 square push
                    if(end.getRow() == 7){  // upgrade
                        move.setMessage("Upgrade");
                    }
                    isValid = true;
                }
                else if(start.getCol() == end.getCol() && start.getRow() + 2 == end.getRow() &&
                        start.getRow() == 1 && board[start.getRow()+1][start.getCol()] == 0 &&
                        board[end.getRow()][end.getCol()] == 0){  // two square push
                    move.setMessage("Two square push");
                    isValid = true;
                }
                else if(start.getRow() + 1 == end.getRow() && board[end.getRow()][end.getCol()] < ChessPiece.WHITE_KING.getValue() && board[end.getRow()][end.getCol()] != 0 &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){     // eating piece to left or right
                    isValid = true;
                }
                else if(start.getRow() + 1 == end.getRow() && board[end.getRow()+1][end.getCol()] == ChessPiece.WHITE_TWO_PAWN.getValue() &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){   //  en passant to left or right
                    move.setMessage("En passant " + end.getRow() + 1 + "," + end.getCol());
                    isValid = true;
                }
                break;
            case 9:
                if(start.getCol() == end.getCol() && start.getRow() + 1 == end.getRow()){  // 1 square push
                    isValid = true;
                }
                else if(start.getRow() + 1 == end.getRow() && board[end.getRow()][end.getCol()] < ChessPiece.WHITE_KING.getValue() && board[end.getRow()][end.getCol()] != 0 &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){     // eating piece to left or right
                    isValid = true;
                }
                break;
            case 10:
                break;
            case 11:
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if((rowDiff == colDiff || rowDiff == -colDiff) &&                                                   // in a diagonal and
                        (board[end.getRow()][end.getCol()] <= 7 &&                                                  // empty or has white piece
                                board[end.getRow()][end.getCol()] != ChessPiece.WHITE_KING.getValue())){   // other than king
                    int row = start.getRow();
                    int col = start.getCol();
                    if(rowDiff > 0 && colDiff > 0){  // end is on the upper left diagonal of start
                        row--;
                        col--;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row--;
                            col--;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff > 0 && colDiff < 0) {  // end is on the upper right diagonal of start
                        row--;
                        col++;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row--;
                            col++;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff < 0 && colDiff > 0) {  // end is on the lower left diagonal of start
                        row++;
                        col--;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row++;
                            col--;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                    else if(rowDiff < 0 && colDiff < 0) {  // end is on the lower right diagonal of start
                        row++;
                        col++;
                        while(row != end.getRow()){
                            if(board[row][col] != 0){  // piece between end and start
                                break;
                            }
                            row++;
                            col++;
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                }
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
        }

        if(isValid){
            return new SuccessDataResult<>(move);
        }
        else{
            return new ErrorDataResult<>();
        }
    }

    private boolean doesItBlockCheck(){
        return false;
    }

    private boolean doesItTakeChecker(){
        return false;
    }

    private boolean isPinned(){
        return false;
    }

    private static byte[][] mapBoardMatrix(String boardMatrix) {
        byte[][] board = new byte[8][8];
        String[] pieces = boardMatrix.split("-");

        if (pieces.length != 64) {
            throw new IllegalArgumentException("Geçersiz tahta verisi, 64 parça olmalı.");
        }

        for (int i = 0; i < 64; i++) {
            int row = i / 8;
            int col = i % 8;
            board[row][col] = Byte.parseByte(pieces[i]);
        }

        return board;
    }
}
