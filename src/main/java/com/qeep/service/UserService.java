package com.qeep.service;

import com.qeep.entity.*;
import com.qeep.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final SpinRecordRepository spinRepo;
    private final PetPurchaseRepository purchaseRepo;

    // Constructor injection (BEST PRACTICE — removes @Autowired warning)
    public UserService(UserRepository userRepo, SpinRecordRepository spinRepo, PetPurchaseRepository purchaseRepo) {
        this.userRepo = userRepo;
        this.spinRepo = spinRepo;
        this.purchaseRepo = purchaseRepo;
    }

    public User register(String username, String email, String pass, String repeatPass) {
        if (!pass.equals(repeatPass)) throw new RuntimeException("Passwords don't match");
        if (userRepo.findByUsername(username) != null) throw new RuntimeException("Username taken");

        User u = new User();
        u.setUsername(username);
        u.setEmail(email.isEmpty() ? null : email);
        u.setPassword(pass);
        u.setCoins(100);
        u.setPetValue(20);
        u.setLastSpinDate(null);  // Important: null = never spun
        return userRepo.save(u);
    }

    public User login(String username, String password) {
        User u = userRepo.findByUsername(username);
        if (u != null && u.getPassword().equals(password)) {
            return u;
        }
        throw new RuntimeException("Invalid credentials");
    }

    // SPIN WHEEL — FIXED & PERFECT
    public int spinWheel(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        // Check if already spun today
        if (user.getLastSpinDate() != null && user.getLastSpinDate().equals(today)) {
            throw new RuntimeException("You already spun today! Come back tomorrow");
        }

        int won = 10 + new Random().nextInt(91); // 10–100 coins
        user.setCoins(user.getCoins() + won);
        user.setLastSpinDate(today);
        userRepo.save(user);

        // Optional: record spin
        SpinRecord rec = new SpinRecord();
        rec.setUserId(userId);
        rec.setCoinsWon(won);
        rec.setSpinDate(today);
        spinRepo.save(rec);

        return won;
    }

    // BUY PET — FINAL PRICE INCREASE (NO MORE 90 → 230 BUG)
    public String buyPet(Long buyerId, Long petId) {
        if (buyerId.equals(petId)) {
            throw new RuntimeException("You can't buy yourself!");
        }

        User buyer = userRepo.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        User pet = userRepo.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        int price = pet.getPetValue();
        if (buyer.getCoins() < price) {
            throw new RuntimeException("Not enough coins!");
        }

        // Deduct coins from buyer
        buyer.setCoins(buyer.getCoins() - price);

        // EXACT PRICE INCREASE — ONLY BACKEND DOES THIS
        int newValue = switch (pet.getPetValue()) {
            case 20  -> 22;
            case 22  -> 26;
            case 26  -> 30;
            case 30  -> 40;
            case 40  -> 55;
            case 55  -> 75;
            case 75  -> 100;
            case 100 -> 130;
            case 130 -> 160;
            default  -> pet.getPetValue() + 30;
        };

        pet.setPetValue(newValue);
        pet.setCurrentOwnerId(buyerId);

        // Save both
        userRepo.save(buyer);
        userRepo.save(pet);

        // Optional: record purchase
        PetPurchase purchase = new PetPurchase();
        purchase.setBuyerId(buyerId);
        purchase.setPetId(petId);
        purchase.setPricePaid(price);
        purchaseRepo.save(purchase);

        return "You bought " + pet.getUsername() + " for " + price + " coins!";
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUser(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}