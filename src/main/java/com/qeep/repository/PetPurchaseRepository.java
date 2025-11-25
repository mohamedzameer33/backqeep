package com.qeep.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qeep.entity.PetPurchase;

public interface PetPurchaseRepository extends JpaRepository<PetPurchase, Long> {
    List<PetPurchase> findByPetIdOrderByPurchasedAtDesc(Long petId);
}