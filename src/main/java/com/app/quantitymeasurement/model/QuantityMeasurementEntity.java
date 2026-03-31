package com.app.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_measurements",
       indexes = {
           @Index(name = "idx_operation", columnList = "operation"),
           @Index(name = "idx_measurement_type", columnList = "thisMeasurementType"),
           @Index(name = "idx_created_at", columnList = "createdAt"),
           @Index(name = "idx_is_error", columnList = "isError")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double thisValue;

    @Column(nullable = false)
    private String thisUnit;

    @Column(nullable = false)
    private String thisMeasurementType;

    @Column(nullable = false)
    private double thatValue;

    @Column(nullable = false)
    private String thatUnit;

    @Column(nullable = false)
    private String thatMeasurementType;

    @Column(nullable = false)
    private String operation;

    @Column
    private String resultString;

    @Column
    private double resultValue;

    @Column
    private String resultUnit;

    @Column
    private String resultMeasurementType;

    @Column
    private String errorMessage;

    @Column(nullable = false)
    private boolean isError;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
