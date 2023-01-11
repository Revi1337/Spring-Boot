package com.example.jwtandaccesstoken.controller;

import com.example.jwtandaccesstoken.domain.Role;
import com.example.jwtandaccesstoken.domain.Users;
import com.example.jwtandaccesstoken.service.UsersService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(usersService.getUsers());
    }

    @PostMapping("/users/save")
    public ResponseEntity<Users> saveUsers(@RequestBody Users users) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/save")
                .toUriString()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usersService.saveUsers(users));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString()
        );

         return ResponseEntity.status(HttpStatus.CREATED)
                 .body(usersService.saveRole(role));
    }

    @PostMapping("/role/addtousers")
    public ResponseEntity<?> addRoleToUsers(@RequestBody RoleToUsersForm form){
        usersService.addRoleToUsers(form.getUsername(), form.getRoleName());

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}

@Data
class RoleToUsersForm {
    private String username;
    private String roleName;
}
