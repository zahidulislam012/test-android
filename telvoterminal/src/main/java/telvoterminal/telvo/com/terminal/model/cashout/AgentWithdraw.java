package telvoterminal.telvo.com.terminal.model.cashout;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by monir on 2/27/18.
 */

public class AgentWithdraw extends DTOBase {
    private String userId;
    private String mobileNumber;
    private String amount;
    private String pinCode;
    private Double commission;
    private Double balance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
