package telvoterminal.telvo.com.terminal.model.cashin;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/23/17.
 */

public class MasterCard extends DTOBase {
    private String userId;
    private String amount;
    private String nonce;
    private Double currentBalance;
    private String depositAmount;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

}
