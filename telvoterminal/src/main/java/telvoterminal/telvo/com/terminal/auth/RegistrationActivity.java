package telvoterminal.telvo.com.terminal.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.hbb20.CountryCodePicker;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightCheckBox;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.support.TermsAndConditionsActivity;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;


public class RegistrationActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private EditText nameEditText;
    private EditText mobileNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private RobotoLightCheckBox termsAndConditionsCheckBox;
    private RobotoRegularButton registerButton;
    private TextView termsAndConditionsTextView;
    private CountryCodePicker countryCodePicker;

    private String name;
    private String mobileNumber;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setToolbar(R.id.toolbar_registration, getString(R.string.registration));

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();
        setTermsAndConditionsText();
    }

    private void initializeViews() {
        nameEditText = getRobotoRegularEditText(R.id.nameEditText);
        mobileNumberEditText = getRobotoRegularEditText(R.id.mobileNumberEditText);
        emailEditText = getRobotoRegularEditText(R.id.emailEditText);
        passwordEditText = getRobotoRegularEditText(R.id.passwordEditText);
        termsAndConditionsCheckBox = getRobotoLightCheckBox(R.id.termsAndConditionsCheckBox);
        registerButton = getRobotoRegularButton(R.id.registerButton);
        registerButton.setOnClickListener(this);
        termsAndConditionsTextView = getRobotoLightTextView(R.id.termsAndConditionsTextView);
        termsAndConditionsTextView.setOnClickListener(this);
        countryCodePicker = getCountryCodePicker(R.id.countryCodePicker);
    }

    private void setTermsAndConditionsText() {
        String termsAndConditions = getString(R.string.terms_and_conditions);
        SpannableString content = new SpannableString(termsAndConditions);
        content.setSpan(new UnderlineSpan(), 0, termsAndConditions.length(), 0);
        termsAndConditionsTextView.setText(content);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registerButton:
                Boolean done = true;

                name = nameEditText.getText().toString().trim();
                mobileNumber = mobileNumberEditText.getText().toString().trim();
                email = emailEditText.getText().toString().trim();
                password = passwordEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.nameEmpty));
                } else if (mobileNumber.isEmpty()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
                } else if (email.isEmpty()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.emailEmpty));
                } else if (!Validation.isEmailValid(email)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.email_not_valid));
                } else if (password.isEmpty()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.passwordEmpty));
                } else if (!Validation.isCheckBoxChecked(termsAndConditionsCheckBox)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.you_must_agree_the_terms_and_conditions));
                }



                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if(Character.isDigit(mobileNumber.charAt(0))){
                            mobileNumber = Integer.parseInt(Character.toString(mobileNumber.charAt(0)))==0?(mobileNumber.substring(1,mobileNumber.length())):mobileNumber;
                            service.setRegistration(name, countryCodePicker.getSelectedCountryCode() + mobileNumber, email, password);
                        }
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            case R.id.termsAndConditionsTextView:
                intent = new Intent(context, TermsAndConditionsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("SIGN_UP")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    /*customAlertDialog.showDialogWithYes(user.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {

                        }
                    });*/

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
}
