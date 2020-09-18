package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Digits(integer = 2, fraction = 0)
    private int guestNumber;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    private double totalPrice;

    @NotNull
    @ManyToOne
    private Client client;

    @NotNull
    @ManyToOne
    private Table table;

    public String getSignatureString() {
        return businessKey + version;
    }

//    @OneToMany
//    private Collection<ReservationMenuMapping> reservationMenuMappings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (client != that.client) return false;
        if (table != that.table) return false;
        if (guestNumber != that.guestNumber) return false;
        if (Double.compare(that.totalPrice, totalPrice) != 0) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(startDate, that.startDate)) return false;
        return Objects.equals(endDate, that.endDate);
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
        result = 31 * result + guestNumber;
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
