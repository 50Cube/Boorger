package pl.lodz.p.it.boorger.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.lodz.p.it.boorger.entities.AccessLevel;
import pl.lodz.p.it.boorger.entities.Account;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private String login;
    @JsonIgnore private String password;
    private boolean active;
    private boolean confirmed;
    @Getter private String language;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(Account account) {
        List<GrantedAuthority> authorityList = account.getAccessLevels().stream()
                .filter(AccessLevel::isActive)
                .map(accessLevel -> new SimpleGrantedAuthority(accessLevel.getAccessLevel()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                account.getLogin(),
                account.getPassword(),
                account.isActive(),
                account.isConfirmed(),
                account.getLanguage(),
                authorityList
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return confirmed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if(obj == null || getClass() != obj.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return this.login.equals(user.login);
    }
}
