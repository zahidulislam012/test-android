package telvoterminal.telvo.com.terminal.service;

import android.content.Context;

import com.google.gson.Gson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import telvoterminal.telvo.com.terminal.model.ActivityLogResult;
import telvoterminal.telvo.com.terminal.model.Encrypt;
import telvoterminal.telvo.com.terminal.model.ShopPayment;
import telvoterminal.telvo.com.terminal.model.Support;
import telvoterminal.telvo.com.terminal.model.TopUp;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.UserLogin;
import telvoterminal.telvo.com.terminal.model.cashin.MasterCard;
import telvoterminal.telvo.com.terminal.model.cashin.RechargeCard;
import telvoterminal.telvo.com.terminal.model.cashout.ATMWithdraw;
import telvoterminal.telvo.com.terminal.model.cashout.AgentWithdraw;
import telvoterminal.telvo.com.terminal.model.cashout.HomeRequest;
import telvoterminal.telvo.com.terminal.model.history.UserHistory;
import telvoterminal.telvo.com.terminal.model.kyc.AddressInfo;
import telvoterminal.telvo.com.terminal.model.kyc.CurrencyInfo;
import telvoterminal.telvo.com.terminal.model.kyc.IntroducerInfo;
import telvoterminal.telvo.com.terminal.model.kyc.PermanentAddress;
import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;
import telvoterminal.telvo.com.terminal.model.kyc.PresentAddress;
import telvoterminal.telvo.com.terminal.model.qrtransfer.QRReceive;
import telvoterminal.telvo.com.terminal.model.qrtransfer.QRTransfer;
import telvoterminal.telvo.com.terminal.model.transfer.PendingTransferResult;
import telvoterminal.telvo.com.terminal.model.transfer.TransferMoney;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.RSACrypt;


public class TerminalService extends BaseService {
    public TerminalService(Context context) {
        super(context);
    }

    public TerminalService(Context context, IServiceResultListener serviceResultListener) {
        super(context, serviceResultListener);
    }

