package telvoterminal.telvo.com.terminal.model.qrtransfer;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/1/17.
 */

public class QRReceive extends DTOBase {
    private String userId;
    private String qrSecret;
    private Double balance;
    private String sender;
    private String currency;
    private String amount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQrSecret() {
        return qrSecret;
    }

    public void setQrSecret(String qrSecret) {
        this.qrSecret = qrSecret;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
