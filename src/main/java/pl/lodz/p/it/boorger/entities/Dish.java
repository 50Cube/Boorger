package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 64)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@$^&*,. -]+")
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private double price;

    @NotBlank
    @Size(min = 1, max = 255)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*():,./\n -]+")
    private String description;

    @NotNull
    @ManyToOne
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish that = (Dish) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (businessKey != null ? businessKey.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
