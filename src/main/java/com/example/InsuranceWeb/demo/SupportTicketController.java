// SupportTicketController.java
package com.example.InsuranceWeb.demo;

import com.example.InsuranceWeb.models.SupportTicket;
import com.example.InsuranceWeb.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportTicketController {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    // Get tickets for a specific user
    @GetMapping("/user/{email}")
    public ResponseEntity<List<SupportTicket>> getUserTickets(@PathVariable String email) {
        List<SupportTicket> tickets = supportTicketRepository.findByEmailOrderByCreatedAtDesc(email);
        return ResponseEntity.ok(tickets);
    }

    // (Optional) Create new support ticket — if not done already
    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        ticket.setResolved(false);
        ticket.setCreatedAt(java.time.LocalDateTime.now());
        return ResponseEntity.ok(supportTicketRepository.save(ticket));
    }
}
