package com.example.springsecurityauthenticationmanagerandauthenticationprovider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
public class SecurityController {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping(value = {"/who/call/**", "/*/who/call/**"}, consumes = MediaType.ALL_VALUE)
    public String parse(@PathVariable(name = "dummy", required = false) String dum,
                        @RequestHeader HttpHeaders httpHeaders
    )
    {
        httpHeaders.forEach((s, strings) -> {
            System.out.print(s + " : ");
            strings.forEach(System.out::print);
            System.out.println();
        });
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("parse called :)");
        log.info("Authenticatied? : {}", authentication.isAuthenticated());
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .toUriString();
    }


    @GetMapping("/mapping")
    ResponseEntity<? extends String> mapping(HttpEntity httpEntity) {
        System.out.println(httpEntity.getBody());
        return ResponseEntity.status(HttpStatus.OK)
                .body(httpEntity.toString());
    }
}
