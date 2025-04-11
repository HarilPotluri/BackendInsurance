package com.example.InsuranceWeb.services;

import com.example.InsuranceWeb.models.PremiumPayment;
import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.repository.PremiumPaymentRepository;
import com.example.InsuranceWeb.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    private final PurchaseRepository purchaseRepository;
    private final PremiumPaymentRepository premiumPaymentRepository;

    public AgentService(PurchaseRepository purchaseRepository, PremiumPaymentRepository premiumPaymentRepository) {
        this.purchaseRepository = purchaseRepository;
        this.premiumPaymentRepository = premiumPaymentRepository;
    }

    // Get All Purchases
    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    // Get Pending Purchases
    public List<Purchase> getPendingPurchases() {
        return purchaseRepository.findByStatus("PENDING");
    }

    // Approve Purchase
    public String approvePurchase(Long id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) {
            return "Purchase not found.";
        }

        Purchase purchase = optionalPurchase.get();
        purchase.setStatus("APPROVED");
        purchaseRepository.save(purchase);

        return "Purchase approved successfully.";
    }

    // Reject Purchase
    public String rejectPurchase(Long id) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isEmpty()) {
            return "Purchase not found.";
        }

        Purchase purchase = optionalPurchase.get();
        purchase.setStatus("REJECTED");
        purchaseRepository.save(purchase);

        return "Purchase rejected successfully.";
    }

    // ✅ Get Approved Purchases Where Premium Not Paid This Month
    public List<Purchase> getUnpaidApprovedPurchasesForCurrentMonth() {
        List<Purchase> approvedPurchases = purchaseRepository.findByStatus("APPROVED");

        return approvedPurchases.stream()
                .filter(purchase -> {
                    List<PremiumPayment> payments = premiumPaymentRepository.findByPurchaseId(purchase.getId());
                    return payments.stream().noneMatch(payment ->
                            payment.getPaymentDate().getMonth().equals(LocalDate.now().getMonth()) &&
                            payment.getPaymentDate().getYear() == LocalDate.now().getYear()
                    );
                })
                .toList();
    }
}
