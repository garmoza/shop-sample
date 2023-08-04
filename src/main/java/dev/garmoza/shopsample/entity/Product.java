package dev.garmoza.shopsample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    @Positive
    private BigDecimal price;

    @Min(0)
    private long stock;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.CONFIRMATION;

    @CreationTimestamp
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinTable(
            name = "PRODUCT_CATEGORY",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"), // id by default
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID") // category_id by default
    )
    @NotNull
    private Category category;

    protected Product() {
    }

    public Product(String name, String description, BigDecimal price, long stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public enum Status {
        FROZEN, REMOVED, ENABLED, CONFIRMATION
    }
}
