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
				new Object[]{new Square((short) 6, (short) 4), new Square((short) 4, (short) 4), true},   // e2 -> e4
				new Object[]{new Square((short) 1, (short) 4), new Square((short) 3, (short) 4), true},   // e7 -> e5
				new Object[]{new Square((short) 6, (short) 3), new Square((short) 5, (short) 3), true},   // d2 -> d3
				new Object[]{new Square((short) 1, (short) 5), new Square((short) 2, (short) 5), true},   // f7 -> f6
				new Object[]{new Square((short) 5, (short) 4), new Square((short) 4, (short) 4), false},  // e3 -> e4 (trying to play with empty square)
				new Object[]{new Square((short) 6, (short) 4), new Square((short) 7, (short) 4), false},  // backwards
				new Object[]{new Square((short) 1, (short) 4), new Square((short) 0, (short) 4), false},  // backwards
				new Object[]{new Square((short) -1, (short) 4), new Square((short) 4, (short) 4), false}, // out of board
				new Object[]{new Square((short) 6, (short) -1), new Square((short) 4, (short) 4), false}, // out of board
				new Object[]{new Square((short) 6, (short) 4), new Square((short) -1, (short) 4), false}, // out of board
				new Object[]{new Square((short) 6, (short) 4), new Square((short) 4, (short) -1), false}  // out of board
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForEnPassantPosition() {
		return Stream.of(
				new Object[]{new Square((short) 3, (short) 2), new Square((short) 2, (short) 1), true, "Eats 3,1"},   // c5 -> b6
				new Object[]{new Square((short) 4, (short) 7), new Square((short) 5, (short) 6), true, "Eats 4,6"}    // h4 -> g3
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForFakeEnPassantPosition() {
		return Stream.of(
				new Object[]{new Square((short) 3, (short) 2), new Square((short) 2, (short) 1), false},   // c5 -> b6
				new Object[]{new Square((short) 4, (short) 7), new Square((short) 5, (short) 6), false}    // h4 -> g3
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForEatPiecePosition() {
		return Stream.of(
				new Object[]{new Square((short) 6, (short) 1), new Square((short) 5, (short) 2), true},    // eating opposite color
				new Object[]{new Square((short) 1, (short) 6), new Square((short) 2, (short) 5), true},    // eating opposite color
				new Object[]{new Square((short) 6, (short) 1), new Square((short) 5, (short) 0), false},   // eating the same color
				new Object[]{new Square((short) 4, (short) 1), new Square((short) 5, (short) 2), false},   // eating backwards
				new Object[]{new Square((short) 4, (short) 3), new Square((short) 5, (short) 2), false},   // eating backwards
				new Object[]{new Square((short) 1, (short) 6), new Square((short) 2, (short) 7), false},   // eating the same color
				new Object[]{new Square((short) 3, (short) 6), new Square((short) 2, (short) 5), false},   // eating backwards
				new Object[]{new Square((short) 3, (short) 4), new Square((short) 2, (short) 5), false}    // eating backwards
		);
	}
	static Stream<Object[]> pawnMoveTestCasesForUpgradePosition() {
		return Stream.of(
				new Object[]{new Square((short) 1, (short) 6), new Square((short) 0, (short) 7), true, "Upgrade"},    // eat upgrade
				new Object[]{new Square((short) 1, (short) 6), new Square((short) 0, (short) 5), true, "Upgrade"},    // eat upgrade
				new Object[]{new Square((short) 1, (short) 1), new Square((short) 0, (short) 1), true, "Upgrade"},    // push upgrade
				new Object[]{new Square((short) 6, (short) 6), new Square((short) 7, (short) 7), true, "Upgrade"},    // eat upgrade
				new Object[]{new Square((short) 6, (short) 6), new Square((short) 7, (short) 5), true, "Upgrade"},    // eat upgrade
				new Object[]{new Square((short) 6, (short) 1), new Square((short) 7, (short) 1), true, "Upgrade"},    // push upgrade
				new Object[]{new Square((short) 6, (short) 6), new Square((short) 7, (short) 6), false, ""},   // push to filled square
				new Object[]{new Square((short) 1, (short) 6), new Square((short) 0, (short) 6), false, ""}    // push to filled square
		);
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForStartingPosition")
	void testPawnMoveStartingPosition(Square from, Square to, boolean expected) {
		DataResult<Move> dataResult = moveService.isMovePossible(startingPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForEnPassantPosition")
	void testPawnMoveEnPassantPosition(Square from, Square to, boolean expected, String expectedMessage) {
		DataResult<Move> dataResult = moveService.isMovePossible(enPassantPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn en passant move from " + from + " to " + to + " failed!");
		assertEquals(expectedMessage, dataResult.getData().getMessage(), "Message is wrong!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForFakeEnPassantPosition")
	void testPawnMoveFakeEnPassantPosition(Square from, Square to, boolean expected) {
		DataResult<Move> dataResult = moveService.isMovePossible(fakeEnPassantPosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Fake en passant move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForEatPiecePosition")
	void testPawnMoveEatPiecePosition(Square from, Square to, boolean expected) {
		DataResult<Move> dataResult = moveService.isMovePossible(eatPiecePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn capturing move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("pawnMoveTestCasesForUpgradePosition")
	void testPawnMoveUpgradePosition(Square from, Square to, boolean expected, String expectedMessage) {
		DataResult<Move> dataResult = moveService.isMovePossible(upgradePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Pawn promotion move from " + from + " to " + to + " failed!");
		assertEquals(expectedMessage, dataResult.getData().getMessage(), "Message is wrong!");
	}
}
