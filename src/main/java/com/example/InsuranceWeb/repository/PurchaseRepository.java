package com.example.InsuranceWeb.repository;

import com.example.InsuranceWeb.models.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByStatus(String status);
    List<Purchase> findByUserId(Long userId);
    void deleteByUserId(Long userId);

}
