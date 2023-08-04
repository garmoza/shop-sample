package dev.garmoza.shopsample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String description;

    // todo: replace String
    private String logo;

    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.CONFIRMATION;

    @CreationTimestamp
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_ORGANIZATIONS"
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    protected Organization() {
    }

    public Organization(String name, String description, String logo) {
        this.name = name;
        this.description = description;
        this.logo = logo;
    }

    public Organization(String name, String description, String logo, BigDecimal balance) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.balance = balance;
    }

    public Organization(String name, String description, String logo, BigDecimal balance, Status status) {
        this.name = name;
        this.description = description;
        this.logo = logo;
        this.balance = balance;
        this.status = status;
    }

    public enum Status {
        FROZEN, REMOVED, ENABLED, CONFIRMATION
    }
}
