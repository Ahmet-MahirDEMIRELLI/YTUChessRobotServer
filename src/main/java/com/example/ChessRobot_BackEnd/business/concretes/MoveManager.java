package com.example.ChessRobot_BackEnd.business.concretes;

import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.business.abstracts.ThreadCheckService;
import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.ErrorDataResult;
import com.example.ChessRobot_BackEnd.core.utilities.results.SuccessDataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.*;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.GameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.LightGameDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.MoveDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;

public class MoveManager implements MoveService {
    private final ThreadCheckService threadCheckService;

    @Autowired
    public MoveManager(ThreadCheckService threadCheckService) {
        super();
        this.threadCheckService = threadCheckService;
    }

    @Override
    public DataResult<MoveDto> isMovePossible(GameDto game, SquareDto pieceStartSquare, SquareDto pieceEndSquare) {
        ArrayList<MoveDto> possibleMoves = new ArrayList<>();
        byte[][] board = game.getBoard();
        if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == 0){
            return new ErrorDataResult<>();
        }
        boolean isWhitePlaying = board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] <= 7;
        LightGameDto matchDto = LightGameDto.builder()
                .isKingMoved(isWhitePlaying ? game.isWhiteKingMoved() : game.isBlackKingMoved())
                .isShortRookMoved(isWhitePlaying ? game.isWhiteShortRookMoved() : game.isBlackShortRookMoved())
                .isLongRookMoved(isWhitePlaying ? game.isWhiteLongRookMoved() : game.isBlackLongRookMoved())
                .build();
        DataResult<MoveDto> result = getMove(matchDto, board, pieceStartSquare, pieceEndSquare);
        if(!result.isSuccess()){
            return new ErrorDataResult<>();
        }


        if(isWhitePlaying){
            if(game.isCheck()){
                if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.WHITE_KING.getValue()){  // playing with the king

                }
                else{
                    if(game.getBlackCheckers()[1].getRow() == -1){  // not double-check
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
            if(game.isCheck()){
                if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.BLACK_KING.getValue()){  // playing with the king

                }
                else{
                    if(game.getWhiteCheckers()[1].getRow() == -1){  // not double-check
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
    public DataResult<GameDto> play(GameDto game, MoveDto move) {
        return null;
    }

    private DataResult<MoveDto> getMove(LightGameDto matchDto, byte[][] board, SquareDto start, SquareDto end){
        boolean isValid = false;
        MoveDto move = new MoveDto();
        move.setRow(end.getRow());
        move.setCol(end.getCol());
        move.setMessage("");
        int rowDiff;
        int colDiff;
        switch (board[start.getRow()][start.getCol()]){
            case 1:  // white pawn
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
            case 2:  // white pawn(pushed two square)
                if(start.getCol() == end.getCol() && start.getRow() - 1 == end.getRow()){  // 1 square push
                    isValid = true;
                }
                else if(start.getRow() - 1 == end.getRow() && board[end.getRow()][end.getCol()] >= ChessPiece.BLACK_PAWN.getValue() &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){    // eating piece to left or right
                    isValid = true;
                }
                break;
            case 3:  // white knight
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                rowDiff = rowDiff < 0 ? -rowDiff : rowDiff;
                colDiff = colDiff < 0 ? -colDiff : colDiff;
                if(((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1)) &&                           // in L
                        (board[end.getRow()][end.getCol()] == 0 ||                                                 // empty or
                                (board[end.getRow()][end.getCol()] >= 8 &&                                         // has black piece
                                        board[end.getRow()][end.getCol()] != ChessPiece.BLACK_KING.getValue()))){  // other than king
                    isValid = true;
                }
                break;
            case 4:  // white bishop
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if((rowDiff == colDiff || rowDiff == -colDiff) &&                                              // in a diagonal and
                        (board[end.getRow()][end.getCol()] == 0 ||                                               // empty or
                                (board[end.getRow()][end.getCol()] >= 8 &&                                       // has black piece
                                        board[end.getRow()][end.getCol()] != ChessPiece.BLACK_KING.getValue()))){   // other than king
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
            case 5:  // white rook
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if(rowDiff == 0 || colDiff == 0 &&                                                          // in vertical or horizontal
                        (board[end.getRow()][end.getCol()] == 0 ||                                          // empty or
                            (board[end.getRow()][end.getCol()] >= 8 &&                                      // has black piece
                                board[end.getRow()][end.getCol()] != ChessPiece.BLACK_KING.getValue()))){   // other than king
                   if(rowDiff == 0){  // playing horizontal
                       int col = start.getCol();
                       if(colDiff > 0){  // end is on the left of start
                           col--;
                           while(col != end.getCol()){
                               if(board[end.getRow()][col] != 0){  // piece between end and start
                                   break;
                               }
                               col--;
                           }
                       }
                       else{
                           col++;
                           while(col != end.getCol()){
                               if(board[end.getRow()][col] != 0){  // piece between end and start
                                   break;
                               }
                               col++;
                           }
                       }
                       if(col == end.getCol()){
                           isValid = true;
                       }
                   }
                   else{
                       int row = start.getRow();
                       if(rowDiff > 0){  // end is on the up of start
                           row--;
                           while(row != end.getRow()){
                               if(board[row][end.getCol()] != 0){  // piece between end and start
                                   break;
                               }
                               row--;
                           }
                       }
                       else{
                           row++;
                           while(row != end.getRow()){
                               if(board[row][end.getCol()] != 0){  // piece between end and start
                                   break;
                               }
                               row++;
                           }
                       }
                       if(row == end.getRow()){
                           isValid = true;
                       }
                   }
                }
                break;
            case 6:  // white queen
                if(board[end.getRow()][end.getCol()] == 0 ||                                               // empty or
                       (board[end.getRow()][end.getCol()] >= 8 &&                                          // has black piece
                               board[end.getRow()][end.getCol()] != ChessPiece.BLACK_KING.getValue())) {    // other than king
                    rowDiff = start.getRow() - end.getRow();
                    colDiff = start.getCol() - end.getCol();
                    if(rowDiff == colDiff || rowDiff == -colDiff){  // bishop move
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
                    else if(rowDiff == 0 || colDiff == 0){  // rook move
                        if(rowDiff == 0){  // playing horizontal
                            int col = start.getCol();
                            if(colDiff > 0){  // end is on the left of start
                                col--;
                                while(col != end.getCol()){
                                    if(board[end.getRow()][col] != 0){  // piece between end and start
                                        break;
                                    }
                                    col--;
                                }
                            }
                            else{
                                col++;
                                while(col != end.getCol()){
                                    if(board[end.getRow()][col] != 0){  // piece between end and start
                                        break;
                                    }
                                    col++;
                                }
                            }
                            if(col == end.getCol()){
                                isValid = true;
                            }
                        }
                        else{
                            int row = start.getRow();
                            if(rowDiff > 0){  // end is on the up of start
                                row--;
                                while(row != end.getRow()){
                                    if(board[row][end.getCol()] != 0){  // piece between end and start
                                        break;
                                    }
                                    row--;
                                }
                            }
                            else{
                                row++;
                                while(row != end.getRow()){
                                    if(board[row][end.getCol()] != 0){  // piece between end and start
                                        break;
                                    }
                                    row++;
                                }
                            }
                            if(row == end.getRow()){
                                isValid = true;
                            }
                        }
                    }
                }
                break;
            case 7:  // white king
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if(rowDiff == 1 || rowDiff == -1 || colDiff == 1 || colDiff == -1){ // normal king move
                    if(!threadCheckService.isSquaresUnderThread(board, new SquareDto[]{new SquareDto(end.getRow(), end.getCol())}, true)){
                        move.setMessage("White King Moved");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 7 && start.getCol() == 4 && end.getRow() == 7 && end.getCol() == 6){  // short castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 7,( byte) 5), new SquareDto((byte)7, (byte)6)};
                    if(!matchDto.isKingMoved() && !matchDto.isShortRookMoved() && board[7][5] == 0 && board[7][6] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, true)){
                        move.setMessage("White Short Castle");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 7 && start.getCol() == 4 && end.getRow() == 7 && end.getCol() == 2){  // long castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 7,( byte) 3), new SquareDto((byte)7, (byte)2)};
                    if(!matchDto.isKingMoved() && !matchDto.isLongRookMoved() && board[7][3] == 0 && board[7][2] == 0 && board[7][1] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, true)){
                        move.setMessage("White Long Castle");
                        isValid = true;
                    }
                }
                break;
            case 8:  // black pawn
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
            case 9:  // black pawn(pushed 2 square)
                if(start.getCol() == end.getCol() && start.getRow() + 1 == end.getRow()){  // 1 square push
                    isValid = true;
                }
                else if(start.getRow() + 1 == end.getRow() && board[end.getRow()][end.getCol()] < ChessPiece.WHITE_KING.getValue() && board[end.getRow()][end.getCol()] != 0 &&
                        (start.getCol() - 1 == end.getCol() || start.getCol() + 1 == end.getCol())){     // eating piece to left or right
                    isValid = true;
                }
                break;
            case 10:  // black knight
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                rowDiff = rowDiff < 0 ? -rowDiff : rowDiff;
                colDiff = colDiff < 0 ? -colDiff : colDiff;
                if(((rowDiff == 1 && colDiff == 2) || (rowDiff == 2 && colDiff == 1)) &&                   // in L
                        (board[end.getRow()][end.getCol()] <= 7 &&                                         // empty or has white piece
                                board[end.getRow()][end.getCol()] != ChessPiece.WHITE_KING.getValue())){   // other than king
                    isValid = true;
                }
                break;
            case 11:  // black bishop
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if((rowDiff == colDiff || rowDiff == -colDiff) &&                                       // in a diagonal and
                        (board[end.getRow()][end.getCol()] <= 7 &&                                      // empty or has white piece
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
            case 12:  // black rook
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if(rowDiff == 0 || colDiff == 0 &&                                                          // in vertical or horizontal
                        (board[end.getRow()][end.getCol()] <= 7 &&                                          // empty or has white piece
                             board[end.getRow()][end.getCol()] != ChessPiece.WHITE_KING.getValue())){    // other than king
                    if(rowDiff == 0){  // playing horizontal
                        int col = start.getCol();
                        if(colDiff > 0){  // end is on the left of start
                            col--;
                            while(col != end.getCol()){
                                if(board[end.getRow()][col] != 0){  // piece between end and start
                                    break;
                                }
                                col--;
                            }
                        }
                        else{
                            col++;
                            while(col != end.getCol()){
                                if(board[end.getRow()][col] != 0){  // piece between end and start
                                    break;
                                }
                                col++;
                            }
                        }
                        if(col == end.getCol()){
                            isValid = true;
                        }
                    }
                    else{
                        int row = start.getRow();
                        if(rowDiff > 0){  // end is on the up of start
                            row--;
                            while(row != end.getRow()){
                                if(board[row][end.getCol()] != 0){  // piece between end and start
                                    break;
                                }
                                row--;
                            }
                        }
                        else{
                            row++;
                            while(row != end.getRow()){
                                if(board[row][end.getCol()] != 0){  // piece between end and start
                                    break;
                                }
                                row++;
                            }
                        }
                        if(row == end.getRow()){
                            isValid = true;
                        }
                    }
                }
                break;
            case 13:  // black queen
                if(board[end.getRow()][end.getCol()] <= 7 &&                                        // empty or has white piece
                        board[end.getRow()][end.getCol()] != ChessPiece.WHITE_KING.getValue()) {    // other than king
                    rowDiff = start.getRow() - end.getRow();
                    colDiff = start.getCol() - end.getCol();
                    if(rowDiff == colDiff || rowDiff == -colDiff){  // bishop move
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
                    else if(rowDiff == 0 || colDiff == 0){  // rook move
                        if(rowDiff == 0){  // playing horizontal
                            int col = start.getCol();
                            if(colDiff > 0){  // end is on the left of start
                                col--;
                                while(col != end.getCol()){
                                    if(board[end.getRow()][col] != 0){  // piece between end and start
                                        break;
                                    }
                                    col--;
                                }
                            }
                            else{
                                col++;
                                while(col != end.getCol()){
                                    if(board[end.getRow()][col] != 0){  // piece between end and start
                                        break;
                                    }
                                    col++;
                                }
                            }
                            if(col == end.getCol()){
                                isValid = true;
                            }
                        }
                        else{
                            int row = start.getRow();
                            if(rowDiff > 0){  // end is on the up of start
                                row--;
                                while(row != end.getRow()){
                                    if(board[row][end.getCol()] != 0){  // piece between end and start
                                        break;
                                    }
                                    row--;
                                }
                            }
                            else{
                                row++;
                                while(row != end.getRow()){
                                    if(board[row][end.getCol()] != 0){  // piece between end and start
                                        break;
                                    }
                                    row++;
                                }
                            }
                            if(row == end.getRow()){
                                isValid = true;
                            }
                        }
                    }
                }
                break;
            case 14:  // black king
                rowDiff = start.getRow() - end.getRow();
                colDiff = start.getCol() - end.getCol();
                if(rowDiff == 1 || rowDiff == -1 || colDiff == 1 || colDiff == -1){ // normal king move
                    if(!threadCheckService.isSquaresUnderThread(board, new SquareDto[]{new SquareDto(end.getRow(), end.getCol())}, false)){
                        move.setMessage("Black King Moved");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 0 && start.getCol() == 4 && end.getRow() == 0 && end.getCol() == 6){  // short castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 0,( byte) 5), new SquareDto((byte)0, (byte)6)};
                    if(!matchDto.isKingMoved() && !matchDto.isShortRookMoved() && board[0][5] == 0 && board[0][6] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, false)){
                        move.setMessage("Black Short Castle");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 0 && start.getCol() == 4 && end.getRow() == 0 && end.getCol() == 2){  // long castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 0,( byte) 3), new SquareDto((byte)0, (byte)2)};
                    if(!matchDto.isKingMoved() && !matchDto.isLongRookMoved() && board[0][3] == 0 && board[0][2] == 0 && board[0][1] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, false)){
                        move.setMessage("Black Long Castle");
                        isValid = true;
                    }
                }
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
}
