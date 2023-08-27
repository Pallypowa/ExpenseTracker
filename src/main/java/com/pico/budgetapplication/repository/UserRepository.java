package com.pico.budgetapplication.repository;

import com.pico.budgetapplication.model.ProjectUserDetails;
import com.pico.budgetapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByUsername(String username);
     Optional<User> findByEmail(String email);
     Optional<ProjectUserDetails> findAllById(Long id);


}