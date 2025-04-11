package com.example.InsuranceWeb.repository;

import com.example.InsuranceWeb.models.Role;
import com.example.InsuranceWeb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role); // ✅ Add this
    


}
