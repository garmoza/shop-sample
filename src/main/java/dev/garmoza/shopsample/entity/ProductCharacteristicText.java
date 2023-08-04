package dev.garmoza.shopsample.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductCharacteristicText extends ProductCharacteristic {

    @NotBlank
    private String value;

    protected ProductCharacteristicText() {
        this.type = Characteristic.Type.TEXT;
    }

    public ProductCharacteristicText(String value) {
        this.value = value;
        this.type = Characteristic.Type.TEXT;
    }
}
