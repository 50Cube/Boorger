//package pl.lodz.p.it.boorger.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.util.Collection;
//import java.util.Objects;
//
//@Getter
//@Setter
//@Entity
//public class Menu extends AbstractEntity {
//
//    @Id
//    @Setter(lombok.AccessLevel.NONE)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;
//
//    @NotBlank
//    private String dishName;
//
//    @NotNull
//    private double price;
//
//    @NotBlank
//    private String description;
//
//    @NotNull
//    private boolean active;
//
//    @OneToMany
//    private Collection<ReservationMenuMapping> reservationMenuMappingsById;
//
//    @OneToMany
//    private Collection<Restaurant> restaurantsById;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Menu that = (Menu) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (Double.compare(that.price, price) != 0) return false;
//        if (active != that.active) return false;
//        if (!Objects.equals(businessKey, that.businessKey)) return false;
//        if (!Objects.equals(createdBy, that.createdBy)) return false;
//        if (!Objects.equals(creationDate, that.creationDate)) return false;
//        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
//        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
//        if (!Objects.equals(dishName, that.dishName)) return false;
//        return Objects.equals(description, that.description);
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        result = (int) (id ^ (id >>> 32));
//        result = 31 * result + (businessKey != null ? businessKey.hashCode() : 0);
//        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
//        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
//        result = 31 * result + (modifiedBy != null ? modifiedBy.hashCode() : 0);
//        result = 31 * result + (modificationDate != null ? modificationDate.hashCode() : 0);
//        result = 31 * result + (int) (version ^ (version >>> 32));
//        result = 31 * result + (dishName != null ? dishName.hashCode() : 0);
//        temp = Double.doubleToLongBits(price);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        result = 31 * result + (description != null ? description.hashCode() : 0);
//        result = 31 * result + (active ? 1 : 0);
//        return result;
//    }
//}
