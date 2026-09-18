package com.dev.driveflowapi.infrastructure.persistence.entity;

import com.dev.driveflowapi.domain.model.FuelType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "vehicle_fuel_types",
            joinColumns = @JoinColumn(name = "vehicle_id")
    )
    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<FuelType> fuelTypes;

    @Column(nullable = false)
    private String color;

    private Integer year;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id", nullable = false)
    private DealerEntity dealer;
}