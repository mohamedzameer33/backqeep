// src/main/java/com/qeep/controller/GameController.java

package com.qeep.controller;

import com.qeep.entity.User;
import com.qeep.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final UserService userService;  // ← FIXED: No @Autowired needed with @RequiredArgsConstructor

    @PostMapping("/spin")
    public String spin(@RequestParam Long userId) {
        int won = userService.spinWheel(userId);
        return "You won " + won + " coins!";
    }

    @PostMapping("/buy")
    public String buyPet(@RequestParam Long buyerId, @RequestParam Long petId) {
        return userService.buyPet(buyerId, petId);
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public User getUser(@RequestParam Long id) {
        return userService.getUser(id);
    }

    @GetMapping("/profile")
    public String profile(@RequestParam Long id) {
        User u = userService.getUser(id);
        String owner = u.getCurrentOwnerId() == null ? "Free" :
                userService.getUser(u.getCurrentOwnerId()).getUsername();
        return "Username: " + u.getUsername() +
               "\nCoins: " + u.getCoins() +
               "\nPet Value: " + u.getPetValue() +
               "\nCurrent Owner: " + owner;
    }
}