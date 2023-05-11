package com.example.sender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sender.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);
}
