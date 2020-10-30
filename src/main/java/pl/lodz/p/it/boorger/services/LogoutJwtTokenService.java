package pl.lodz.p.it.boorger.services;

import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import pl.lodz.p.it.boorger.configuration.transactions.ServiceTransaction;
import pl.lodz.p.it.boorger.entities.LogoutJwtToken;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.exceptions.DatabaseException;
import pl.lodz.p.it.boorger.repositories.LogoutJwtTokenRepository;

import java.time.ZoneId;
import java.util.Date;

@Log
@Service
@ServiceTransaction
@AllArgsConstructor
@Retryable(value = TransactionException.class)
public class LogoutJwtTokenService {

    private LogoutJwtTokenRepository logoutJwtTokenRepository;
    private Environment env;

    public void handleLogout(String token) throws AppBaseException {
        try {
            LogoutJwtToken logoutJwtToken = new LogoutJwtToken();
            logoutJwtToken.setToken(token);

            Date expireDate = Jwts.parser().setSigningKey(env.getProperty("JWT_SECRET")).parseClaimsJws(token).getBody().getExpiration();
            logoutJwtToken.setExpireDate(expireDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            logoutJwtTokenRepository.saveAndFlush(logoutJwtToken);
        } catch (DataAccessException e) {
            throw new DatabaseException();
        }
    }
}
