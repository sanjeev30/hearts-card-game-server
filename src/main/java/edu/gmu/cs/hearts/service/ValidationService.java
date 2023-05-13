package edu.gmu.cs.hearts.service;

import edu.gmu.cs.hearts.exception.ValidationException;
import edu.gmu.cs.hearts.model.AuthenticationRequest;
import edu.gmu.cs.hearts.model.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateRegisterRequest(RegisterRequest request) throws ValidationException {
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            throw new ValidationException("First name is required");
        }
        if (!request.getFirstName().matches("[a-zA-Z]+")) {
            throw new ValidationException("First name must contain only letters");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
        if (!request.getLastName().matches("[a-zA-Z]+")) {
            throw new ValidationException("Last name must contain only letters");
        }
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!request.getEmail().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new ValidationException("Invalid email format");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (!request.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$%*])(?=\\S+$).{8,}$")) {
            throw new ValidationException("Password must contain at least 8 characters, including at least one digit, one uppercase letter, one lowercase letter, and one special character (@$%*)");
        }
    }

    public void validateAuthenticationRequest(AuthenticationRequest request) throws ValidationException {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new ValidationException("Password is required");
        }

    }

}
