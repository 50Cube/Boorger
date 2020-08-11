package pl.lodz.p.it.boorger.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lodz.p.it.boorger.entities.AuthData;

public interface AuthDataRepository extends JpaRepository<AuthData, Long> {
}
