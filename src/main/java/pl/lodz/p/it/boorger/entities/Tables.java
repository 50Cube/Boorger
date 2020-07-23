//package pl.lodz.p.it.boorger.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import pl.lodz.p.it.boorger.abstraction.AbstractEntity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import java.util.Collection;
//import java.util.Objects;
//
//@Getter
//@Setter
//@Entity
//public class Tables extends AbstractEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotNull
//    private int number;
//
//    @NotNull
//    private int capacity;
//
//    @NotNull
//    private boolean active;
//
//    @NotNull
//    private long restaurantId;
//
//    @OneToMany
//    private Collection<Reservation> reservationsById;
//
//    @ManyToOne
//    private Restaurant restaurantByRestaurantId;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Tables that = (Tables) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (number != that.number) return false;
//        if (capacity != that.capacity) return false;
//        if (active != that.active) return false;
//        if (restaurantId != that.restaurantId) return false;
//        if (!Objects.equals(businessKey, that.businessKey)) return false;
//        if (!Objects.equals(createdBy, that.createdBy)) return false;
//        if (!Objects.equals(creationDate, that.creationDate)) return false;
//        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
//        return Objects.equals(modificationDate, that.modificationDate);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (businessKey != null ? businessKey.hashCode() : 0);
//        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
//        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
//        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
//        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
//        result = 31 * result + (int) (version ^ (version >>> 32));
//        result = 31 * result + number;
//        result = 31 * result + capacity;
//        result = 31 * result + (active ? 1 : 0);
//        result = 31 * result + (int) (restaurantId ^ (restaurantId >>> 32));
//        return result;
//    }
//}
