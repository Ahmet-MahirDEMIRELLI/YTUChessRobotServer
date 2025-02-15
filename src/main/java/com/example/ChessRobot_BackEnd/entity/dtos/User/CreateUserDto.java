package com.example.ChessRobot_BackEnd.entity.dtos.User;

import com.example.ChessRobot_BackEnd.entity.concretes.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateUserDto {
    private String username;

    private String email;

    private String password;

    private short bulletRating;

    private short blitzRating;

    private short rapidRating;

    private short standardRating;

    private short robotRating;

    private Role role;
}
