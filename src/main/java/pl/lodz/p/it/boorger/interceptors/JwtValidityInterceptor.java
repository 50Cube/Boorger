package pl.lodz.p.it.boorger.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pl.lodz.p.it.boorger.services.LogoutJwtTokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtValidityInterceptor implements HandlerInterceptor {

    @Value("${boorger.jwtHeaderString}")
    private String auth;

    @Value("${boorger.jwtTokenPrefix}")
    private String bearer;

    @Autowired
    private LogoutJwtTokenService logoutJwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getHeader(auth.toLowerCase()) != null)
            logoutJwtTokenService.checkJwtValidity(request.getHeader(auth).replace(bearer, ""));
        return true;
    }
}
