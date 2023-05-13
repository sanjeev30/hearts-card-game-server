package edu.gmu.cs.hearts.controller;

import edu.gmu.cs.hearts.exception.ValidationException;
import edu.gmu.cs.hearts.model.AuthenticationRequest;
import edu.gmu.cs.hearts.model.AuthenticationResponse;
import edu.gmu.cs.hearts.model.RegisterRequest;
import edu.gmu.cs.hearts.service.AuthenticationService;
import edu.gmu.cs.hearts.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ValidationService validationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            validationService.validateRegisterRequest(request);
        } catch (ValidationException ve) {
            log.info(ve.getMessage());
            return ResponseEntity.badRequest().body(ve.getMessage());
        }
        return ResponseEntity.ok(authenticationService.register(request));
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
