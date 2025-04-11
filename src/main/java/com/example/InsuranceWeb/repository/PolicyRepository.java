package com.example.InsuranceWeb.repository;

import com.example.InsuranceWeb.models.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    List<Policy> findByType(String type); // to fetch policies by type
}
