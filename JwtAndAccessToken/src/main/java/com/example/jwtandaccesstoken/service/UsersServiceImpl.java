package com.example.jwtandaccesstoken.service;

import com.example.jwtandaccesstoken.domain.Role;
import com.example.jwtandaccesstoken.domain.Users;
import com.example.jwtandaccesstoken.repository.RoleRepository;
import com.example.jwtandaccesstoken.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UsersServiceImpl implements UsersService, UserDetailsService {

    private final UsersRepository usersRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByUsername(username);
        if(users == null ){
            log.error("Users not found in the database");
            throw new UsernameNotFoundException("Users not found in the database");
        } else {
            log.info("Users found in the database: {}", username);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        users.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role.getName()))
        );
        return new org.springframework.security.core.userdetails.User(users.getUsername(), users.getPassword(), authorities);
    }

    @Override
    public Users saveUsers(Users users) {
        log.info("Saving new users {} to the database", users.getName());
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return usersRepository.save(users);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUsers(String username, String roleName) {
        log.info("Adding role {} to users {}", roleName, username);
        Users users = usersRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        users.getRoles().add(role);
    }

    @Override
    public Users getUsers(String username) {
        log.info("Fetching users {}", username);
        return usersRepository.findByUsername(username);
    }

    @Override
    public List<Users> getUsers() {
        log.info("Fetching All users");
        return usersRepository.findAll();
    }

}
