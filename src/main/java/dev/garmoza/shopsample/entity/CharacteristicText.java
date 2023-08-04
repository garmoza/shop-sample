package dev.garmoza.shopsample.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CharacteristicText extends Characteristic {

    protected CharacteristicText() {
        this.type = Type.TEXT;
    }

    public CharacteristicText(String name) {
        super(name);
        this.type = Type.TEXT;
    }
}
