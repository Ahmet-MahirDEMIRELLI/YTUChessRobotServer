package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.business.constants.MoveMessages;
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
            return new ErrorDataResult<>(MoveMessages.noPieceToMove);
        }

        boolean isWhitePlaying = board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] <= 7;

        if(match.isCheck()){
            if(isWhitePlaying){
                if(match.getBlackCheckers().startsWith("-1", 9)){  // double check
                    if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.WHITE_KING.getValue()){
                        possibleMoves = getNormalMoves();
                    }
                    else{
                        return new ErrorDataResult<>(MoveMessages.doubleCheck);
                    }
                }
                else{
                    possibleMoves = getBlockingMoves();
                    Move eatingMove = getEatingMove();
                    if(eatingMove != null){
                        possibleMoves.add(eatingMove);
                    }
                }
            }
            else{
                if(match.getWhiteCheckers().startsWith("-1", 9)){  // double check
                    if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.BLACK_KING.getValue()){
                        possibleMoves = getNormalMoves();
                    }
                    else{
                        return new ErrorDataResult<>(MoveMessages.doubleCheck);
                    }
                }
                else{
                    possibleMoves = getBlockingMoves();
                    Move eatingMove = getEatingMove();
                    if(eatingMove != null){
                        possibleMoves.add(eatingMove);
                    }
                }
            }
        }
        else{
            possibleMoves = getNormalMoves();
        }

        if(possibleMoves == null){
            return new ErrorDataResult<>(MoveMessages.noPossibleMove);
        }

        for (Move move : possibleMoves) {
            if (move.getRow() == pieceStartSquare.getRow() && move.getCol() == pieceEndSquare.getCol()) {
                return new SuccessDataResult<>(move, MoveMessages.moveIsPlayable);
            }
        }

        return new ErrorDataResult<>(MoveMessages.moveIsNotPossible);
    }

    @Override
    public DataResult<Game> play(Game game, Move move) {
        return null;
    }

    private ArrayList<Move> getNormalMoves(){
        return null;
    }

    private ArrayList<Move> getBlockingMoves(){
        return null;
    }

    private Move getEatingMove(){
        return null;
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
