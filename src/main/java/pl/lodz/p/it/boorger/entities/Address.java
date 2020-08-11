//package pl.lodz.p.it.boorger.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import java.util.Collection;
//import java.util.Objects;
//
//@Getter
//@Setter
//@Entity
//public class Address extends AbstractEntity {
//
//    @Id
//    @Setter(lombok.AccessLevel.NONE)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;
//
//    @NotBlank
//    private String street;
//
//    @NotBlank
//    private int streetNo;
//
//    @NotBlank
//    private String city;
//
//    @OneToMany
//    private Collection<Restaurant> restaurantsById;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Address that = (Address) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (streetNo != that.streetNo) return false;
//        if (!Objects.equals(businessKey, that.businessKey)) return false;
//        if (!Objects.equals(createdBy, that.createdBy)) return false;
//        if (!Objects.equals(creationDate, that.creationDate)) return false;
//        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
//        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
//        if (!Objects.equals(street, that.street)) return false;
//        return Objects.equals(city, that.city);
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
//        result = 31 * result + (street != null ? street.hashCode() : 0);
//        result = 31 * result + streetNo;
//        result = 31 * result + (city != null ? city.hashCode() : 0);
//        return result;
//    }
//}
