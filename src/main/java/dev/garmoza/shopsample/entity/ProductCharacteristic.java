package dev.garmoza.shopsample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class ProductCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    protected Characteristic.Type type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(
            name = "PRODUCT_CHARACTERISTIC_CHARACTERISTIC",
            joinColumns = @JoinColumn(name = "PRODUCT_CHARACTERISTIC_ID", unique = true),
            inverseJoinColumns = @JoinColumn(name = "CHARACTERISTIC_ID")
    )
    @NotNull
    private Characteristic characteristic;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Product product;

    protected ProductCharacteristic() {
    }
}
