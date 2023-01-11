package com.example.jwtandaccesstoken;

import com.example.jwtandaccesstoken.domain.Role;
import com.example.jwtandaccesstoken.domain.Users;
import com.example.jwtandaccesstoken.service.UsersService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class JwtAndAccessTokenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAndAccessTokenApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UsersService usersService) {
        return args -> {
            usersService.saveRole(new Role(null, "ROLE_USER"));
            usersService.saveRole(new Role(null, "ROLE_MANAGER"));
            usersService.saveRole(new Role(null, "ROLE_ADMIN"));
            usersService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

            usersService.saveUsers(new Users(null, "John Travolta", "john", "1234", new ArrayList<>()));
            usersService.saveUsers(new Users(null, "Will Smith", "will", "1234", new ArrayList<>()));
            usersService.saveUsers(new Users(null, "Jim Carry", "jim", "1234", new ArrayList<>()));
            usersService.saveUsers(new Users(null, "Arnold Schwarzenegger", "arnold", "1234", new ArrayList<>()));

            usersService.addRoleToUsers("john", "ROLE_USER");
            usersService.addRoleToUsers("john", "ROLE_MANAGER");
            usersService.addRoleToUsers("will", "ROLE_MANAGER");
            usersService.addRoleToUsers("jim", "ROLE_ADMIN");
            usersService.addRoleToUsers("arnold", "ROLE_SUPER_ADMIN");
            usersService.addRoleToUsers("arnold", "ROLE_ADMIN");
            usersService.addRoleToUsers("arnold", "ROLE_USER");
        };
    }
}
