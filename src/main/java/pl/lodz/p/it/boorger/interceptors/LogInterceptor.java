package pl.lodz.p.it.boorger.interceptors;

import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import pl.lodz.p.it.boorger.utils.DateFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Log
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String user = request.getRemoteUser() != null ? request.getRemoteUser() : "";

        log.info("[" + DateFormatter.dateToString(LocalDateTime.now()) + "] method invoked: " + request.getMethod() + " " +
                request.getRequestURI() + " by user: " + user + " with IP: " + request.getRemoteAddr());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String user = request.getRemoteUser() != null ? request.getRemoteUser() : "";

        log.info("[" + DateFormatter.dateToString(LocalDateTime.now()) + "] request completed: " + request.getMethod() + " " +
                request.getRequestURI() + " by user: " + user + " with IP: " + request.getRemoteAddr());
    }
}
