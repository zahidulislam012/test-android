package telvoterminal.telvo.com.terminal.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightTextView;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.forgotpassword.FPSendNumberActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.UserLogin;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class LoginActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularEditText mobileNumberEditText;
    private RobotoRegularEditText passwordEditText;
    private RobotoLightTextView forgotPasswordTextView;
    private RobotoLightTextView registerHereTextView;
    private RobotoRegularButton loginButton;
    private CountryCodePicker countryCodePicker;

    private String mobileNumber;
    private String password;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbarWithoutBack(R.id.toolbar_login, getString(R.string.login));
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        initializeViews();
        setForgotPasswordText();
        setRegisterHereText();

        /*//send Crashlytics log
        Crashlytics.log("before crash");
        Crashlytics.setString("key", "test_key" *//* string value *//*);
        Crashlytics.setUserIdentifier("test_user");
        String test=null;
        test.toString();
*/
        if(!preferences.getValue("MOBILE_NUMBER").equals("")){
            mobileNumberEditText.setText(preferences.getValue("MOBILE_NUMBER"));
        }
//        if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
//            if (!Settings.canDrawOverlays(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
//                startActivityForResult(intent, 100);
//                return;
//            }}

    }

    private void initializeViews() {
        mobileNumberEditText = getRobotoRegularEditText(R.id.mobileNumberEditText);
        passwordEditText = getRobotoRegularEditText(R.id.passwordEditText);
        registerHereTextView = getRobotoLightTextView(R.id.registerHereTextView);
        registerHereTextView.setOnClickListener(this);
        forgotPasswordTextView = getRobotoLightTextView(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(this);
        loginButton = getRobotoRegularButton(R.id.loginButton);
        loginButton.setOnClickListener(this);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.countryCodePicker);

    }

    private void setForgotPasswordText() {
        String forgotPassword = getString(R.string.forgot_password);
        SpannableString content = new SpannableString(forgotPassword);
        content.setSpan(new UnderlineSpan(), 0, forgotPassword.length(), 0);
        forgotPasswordTextView.setText(content);
    }

    private void setRegisterHereText() {
        String registerHere = getString(R.string.register_here);
        SpannableString content = new SpannableString(registerHere);
        content.setSpan(new UnderlineSpan(), 0, registerHere.length(), 0);
        registerHereTextView.setText(content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerHereTextView:
                intent = new Intent(context, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.loginButton:
                mobileNumber = mobileNumberEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();
                Boolean done = loginValidation(mobileNumber,password);
                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        //sendLoginAnalyticsLogReport(mobileNumber);
                        if(Character.isDigit(mobileNumber.charAt(0)))
                        {
                            mobileNumber = Integer.parseInt(Character.toString(mobileNumber.charAt(0)))==0?(mobileNumber.substring(1,mobileNumber.length())):mobileNumber;
                            service.setUserLogin(countryCodePicker.getSelectedCountryCode() + mobileNumber, password, FirebaseInstanceId.getInstance().getToken(), AppManager.getDeviceId(context));
                        }
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            case R.id.forgotPasswordTextView:
                intent = new Intent(context, FPSendNumberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
//            case R.id.languageTextView:
//                AppManager.setAlert(context, preferences, new ButtonClick() {
//                    @Override
//                    public void Do() {
//                        intent = new Intent(context, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                break;
            default:
                return;
        }
    }

    @NonNull
    public Boolean loginValidation(String amobileNumber, String apassword) {
        Boolean done = true;
        if (amobileNumber.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
        } else if (apassword.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.passwordEmpty));
        }
        return done;
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("USER_LOGIN")) {
                UserLogin user = (UserLogin) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    preferences.setValue("TOKEN", user.getToken());
                    preferences.setValue("MOBILE_NUMBER", mobileNumber);
                    preferences.setValue("USER_ID", user.getUser().get_id());
                    preferences.setValue("USER", AppManager.getClassString(user.getUser()));
                    preferences.setValue("AllVerified", user.getUser().getKyc().getAllVerified());

                    saveAdminPoints(user.getAdminPoints());

                    if(user.getUser().getPinCode().equals("")){
                        intent = new Intent(context, SetPinCodeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else{
                        preferences.setValue(Constant.USER_LOGGED_IN, true);
                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else if (user.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(user.getMessage());
                } else if (user.getStatus().equals(Constant.verify)) {
                    customAlertDialog.showDialogWithYesNo(getStringResources(R.string.SMS_Verification), getStringResources(R.string.SMS_Verification_Message), new ButtonClick() {
                        @Override
                        public void Do() {
                            if (AppManager.hasInternetConnection(context)) {
                                service.setResendVerificationCode(countryCodePicker.getSelectedCountryCode() + mobileNumber);
                            } else {
                                customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                            }
                        }
                    }, new ButtonClick() {
                        @Override
                        public void Do() {

                        }
                    });
                }
            } else if (method.equals("USER_RESEND_VERIFY")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SMSVerificationActivity.class);
                    bundle = new Bundle();
                    bundle.putString(Constant.SMS_VERIFICATION_MESSAGE, user.getMessage());
                    bundle.putString("mobileNumber", countryCodePicker.getSelectedCountryCode() + mobileNumber);
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (user.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(user.getMessage());
                }
            }
        }
    }

    private void saveAdminPoints(ArrayList<String> adminPoints) {
        if (adminPoints == null) return;
        Gson gson = new Gson();
        String adminPointsJson = gson.toJson(adminPoints);
        preferences.setValue("ADMIN_POINTS", adminPointsJson);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_language, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.nav_language:
                    AppManager.setAlert(context, preferences, new ButtonClick() {
                        @Override
                        public void Do() {
                            finish();
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendLoginAnalyticsLogReport(String mobileNumber) {
        Bundle bundle = new Bundle();
        bundle.putString("login_user_phone_id", mobileNumber);
        mFirebaseAnalytics.logEvent("login_data", bundle);
    }

}
