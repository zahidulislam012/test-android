package telvoterminal.telvo.com.terminal.model;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/17/17.
 */

public class UserLogin extends DTOBase {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
