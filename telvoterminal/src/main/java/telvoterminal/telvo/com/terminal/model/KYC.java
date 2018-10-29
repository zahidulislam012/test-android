package telvoterminal.telvo.com.terminal.model;

import telvoterminal.telvo.com.terminal.model.kyc.AddressInfo;
import telvoterminal.telvo.com.terminal.model.kyc.CurrencyInfo;
import telvoterminal.telvo.com.terminal.model.kyc.IntroducerInfo;
import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;
import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/26/17.
 */

public class KYC extends DTOBase {
    private IntroducerInfo introducerInfo;
    private AddressInfo addressInfo;
    private PersonalInfo personalInfo;
    private Boolean allVerified;

    public IntroducerInfo getIntroducerInfo() {
        return introducerInfo;
    }

    public void setIntroducerInfo(IntroducerInfo introducerInfo) {
        this.introducerInfo = introducerInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Boolean getAllVerified() {
        return allVerified;
    }

    public void setAllVerified(Boolean allVerified) {
        this.allVerified = allVerified;
    }
}
