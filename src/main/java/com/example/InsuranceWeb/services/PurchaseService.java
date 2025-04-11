package com.example.InsuranceWeb.services;

import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public boolean renewPolicy(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElse(null);
        if (purchase == null || !"APPROVED".equals(purchase.getStatus())) {
            return false;
        }

        if (purchase.getExpiryDate() == null) {
            // If expiryDate was never set, initialize it from today
            purchase.setExpiryDate(LocalDate.now().plusYears(1));
        } else {
            // Extend existing expiry by 1 year
            purchase.setExpiryDate(purchase.getExpiryDate().plusYears(1));
        }

        purchaseRepository.save(purchase);
        return true;
    }

    // Optional: use this for agent approval as well if needed
    public boolean approvePurchase(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId).orElse(null);
        if (purchase == null || !"PENDING".equals(purchase.getStatus())) {
            return false;
        }

        purchase.setStatus("APPROVED");
        purchase.setExpiryDate(LocalDate.now().plusYears(1));
        purchaseRepository.save(purchase);
        return true;
    }
}
