package telvoterminal.telvo.com.terminal.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightTextView;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.PermissionManager;

public class SMSVerificationActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private static final String TAG = "SMSVerificationActivity";

    private RobotoLightTextView smsVerificationMessageTextView;
    private EditText pinCodeEditText1;
    private EditText pinCodeEditText2;
    private EditText pinCodeEditText3;
    private EditText pinCodeEditText4;
    private RobotoRegularButton btnSubmitCode;

    private String codeOne;
    private String codeTwo;
    private String codeThree;
    private String codeFour;
    private String mobileNumber;
    private String message;

    private BroadcastReceiver broadcastReceiver;
    private SmsManager sms = SmsManager.getDefault();
    private Bundle smsbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_sms_verification, getString(R.string.sms_verification));

        bundle = getIntent().getExtras();
        if (bundle != null) {
            mobileNumber = bundle.getString("mobileNumber");
            message = bundle.getString(Constant.SMS_VERIFICATION_MESSAGE);
        }

        initializeViews();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsbundle = intent.getExtras();
                try {

                    if (smsbundle != null) {

                        final Object[] pdusObj = (Object[]) smsbundle.get("pdus");

                        for (int i = 0; i < pdusObj.length; i++) {
                            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                                String message = currentMessage.getDisplayMessageBody();
                                if (message.length() != 0 && message.contains("Telvo Terminal")) {
                                    pinCodeEditText1.setText(Character.toString(message.charAt(19)));
                                    pinCodeEditText2.setText(Character.toString(message.charAt(20)));
                                    pinCodeEditText3.setText(Character.toString(message.charAt(21)));
                                    pinCodeEditText4.setText(Character.toString(message.charAt(22)));
                                    setSMSVerification();
                                }
                        }
                    }

                } catch (Exception e) {
                    Log.e("SmsReceiver", "Exception smsReceiver" + e);

                }
            }
        };

        PermissionManager.checkReceiveSMSPermission(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
        super.onDestroy();
    }


    private void initializeViews() {
        smsVerificationMessageTextView = getRobotoLightTextView(R.id.smsVerificationMessageTextView);
        smsVerificationMessageTextView.setText(message + ". Submit the code here:");
        pinCodeEditText1 = getEditText(R.id.pinCodeEditText1);
        pinCodeEditText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCodeEditText2.requestFocus();
            }
        });
        pinCodeEditText2 = getEditText(R.id.pinCodeEditText2);
        pinCodeEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCodeEditText3.requestFocus();
            }
        });
        pinCodeEditText3 = getEditText(R.id.pinCodeEditText3);
        pinCodeEditText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                pinCodeEditText4.requestFocus();
            }
        });
        pinCodeEditText4 = getEditText(R.id.pinCodeEditText4);
        pinCodeEditText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                //to show soft keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //to hide it, call the method again
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        btnSubmitCode = getRobotoRegularButton(R.id.submitVerificationCodeButton);
        btnSubmitCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitVerificationCodeButton:
                setSMSVerification();
                break;
            default:
                return;
        }
    }

    private void setSMSVerification() {
        Boolean done = true;
        codeOne = pinCodeEditText1.getText().toString().trim();
        codeTwo = pinCodeEditText2.getText().toString().trim();
        codeThree = pinCodeEditText3.getText().toString().trim();
        codeFour = pinCodeEditText4.getText().toString().trim();
        if (codeOne.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.sms_verificationCodeEmpty));
        } else if (codeTwo.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.sms_verificationCodeEmpty));
        } else if (codeThree.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.sms_verificationCodeEmpty));
        } else if (codeFour.isEmpty()) {
            done = false;
            customAlertDialog.showDialog(getStringResources(R.string.sms_verificationCodeEmpty));
        }

        if (done) {
            if (AppManager.hasInternetConnection(context)) {
                service.setSMSVerification(mobileNumber, codeOne + codeTwo + codeThree + codeFour);
            } else {
                customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
            }
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        Log.e(TAG, "Begin OnServiceResult");

        if (success) {
            if (method.equals("USER_VERIFY")) {
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(user.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else if (user.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(user.getMessage());
                }
            }
        }

        Log.e(TAG, "End OnServiceResult");
    }

}
