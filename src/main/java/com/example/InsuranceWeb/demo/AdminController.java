package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.User;
import com.example.InsuranceWeb.models.Policy;
import com.example.InsuranceWeb.models.Role;
import com.example.InsuranceWeb.models.SupportTicket;
import com.example.InsuranceWeb.repository.UserRepository;
import com.example.InsuranceWeb.services.UserService;
import com.example.InsuranceWeb.repository.PolicyRepository;
import com.example.InsuranceWeb.repository.SupportTicketRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PolicyRepository policyRepository;

    // View all customers
    @GetMapping("/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        List<User> customers = userRepository.findByRole(Role.CUSTOMER);
        return ResponseEntity.ok(customers);
    }

    // View all agents
    @GetMapping("/agents")
    public ResponseEntity<List<User>> getAllAgents() {
        List<User> agents = userRepository.findByRole(Role.AGENT);
        return ResponseEntity.ok(agents);
    }

    // View all support tickets
    @GetMapping("/tickets")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(supportTicketRepository.findAll());
    }

 // Mark ticket as resolved (log if ticket is already resolved)
    @PutMapping("/tickets/{id}/resolve")
    public ResponseEntity<?> resolveTicket(@PathVariable Long id) {
        return supportTicketRepository.findById(id).map(ticket -> {
            if (ticket.isResolved()) {
                return ResponseEntity.ok(ticket); // ✅ already resolved
            }
            ticket.setResolved(true);
            supportTicketRepository.save(ticket);
            return ResponseEntity.ok(ticket); // ✅ return the updated ticket
        }).orElse(null);
    }

    
 // Delete a user by ID
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
    
 // 1. Get all policies
    @GetMapping("/policies")
    public ResponseEntity<List<Policy>> getAllPolicies() {
        return ResponseEntity.ok(policyRepository.findAll());
    }

    // 2. Add a new policy
    @PostMapping("/policies")
    public ResponseEntity<Policy> addPolicy(@RequestBody Policy policy) {
        return ResponseEntity.ok(policyRepository.save(policy));
    }

    // 3. Update an existing policy
    @PutMapping("/policies/{id}")
    public ResponseEntity<?> updatePolicy(@PathVariable Long id, @RequestBody Policy updatedPolicy) {
        return policyRepository.findById(id).map(existing -> {
            existing.setName(updatedPolicy.getName());
            existing.setType(updatedPolicy.getType());
            existing.setPremium(updatedPolicy.getPremium());
            existing.setDescription(updatedPolicy.getDescription());
            policyRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseThrow();
    }

    // 4. Delete a policy
    @DeleteMapping("/policies/{id}")
    public ResponseEntity<String> deletePolicy(@PathVariable Long id) {
        if (policyRepository.existsById(id)) {
            policyRepository.deleteById(id);
            return ResponseEntity.ok("Policy deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Policy not found");
        }
    }
    



}
