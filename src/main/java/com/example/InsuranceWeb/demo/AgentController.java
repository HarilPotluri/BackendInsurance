package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.services.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "http://localhost:3000")
public class AgentController {

    @Autowired
    private AgentService agentService;

    // Get All Purchases
    @GetMapping("/purchases")
    public ResponseEntity<List<Purchase>> getAllCustomerPurchases() {
        List<Purchase> purchases = agentService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    // Get Only Pending Purchases
    @GetMapping("/purchases/pending")
    public ResponseEntity<List<Purchase>> getPendingPurchases() {
        List<Purchase> pendingList = agentService.getPendingPurchases();
        return ResponseEntity.ok(pendingList);
    }

    // Approve Purchase
    @PutMapping("/purchases/{id}/approve")
    public ResponseEntity<String> approvePurchase(@PathVariable Long id) {
        String result = agentService.approvePurchase(id);
        if (result.equals("Purchase not found.")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // Reject Purchase
    @PutMapping("/purchases/{id}/reject")
    public ResponseEntity<String> rejectPurchase(@PathVariable Long id) {
        String result = agentService.rejectPurchase(id);
        if (result.equals("Purchase not found.")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }
    
 // Get Approved Purchases with No Payment this Month
    @GetMapping("/purchases/unpaid")
    public ResponseEntity<List<Purchase>> getUnpaidApprovedPurchases() {
        List<Purchase> unpaidPurchases = agentService.getUnpaidApprovedPurchasesForCurrentMonth();
        return ResponseEntity.ok(unpaidPurchases);
    }
}
