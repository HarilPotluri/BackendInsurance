package com.example.InsuranceWeb.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.InsuranceWeb.models.Notification;
import com.example.InsuranceWeb.models.User;
import com.example.InsuranceWeb.repository.UserRepository;
import com.example.InsuranceWeb.services.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/agent/notify/{purchaseId}")
    public ResponseEntity<String> notifyCustomer(@PathVariable Long purchaseId) {
        notificationService.notifyCustomer(purchaseId);
        return ResponseEntity.ok("Notification sent");
    }

    @GetMapping("/customer/notifications")
    public ResponseEntity<List<Notification>> getCustomerNotifications(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }
}