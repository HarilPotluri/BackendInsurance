package com.example.InsuranceWeb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.InsuranceWeb.models.Notification;
import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.repository.NotificationRepository;
import com.example.InsuranceWeb.repository.PurchaseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public void notifyCustomer(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Notification notification = new Notification();
        notification.setMessage("Reminder: Please pay your premium for policy - " + purchase.getTitle());
        notification.setTimestamp(LocalDateTime.now());
        notification.setUser(purchase.getUser());

        notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByTimestampDesc(userId);
    }
}
