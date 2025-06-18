package com.carrental.payment.infrastructure.implementations;

import com.carrental.payment.domain.entity.Payment;
import com.carrental.payment.domain.repository.PaymentRepository;
import com.carrental.payment.infrastructure.entity.PaymentEntity;
import com.carrental.payment.infrastructure.mapper.PaymentMapper;
import com.carrental.payment.infrastructure.repository.PaymentJpaRepository;
import com.example.common.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;
    private final PaymentMapper paymentMapper;

    public PaymentRepositoryImpl(PaymentJpaRepository jpaRepository, PaymentMapper paymentMapper) {
        this.jpaRepository = jpaRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity savedEntity = jpaRepository.save(entity);
        return paymentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return jpaRepository.findById(id).map(paymentMapper::toDomain);
    }

    @Override
    public List<Payment> findAll(int page, int size) {
        Page<PaymentEntity> pageResult = jpaRepository.findAll(PageRequest.of(page, size));
        return pageResult.getContent().stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> findByStatus(PaymentStatus status, int page, int size) {
        Page<PaymentEntity> pageResult = jpaRepository.findAllByStatus(status, PageRequest.of(page, size));
        return pageResult.getContent().stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }


}