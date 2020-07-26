//package pl.lodz.p.it.boorger.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//import pl.lodz.p.it.boorger.abstraction.AbstractEntity;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import java.util.Objects;
//
//@Getter
//@Setter
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "relation_type")
//public abstract class ClientRestaurants extends AbstractEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    private Long id;
//
//    @NotNull
//    private long clientId;
//
//    @NotNull
//    private long restaurantId;
//
//    @NotBlank
//    private String relationType;
//
//    @ManyToOne
//    private AccessLevel accessLevelByClientId;
//
//    @ManyToOne
//    private Restaurant restaurantByRestaurantId;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        ClientRestaurants that = (ClientRestaurants) o;
//
//        if (id != that.id) return false;
//        if (version != that.version) return false;
//        if (clientId != that.clientId) return false;
//        if (restaurantId != that.restaurantId) return false;
//        if (!Objects.equals(businessKey, that.businessKey)) return false;
//        if (!Objects.equals(createdBy, that.createdBy)) return false;
//        if (!Objects.equals(creationDate, that.creationDate)) return false;
//        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
//        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
//        return Objects.equals(relationType, that.relationType);
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
//        result = 31 * result + (int) (clientId ^ (clientId >>> 32));
//        result = 31 * result + (int) (restaurantId ^ (restaurantId >>> 32));
//        result = 31 * result + (relationType != null ? relationType.hashCode() : 0);
//        return result;
//    }
//}
