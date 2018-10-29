package telvoterminal.telvo.com.terminal.forgotpassword;


import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.hbb20.CountryCodePicker;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.alertdialog.CustomAlertDialog;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class FPSendNumberActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private CountryCodePicker countryCodePicker;
    private RobotoRegularEditText mobileNumberEditText;
    private RobotoRegularButton submitButton;

    private TextView skipTextView;

    private String countryCode;
    private String mobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fp_send_number);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_fp_send_number, getString(R.string.password_recovery));

        initializeViews();
        setSecurityCodeText();
    }

    private void setSecurityCodeText() {
        String securityCodeText = getString(R.string.i_already_have_a_verification_code);
        SpannableString content = new SpannableString(securityCodeText);
        content.setSpan(new UnderlineSpan(), 0, securityCodeText.length(), 0);
        skipTextView.setText(content);
    }

    private void initializeViews() {
        countryCodePicker = findViewById(R.id.countryCodePicker);
        mobileNumberEditText = getRobotoRegularEditText(R.id.mobileNumberEditText);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
        skipTextView = getTextView(R.id.skipTextView);
        skipTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.submitButton:
                countryCode = countryCodePicker.getSelectedCountryCode();
                mobileNumber = mobileNumberEditText.getText().toString().trim();

                if (Validation.isTextEmpty(mobileNumber)) {
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setForgotPassword(countryCode + mobileNumber);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            case R.id.skipTextView:
                intent = new Intent(context, FPResetPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("FORGOT_PASSWORD")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(user.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            intent = new Intent(context, FPResetPasswordActivity.class);
                            intent.putExtra("countryCode", countryCode);
                            intent.putExtra("mobileNumber", mobileNumber);
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
