package com.example.ChessRobot_BackEnd.MoveServiceTests;

import com.example.ChessRobot_BackEnd.business.abstracts.MoveService;
import com.example.ChessRobot_BackEnd.core.utilities.results.DataResult;
import com.example.ChessRobot_BackEnd.entity.concretes.Match;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.MoveDto;
import com.example.ChessRobot_BackEnd.entity.dtos.Game.SquareDto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Date;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BishopMoveTests {
	@Autowired
	private MoveService moveService;
	private final Match playEmptySquareOrEatPiecePosition = Match.builder()
			.id(1)
			.boardMatrix("12-10-11-13-14-11-10-12-" +
						 " 8- 8- 8- 8- 0- 8- 8- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 12-" +
						 " 0- 0- 0- 0- 0- 0- 8- 0-" +
						 " 0- 2- 0- 0- 0- 0- 0- 0-" +
						 " 1- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 1- 0- 1- 1- 1- 1-" +
						 " 5- 3- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	static Stream<Object[]> bishopMoveTestCasesForPlayEmptySquareOrEatPiecePosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte)7, (byte)2), new SquareDto((byte)6, (byte)3), true},   // Fc1 -> Fd2
				new Object[]{new SquareDto((byte)7, (byte)2), new SquareDto((byte)4, (byte)5), true},   // Fc1 -> Ff4
				new Object[]{new SquareDto((byte)7, (byte)2), new SquareDto((byte)3, (byte)6), true},   // Fc1 -> Fxg5
				new Object[]{new SquareDto((byte)0, (byte)5), new SquareDto((byte)1, (byte)4), true},   // Ff8 -> Fe7
				new Object[]{new SquareDto((byte)0, (byte)5), new SquareDto((byte)3, (byte)2), true},   // Ff8 -> Fc5
				new Object[]{new SquareDto((byte)0, (byte)5), new SquareDto((byte)4, (byte)1), true},   // Ff8 -> Fxb4
				new Object[]{new SquareDto((byte)0, (byte)5), new SquareDto((byte)1, (byte)6), false},  // eating same color
				new Object[]{new SquareDto((byte)7, (byte)2), new SquareDto((byte)5, (byte)0), false},  // eating same color
				new Object[]{new SquareDto((byte)7, (byte)2), new SquareDto((byte)2, (byte)7), false},  // trying to step over a piece
				new Object[]{new SquareDto((byte)0, (byte)5), new SquareDto((byte)5, (byte)0), false}   // trying to step over a piece
		);
	}

	@ParameterizedTest
	@MethodSource("bishopMoveTestCasesForPlayEmptySquareOrEatPiecePosition")
	void testPlayEmptySquareOrEatPiecePosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(playEmptySquareOrEatPiecePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Bishops move from " + from + " to " + to + " failed!");
	}
}
