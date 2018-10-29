package telvoterminal.telvo.com.terminal.model.kyc;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/26/17.
 */

public class PersonalInfo extends DTOBase {

    private Boolean inserted;
    private Boolean declined;
    private Boolean verified;
    private String userId;
    private String nid ;
    private String gender;
    private String dob;
    private String nationality;
    private String motherName;
    private String fatherName;
    private String spouseName;
    private String nidType;
    private String image;
    private String signImage;

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

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getNidType() {
        return nidType;
    }

    public void setNidType(String nidType) {
        this.nidType = nidType;
    }

    public String getSignImage() {
        return signImage;
    }

    public void setSignImage(String signImage) {
        this.signImage = signImage;
    }
}
