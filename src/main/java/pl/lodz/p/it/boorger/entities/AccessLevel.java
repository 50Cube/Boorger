package pl.lodz.p.it.boorger.entities;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.boorger.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
public class AccessLevel extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String accessLevel;

    @NotNull
    private boolean active;

    @NotNull
    @ManyToOne
    private Account account;

//    @OneToMany
//    private Collection<ClientRestaurants> clientRestaurantsById;
//
//    @OneToMany
//    private Collection<Opinion> opinionsById;
//
//    @OneToMany
//    private Collection<Reservation> reservationsById;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessLevel that = (AccessLevel) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (active != that.active) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        return Objects.equals(accessLevel, that.accessLevel);
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
        result = 31 * result + (accessLevel != null ? accessLevel.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }
}
