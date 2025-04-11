// UserService.java
package com.example.InsuranceWeb.services;

import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.models.User;
import com.example.InsuranceWeb.repository.NotificationRepository;
import com.example.InsuranceWeb.repository.PremiumPaymentRepository;
import com.example.InsuranceWeb.repository.PurchaseRepository;
import com.example.InsuranceWeb.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private PremiumPaymentRepository premiumPaymentRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        if (user.getRole() == null) {
            user.setRole(com.example.InsuranceWeb.models.Role.CUSTOMER);
        }
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String createResetToken(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String token = UUID.randomUUID().toString();
            user.setResetToken(token);
            user.setTokenExpiry(LocalDateTime.now().plusMinutes(15));
            userRepository.save(user);
            return token;
        }
        return null;
    }

    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetToken()))
                .findFirst();

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getTokenExpiry().isAfter(LocalDateTime.now())) {
                user.setPassword(newPassword);
                user.setResetToken(null);
                user.setTokenExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteUserById(Long id) {
        // Delete dependent notifications
        notificationRepository.deleteAll(notificationRepository.findByUserIdOrderByTimestampDesc(id));

        // Get all purchases made by this user
        List<Purchase> userPurchases = purchaseRepository.findByUserId(id);

        // For each purchase, delete its associated premium payments
        for (Purchase purchase : userPurchases) {
            premiumPaymentRepository.deleteAll(premiumPaymentRepository.findByPurchaseId(purchase.getId()));
        }

        // Now delete purchases
        purchaseRepository.deleteAll(userPurchases);

        // Finally delete the user
        userRepository.deleteById(id);
    }

}
