package pl.lodz.p.it.boorger.entities;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.boorger.entities.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
public class AuthData extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDateTime lastSuccessfulAuth;
    private LocalDateTime lastFailedAuth;
    private String lastAuthIp;

    @NotNull
    private int failedAuthCounter;

    @NotNull
    @OneToOne
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuthData that = (AuthData) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (failedAuthCounter != that.failedAuthCounter) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(lastSuccessfulAuth, that.lastSuccessfulAuth)) return false;
        if (!Objects.equals(lastFailedAuth, that.lastFailedAuth)) return false;
        return Objects.equals(lastAuthIp, that.lastAuthIp);
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
        result = 31 * result + (lastSuccessfulAuth != null ? lastSuccessfulAuth.hashCode() : 0);
        result = 31 * result + (lastFailedAuth != null ? lastFailedAuth.hashCode() : 0);
        result = 31 * result + (lastAuthIp != null ? lastAuthIp.hashCode() : 0);
        result = 31 * result + failedAuthCounter;
        return result;
    }
}
