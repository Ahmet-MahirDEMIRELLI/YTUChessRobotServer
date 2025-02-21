package com.example.ChessRobot_BackEnd.MoveServiceTests;

import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.Match;
import com.example.ChessRobot_BackEnd.entity.concretes.Move;
import com.example.ChessRobot_BackEnd.entity.concretes.Square;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RookMoveTests {
	@Autowired
	private MoveService moveService;
	private final Match playEmptySquareOrEatPiecePosition = Match.builder()
			.id(1)
			.boardMatrix("12-10-11-13-14-11-10- 0-" +
						 " 8- 8- 8- 8- 8- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 1- 1- 0- 0-12- 0- 0-" +
						 " 0- 0- 5- 0- 0- 8- 8- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 1- 1- 1- 1- 1-" +
						 " 0- 3- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	static Stream<Object[]> rookMoveTestCasesForPlayEmptySquareOrEatPiecePosition() {
		return Stream.of(
				new Object[]{new Square((short) 4, (short) 2), new Square((short) 4, (short) 0), true},   // Kc4 -> Ka4
				new Object[]{new Square((short) 4, (short) 2), new Square((short) 4, (short) 5), true},   // Kc4 -> Kxf4
				new Object[]{new Square((short) 4, (short) 2), new Square((short) 7, (short) 2), false},  // eating same color
				new Object[]{new Square((short) 4, (short) 2), new Square((short) 1, (short) 2), false},  // trying to jump over a piece
				new Object[]{new Square((short) 4, (short) 2), new Square((short) 4, (short) 6), false},  // trying to jump over a piece
				new Object[]{new Square((short) 3, (short) 5), new Square((short) 3, (short) 7), true},   // Kf5 -> Kc5
				new Object[]{new Square((short) 3, (short) 5), new Square((short) 3, (short) 2), true},   // Kf5 -> Kxf4
				new Object[]{new Square((short) 3, (short) 5), new Square((short) 0, (short) 5), false},  // eating same color
				new Object[]{new Square((short) 3, (short) 5), new Square((short) 6, (short) 5), false},  // trying to jump over a piece
				new Object[]{new Square((short) 3, (short) 5), new Square((short) 3, (short) 1), false}   // trying to jump over a piece
		);
	}

	@ParameterizedTest
	@MethodSource("rookMoveTestCasesForPlayEmptySquareOrEatPiecePosition")
	void testPlayEmptySquareOrEatPiecePosition(Square from, Square to, boolean expected) {
		DataResult<Move> dataResult = moveService.isMovePossible(playEmptySquareOrEatPiecePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Rooks move from " + from + " to " + to + " failed!");
	}
}
