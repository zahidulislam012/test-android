package telvoterminal.telvo.com.terminal.model.history;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/2/17.
 */

public class UserHistory extends DTOBase {
    private String userId;
    private History history;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
