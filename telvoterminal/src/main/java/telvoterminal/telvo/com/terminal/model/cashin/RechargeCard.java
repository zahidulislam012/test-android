package telvoterminal.telvo.com.terminal.model.cashin;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/23/17.
 */

public class RechargeCard extends DTOBase {
    private String userId;
    private String code;
    private Double currentBalance;
    private String depositAmount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
