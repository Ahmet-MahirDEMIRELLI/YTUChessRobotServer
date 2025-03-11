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

import java.util.ArrayList;

public class MoveManager implements MoveService {
    private final ThreadCheckService threadCheckService;

    @Autowired
    public MoveManager(ThreadCheckService threadCheckService) {
        super();
        this.threadCheckService = threadCheckService;
    }

    @Override
    public DataResult<MoveDto> isMovePossible(GameDto game, SquareDto pieceStartSquare, SquareDto pieceEndSquare) {
        byte[][] board = game.getBoard();
        if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == 0){
            return new ErrorDataResult<>();
        }
        boolean isWhitePlaying = board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] <= 7;
        LightGameDto lightGameDto = LightGameDto.builder()
                .isKingMoved(isWhitePlaying ? game.isWhiteKingMoved() : game.isBlackKingMoved())
                .isShortRookMoved(isWhitePlaying ? game.isWhiteShortRookMoved() : game.isBlackShortRookMoved())
                .isLongRookMoved(isWhitePlaying ? game.isWhiteLongRookMoved() : game.isBlackLongRookMoved())
                .build();
        DataResult<MoveDto> result = getMove(lightGameDto, board, pieceStartSquare, pieceEndSquare);
        if(!result.isSuccess()){
            return new ErrorDataResult<>();
        }

        if(isWhitePlaying){
            if(game.isCheck()){
                if(board[pieceStartSquare.getRow()][pieceStartSquare.getCol()] == ChessPiece.WHITE_KING.getValue()){  // playing with the king
                    return new SuccessDataResult<>(result.getData());
                }
                else{
                    if(game.getBlackCheckers()[1].getRow() == -1){  // not double-check
                        if(doesItBlockCheck(game.getWhiteKingPosition(), game.getBlackCheckers()[0], pieceEndSquare) || doesItTakeChecker(game.getBlackCheckers()[0], pieceEndSquare)){
                            return new SuccessDataResult<>(result.getData());
                        }
                    }
                    return new ErrorDataResult<>();
                }
            }
            else{
                if(isPinned(board, game.getWhiteKingPosition(), pieceStartSquare, pieceEndSquare, true)){
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
                    return new SuccessDataResult<>(result.getData());
                }
                else{
                    if(game.getWhiteCheckers()[1].getRow() == -1){  // not double-check
                        if(doesItBlockCheck(game.getBlackKingPosition(), game.getWhiteCheckers()[0], pieceEndSquare) || doesItTakeChecker(game.getWhiteCheckers()[0], pieceEndSquare)){
                            return new SuccessDataResult<>(result.getData());
                        }
                    }
                    return new ErrorDataResult<>();
                }
            }
            else{
                if(isPinned(board, game.getBlackKingPosition(), pieceStartSquare, pieceEndSquare, false)){
                    return new ErrorDataResult<>();
                }
                else{
                    return new SuccessDataResult<>(result.getData());
                }
            }
        }
    }

    @Override
    public DataResult<GameDto> play(GameDto game, String upgradeChoice, SquareDto pieceStartSquare, MoveDto move) {
        byte[][] newBoard = game.getBoard();
        byte pieceValue = game.getBoard()[pieceStartSquare.getRow()][pieceStartSquare.getCol()];
        String notation = getNotation(pieceValue, pieceStartSquare, move, newBoard[move.getRow()][move.getCol()] != 0, upgradeChoice);

        // first delete the piece from board
        newBoard[pieceStartSquare.getRow()][pieceStartSquare.getCol()] = 0;
        if(pieceValue <= ChessPiece.WHITE_KING.getValue()){
            // Update two pawn if exists
            if(game.getWhiteTwoPawn().getRow() != -1){
                newBoard[game.getWhiteTwoPawn().getRow()][game.getWhiteTwoPawn().getCol()] = (byte)ChessPiece.WHITE_PAWN.getValue();
            }
            // Subtract point if move takes a piece
            if(newBoard[move.getRow()][move.getCol()] != 0){
                switch (newBoard[move.getRow()][move.getCol()]){
                    case 8:
                        game.setBlacksPoints(game.getBlacksPoints() - 1);
                        break;
                    case 9:
                        game.setBlacksPoints(game.getBlacksPoints() - 1);
                        game.setBlackTwoPawn(new SquareDto((byte)-1, (byte)-1));
                        break;
                    case 10:
                    case 11:
                        game.setBlacksPoints(game.getBlacksPoints() - 3);
                        break;
                    case 12:
                        game.setBlacksPoints(game.getBlacksPoints() - 5);
                        break;
                    case 13:
                        game.setBlacksPoints(game.getBlacksPoints() - 9);
                        break;
                }
            }

            if(!move.getMessage().isEmpty()){
                if(move.getMessage().equals("Upgrade")){
                    switch (upgradeChoice){
                        case "Knight":
                            game.setWhitesPoints(game.getWhitesPoints() + 2);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_KNIGHT.getValue();
                            break;
                        case "Bishop":
                            game.setWhitesPoints(game.getWhitesPoints() + 2);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_BISHOP.getValue();
                            break;
                        case "Rook":
                            game.setWhitesPoints(game.getWhitesPoints() + 4);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_ROOK.getValue();
                            break;
                        case "Queen":
                            game.setWhitesPoints(game.getWhitesPoints() + 8);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_QUEEN.getValue();
                            break;
                    }
                }
                else if(move.getMessage().contains("Two Square Push")){
                    game.setWhiteTwoPawn(new SquareDto(move.getRow(), move.getCol()));
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_TWO_PAWN.getValue();
                }
                else if(move.getMessage().contains("En passant")){
                    SquareDto pieceToEat = SquareDto.builder()
                            .row(Byte.parseByte(move.getMessage().split(" ")[2].split(",")[0]))
                            .col(Byte.parseByte(move.getMessage().split(" ")[2].split(",")[1]))
                            .build();
                    game.setBlacksPoints(game.getBlacksPoints() - 1);            // Update the point
                    newBoard[pieceToEat.getRow()][pieceToEat.getCol()] = 0;      // Delete the pawn
                    game.setBlackTwoPawn(new SquareDto((byte)-1, (byte)-1));     // Delete two pawn info
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_PAWN.getValue();
                }
                else if(move.getMessage().contains("Rook Moved")){
                    if(move.getMessage().contains("Short")){
                        game.setWhiteShortRookMoved(true);
                        newBoard[7][7] = 0;
                    }
                    else{
                        game.setWhiteLongRookMoved(true);
                        newBoard[7][0] = 0;
                    }
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_ROOK.getValue();
                }
                else if(move.getMessage().contains("King Moved")){
                    game.setWhiteKingMoved(true);
                    newBoard[game.getWhiteKingPosition().getRow()][game.getWhiteKingPosition().getCol()] = 0;
                    game.setWhiteKingPosition(new SquareDto(move.getRow(), move.getCol()));
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.WHITE_KING.getValue();
                }
                else if(move.getMessage().contains("Castle")){
                    if(move.getMessage().contains("Short")){
                        game.setWhiteShortRookMoved(true);
                        newBoard[7][7] = 0;
                        newBoard[7][5] = (byte)ChessPiece.WHITE_ROOK.getValue();
                        newBoard[7][6] = (byte)ChessPiece.WHITE_KING.getValue();
                    }
                    else{
                        game.setWhiteLongRookMoved(true);
                        newBoard[7][0] = 0;
                        newBoard[7][3] = (byte)ChessPiece.WHITE_ROOK.getValue();
                        newBoard[7][2] = (byte)ChessPiece.WHITE_KING.getValue();
                    }
                    newBoard[7][4] = 0;
                    game.setWhiteKingMoved(true);
                }
            }
            else{
                newBoard[move.getRow()][move.getCol()] = pieceValue;
            }

            if(threadCheckService.traceToKing(newBoard, game.getBlackKingPosition(), new SquareDto(move.getRow(), move.getCol())) != 0){
                game.setCheck(true);
                notation += "+";
            }

            game.getWhiteMoves().add(notation);
        }
        else{
            // Update two pawn if exists
            if(game.getBlackTwoPawn().getRow() != -1){
                newBoard[game.getBlackTwoPawn().getRow()][game.getBlackTwoPawn().getCol()] = (byte)ChessPiece.BLACK_PAWN.getValue();
            }
            // Subtract point if move takes a piece
            if(newBoard[move.getRow()][move.getCol()] != 0){
                switch (newBoard[move.getRow()][move.getCol()]){
                    case 1:
                        game.setWhitesPoints(game.getWhitesPoints() - 1);
                        break;
                    case 2:
                        game.setWhitesPoints(game.getWhitesPoints() - 1);
                        game.setWhiteTwoPawn(new SquareDto((byte)-1, (byte)-1));
                        break;
                    case 3:
                    case 4:
                        game.setWhitesPoints(game.getWhitesPoints() - 3);
                        break;
                    case 5:
                        game.setWhitesPoints(game.getWhitesPoints() - 5);
                        break;
                    case 6:
                        game.setWhitesPoints(game.getWhitesPoints() - 9);
                        break;
                }
            }

            if(!move.getMessage().isEmpty()){
                if(move.getMessage().equals("Upgrade")){
                    if(newBoard[move.getRow()][move.getCol()] != 0){  // eating and upgrading
                        switch (newBoard[move.getRow()][move.getCol()]){
                            case 3:
                            case 4:
                                game.setWhitesPoints(game.getWhitesPoints() - 3);
                                break;
                            case 12:
                                game.setWhitesPoints(game.getWhitesPoints() - 5);
                                break;
                            case 13:
                                game.setWhitesPoints(game.getWhitesPoints() - 9);
                                break;
                        }
                    }

                    switch (upgradeChoice){
                        case "Knight":
                            game.setBlacksPoints(game.getBlacksPoints() + 2);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_KNIGHT.getValue();
                            break;
                        case "Bishop":
                            game.setBlacksPoints(game.getBlacksPoints() + 2);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_BISHOP.getValue();
                            break;
                        case "Rook":
                            game.setBlacksPoints(game.getBlacksPoints() + 4);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_ROOK.getValue();
                            break;
                        case "Queen":
                            game.setBlacksPoints(game.getBlacksPoints() + 8);
                            newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_QUEEN.getValue();
                            break;
                    }
                }
                else if(move.getMessage().contains("Two Square Push")){
                    game.setBlackTwoPawn(new SquareDto(move.getRow(), move.getCol()));
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_TWO_PAWN.getValue();
                }
                else if(move.getMessage().contains("En passant")){
                    SquareDto pieceToEat = SquareDto.builder()
                            .row(Byte.parseByte(move.getMessage().split(" ")[2].split(",")[0]))
                            .col(Byte.parseByte(move.getMessage().split(" ")[2].split(",")[1]))
                            .build();
                    game.setWhitesPoints(game.getWhitesPoints() - 1);            // Update the point
                    newBoard[pieceToEat.getRow()][pieceToEat.getCol()] = 0;      // Delete the pawn
                    game.setWhiteTwoPawn(new SquareDto((byte)-1, (byte)-1));     // Delete two pawn info
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_PAWN.getValue();
                }
                else if(move.getMessage().contains("Rook Moved")){
                    if(move.getMessage().contains("Short")){
                        game.setBlackShortRookMoved(true);
                        newBoard[7][7] = 0;
                    }
                    else{
                        game.setBlackLongRookMoved(true);
                        newBoard[7][0] = 0;
                    }
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_ROOK.getValue();
                }
                else if(move.getMessage().contains("King Moved")){
                    game.setBlackKingMoved(true);
                    newBoard[game.getBlackKingPosition().getRow()][game.getBlackKingPosition().getCol()] = 0;
                    game.setBlackKingPosition(new SquareDto(move.getRow(), move.getCol()));
                    newBoard[move.getRow()][move.getCol()] = (byte)ChessPiece.BLACK_KING.getValue();
                }
                else if(move.getMessage().contains("Castle")){
                    if(move.getMessage().contains("Short")){
                        game.setBlackShortRookMoved(true);
                        newBoard[0][7] = 0;
                        newBoard[0][5] = (byte)ChessPiece.BLACK_ROOK.getValue();
                        newBoard[0][6] = (byte)ChessPiece.BLACK_KING.getValue();
                    }
                    else{
                        game.setBlackLongRookMoved(true);
                        newBoard[0][0] = 0;
                        newBoard[0][3] = (byte)ChessPiece.BLACK_ROOK.getValue();
                        newBoard[0][2] = (byte)ChessPiece.BLACK_KING.getValue();
                    }
                    newBoard[0][4] = 0;
                    game.setBlackKingMoved(true);
                }
            }
            else{
                newBoard[move.getRow()][move.getCol()] = pieceValue;
            }

            if(threadCheckService.traceToKing(newBoard, game.getWhiteKingPosition(), new SquareDto(move.getRow(), move.getCol())) != 0){
                game.setCheck(true);
                notation += "+";
            }

            game.getBlackMoves().add(notation);
        }

        game.setBoard(newBoard);
        return new SuccessDataResult<>(game);
    }

    private DataResult<MoveDto> getMove(LightGameDto lightGameDto, byte[][] board, SquareDto start, SquareDto end){
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
                    move.setMessage("White Two Square Push");
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
                if((rowDiff == colDiff || rowDiff == -colDiff) &&                                                  // in a diagonal and
                        (board[end.getRow()][end.getCol()] == 0 ||                                                 // empty or
                                (board[end.getRow()][end.getCol()] >= 8 &&                                         // has black piece
                                        board[end.getRow()][end.getCol()] != ChessPiece.BLACK_KING.getValue()))){  // other than king
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
                if(start.getRow() == 7){
                    if(start.getCol() == 0 && !lightGameDto.isLongRookMoved()){
                        move.setMessage("White Long Rook Moved");
                    }
                    else if(start.getCol() == 7 && !lightGameDto.isShortRookMoved()){
                        move.setMessage("White Short Rook Moved");
                    }
                }
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
                    if(!lightGameDto.isKingMoved() && !lightGameDto.isShortRookMoved() && board[7][5] == 0 && board[7][6] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, true)){
                        move.setMessage("White Short Castle");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 7 && start.getCol() == 4 && end.getRow() == 7 && end.getCol() == 2){  // long castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 7,( byte) 3), new SquareDto((byte)7, (byte)2)};
                    if(!lightGameDto.isKingMoved() && !lightGameDto.isLongRookMoved() && board[7][3] == 0 && board[7][2] == 0 && board[7][1] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, true)){
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
                    move.setMessage("Black Two Square Push");
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
                if(start.getRow() == 0){
                    if(start.getCol() == 0 && !lightGameDto.isLongRookMoved()){
                        move.setMessage("Black Long Rook Moved");
                    }
                    else if(start.getCol() == 7 && !lightGameDto.isShortRookMoved()){
                        move.setMessage("Black Short Rook Moved");
                    }
                }
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
                    if(!lightGameDto.isKingMoved() && !lightGameDto.isShortRookMoved() && board[0][5] == 0 && board[0][6] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, false)){
                        move.setMessage("Black Short Castle");
                        isValid = true;
                    }
                }
                else if(start.getRow() == 0 && start.getCol() == 4 && end.getRow() == 0 && end.getCol() == 2){  // long castle
                    SquareDto[] squaresToCheck = {new SquareDto((byte) 0,( byte) 3), new SquareDto((byte)0, (byte)2)};
                    if(!lightGameDto.isKingMoved() && !lightGameDto.isLongRookMoved() && board[0][3] == 0 && board[0][2] == 0 && board[0][1] == 0 && !threadCheckService.isSquaresUnderThread(board, squaresToCheck, false)){
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

    private boolean doesItBlockCheck(SquareDto king, SquareDto checker, SquareDto end){
        boolean doesItBlockCheck = false;
        int checkerRowDiff = checker.getRow() - king.getRow();
        int checkerColDiff = checker.getCol() - king.getCol();
        int blockerRowDiff = end.getRow() - king.getRow();
        int blockerColDiff = end.getCol() - king.getCol();
        if(checkerRowDiff == 0 && checkerColDiff > 0){  // checker is on the right
            if(blockerRowDiff == 0 && blockerColDiff > 0 && blockerColDiff < checkerColDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerRowDiff == 0 && checkerColDiff < 0){  // checker is on the left
            if(blockerRowDiff == 0 && blockerColDiff < 0 && blockerColDiff > checkerColDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerColDiff == 0 && checkerRowDiff > 0){  // checker is on the lower
            if(blockerColDiff == 0 && blockerRowDiff > 0 && blockerRowDiff < checkerRowDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerColDiff == 0 && checkerRowDiff < 0){  // checker is on the upper
            if(blockerColDiff == 0 && blockerRowDiff < 0 && blockerRowDiff > checkerRowDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerRowDiff > 0 && checkerColDiff > 0){  // checker is on the lower-right
            if(blockerRowDiff > 0 && blockerRowDiff == blockerColDiff && blockerRowDiff < checkerRowDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerRowDiff < 0 && checkerColDiff > 0){  // checker is on the upper-right
            if(blockerRowDiff < 0 && blockerRowDiff == -blockerColDiff && blockerRowDiff > checkerRowDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerRowDiff < 0){  // checker is on the upper-left
            if(blockerRowDiff < 0 && blockerRowDiff == blockerColDiff && blockerRowDiff > checkerRowDiff){
                doesItBlockCheck = true;
            }
        }
        else if(checkerRowDiff > 0){  // checker is on the lower-left
            if(blockerRowDiff > 0 && blockerRowDiff == -blockerColDiff && blockerRowDiff < checkerRowDiff){
                doesItBlockCheck = true;
            }
        }

        return doesItBlockCheck;
    }

    private boolean doesItTakeChecker(SquareDto checker, SquareDto end){
        return checker.getRow() == end.getRow() && checker.getCol() == end.getCol();
    }

    private boolean isPinned(byte[][] board, SquareDto king, SquareDto pieceToMove, SquareDto squareToPlay, boolean isWhitePlaying){
        boolean isPinned = false;
        int type = threadCheckService.traceToKing(board, king, pieceToMove);
        int row = pieceToMove.getRow();
        int col = pieceToMove.getCol();
        switch (type){
            case 0:
                break;
            case 1:  // check left
                if(pieceToMove.getRow() == squareToPlay.getRow() && pieceToMove.getCol() > squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    col--;
                    if(isWhitePlaying){
                        while(col >= 0){
                            if(board[row][col] == ChessPiece.BLACK_ROOK.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            col--;
                        }
                    }
                    else{
                        while(col >= 0){
                            if(board[row][col] == ChessPiece.WHITE_ROOK.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            col--;
                        }
                    }
                }
                break;
            case 2:  // check right
                if(pieceToMove.getRow() == squareToPlay.getRow() && pieceToMove.getCol() < squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    col++;
                    if(isWhitePlaying){
                        while(col <= 7){
                            if(board[row][col] == ChessPiece.BLACK_ROOK.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            col++;
                        }
                    }
                    else{
                        while(col <= 7){
                            if(board[row][col] == ChessPiece.WHITE_ROOK.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            col++;
                        }
                    }
                }
                break;
            case 3:  // check up
                if(pieceToMove.getRow() > squareToPlay.getRow() && pieceToMove.getCol() == squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row--;
                    if(isWhitePlaying){
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.BLACK_ROOK.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                        }
                    }
                    else{
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.WHITE_ROOK.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                        }
                    }
                }
                break;
            case 4:  // check down
                if(pieceToMove.getRow() < squareToPlay.getRow() && pieceToMove.getCol() == squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row++;
                    if(isWhitePlaying){
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.BLACK_ROOK.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                        }
                    }
                    else{
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.WHITE_ROOK.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                        }
                    }
                }
                break;
            case 5:  // check down-right
                if(pieceToMove.getRow() < squareToPlay.getRow() && pieceToMove.getCol() < squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row++;
                    col++;
                    if(isWhitePlaying){
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.BLACK_BISHOP.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                            col++;
                        }
                    }
                    else{
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.WHITE_BISHOP.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                            col++;
                        }
                    }
                }
                break;
            case 6:  // check up-right
                if(pieceToMove.getRow() > squareToPlay.getRow() && pieceToMove.getCol() < squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row--;
                    col++;
                    if(isWhitePlaying){
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.BLACK_BISHOP.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                            col++;
                        }
                    }
                    else{
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.WHITE_BISHOP.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                            col++;
                        }
                    }
                }
                break;
            case 7:  // check up-left
                if(pieceToMove.getRow() > squareToPlay.getRow() && pieceToMove.getCol() > squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row--;
                    col--;
                    if(isWhitePlaying){
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.BLACK_BISHOP.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                            col--;
                        }
                    }
                    else{
                        while(row >= 0){
                            if(board[row][col] == ChessPiece.WHITE_BISHOP.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row--;
                            col--;
                        }
                    }
                }
                break;
            case 8:  // check down-left
                if(pieceToMove.getRow() < squareToPlay.getRow() && pieceToMove.getCol() > squareToPlay.getCol()){  // already playing to that direction so no problem
                    break;
                }
                else{
                    row++;
                    col--;
                    if(isWhitePlaying){
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.BLACK_BISHOP.getValue() || board[row][col] == ChessPiece.BLACK_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                            col--;
                        }
                    }
                    else{
                        while(row <= 7){
                            if(board[row][col] == ChessPiece.WHITE_BISHOP.getValue() || board[row][col] == ChessPiece.WHITE_QUEEN.getValue()){
                                isPinned = true;
                                break;
                            }
                            else if(board[row][col] != 0){  // another piece
                                break;
                            }
                            row++;
                            col--;
                        }
                    }
                }
                break;
        }

        return isPinned;
    }

    private String getNotation(int pieceValue, SquareDto start, MoveDto move, boolean takesAPiece, String upgradeChoice){
        String notation = "";
        switch (pieceValue){
            case 1:
                if(start.getCol() != move.getCol()){
                    notation = translateCol(start.getCol()) + "x" +  translateCol(move.getCol()) + translateRow(move.getRow());
                }
                else{
                    notation = translateCol(move.getCol()) + String.valueOf(translateRow(move.getRow()));
                }

                if(move.getRow() == 0){
                    notation += "=" + translatePiece(upgradeChoice);
                }
                break;
            case 2:
            case 9:
                if(start.getCol() != move.getCol()){
                    notation = translateCol(start.getCol()) +  translateCol(move.getCol()) + String.valueOf(translateRow(move.getRow()));
                }
                else{
                    notation = translateCol(move.getCol()) + String.valueOf(translateRow(move.getRow()));
                }
                break;
            case 3:
            case 10:
                notation = "N" + (takesAPiece ? "x" : "") + translateCol(move.getCol()) + translateRow(move.getRow());
                break;
            case 4:
            case 11:
                notation = "B" + (takesAPiece ? "x" : "")  + translateCol(move.getCol()) + translateRow(move.getRow());
                break;
            case 5:
            case 12:
                notation = "R" + (takesAPiece ? "x" : "")  + translateCol(move.getCol()) + translateRow(move.getRow());
                break;
            case 6:
            case 13:
                notation = "Q" + (takesAPiece ? "x" : "")  + translateCol(move.getCol()) + translateRow(move.getRow());
                break;
            case 7:
            case 14:
                notation = "K" + (takesAPiece ? "x" : "")  + translateCol(move.getCol()) + translateRow(move.getRow());
                break;
            case 8:
                if(start.getCol() != move.getCol()){
                    notation = translateCol(start.getCol()) + "x" +  translateCol(move.getCol()) + translateRow(move.getRow());
                }
                else{
                    notation = translateCol(move.getCol()) + String.valueOf(translateRow(move.getRow()));
                }

                if(move.getRow() == 7){
                    notation += "=" + translatePiece(upgradeChoice);
                }
                break;
        }

        return notation;
    }

    private char translateCol(int col){
        return (char) (97 + col);
    }

    private int translateRow(int row){
        return 8 - row;
    }

    private char translatePiece(String piece){
        return switch (piece) {
            case "Knight" -> 'N';
            case "Bishop" -> 'B';
            case "Rook" -> 'R';
            case "Queen" -> 'Q';
            default -> 'x';
        };
    }
}
