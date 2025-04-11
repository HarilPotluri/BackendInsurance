package com.example.InsuranceWeb.repository;

import com.example.InsuranceWeb.models.PremiumPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PremiumPaymentRepository extends JpaRepository<PremiumPayment, Long> {
    List<PremiumPayment> findByPurchaseUserEmail(String email);
    List<PremiumPayment> findByPurchaseId(Long purchaseId);

}
