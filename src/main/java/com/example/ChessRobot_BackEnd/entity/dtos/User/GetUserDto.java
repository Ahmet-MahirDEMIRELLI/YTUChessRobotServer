package com.example.ChessRobot_BackEnd.entity.dtos.User;

import com.example.ChessRobot_BackEnd.entity.concretes.Role;
import com.example.ChessRobot_BackEnd.entity.concretes.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDto {
    private int id;

    private String username;

    private String email;

    private short bulletRating;

    private short blitzRating;

    private short rapidRating;

    private short standardRating;

    private short robotRating;

    private boolean isBanned;

    private Set<Role> authorities;

    public GetUserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.isBanned = user.isBanned();
        this.blitzRating = user.getBlitzRating();
        this.rapidRating = user.getRapidRating();
        this.bulletRating = user.getBulletRating();
        this.standardRating = user.getStandardRating();
        this.robotRating = user.getRobotRating();
        this.authorities = user.getAuthorities();
    }

    public List<GetUserDto> buildListGetUserDto(List<User> users) {
        return users.stream()
                .map(GetUserDto::new)
                .collect(Collectors.toList());
    }
}
