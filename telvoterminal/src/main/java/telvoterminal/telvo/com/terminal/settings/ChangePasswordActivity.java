package telvoterminal.telvo.com.terminal.settings;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
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

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener,IServiceResultListener {

    private RobotoRegularEditText oldPasswordEditText;
    private RobotoRegularEditText newPasswordEditText;
    private RobotoRegularEditText confirmPasswordEditText;
    private RobotoRegularButton submitButton;

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_change_password, getStringResources(R.string.change_password));

        initializeViews();
    }

    private void initializeViews() {
        oldPasswordEditText = getRobotoRegularEditText(R.id.oldPasswordEditText);
        newPasswordEditText = getRobotoRegularEditText(R.id.newPasswordEditText);
        confirmPasswordEditText = getRobotoRegularEditText(R.id.confirmPasswordEditText);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.submitButton:
                oldPassword = oldPasswordEditText.getText().toString().trim();
                newPassword = newPasswordEditText.getText().toString().trim();
                confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (Validation.isTextEmpty(oldPassword)) {
                    customAlertDialog.showDialog(getStringResources(R.string.enter_old_password));
                } else if (Validation.isTextEmpty(newPassword)) {
                    customAlertDialog.showDialog(getStringResources(R.string.enter_new_password));
                } else if (Validation.isTextEmpty(confirmPassword)) {
                    customAlertDialog.showDialog(getStringResources(R.string.enter_confirm_password));
                } else if (!newPassword.equals(confirmPassword)) {
                    customAlertDialog.showDialog(getStringResources(R.string.new_password_and_confirm_password_did_not_match));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setChangePassword(oldPassword,newPassword);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("USER_CHANGE_PASSWORD")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(user.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            finish();
                        }
                    });
                } else {
                    customAlertDialog.showDialog(user.getMessage());
                }
            }
        }
    }
}
