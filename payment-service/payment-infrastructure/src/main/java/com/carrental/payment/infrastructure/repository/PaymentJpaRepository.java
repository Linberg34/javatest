package com.carrental.payment.infrastructure.repository;

import com.carrental.payment.infrastructure.entity.PaymentEntity;
import com.example.common.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {
    Page<PaymentEntity> findAllByStatus(PaymentStatus status, Pageable pageable);
    Page<PaymentEntity> findAll(Pageable pg);
}
