package com.example.jwtandaccesstoken.service;

import com.example.jwtandaccesstoken.domain.Role;
import com.example.jwtandaccesstoken.domain.Users;

import java.util.List;

public interface UsersService {

    Users saveUsers(Users users);

    Role saveRole(Role role);

    void addRoleToUsers(String username, String roleName);

    Users getUsers(String username);

    List<Users> getUsers();
}
