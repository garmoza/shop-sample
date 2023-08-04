package dev.garmoza.shopsample.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class CharacteristicSelect extends Characteristic {

    @ElementCollection(fetch = FetchType.EAGER)
    @NotEmpty
    private List<String> options;

    protected CharacteristicSelect() {
        this.type = Type.SELECT;
    }

    public CharacteristicSelect(String name, List<String> options) {
        super(name);
        this.type = Type.SELECT;
        this.options = options;
    }
}
