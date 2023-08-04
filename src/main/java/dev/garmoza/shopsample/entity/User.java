package dev.garmoza.shopsample.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    @JsonIgnore
    private String password;

    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.CONFIRMATION;

    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty
    private Set<String> authorities;

    protected User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(String username, String email, String password, BigDecimal balance) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }

    public User(String username, String email, String password, BigDecimal balance, Status status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.balance = balance;
        this.status = status;
    }

    public enum Status {
        FROZEN, REMOVED, ENABLED, CONFIRMATION
    }

}
