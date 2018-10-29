package telvoterminal.telvo.com.terminal.model.kyc;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/26/17.
 */

public class AddressInfo extends DTOBase {
    private String userId;
    private PermanentAddress permanentAddress;
    private PresentAddress presentAddress;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PermanentAddress getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(PermanentAddress permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public PresentAddress getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(PresentAddress presentAddress) {
        this.presentAddress = presentAddress;
    }
}
