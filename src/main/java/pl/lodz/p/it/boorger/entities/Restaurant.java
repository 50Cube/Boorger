package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@*,. -]+")
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    @Pattern(regexp = "[a-zA-Z0-9ąćęłńóśźżĄĆĘŁŃÓŚŹŻ!@#$%^&*,. -]+")
    private String description;

    @NotNull
    @Digits(integer = 3, fraction = 0)
    private int installment;

    @NotNull
    private boolean active;
    private byte[] photo;

    @NotNull
    @ManyToOne
    private Address address;

    @NotNull
    @OneToOne(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    private Hours hours;

    @Builder.Default
    @OneToMany(mappedBy = "restaurant")
    private Collection<Dish> dishes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.PERSIST)
    private Collection<Table> tables = new ArrayList<>();

//    @OneToMany
//    private Collection<ClientRestaurants> clientRestaurants;
//
//    @OneToMany
//    private Collection<Opinion> opinions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (installment != that.installment) return false;
        if (active != that.active) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(description, that.description)) return false;
        return (!Arrays.equals(photo, that.photo));
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (businessKey != null ? businessKey.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + installment;
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + Arrays.hashCode(photo);
        return result;
    }
}
