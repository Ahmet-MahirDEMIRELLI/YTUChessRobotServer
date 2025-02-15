package com.example.ChessRobot_BackEnd.entity.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BannedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @JoinColumn(name = "banned_user_id", referencedColumnName = "id")
    @OneToOne
    private User bannedUser;

    @JoinColumn(name = "banned_by_id", referencedColumnName = "id")
    @ManyToOne
    private User bannedBy;

    @Column(name = "banned_at")
    private Date bannedAt;

    @Column(name = "ban_reason")
    private String banReason;
}
