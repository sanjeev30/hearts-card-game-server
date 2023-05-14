package edu.gmu.cs.hearts.service;

import edu.gmu.cs.hearts.domain.Player;
import edu.gmu.cs.hearts.domain.PlayerStatistics;
import edu.gmu.cs.hearts.domain.Role;
import edu.gmu.cs.hearts.model.AuthenticationRequest;
import edu.gmu.cs.hearts.model.AuthenticationResponse;
import edu.gmu.cs.hearts.model.RegisterRequest;
import edu.gmu.cs.hearts.repository.PlayerRepository;
import edu.gmu.cs.hearts.repository.PlayerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PlayerStatisticsRepository playerStatisticsRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        var player = Player.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PLAYER)
                .build();
        playerRepository.save(player);
        var playerStatistics = PlayerStatistics.builder()
                .playerId(player.getId())
                .gamesPlayed(0)
                .gamesWon(0)
                .gamesLost(0)
                .roundsWon(0)
                .roundsLost(0)
                .build();
        playerStatisticsRepository.save(playerStatistics);
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .firstName(request.getFirstName())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var player = playerRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .firstName(player.getFirstName())
                .build();
    }
}
