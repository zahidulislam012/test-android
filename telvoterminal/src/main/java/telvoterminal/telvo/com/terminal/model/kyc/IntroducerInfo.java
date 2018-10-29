package telvoterminal.telvo.com.terminal.model.kyc;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/26/17.
 */

public class IntroducerInfo extends DTOBase {
    private Boolean inserted;
    private String userId;
    private Boolean declined;
    private Boolean verified;
    private Boolean existingUser;
    private String countryCode;
    private String mobileNumber;
    private String name;
    private String relation;

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getDeclined() {
        return declined;
    }

    public void setDeclined(Boolean declined) {
        this.declined = declined;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getExistingUser() {
        return existingUser;
    }

    public void setExistingUser(Boolean existingUser) {
        this.existingUser = existingUser;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
