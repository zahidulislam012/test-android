package telvoterminal.telvo.com.terminal.model.kyc;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/26/17.
 */

public class PresentAddress extends DTOBase {
    private Boolean inserted;
    private Boolean declined;
    private Boolean verified;
    private String country;
    private String postalCode;
    private String city;
    private String address;

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
