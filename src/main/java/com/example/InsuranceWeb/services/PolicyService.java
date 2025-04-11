package com.example.InsuranceWeb.services;

import com.example.InsuranceWeb.models.Policy;
import com.example.InsuranceWeb.repository.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    public List<Policy> getPoliciesByType(String type) {
        return policyRepository.findByType(type);
    }

    public Policy savePolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    public Optional<Policy> getPolicyById(Long id) {
        return policyRepository.findById(id);
    }
}
