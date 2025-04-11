package com.example.InsuranceWeb.services;

import com.example.InsuranceWeb.models.PremiumPayment;
import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.repository.PremiumPaymentRepository;
import com.example.InsuranceWeb.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PremiumPaymentService {

    @Autowired
    private PremiumPaymentRepository paymentRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    public PremiumPayment makePayment(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        if (!"APPROVED".equalsIgnoreCase(purchase.getStatus())) {
            throw new RuntimeException("Only approved purchases can make premium payments.");
        }

        PremiumPayment payment = new PremiumPayment(LocalDate.now(), purchase.getPremium(), purchase);
        return paymentRepository.save(payment);
    }

    public List<PremiumPayment> getPaymentsByEmail(String email) {
        return paymentRepository.findByPurchaseUserEmail(email);
    }
}
