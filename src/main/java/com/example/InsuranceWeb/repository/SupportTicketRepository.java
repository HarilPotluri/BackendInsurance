package com.example.InsuranceWeb.repository;

import com.example.InsuranceWeb.models.SupportTicket;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> 
{
    List<SupportTicket> findByEmailOrderByCreatedAtDesc(String email);

}

