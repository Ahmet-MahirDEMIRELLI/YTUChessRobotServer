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
class KnightMoveTests {
	@Autowired
	private MoveService moveService;
	private final Match playEmptySquareOrEatPiecePosition = Match.builder()
			.id(1)
			.boardMatrix("12-10-11-13-14-11- 0-12-" +
						 " 8- 8- 8- 8- 8- 0- 8- 8-" +
						 " 0- 0- 0- 0- 0- 8- 0- 0-" +
						 " 0- 0- 0-10- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 3- 0- 0- 0-" +
						 " 0- 0- 1- 0- 0- 0- 0- 0-" +
						 " 1- 1- 0- 1- 1- 1- 1- 1-" +
						 " 5- 0- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	static Stream<Object[]> knightMoveTestCasesForPlayEmptySquareOrEatPiecePosition() {
		return Stream.of(
				new Object[]{new Square((short) 4, (short) 4), new Square((short) 2, (short) 5), true},   // Ae4 -> Axf6
				new Object[]{new Square((short) 4, (short) 4), new Square((short) 5, (short) 6), true},   // Ae4 -> Ag3
				new Object[]{new Square((short) 4, (short) 4), new Square((short) 5, (short) 2), false},  // eating same color
				new Object[]{new Square((short) 3, (short) 3), new Square((short) 5, (short) 2), true},   // Ad5 -> Axc3
				new Object[]{new Square((short) 3, (short) 3), new Square((short) 2, (short) 1), true},   // Ad5 -> Ab6
				new Object[]{new Square((short) 3, (short) 3), new Square((short) 1, (short) 4), false}   // eating same color
		);
	}

	@ParameterizedTest
	@MethodSource("knightMoveTestCasesForPlayEmptySquareOrEatPiecePosition")
	void testPlayEmptySquareOrEatPiecePosition(Square from, Square to, boolean expected) {
		DataResult<Move> dataResult = moveService.isMovePossible(playEmptySquareOrEatPiecePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Knight move from " + from + " to " + to + " failed!");
	}
}
