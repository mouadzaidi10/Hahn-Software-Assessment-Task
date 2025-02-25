package com.hahnsoftware.ticketsystem.repository;

import com.hahnsoftware.ticketsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}