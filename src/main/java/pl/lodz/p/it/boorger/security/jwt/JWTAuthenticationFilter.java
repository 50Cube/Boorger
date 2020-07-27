package pl.lodz.p.it.boorger.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.lodz.p.it.boorger.exceptions.AppJWTException;
import pl.lodz.p.it.boorger.security.services.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private JWTUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;
    private Environment env;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = parseJWT(request);
        try {
            if(jwt != null && jwtUtils.validateJwtToken(jwt)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtils.getLoginFromJwtToken(jwt));
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (AppJWTException e) {
            // TODO ?
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String parseJWT(HttpServletRequest request) {
        String headerAuth = request.getHeader(env.getProperty("boorger.jwtHeaderString"));
        String prefixAuth = env.getProperty("boorger.jwtTokenPrefix");

        if(prefixAuth != null && StringUtils.hasText(headerAuth) && headerAuth.startsWith(prefixAuth))
            return headerAuth.substring(prefixAuth.length());
        return null;
    }
}
