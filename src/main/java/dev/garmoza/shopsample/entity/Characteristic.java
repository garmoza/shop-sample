package dev.garmoza.shopsample.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    protected Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Category category;

    protected Characteristic() {
    }

    protected Characteristic(String name) {
        this.name = name;
    }

    public enum Type {
        TEXT, SELECT
    }
}
