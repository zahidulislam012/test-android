package telvoterminal.telvo.com.terminal.cashout;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.OtpView.OtpView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.cashout.AgentWithdraw;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class ConfirmAgentWithdrawActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView agentTextView;
    private RobotoRegularTextView amountTextView;
    private RobotoRegularTextView feesTextView;
    private RobotoRegularTextView totalTextView;
    /*private RobotoRegularEditText pinCode1EditText;
    private RobotoRegularEditText pinCode2EditText;
    private RobotoRegularEditText pinCode3EditText;
    private RobotoRegularEditText pinCode4EditText;*/
    private RobotoRegularButton confirmWithdrawButton;

   /* private String pinCode1;
    private String pinCode2;
    private String pinCode3;
    private String pinCode4;
    private String pinCode;
*/
    private String agentNumber;
    private String amount;
    private String fees;
    private String total;

    private OtpView pinCodeOtpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_agent_withdraw);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_confirm_agent_withdraw, getString(R.string.confirm_withdraw));

        initializeViews();

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }

        showWithdrawValues();
    }

    private void showWithdrawValues() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            agentNumber = bundle.getString("agentNumber");
            agentTextView.setText(agentNumber);
            amount = bundle.getString("amount");
            amountTextView.setText(amount);
            fees = bundle.getString("fees");
            feesTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + fees);
            total = bundle.getString("total");
            totalTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + total);
        }
    }

    private void initializeViews() {
        agentTextView = getRobotoRegularTextView(R.id.agentTextView);
        amountTextView = getRobotoRegularTextView(R.id.amountTextView);
        feesTextView = getRobotoRegularTextView(R.id.feesTextView);
        totalTextView = getRobotoRegularTextView(R.id.totalTextView);
        pinCodeOtpView = findViewById(R.id.pinCodeOtpView);
      /*  pinCode1EditText = getRobotoRegularEditText(R.id.pinCode1EditText);
        pinCode1EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCode2EditText.requestFocus();
            }
        });
        pinCode2EditText = getRobotoRegularEditText(R.id.pinCode2EditText);
        pinCode2EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCode3EditText.requestFocus();
            }
        });
        pinCode3EditText = getRobotoRegularEditText(R.id.pinCode3EditText);
        pinCode3EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCode4EditText.requestFocus();
            }
        });
        pinCode4EditText = getRobotoRegularEditText(R.id.pinCode4EditText);
        pinCode4EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Hide the keyboard
                hideKeyboard(pinCode4EditText);
            }
        });*/
        confirmWithdrawButton = getRobotoRegularButton(R.id.confirmWithdrawButton);
        confirmWithdrawButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmWithdrawButton:
               /* pinCode1 = pinCode1EditText.getText().toString().trim();
                pinCode2 = pinCode2EditText.getText().toString().trim();
                pinCode3 = pinCode3EditText.getText().toString().trim();
                pinCode4 = pinCode4EditText.getText().toString().trim();
                pinCode = pinCode1 + pinCode2 + pinCode3 + pinCode4;*/

                if (Validation.isTextEmpty(pinCodeOtpView.getOTP())) {
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                }else if(!pinCodeOtpView.hasValidOTP()){
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_pin_size));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setAgentWithdraw(agentNumber, amount, pinCodeOtpView.getOTP());
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
        }
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(
                INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("AGENT_WITHDRAW")) {
                AgentWithdraw agentWithdraw = (AgentWithdraw) dtoBase;
                if (agentWithdraw.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("Agent");
                    value.add(agentNumber);
                    title.add("Amount");
                    value.add(amount);

                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getString(R.string.confirm_withdraw));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

                    if (!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(agentWithdraw.getBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    startActivity(intent);
                    finish();
                } else if (agentWithdraw.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(agentWithdraw.getMessage());
                }
            }
        }
    }
}
