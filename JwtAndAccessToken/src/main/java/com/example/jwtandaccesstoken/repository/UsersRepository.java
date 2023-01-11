package com.example.jwtandaccesstoken.repository;

import com.example.jwtandaccesstoken.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Users findByUsername(String name);

}
