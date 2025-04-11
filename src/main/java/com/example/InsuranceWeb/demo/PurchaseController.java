package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.Policy;
import com.example.InsuranceWeb.models.PremiumPayment;
import com.example.InsuranceWeb.models.Purchase;
import com.example.InsuranceWeb.models.SupportTicket;
import com.example.InsuranceWeb.models.User;
import com.example.InsuranceWeb.repository.PolicyRepository;
import com.example.InsuranceWeb.repository.PremiumPaymentRepository;
import com.example.InsuranceWeb.repository.PurchaseRepository;
import com.example.InsuranceWeb.repository.SupportTicketRepository;
import com.example.InsuranceWeb.repository.UserRepository;
import com.example.InsuranceWeb.services.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PolicyRepository policyRepository;
    
    @Autowired
    private PremiumPaymentRepository premiumPaymentRepository;
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
    @Autowired
    private PurchaseService purchaseService;



    // -------------------- Create Purchase (Customer) --------------------
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> makePurchase(
            @RequestParam("email") String userEmail,
            @RequestParam("policyId") Long policyId,
            @RequestParam("fullName") String fullName,
            @RequestParam("dob") String dob,
            @RequestParam("address") String address,
            @RequestParam("occupation") String occupation,
            @RequestParam("aadharFile") MultipartFile aadharFile
    ) {
        try {
            System.out.println("➡️ Received purchase request from: " + userEmail);

            // Validate user
            Optional<User> optionalUser = userRepository.findByEmail(userEmail);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid user email.");
            }
            User user = optionalUser.get();

            // Validate policy
            Optional<Policy> optionalPolicy = policyRepository.findById(policyId);
            if (optionalPolicy.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid policy ID.");
            }
            Policy policy = optionalPolicy.get();

            String title = policy.getName();
            String policyType = policy.getType();
            double premium = policy.getPremium();
            String purchaseDate = LocalDate.now().toString();

            // Set expiryDate to 1 year after purchaseDate
            LocalDate expiryDate = LocalDate.now().plusYears(1);

            // Save Aadhar file to uploads/aadhar
            String uploadFolder = System.getProperty("user.dir") + File.separator + "uploads" + File.separator + "aadhar";
            File dir = new File(uploadFolder);
            if (!dir.exists()) dir.mkdirs();

            String fileName = UUID.randomUUID() + "_" + aadharFile.getOriginalFilename();
            File destFile = new File(uploadFolder + File.separator + fileName);
            aadharFile.transferTo(destFile);

            // ✅ Store relative path only in DB (for frontend use)
            String relativePath = "aadhar/" + fileName;

            // Save purchase request with status "PENDING"
            Purchase purchase = new Purchase(
                    policyType,
                    premium,
                    purchaseDate,
                    title,
                    userEmail,
                    fullName,
                    dob,
                    address,
                    occupation,
                    relativePath, // ✅ Correct path saved
                    user,
                    policy
            );

            // Set expiryDate for the purchase
            purchase.setExpiryDate(expiryDate);
            purchase.setStatus("PENDING"); // Agent must approve
            purchaseRepository.save(purchase);

            System.out.println("✅ Purchase saved successfully.");
            return ResponseEntity.ok("Purchase request submitted for agent approval.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload Aadhar file.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error while processing purchase: " + e.getMessage());
        }
    }

 // ✅ New endpoint: Get ALL purchases for MyPurchases view
    @GetMapping("/email/all/{email}")
    public ResponseEntity<?> getAllPurchasesByEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOptional.get();
        List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());

        // 🔁 No filtering, return all purchases regardless of status
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getPurchasesByEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        User user = userOptional.get();
        List<Purchase> purchases = purchaseRepository.findByUserId(user.getId());

        // Filter out purchases already paid for this month
        List<Purchase> filtered = purchases.stream()
            .filter(p -> "APPROVED".equals(p.getStatus()))
            .filter(p -> {
                List<PremiumPayment> payments = premiumPaymentRepository.findByPurchaseId(p.getId());
                return payments.stream().noneMatch(payment ->
                    payment.getPaymentDate().getMonth().equals(LocalDate.now().getMonth()) &&
                    payment.getPaymentDate().getYear() == LocalDate.now().getYear()
                );
            })
            .toList();

        return ResponseEntity.ok(filtered);
    }
    
 // PurchaseController.java
    @PutMapping("/renew/{purchaseId}")
    public ResponseEntity<String> renewPolicy(@PathVariable Long purchaseId) {
        boolean renewed = purchaseService.renewPolicy(purchaseId);
        if (renewed) {
            return ResponseEntity.ok("Policy renewed successfully.");
        } else {
            return ResponseEntity.badRequest().body("Policy renewal failed.");
        }
    }
    
    @PostMapping("/ticket")
    public ResponseEntity<String> createTicket(@RequestBody SupportTicket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setResolved(false);
        supportTicketRepository.save(ticket);
        return ResponseEntity.ok("Ticket submitted successfully.");
    }



}
