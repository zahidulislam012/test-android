package telvoterminal.telvo.com.terminal.model.qrtransfer;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/29/17.
 */

public class QRTransfer extends DTOBase {
    private String userId;
    private String amount;
    private String pinCode;
    private String qrSecret;

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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getQrSecret() {
        return qrSecret;
    }

    public void setQrSecret(String qrSecret) {
        this.qrSecret = qrSecret;
    }
}
