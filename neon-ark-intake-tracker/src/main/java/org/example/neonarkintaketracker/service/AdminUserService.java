package org.example.neonarkintaketracker.service;

import org.example.neonarkintaketracker.dto.SystemUserResponse;
import org.example.neonarkintaketracker.entity.SystemUser;
import org.example.neonarkintaketracker.repository.SystemUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final SystemUserRepository systemUserRepository;

    public AdminUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    public List<SystemUserResponse> getAllUsers() {
        return systemUserRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private SystemUserResponse mapToResponse(SystemUser user) {
        SystemUserResponse response = new SystemUserResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        return response;
    }
}
