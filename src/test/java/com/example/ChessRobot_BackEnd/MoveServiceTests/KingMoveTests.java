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
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class KingMoveTests {  // These tests indirectly tests isSquaresUnderThread function of ThreadCheckService
	@Autowired
	private MoveService moveService;
	private final Match cantCastleBecausePawnPosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 1- 8- 8- 8- 8- 1- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 8- 1- 1- 1- 8- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseKnightPosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
					 	 " 8- 8- 8- 8- 8- 8- 8- 8-" +
						 " 0- 0- 3- 0- 0- 0- 3- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0-10- 0- 0- 0-10- 0- 0-" +
						 " 1- 1- 1- 1- 1- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseBishopPosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 8- 0- 0- 8- 8-" +
						 "11- 0- 0- 0- 0- 0- 0-11-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 4-" +
						 " 0- 4- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 1- 0- 0- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseRookPosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 0- 8- 8- 0- 8-" +
						 " 0- 0-12- 0- 0-12- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 5- 0- 0- 5- 0-" +
						 " 1- 1- 0- 1- 1- 0- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseQueenPositionOne = Match.builder()  // rook feature
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 0- 8- 8- 0- 8-" +
						 " 0- 0-13- 0- 0-13- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 6- 0- 0- 6- 0-" +
						 " 1- 1- 0- 1- 1- 0- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseQueenPositionTwo = Match.builder()  // bishop feature
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 8- 0- 0- 8- 8-" +
						 "14- 0- 0- 0- 0- 0- 0-14-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 6-" +
						 " 0- 6- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 1- 0- 0- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseKingPosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 7- 8- 8- 8- 7- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1-14- 1- 1- 1-14- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseKingPlayedBeforePosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 8- 8- 8- 8- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 1- 1- 1- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.isWhiteKingMoved(true)
			.isBlackKingMoved(true)
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match cantCastleBecauseRookPlayedBeforePosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 8- 8- 8- 8- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 1- 1- 1- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.isWhiteShortRookMoved(true)
			.isWhiteLongRookMoved(true)
			.isBlackShortRookMoved(true)
			.isBlackLongRookMoved(true)
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match castlePosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0-14- 0- 0-12-" +
						 " 8- 8- 8- 8- 8- 8- 8- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 1- 1- 1- 1- 1- 1- 1- 1-" +
						 " 5- 0- 0- 0- 7- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	private final Match normalMovePosition = Match.builder()
			.id(1)
			.boardMatrix("12- 0- 0- 0- 0- 0- 0-12-" +
						 " 8- 8- 8- 0- 8- 0- 8- 8-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 0- 0-14- 0- 7- 0- 0-" +
						 " 0- 0- 0- 0- 0- 0- 0- 0-" +
						 " 0- 1- 0- 0- 0- 0- 0- 0-" +
						 " 1- 0- 1- 1- 1- 1- 1- 1-" +
						 " 5- 0- 0- 0- 0- 0- 0- 5")
			.whiteRemainingSeconds(1)
			.blackRemainingSeconds(1)
			.lastWhiteMoveDateTime(new Date())
			.lastBlackMoveDateTime(new Date())
			.build();
	static Stream<Object[]> kingMoveTestCasesForNormalMovePosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)3, (byte)6), true},   // Kf5 -> Kg5
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)4, (byte)6), true},   // Kf5 -> Kg4
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)4, (byte)5), true},   // Kf5 -> Kf4
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)2, (byte)2), true},   // Kd5 -> Kc6
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)3, (byte)2), true},   // Kd5 -> Kc5
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)4, (byte)3), true},   // Kd5 -> Kd4
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)2, (byte)3), true},   // Kd5 -> Kd6
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)2, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)3, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)4, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)2, (byte)5), false},  // black pieces leg
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)2, (byte)6), false},  // black pieces leg
				new Object[]{new SquareDto((byte)3, (byte)5), new SquareDto((byte)5, (byte)5), false},  // not king move
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)4, (byte)2), false},  // white pieces leg
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)4, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)3, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)2, (byte)4), false},  // Kings crossed
				new Object[]{new SquareDto((byte)3, (byte)3), new SquareDto((byte)3, (byte)0), false}   // not king move

		);
	}
	static Stream<Object[]> kingMoveTestCasesForImpossibleCastlePositions() {
		return Stream.of(
				new Object[]{new SquareDto((byte)7, (byte)4), new SquareDto((byte)7, (byte)6), false},   // 0-0
				new Object[]{new SquareDto((byte)7, (byte)4), new SquareDto((byte)7, (byte)2), false},   // 0-0-0
				new Object[]{new SquareDto((byte)0, (byte)4), new SquareDto((byte)0, (byte)6), false},   // 0-0
				new Object[]{new SquareDto((byte)0, (byte)4), new SquareDto((byte)0, (byte)2), false}    // 0-0-0
		);
	}
	static Stream<Object[]> kingMoveTestCasesForCastlePosition() {
		return Stream.of(
				new Object[]{new SquareDto((byte)7, (byte)4), new SquareDto((byte)7, (byte)6), true},   // 0-0
				new Object[]{new SquareDto((byte)7, (byte)4), new SquareDto((byte)7, (byte)2), true},   // 0-0-0
				new Object[]{new SquareDto((byte)0, (byte)4), new SquareDto((byte)0, (byte)6), true},   // 0-0
				new Object[]{new SquareDto((byte)0, (byte)4), new SquareDto((byte)0, (byte)2), true}    // 0-0-0
		);
	}

	@ParameterizedTest
	@MethodSource("kingMoveTestCasesForImpossibleCastlePositions")
	void testImpossibleCastlePositions(SquareDto from, SquareDto to, boolean expected) {
		List<Match> testCases = List.of(
				cantCastleBecausePawnPosition,
				cantCastleBecauseKnightPosition,
				cantCastleBecauseBishopPosition,
				cantCastleBecauseRookPosition,
				cantCastleBecauseQueenPositionOne,
				cantCastleBecauseQueenPositionTwo,
				cantCastleBecauseKingPosition,
				cantCastleBecauseKingPlayedBeforePosition,
				cantCastleBecauseRookPlayedBeforePosition
		);

		List<String> caseNames = List.of(
				"Pawn position",
				"Knight position",
				"Bishop position",
				"Rook position",
				"Queen position (Rook feature)",
				"Queen position (Bishop feature)",
				"King position",
				"King played before position",
				"Rook played before position"
		);

		for (int i = 0; i < testCases.size(); i++) {
			DataResult<MoveDto> dataResult = moveService.isMovePossible(testCases.get(i), from, to);
			assertEquals(expected, dataResult.isSuccess(), "Castle move from " + from + " to " + to + " failed on " + caseNames.get(i));
		}
	}

	@ParameterizedTest
	@MethodSource("kingMoveTestCasesForCastlePosition")
	void testCastlePositions(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(castlePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Kings move from " + from + " to " + to + " failed!");
	}

	@ParameterizedTest
	@MethodSource("kingMoveTestCasesForNormalMovePosition")
	void testNormalMovePositions(SquareDto from, SquareDto to, boolean expected) {
		DataResult<MoveDto> dataResult = moveService.isMovePossible(normalMovePosition, from, to);
		assertEquals(expected, dataResult.isSuccess(), "Kings move from " + from + " to " + to + " failed!");
	}
}
