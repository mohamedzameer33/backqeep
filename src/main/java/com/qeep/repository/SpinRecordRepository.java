package com.qeep.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qeep.entity.SpinRecord;

public interface SpinRecordRepository extends JpaRepository<SpinRecord, Long> {
    boolean existsByUserIdAndSpinDate(Long userId, LocalDate date);
}