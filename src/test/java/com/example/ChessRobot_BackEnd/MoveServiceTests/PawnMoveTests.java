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
class PawnMoveTests {
	@Autowired
	private MoveService moveService;
	private final Match startingPosition = Match.builder()
			.id(1)
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match enPassantPosition = Match.builder()
			.id(1)
			.boardMatrix("12-10-11-13-14-11-10-12-" +
						 " 8- 0- 8- 8- 8- 8- 8- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 9- 1- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 2- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 0- 1- 1- 1- 0- 1-" +
						 " 5- 3- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match fakeEnPassantPosition = Match.builder()
			.id(1)
			.boardMatrix("12-10-11-13-14-11-10-12-" +
						 " 8- 0- 8- 8- 8- 8- 8- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 8- 1- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 1- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 0- 1- 1- 1- 0- 1-" +
						 " 5- 3- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match eatPiecePosition = Match.builder()
			.id(1)
			.boardMatrix(" 0-10-11-13-14-11-10- 0-" +
						 " 0- 0- 8- 8- 0- 8- 8- 0-" +
						 " 0- 0- 0- 0- 0- 5- 0-12-" +
						 " 0- 0- 0- 0- 9- 0- 8- 0-" +
						 " 0- 1- 0- 2- 0- 0- 0- 0-" +
						 " 5- 0-12- 0- 0- 0- 0- 0-" +
						 " 0- 1- 1- 0- 1- 1- 1- 0-" +
						 " 0- 3- 4- 6- 7- 4- 3- 0")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match upgradePosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0-11-13-14-11-10-12-" +
						 " 8- 1- 8- 8- 8- 8- 1- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 8- 1- 1- 1- 1- 8- 1-" +
						 " 5- 0- 4- 6- 7- 4- 3- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	static Stream<Object[]> pawnMoveTestCasesForStartingPosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 6, (byte) 4), new SquareDto((byte) 4, (byte) 4), true},   // e2 -> e4
				new Object[]{new SquareDto((byte) 1, (byte) 4), new SquareDto((byte) 3, (byte) 4), true},   // e7 -> e5
				new Object[]{new SquareDto((byte) 6, (byte) 3), new SquareDto((byte) 5, (byte) 3), true},   // d2 -> d3
				new Object[]{new SquareDto((byte) 1, (byte) 5), new SquareDto((byte) 2, (byte) 5), true},   // f7 -> f6
				new Object[]{new SquareDto((byte) 5, (byte) 4), new SquareDto((byte) 4, (byte) 4), false},  // e3 -> e4 (trying to play with empty square)
				new Object[]{new SquareDto((byte) 6, (byte) 4), new SquareDto((byte) 7, (byte) 4), false},  // backwards
				new Object[]{new SquareDto((byte) 6, (byte) 4), new SquareDto((byte) 6, (byte) 2), false},  // not pawn move
				new Object[]{new SquareDto((byte) 1, (byte) 4), new SquareDto((byte) 0, (byte) 4), false},  // backwards
				new Object[]{new SquareDto((byte) 1, (byte) 4), new SquareDto((byte) 0, (byte) 6), false},  // not pawn move
				new Object[]{new SquareDto((byte) -1, (byte) 4), new SquareDto((byte) 4, (byte) 4), false}, // out of board
				new Object[]{new SquareDto((byte) 6, (byte) -1), new SquareDto((byte) 4, (byte) 4), false}, // out of board
				new Object[]{new SquareDto((byte) 6, (byte) 4), new SquareDto((byte) -1, (byte) 4), false}, // out of board
				new Object[]{new SquareDto((byte) 6, (byte) 4), new SquareDto((byte) 4, (byte) -1), false}  // out of board
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForEnPassantPosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 3, (byte) 2), new SquareDto((byte) 2, (byte) 1), true, "Eats 3,1"},   // c5 -> b6
				new Object[]{new SquareDto((byte) 4, (byte) 7), new SquareDto((byte) 5, (byte) 6), true, "Eats 4,6"}    // h4 -> g3
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForFakeEnPassantPosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 3, (byte) 2), new SquareDto((byte) 2, (byte) 1), false},   // c5 -> b6
				new Object[]{new SquareDto((byte) 4, (byte) 7), new SquareDto((byte) 5, (byte) 6), false}    // h4 -> g3
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForEatPiecePosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 6, (byte) 1), new SquareDto((byte) 5, (byte) 2), true},    // eating opposite color
				new Object[]{new SquareDto((byte) 1, (byte) 6), new SquareDto((byte) 2, (byte) 5), true},    // eating opposite color
				new Object[]{new SquareDto((byte) 6, (byte) 1), new SquareDto((byte) 5, (byte) 0), false},   // eating the same color
				new Object[]{new SquareDto((byte) 4, (byte) 1), new SquareDto((byte) 5, (byte) 2), false},   // eating backwards
				new Object[]{new SquareDto((byte) 4, (byte) 3), new SquareDto((byte) 5, (byte) 2), false},   // eating backwards
				new Object[]{new SquareDto((byte) 1, (byte) 6), new SquareDto((byte) 2, (byte) 7), false},   // eating the same color
				new Object[]{new SquareDto((byte) 3, (byte) 6), new SquareDto((byte) 2, (byte) 5), false},   // eating backwards
				new Object[]{new SquareDto((byte) 3, (byte) 4), new SquareDto((byte) 2, (byte) 5), false}    // eating backwards
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForUpgradePosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte) 1, (byte) 6), new SquareDto((byte) 0, (byte) 7), true, "Upgrade"},    // eat upgrade
				new Object[]{new SquareDto((byte) 1, (byte) 6), new SquareDto((byte) 0, (byte) 5), true, "Upgrade"},    // eat upgrade
				new Object[]{new SquareDto((byte) 1, (byte) 1), new SquareDto((byte) 0, (byte) 1), true, "Upgrade"},    // push upgrade
				new Object[]{new SquareDto((byte) 6, (byte) 6), new SquareDto((byte) 7, (byte) 7), true, "Upgrade"},    // eat upgrade
				new Object[]{new SquareDto((byte) 6, (byte) 6), new SquareDto((byte) 7, (byte) 5), true, "Upgrade"},    // eat upgrade
				new Object[]{new SquareDto((byte) 6, (byte) 1), new SquareDto((byte) 7, (byte) 1), true, "Upgrade"},    // push upgrade
				new Object[]{new SquareDto((byte) 6, (byte) 6), new SquareDto((byte) 7, (byte) 6), false, ""},   // push to filled square
				new Object[]{new SquareDto((byte) 1, (byte) 6), new SquareDto((byte) 0, (byte) 6), false, ""}    // push to filled square
		);
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForStartingPosition")
	void testPawnMoveStartingPosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(startingPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForEnPassantPosition")
	void testPawnMoveEnPassantPosition(SquareDto from, SquareDto to, boolean expected, String expectedMessage) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(enPassantPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn en passant move from " + from + " to " + to + " failed!");
		assertEquals(expectedMessage, dataResult.getData().getMessage(), "Message is wrong!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForFakeEnPassantPosition")
	void testPawnMoveFakeEnPassantPosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(fakeEnPassantPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Fake en passant move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForEatPiecePosition")
	void testPawnMoveEatPiecePosition(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(eatPiecePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn capturing move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForUpgradePosition")
	void testPawnMoveUpgradePosition(SquareDto from, SquareDto to, boolean expected, String expectedMessage) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(upgradePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn promotion move from " + from + " to " + to + " failed!");
		assertEquals(expectedMessage, dataResult.getData().getMessage(), "Message is wrong!");
	}
}
