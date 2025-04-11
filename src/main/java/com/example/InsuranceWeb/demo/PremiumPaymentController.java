package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.PremiumPayment;
import com.example.InsuranceWeb.services.PremiumPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/premium")
@CrossOrigin(origins = "http://localhost:3000")
public class PremiumPaymentController {

    @Autowired
    private PremiumPaymentService paymentService;

    @PostMapping("/pay/{purchaseId}")
    public PremiumPayment payPremium(@PathVariable Long purchaseId) {
        return paymentService.makePayment(purchaseId);
    }

    @GetMapping("/history")
    public List<PremiumPayment> getPaymentHistory(@RequestParam String email) {
        return paymentService.getPaymentsByEmail(email);
    }
}
