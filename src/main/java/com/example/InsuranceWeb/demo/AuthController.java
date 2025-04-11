// AuthController.java
package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.User;
import com.example.InsuranceWeb.models.LoginRequest;
import com.example.InsuranceWeb.services.UserService;
import com.example.InsuranceWeb.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginRequest.getPassword())) {
                // ✅ Check if selected role matches user's actual role
                if (!user.getRole().equals(loginRequest.getRole())) {
                    return ResponseEntity.status(403).body("Role mismatch. Please select the correct role.");
                }

                String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("name", user.getName());
                response.put("email", user.getEmail());
                response.put("role", user.getRole().name());

                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }


    
 // Add to AuthController.java

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        String token = userService.createResetToken(email);
        if (token != null) {
            // For now, return token in response. In production, send via email.
            return ResponseEntity.ok(Map.of(
                "message", "Password reset token generated.",
                "resetToken", token
            ));
        }
        return ResponseEntity.status(404).body("User with this email not found.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token,
                                           @RequestParam String newPassword) {
        boolean success = userService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password has been reset successfully.");
        }
        return ResponseEntity.status(400).body("Invalid or expired reset token.");
    }

}
