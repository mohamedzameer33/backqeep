package com.qeep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qeep.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByUsernameOrEmail(String username, String email);
}