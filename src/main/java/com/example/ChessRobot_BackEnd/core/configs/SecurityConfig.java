package com.example.ChessRobot_BackEnd.core.configs;

import com.example.ChessRobot_BackEnd.business.abstracts.UserService;
import com.example.ChessRobot_BackEnd.core.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserService userService, PasswordEncoder passwordEncoder) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(x ->
                        x
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                .requestMatchers("/webApi/auth/**").permitAll()

                                .requestMatchers("/webApi/users/addUser").permitAll()
                                .requestMatchers("/webApi/users/deleteUser").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/webApi/users/updateUser").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/webApi/users/getUserById").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/webApi/users/getUserByEmail").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/webApi/users/getUserByUsername").hasAnyRole("ADMIN", "MODERATOR")
                                .requestMatchers("/webApi/users/getAllUsers").hasAnyRole("ADMIN", "MODERATOR")
                                //.requestMatchers("/webApi/users/addModerator").hasRole("ADMIN")
                                //.requestMatchers("/webApi/users/removeModerator").hasRole("ADMIN")
                                .requestMatchers("/webApi/users/addUserToGame").hasAnyRole("ADMIN", "MODERATOR")

                                //other endpoints

                                .anyRequest().permitAll()
                )
                .sessionManagement(x -> x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
