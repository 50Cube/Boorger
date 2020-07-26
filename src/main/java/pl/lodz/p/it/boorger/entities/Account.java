package pl.lodz.p.it.boorger.entities;

import lombok.*;
import pl.lodz.p.it.boorger.abstraction.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_login_data", schema = "public")
@SecondaryTable(name = "account_personal_data", schema = "public", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class Account extends AbstractEntity {

    @Id
    @Setter(lombok.AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String login;

    @NotBlank
    @Size(min = 60, max = 60)
    private String password;

    @NotNull
    private boolean active;

    @NotNull
    private boolean confirmed;

    @NotBlank
    private String language;

    @NotBlank
    @Column(name = "firstname", table = "account_personal_data")
    private String firstname;

    @NotBlank
    @Column(name = "lastname", table = "account_personal_data")
    private String lastname;

    @NotBlank
    @Column(name = "email", table = "account_personal_data")
    private String email;

    @NotNull
    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.PERSIST)
    private Collection<AccessLevel> accessLevels = new ArrayList<>();

//    @OneToMany
//    private Collection<AccountToken> accountTokensById;

//    @OneToMany
//    private Collection<AuthData> authDataById;

//    @OneToMany
//    private Collection<PasswordHistory> passwordHistoriesById;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account that = (Account) o;

        if (!id.equals(that.id)) return false;
        if (version != that.version) return false;
        if (active != that.active) return false;
        if (confirmed != that.confirmed) return false;
        if (!Objects.equals(businessKey, that.businessKey)) return false;
        if (!Objects.equals(createdBy, that.createdBy)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(modifiedBy, that.modifiedBy)) return false;
        if (!Objects.equals(modificationDate, that.modificationDate)) return false;
        if (!Objects.equals(login, that.login)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(language, that.language)) return false;
        if (!Objects.equals(firstname, that.firstname)) return false;
        if (!Objects.equals(lastname, that.lastname)) return false;
        return Objects.equals(email, that.email);
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
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (confirmed ? 1 : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
