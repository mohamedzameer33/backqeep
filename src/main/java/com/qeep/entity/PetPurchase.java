package com.qeep.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class PetPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId;
    private Long petId;
    private int pricePaid;
    private LocalDateTime purchasedAt = LocalDateTime.now();
}