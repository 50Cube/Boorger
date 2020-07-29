package pl.lodz.p.it.boorger.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.boorger.exceptions.AppJWTException;
import pl.lodz.p.it.boorger.security.services.UserDetailsImpl;

import java.util.Date;
import java.util.Objects;

@Log
@Component
public class JWTUtils {

    private int jwtExpirationMs;
    private String jwtSecret;

    @Autowired
    public JWTUtils(Environment env) {
        this.jwtExpirationMs = Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.jwtExpirationMs")));
        this.jwtSecret = env.getProperty("JWT_SECRET");
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getLoginFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String token) throws AppJWTException {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.severe("JWT error occurred: " + e.getClass() + ": " + e.getMessage());
            throw new AppJWTException();
        }
    }
}
