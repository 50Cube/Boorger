package pl.lodz.p.it.boorger.controllers;

import pl.lodz.p.it.boorger.exceptions.AppBaseException;

public interface LogoutController {

    void handleLogout(String token) throws AppBaseException;
}
