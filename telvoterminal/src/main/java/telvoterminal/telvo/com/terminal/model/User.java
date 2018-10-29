package telvoterminal.telvo.com.terminal.model;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 14-Sep-17.
 */

public class User extends DTOBase {
    /* User Registration Variable*/
    private String name;
    private String mobileNumber;
    private String email;
    private String password;

    /* User verify Variable */
    private String verificationCode;

    /* Password Reset Variable*/
    private String secretCode;
    private String newPassword;
    private String oldPassword;

    /* User Login Variable */
    private String pushCode;
    private String device;

     /* User Login Result  */
    private String _id;
    private String pinCode;
    private Double balance;
    private String currency;
    private String lastLogin;

    /* For User PinCode */
    private String userId;

    private KYC kyc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getPushCode() {
        return pushCode;
    }

    public void setPushCode(String pushCode) {
        this.pushCode = pushCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public KYC getKyc() {
        return kyc;
    }

    public void setKyc(KYC kyc) {
        this.kyc = kyc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
