package org.example.neonarkintaketracker.repository;

import org.example.neonarkintaketracker.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
}