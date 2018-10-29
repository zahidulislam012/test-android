package telvoterminal.telvo.com.terminal.utility;

/**
 * Created by Invariant on 9/17/17.
 */

public class BaseUrl {

    public static String BASE_SERVER = "https://api.telvoterminal.com/v1.0/";
    public static String IMAGE_BASE = "https://api.telvoterminal.com/static/users/";

    public static final String SIGN_UP = BASE_SERVER + "users/";
    public static final String USER_BALANCE = BASE_SERVER + "users/";
    public static final String USER_VERIFY = BASE_SERVER + "users/verify";
    public static final String USER_RESEND_VERIFY = BASE_SERVER + "users/resendVerificationCode";
    public static final String USER_LOGIN = BASE_SERVER + "auth/userLogin";
    public static final String CURRENCY_INFORMATION = BASE_SERVER + "users/currencyInfo";
    public static final String INTRODUCER_INFORMATION = BASE_SERVER + "users/introducerInfo";
    public static final String PERSONAL_INFORMATION = BASE_SERVER + "users/personalInfo";
    public static final String ADDRESS_INFORMATION = BASE_SERVER + "users/addressInfo";
    public static final String PIN_CODE = BASE_SERVER + "users/pinCode";

    /* User Cash In All API */
    public static final String RECHARGE_BALANCE = BASE_SERVER + "users/rechargeBalance";
    public static final String CREDIT_CARD = BASE_SERVER + "users/depositMasterCard";

    /* User Cash Out API */
    public static final String ATM_QR = BASE_SERVER + "users/generateQRCode";
    public static final String PRE_AGENT_WITHDRAW = BASE_SERVER + "users/precheckAgentWithdraw";
    public static final String AGENT_WITHDRAW = BASE_SERVER + "users/agentWithdraw";
    public static final String PRE_HOME_REQUEST = BASE_SERVER + "users/precheckHomeWithdraw";
    public static final String HOME_REQUEST = BASE_SERVER + "users/homeWithdraw";

    /* User Transfer All API */
    public static final String TRANSFER_CHECK = BASE_SERVER + "users/preTransferCheck";
    public static final String TRANSFER_MONEY = BASE_SERVER + "users/transferMoney";
    public static final String TRANSFER_MONEY_NONUSER = BASE_SERVER + "users/transferNonUser";
    public static final String PENDING_TRANSFER = BASE_SERVER + "users/pendingTransfers";
    public static final String PENDING_TRANSFER_DELETE = BASE_SERVER + "users/pendingTransfers";
    public static final String QR_TRANSFER = BASE_SERVER + "users/generateTransferQR";
    public static final String QR_RECEIVER = BASE_SERVER + "users/receiveMoney";

    /* User Shop Payment */
    public static final String SHOP_PAYMENT = BASE_SERVER + "users/shopPayment";

    /* user Mobile top up*/
    public static final String MOBILE_TOPUP = BASE_SERVER + "users/mobileRecharge";

    /* user Support */
    public static final String SUPPORT = BASE_SERVER + "users/support";
    public static final String ACTIVITY_LOGS = BASE_SERVER + "users/activityLog";
    public static final String USER_History = BASE_SERVER + "users/userHistory";

    /* user password change */
    public static final String USER_FORGOT_PASSWORD = BASE_SERVER + "users/forgetPassword";
    public static final String USER_CHANGE_FORGOT_PASSWORD = BASE_SERVER + "users/changeForgetPassword";
    public static final String USER_CHANGE_PASSWORD = BASE_SERVER + "users/changePassword";
}
