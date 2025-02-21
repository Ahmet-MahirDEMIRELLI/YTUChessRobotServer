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
class QueenMoveTests {
	@Autowired
	private MoveService moveService;
	private final Match playEmptySquareOrEatPieceLikeBishopPosition = Match.builder()
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
	private final Match playEmptySquareOrEatPieceLikeRookPosition = Match.builder()
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
	static Stream<Object[]> bishopFeatureMoveTestCasesForPlayEmptySquareOrEatPieceLikeBishopPosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 7, (byte) 2), new SquareDto((byte) 6, (byte) 3), true},   // Vc1 -> Vd2
				new Object[]{new SquareDto((byte) 7, (byte) 2), new SquareDto((byte) 4, (byte) 5), true},   // Vc1 -> Vf4
				new Object[]{new SquareDto((byte) 7, (byte) 2), new SquareDto((byte) 3, (byte) 6), true},   // Vc1 -> Vxg5
				new Object[]{new SquareDto((byte) 0, (byte) 5), new SquareDto((byte) 1, (byte) 4), true},   // Vf8 -> Ve7
				new Object[]{new SquareDto((byte) 0, (byte) 5), new SquareDto((byte) 3, (byte) 2), true},   // Vf8 -> Vc5
				new Object[]{new SquareDto((byte) 0, (byte) 5), new SquareDto((byte) 4, (byte) 1), true},   // Vf8 -> Vxb4
				new Object[]{new SquareDto((byte) 0, (byte) 5), new SquareDto((byte) 1, (byte) 6), false},  // eating same color
				new Object[]{new SquareDto((byte) 7, (byte) 2), new SquareDto((byte) 5, (byte) 0), false},  // eating same color
				new Object[]{new SquareDto((byte) 7, (byte) 2), new SquareDto((byte) 2, (byte) 7), false},  // trying to step over a piece
				new Object[]{new SquareDto((byte) 0, (byte) 5), new SquareDto((byte) 5, (byte) 0), false}   // trying to step over a piece
		);
	}
	static Stream<Object[]> rookFeatureMoveTestCasesForPlayEmptySquareOrEatPieceLikeRookPosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 4, (byte) 2), new SquareDto((byte) 4, (byte) 0), true},   // Kc4 -> Ka4
				new Object[]{new SquareDto((byte) 4, (byte) 2), new SquareDto((byte) 4, (byte) 5), true},   // Kc4 -> Kxf4
				new Object[]{new SquareDto((byte) 4, (byte) 2), new SquareDto((byte) 7, (byte) 2), false},  // eating same color
				new Object[]{new SquareDto((byte) 4, (byte) 2), new SquareDto((byte) 1, (byte) 2), false},  // trying to jump over a piece
				new Object[]{new SquareDto((byte) 4, (byte) 2), new SquareDto((byte) 4, (byte) 6), false},  // trying to jump over a piece
				new Object[]{new SquareDto((byte) 3, (byte) 5), new SquareDto((byte) 3, (byte) 7), true},   // Kf5 -> Kc5
				new Object[]{new SquareDto((byte) 3, (byte) 5), new SquareDto((byte) 3, (byte) 2), true},   // Kf5 -> Kxf4
				new Object[]{new SquareDto((byte) 3, (byte) 5), new SquareDto((byte) 0, (byte) 5), false},  // eating same color
				new Object[]{new SquareDto((byte) 3, (byte) 5), new SquareDto((byte) 6, (byte) 5), false},  // trying to jump over a piece
				new Object[]{new SquareDto((byte) 3, (byte) 5), new SquareDto((byte) 3, (byte) 1), false}   // trying to jump over a piece
		);
	}

	@ParameterizedTest
	@MethodSource("bishopFeatureMoveTestCasesForPlayEmptySquareOrEatPieceLikeBishopPosition")
	void testPlayEmptySquareOrEatPieceLikeBishopPosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(playEmptySquareOrEatPieceLikeBishopPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Queens move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("rookFeatureMoveTestCasesForPlayEmptySquareOrEatPieceLikeRookPosition")
	void testPlayEmptySquareOrEatPieceLikeRookPosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(playEmptySquareOrEatPieceLikeRookPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Queens move from " + from + " to " + to + " failed!");
	}
}
