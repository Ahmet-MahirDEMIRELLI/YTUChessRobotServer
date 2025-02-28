package com.example.ChessRobot_BackEnd.entity.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "completed_matches")
public class CompletedMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "rated")
    private boolean rated;

    @Column(name = "time")
    private String time;

    @OneToOne(mappedBy = "games")
    @JoinColumn(name = "white_player_id")
    private User whitePlayer;

    @OneToOne(mappedBy = "games")
    @JoinColumn(name = "black_player_id")
    private User blackPlayer;

    @Column(name = "created_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdAt;

    @Column(name = "ended_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endedAt;

    @Column(name = "notation")
    private String notation = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "game_status")
    private GameStatus gameStatus;
}
