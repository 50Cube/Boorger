package pl.lodz.p.it.boorger.entities;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "token_type")
public abstract class AccountToken extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private LocalDateTime expireDate;

    @NotBlank
    @Column(name = "token_type", nullable = false, insertable = false, updatable = false)
    private String tokenType;

    @ManyToOne
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountToken that = (AccountToken) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(expireDate, that.expireDate)) return false;
        return Objects.equals(tokenType, that.tokenType);
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
        result = 31 * result + (expireDate != null ? expireDate.hashCode() : 0);
        result = 31 * result + (tokenType != null ? tokenType.hashCode() : 0);
        return result;
    }
}
