package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.boorger.controllers.LogoutController;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.LogoutJwtTokenService;

@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class LogoutControllerImpl implements LogoutController {

    private LogoutJwtTokenService logoutJwtTokenService;

    @PostMapping("logout/{token}")
    public void handleLogout(@PathVariable String token) throws AppBaseException {
        logoutJwtTokenService.handleLogout(token);
    }
}
