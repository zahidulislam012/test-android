package telvoterminal.telvo.com.terminal.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.OtpView.OtpView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class SetPinCodeActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularButton submitButton;

    private OtpView newPinCodeOtpView, confirmPinCodeOtpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin_code);

        setToolbar(R.id.toolbar_set_pin_code, getStringResources(R.string.set_pin));

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }
    }

    private void initializeViews() {
        newPinCodeOtpView = findViewById(R.id.newPinCodeOtpView);
        confirmPinCodeOtpView = findViewById(R.id.confirmPinCodeOtpView);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                boolean done = true;

                if (Validation.isTextEmpty(newPinCodeOtpView.getOTP())) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_new_pin_code));
                } else if (!newPinCodeOtpView.hasValidOTP()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_new_pin_size));
                } else if (Validation.isTextEmpty(confirmPinCodeOtpView.getOTP())) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_confirm_pin_code));
                } else if (!confirmPinCodeOtpView.hasValidOTP()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_confirm_pin_size));
                }

                if (!((newPinCodeOtpView.getOTP()).equals(confirmPinCodeOtpView.getOTP()))) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.new_pin_and_confirm_pin_did_not_match));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setPinCodeAndChange(newPinCodeOtpView.getOTP());
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("PIN_CODE")) {
                if (dtoBase.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(dtoBase.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            user.setPinCode(newPinCodeOtpView.getOTP());
                            preferences.setValue("USER", AppManager.getClassString(user));
                            intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (dtoBase.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(dtoBase.getMessage());
                }
            }
        }
    }
}
