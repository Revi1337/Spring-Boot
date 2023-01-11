package com.example.jwtandaccesstoken.repository;

import com.example.jwtandaccesstoken.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}

