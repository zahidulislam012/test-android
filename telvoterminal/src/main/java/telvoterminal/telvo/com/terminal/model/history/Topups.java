package telvoterminal.telvo.com.terminal.model.history;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/2/17.
 */

public class Topups extends DTOBase {
    private String amount;
    private String receiver;
    private HistoryUser user;
    private String createdAt;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public HistoryUser getUser() {
        return user;
    }

    public void setUser(HistoryUser user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
