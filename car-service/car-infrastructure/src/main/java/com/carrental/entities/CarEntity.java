package com.carrental.entities;

import com.example.common.entity.BaseAuditableEntity;
import com.example.common.enums.CarStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cars")
@Getter @Setter @NoArgsConstructor  @AllArgsConstructor  @Builder
public class CarEntity  extends BaseAuditableEntity {

    @Column(nullable = false)
    private String make;

    @Column(nullable = false)
    private String model;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

}
