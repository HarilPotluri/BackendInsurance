package com.example.InsuranceWeb.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InsuranceWeb.models.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);
}
