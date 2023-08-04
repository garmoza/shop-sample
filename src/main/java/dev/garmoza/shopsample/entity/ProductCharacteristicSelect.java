package dev.garmoza.shopsample.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class ProductCharacteristicSelect extends ProductCharacteristic {

    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty
    private Set<String> selectedOptions;

    protected ProductCharacteristicSelect() {
        this.type = Characteristic.Type.SELECT;
    }

    public ProductCharacteristicSelect(Set<String> selectedOptions) {
        this.type = Characteristic.Type.SELECT;
        this.selectedOptions = selectedOptions;
    }
}
