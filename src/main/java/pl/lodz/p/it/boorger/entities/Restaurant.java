//package pl.lodz.p.it.boorger.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Objects;
//
//@Getter
//@Setter
//@Entity
//public class Restaurant extends AbstractEntity {
//
//    @Id
//    @Setter(lombok.AccessLevel.NONE)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;
//
//    @NotBlank
//    private String name;
//
//    @NotNull
//    private long addressId;
//
//    @NotBlank
//    private String description;
//
//    @NotNull
//    private int installment;
//
//    @NotNull
//    private boolean active;
//    private byte[] photo;
//
//    @NotNull
//    private Long hoursId;
//
//    @NotNull
//    private long menuId;
//
//    @OneToMany
//    private Collection<ClientRestaurants> clientRestaurantsById;
//
//    @OneToMany
//    private Collection<Opinion> opinionsById;
//
//    @ManyToOne
//    private Address addressByAddressId;
//
//    @ManyToOne
//    private Hours hoursByHoursId;
//
//    @ManyToOne
//    private Menu menuByMenuId;
//
//    @OneToMany
//    private Collection<Tables> tablesById;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Restaurant that = (Restaurant) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (addressId != that.addressId) return false;
//        if (installment != that.installment) return false;
//        if (active != that.active) return false;
//        if (menuId != that.menuId) return false;
//        if (!Objects.equals(businessKey, that.businessKey)) return false;
//        if (!Objects.equals(createdBy, that.createdBy)) return false;
//        if (!Objects.equals(creationDate, that.creationDate)) return false;
//        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
//        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
//        if (!Objects.equals(name, that.name)) return false;
//        if (!Objects.equals(description, that.description)) return false;
//        if (!Arrays.equals(photo, that.photo)) return false;
//        return Objects.equals(hoursId, that.hoursId);
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
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (int) (addressId ^ (addressId >>> 32));
//        result = 31 * result + (description != null ? description.hashCode() : 0);
//        result = 31 * result + installment;
//        result = 31 * result + (active ? 1 : 0);
//        result = 31 * result + Arrays.hashCode(photo);
//        result = 31 * result + (hoursId != null ? hoursId.hashCode() : 0);
//        result = 31 * result + (int) (menuId ^ (menuId >>> 32));
//        return result;
//    }
//}
