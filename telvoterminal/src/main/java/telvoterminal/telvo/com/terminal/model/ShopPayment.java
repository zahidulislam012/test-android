package telvoterminal.telvo.com.terminal.model;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/30/17.
 */

public class ShopPayment extends DTOBase {
    private String userId;
    private String amount;
    private String shopName;
    private String secretCode;
    private String pinCode;
    private Double balance;

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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
