package telvoterminal.telvo.com.terminal.forgotpassword;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.hbb20.CountryCodePicker;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.alertdialog.CustomAlertDialog;
import telvoterminal.telvo.com.terminal.auth.LoginActivity;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class FPResetPasswordActivity extends BaseActivity implements View.OnClickListener,IServiceResultListener {

    private CountryCodePicker countryCodePicker;
    private RobotoRegularEditText mobileNumberEditText;
    private RobotoRegularEditText secretCodeEditText;
    private RobotoRegularEditText newPasswordEditText;
    private RobotoRegularButton submitButton;

    private String countryCode;
    private String mobileNumber;
    private String verificationCode;
    private String newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_reset_password);

        setToolbar(R.id.toolbar_fp_reset_password, getString(R.string.password_recovery));
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        bundle = getIntent().getExtras();
        if(bundle!=null){
            countryCode = getIntent().getStringExtra("countryCode");
            countryCodePicker.setCountryForNameCode(countryCode);
            countryCodePicker.setCcpClickable(false);
            mobileNumber = getIntent().getStringExtra("mobileNumber");
            mobileNumberEditText.setText(mobileNumber);
            mobileNumberEditText.setEnabled(false);
        }

    }

    private void initializeViews() {
        countryCodePicker = getCountryCodePicker(R.id.countryCodePicker);
        mobileNumberEditText = getRobotoRegularEditText(R.id.mobileNumberEditText);
        secretCodeEditText = getRobotoRegularEditText(R.id.secretCodeEditText);
        newPasswordEditText = getRobotoRegularEditText(R.id.newPasswordEditText);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.submitButton:
                countryCode = countryCodePicker.getSelectedCountryCode();
                mobileNumber = mobileNumberEditText.getText().toString().trim();
                verificationCode = secretCodeEditText.getText().toString().trim();
                newPassword = newPasswordEditText.getText().toString().trim();

                if (Validation.isTextEmpty(mobileNumber)) {
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
                } else if (Validation.isTextEmpty(verificationCode)) {
                    customAlertDialog.showDialog(getStringResources(R.string.secret_code_empty));
                } else if (Validation.isTextEmpty(newPassword)) {
                    customAlertDialog.showDialog(getStringResources(R.string.passwordEmpty));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setChangeForgotPassword(countryCode+mobileNumber,verificationCode,newPassword);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("CHANGE_FORGOT_PASSWORD")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(user.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                } else {
                    customAlertDialog.showDialog(user.getMessage());
                }
            }
        }
    }
}
