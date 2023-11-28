package com.Jwt.project.web.rest;

import com.Jwt.project.domain.User;
import com.Jwt.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity create(@RequestBody User user){

        User secret=userService.save(user);
        return ResponseEntity.ok(secret);

    }

    @GetMapping("/users")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(userService.findAll());
    }
}
