package telvoterminal.telvo.com.terminal.model.history;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/2/17.
 */

public class Transfers extends DTOBase {
    private String amount;
    private Currency currency;
    private Integer serviceType;
    private HistoryUser user;
    private String createdAt;
    private Receiver receiver;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }
}