    public void setUserLogin(String mobileNumber, String password, String pushCode, String device) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setPassword(password);
        user.setPushCode(pushCode);
        user.setDevice(device);
        setPostRequest(BaseUrl.USER_LOGIN, user, new UserLogin(), "USER_LOGIN", true, "POST", false);
    }

    public void getBalance(String userId){
        User user = new User();
        user.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.USER_BALANCE+userId+"/balance",user,user,"USER_BALANCE", false, "GET", false);
    }

    public void setRegistration(String name, String mobileNumber, String email, String password) {
        User user = new User();
        user.setName(name);
        user.setMobileNumber(mobileNumber);
        user.setEmail(email);
        user.setPassword(password);
        setPostRequest(BaseUrl.SIGN_UP, user, user, "SIGN_UP", true, "POST", false);
    }

    public void setSMSVerification(String mobileNumber, String code) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setVerificationCode(code);
        setPostRequest(BaseUrl.USER_VERIFY, user, user, "USER_VERIFY", true, "POST", false);
    }

    public void setResendVerificationCode(String mobileNumber) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        setPostRequest(BaseUrl.USER_RESEND_VERIFY, user, user, "USER_RESEND_VERIFY", true, "POST", false);
    }

    public void setIntroducer(String name, String relationShip,String countryCode,String mobileNumber, Boolean existingUser) {
        IntroducerInfo introducerInfo = new IntroducerInfo();
        introducerInfo.setUserId(preferences.getValue(Constant.USER_ID));
        introducerInfo.setName(name);
        introducerInfo.setRelation(relationShip);
        introducerInfo.setCountryCode(countryCode);
        introducerInfo.setMobileNumber(mobileNumber);
        introducerInfo.setExistingUser(existingUser);
        introducerInfo.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.INTRODUCER_INFORMATION, introducerInfo, introducerInfo, "INTRODUCER_INFORMATION", true, "POST", false);

    }

    public void setAddress(String presentAddressLine1, String presentAddressLine2, String presentCity, String presentPostalCode, String presentCountry, String permanentAddressLine1, String permanentAddressLine2, String permanentCity, String permanentPostalCode, String permanentCountry) {
        AddressInfo addressInfo = new AddressInfo();
        PresentAddress presentAddress = new PresentAddress();

        presentAddress.setAddress(presentAddressLine1 + "$$" + presentAddressLine2);
        presentAddress.setCity(presentCity);
        presentAddress.setPostalCode(presentPostalCode);
        presentAddress.setCountry(presentCountry);
        addressInfo.setPresentAddress(presentAddress);

        PermanentAddress permanentAddress = new PermanentAddress();

        permanentAddress.setAddress(permanentAddressLine1 + "$$" + permanentAddressLine2);
        permanentAddress.setCity(permanentCity);
        permanentAddress.setPostalCode(permanentPostalCode);
        permanentAddress.setCountry(permanentCountry);
        addressInfo.setPermanentAddress(permanentAddress);
        addressInfo.setUserId(preferences.getValue(Constant.USER_ID));
        addressInfo.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.ADDRESS_INFORMATION, addressInfo, addressInfo, "ADDRESS_INFORMATION", true, "POST", false);

    }

    public void setPersonalInfo(String userImage, String signImage, String fatherName, String motherName, String spouseName, String nationality, String dateOfBirth, String gender, String nidOrPassport, String nidOrPassportNumber) {
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setUserId(preferences.getValue(Constant.USER_ID));
        personalInfo.setImage(userImage);
        personalInfo.setSignImage(signImage);
        personalInfo.setFatherName(fatherName);
        personalInfo.setMotherName(motherName);
        personalInfo.setSpouseName(spouseName);
        personalInfo.setNationality(nationality);
        personalInfo.setDob(dateOfBirth);
        personalInfo.setGender(gender);
        personalInfo.setNidType(nidOrPassport);
        personalInfo.setNid(nidOrPassportNumber);
        personalInfo.setToken(preferences.getValue(Constant.TOKEN));

        setPostRequest(BaseUrl.PERSONAL_INFORMATION, personalInfo, personalInfo, "PERSONAL_INFORMATION", true, "POST", false);

    }

    public void setCurrency(String currency) {
        CurrencyInfo currencyInfo = new CurrencyInfo();
        currencyInfo.setUserId(preferences.getValue(Constant.USER_ID));
        currencyInfo.setCurrency(currency);
        currencyInfo.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.CURRENCY_INFORMATION, currencyInfo, new CurrencyInfo(), "CURRENCY_INFORMATION", true, "POST", false);
    }

    public void setCreditCard(String amount, String nonce) {
        MasterCard masterCard = new MasterCard();
        masterCard.setUserId(preferences.getValue(Constant.USER_ID));
        masterCard.setToken(preferences.getValue(Constant.TOKEN));
        masterCard.setAmount(amount);
        masterCard.setNonce(nonce);
        setPostRequest(BaseUrl.CREDIT_CARD, masterCard, masterCard, "CREDIT_CARD", false, "POST", false);
    }

    public void setRechargeCard(String cardNumber) {
        RechargeCard rechargeCard = new RechargeCard();
        rechargeCard.setUserId(preferences.getValue(Constant.USER_ID));
        rechargeCard.setToken(preferences.getValue(Constant.TOKEN));
        rechargeCard.setCode(cardNumber);
        setPostRequest(BaseUrl.RECHARGE_BALANCE, rechargeCard, rechargeCard, "RECHARGE_BALANCE", true, "POST", false);
    }

    public void setAtmWithdraw(String amount, String pinCode) {
        ATMWithdraw atmWithdraw = new ATMWithdraw();
        atmWithdraw.setUserId(preferences.getValue(Constant.USER_ID));
        atmWithdraw.setAmount(amount);
        atmWithdraw.setPinCode(pinCode);
        atmWithdraw.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.ATM_QR, atmWithdraw, atmWithdraw, "ATM_QR", true, "POST", false);
    }

    public void setQRTransfer(String amount, String pinCode) {
        QRTransfer qrTransfer = new QRTransfer();
        qrTransfer.setUserId(preferences.getValue(Constant.USER_ID));
        qrTransfer.setAmount(amount);
        qrTransfer.setPinCode(pinCode);
        qrTransfer.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.QR_TRANSFER, qrTransfer, qrTransfer, "QR_TRANSFER", true, "POST", false);
    }

    public void setQRReceive(String qrSecret) {
        QRReceive qrReceive = new QRReceive();
        qrReceive.setUserId(preferences.getValue(Constant.USER_ID));
        qrReceive.setQrSecret(qrSecret);
        qrReceive.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.QR_RECEIVER, qrReceive, qrReceive, "QR_RECEIVER", true, "POST", false);
    }

    public void setPreHomeRequest(String amount) {
        HomeRequest homeRequest = new HomeRequest();
        homeRequest.setUserId(preferences.getValue(Constant.USER_ID));
        homeRequest.setToken(preferences.getValue(Constant.TOKEN));
        homeRequest.setAmount(amount);
        setPostRequest(BaseUrl.PRE_HOME_REQUEST, homeRequest, homeRequest, "PRE_HOME_REQUEST", true, "POST", false);
    }

    public void setHomeRequest(String amount, String district, String area, String address, String pinCode) {
        HomeRequest homeRequest = new HomeRequest();
        homeRequest.setUserId(preferences.getValue(Constant.USER_ID));
        homeRequest.setToken(preferences.getValue(Constant.TOKEN));
        homeRequest.setAmount(amount);
        homeRequest.setCity(district);
        homeRequest.setArea(area);
        homeRequest.setAddress(address);
        homeRequest.setPinCode(pinCode);
        setPostRequest(BaseUrl.HOME_REQUEST, homeRequest, homeRequest, "HOME_REQUEST", true, "POST", false);
    }

    public void setPinCodeAndChange(String newPin) {
        User user = new User();
        user.setPinCode(newPin);
        user.setUserId(preferences.getValue(Constant.USER_ID));
        user.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.PIN_CODE, user, new DTOBase(), "PIN_CODE", true, "POST", false);
    }

    public void setConfirmShopPayment(String pinCode, String secretCode) {
        ShopPayment shopPayment = new ShopPayment();
        shopPayment.setUserId(preferences.getValue(Constant.USER_ID));
        shopPayment.setToken(preferences.getValue(Constant.TOKEN));
        shopPayment.setPinCode(pinCode);
        shopPayment.setSecretCode(secretCode);
        setPostRequest(BaseUrl.SHOP_PAYMENT, shopPayment, shopPayment, "SHOP_PAYMENT", true, "POST", false);
    }

    public void setHelpAndSupport(String subject, String details) {
        Support support = new Support();
        support.setUserId(preferences.getValue(Constant.USER_ID));
        support.setToken(preferences.getValue(Constant.TOKEN));
        support.setTitle(subject);
        support.setMessage(details);
        setPostRequest(BaseUrl.SUPPORT, support, support, "SUPPORT", true, "POST", false);
    }

    public void setPreTransfer(String accountNumber, String amount) {
        TransferMoney transfer = new TransferMoney();
        transfer.setUserId(preferences.getValue(Constant.USER_ID));
        transfer.setToken(preferences.getValue(Constant.TOKEN));
        transfer.setReceiver(accountNumber);
        transfer.setAmount(amount);
        setPostRequest(BaseUrl.TRANSFER_CHECK, transfer, transfer, "TRANSFER_CHECK", true, "POST", false);
    }

    public void getActivityLogs() {
        ActivityLogResult activityLogResult = new ActivityLogResult();
        activityLogResult.setUserId(preferences.getValue(Constant.USER_ID));
        activityLogResult.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.ACTIVITY_LOGS, activityLogResult, activityLogResult, "ACTIVITY_LOGS", true, "POST", false);
    }

    public void getUserHistory() {
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(preferences.getValue(Constant.USER_ID));
        userHistory.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.USER_History, userHistory, userHistory, "USER_History", true, "POST", false);
    }

    public void setTransferToNonUser(String receiver, String amount, String nid, String name, String time, String message, String pinCode) {
        TransferMoney transfer = new TransferMoney();
        transfer.setUserId(preferences.getValue(Constant.USER_ID));
        transfer.setToken(preferences.getValue(Constant.TOKEN));
        transfer.setReceiver(receiver);
        transfer.setAmount(amount);
        transfer.setMessage(message);
        transfer.setPinCode(pinCode);
        transfer.setTime(time);
        transfer.setReceiverName(name);
        transfer.setReceiverNid(nid);
        setPostRequest(BaseUrl.TRANSFER_MONEY_NONUSER, transfer, transfer, "TRANSFER_MONEY_NONUSER", true, "POST", false);
    }

    public void setConfirmTransfer(String receiver, String amount, String message, String pinCode, String time) {
        TransferMoney transfer = new TransferMoney();
        transfer.setUserId(preferences.getValue(Constant.USER_ID));
        transfer.setToken(preferences.getValue(Constant.TOKEN));
        transfer.setReceiver(receiver);
        transfer.setAmount(amount);
        transfer.setMessage(message);
        transfer.setPinCode(pinCode);
        transfer.setTime(time);
        setPostRequest(BaseUrl.TRANSFER_MONEY, transfer, transfer, "TRANSFER_MONEY", true, "POST", false);
    }

    public void setTopUp(String phoneNumber, String phoneType, String operator, String amount, String pincode) {
        TopUp topUp = new TopUp();
        topUp.setUserId(preferences.getValue(Constant.USER_ID));
        topUp.setToken(preferences.getValue(Constant.TOKEN));
        topUp.setReceiver(phoneNumber);
        topUp.setServiceType(phoneType);
        topUp.setOperator(operator);
        topUp.setAmount(amount);
        topUp.setPinCode(pincode);
        setPostRequest(BaseUrl.MOBILE_TOPUP, topUp, topUp, "MOBILE_TOPUP", true, "POST", false);
    }

    public void setPendingTransfer() {
        PendingTransferResult transferResult = new PendingTransferResult();
        transferResult.setUserId(preferences.getValue(Constant.USER_ID));
        transferResult.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.PENDING_TRANSFER, transferResult, transferResult, "PENDING_TRANSFER", true, "POST", false);
    }

    public void setDeletePendingTransfer(String pendingId) {
        PendingTransferResult transferResult = new PendingTransferResult();
        transferResult.setPendingId(pendingId);
        transferResult.setUserId(preferences.getValue(Constant.USER_ID));
        transferResult.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.PENDING_TRANSFER_DELETE, transferResult, transferResult, "PENDING_TRANSFER_DELETE", true, "DELETE", false);
    }

    public void setData(String data){
        Encrypt encrypt = new Encrypt();

            encrypt.setData(new RSACrypt().encryptRSAToString(data));
        setPostRequest("https://api.telvoterminal.com/v1.0/test/decrypt", encrypt, encrypt, "PENDING_TRANSFER_DELETE", true, "POST", false);
    }

    public void setForgotPassword(String mobileNumber) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        setPostRequest(BaseUrl.USER_FORGOT_PASSWORD, user, user, "FORGOT_PASSWORD", true, "POST", false);
    }

    public void setChangeForgotPassword(String mobileNumber,String secretCode,String newPassword) {
        User user = new User();
        user.setMobileNumber(mobileNumber);
        user.setNewPassword(newPassword);
        user.setSecretCode(secretCode);
        setPostRequest(BaseUrl.USER_CHANGE_FORGOT_PASSWORD, user, user, "CHANGE_FORGOT_PASSWORD", true, "POST", false);
    }

    public void setChangePassword(String oldPassword,String newPassword) {
        User user = new User();
        user.setNewPassword(newPassword);
        user.setOldPassword(oldPassword);
        user.setUserId(preferences.getValue(Constant.USER_ID));
        user.setToken(preferences.getValue(Constant.TOKEN));
        setPostRequest(BaseUrl.USER_CHANGE_PASSWORD, user, user, "USER_CHANGE_PASSWORD", true, "POST", false);
    }

    public void setPreAgentWithdraw(String agentNumber, String amount) {
        AgentWithdraw agentWithdraw = new AgentWithdraw();
        agentWithdraw.setUserId(preferences.getValue(Constant.USER_ID));
        agentWithdraw.setToken(preferences.getValue(Constant.TOKEN));
        agentWithdraw.setMobileNumber(agentNumber);
        agentWithdraw.setAmount(amount);
        setPostRequest(BaseUrl.PRE_AGENT_WITHDRAW, agentWithdraw, agentWithdraw, "PRE_AGENT_WITHDRAW", true, "POST", false);
    }

    public void setAgentWithdraw(String agentNumber, String amount, String pinCode) {
        AgentWithdraw agentWithdraw = new AgentWithdraw();
        agentWithdraw.setUserId(preferences.getValue(Constant.USER_ID));
        agentWithdraw.setToken(preferences.getValue(Constant.TOKEN));
        agentWithdraw.setMobileNumber(agentNumber);
        agentWithdraw.setAmount(amount);
        agentWithdraw.setPinCode(pinCode);
        setPostRequest(BaseUrl.AGENT_WITHDRAW, agentWithdraw, agentWithdraw, "AGENT_WITHDRAW", true, "POST", false);
    }
}
