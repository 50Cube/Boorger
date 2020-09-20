package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@javax.persistence.Table(name = "tables")
public class Table extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Digits(integer = 2, fraction = 0)
    private int number;

    @NotNull
    @Digits(integer = 1, fraction = 0)
    private int capacity;

    @NotNull
    private boolean active;

    @OneToMany(mappedBy = "table")
    private Collection<Reservation> reservations = new ArrayList<>();

    @NotNull
    @ManyToOne
    private Restaurant restaurant;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table that = (Table) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (number != that.number) return false;
        if (capacity != that.capacity) return false;
        if (active != that.active) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        return Objects.equals(modificationDate, that.modificationDate);
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
        result = 31 * result + number;
        result = 31 * result + capacity;
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
