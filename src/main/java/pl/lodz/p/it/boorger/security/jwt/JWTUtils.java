package pl.lodz.p.it.boorger.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.java.Log;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.boorger.exceptions.AppJWTException;
import pl.lodz.p.it.boorger.security.services.UserDetailsImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Log
@Component
public class JWTUtils {

    private int jwtExpirationMs;
    private String jwtSecret;

    public JWTUtils(Environment env) {
        this.jwtExpirationMs = Integer.parseInt(Objects.requireNonNull(env.getProperty("boorger.jwtExpirationMs")));
        this.jwtSecret = env.getProperty("JWT_SECRET");
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("roles", roles)
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
