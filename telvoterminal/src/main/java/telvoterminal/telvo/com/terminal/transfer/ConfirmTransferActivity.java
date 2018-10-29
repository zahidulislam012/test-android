package telvoterminal.telvo.com.terminal.transfer;

import android.content.Context;
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
import telvoterminal.telvo.com.terminal.model.transfer.TransferMoney;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class ConfirmTransferActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView toTextView;
    private RobotoRegularTextView amountTextView;
    private RobotoRegularTextView referenceTextView;

    private RobotoRegularButton confirmButton;
    private RobotoRegularTextView fessTextView;
    private RobotoRegularTextView totalTextView;

    private OtpView pinCodeOtpView;
    private String pinCode;

    private String type;
    private String accountNumber;
    private String amount;
    private String waitingTime;
    private String reference;

    private String name;
    private String nid;
    private String commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_transfer);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_confirm_transfer, getStringResources(R.string.transfer));

        initializeViews();

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("TYPE");
            accountNumber = bundle.getString("receiver");
            toTextView.setText(accountNumber.substring(2));
            amount = bundle.getString("amount");
            amountTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + Double.parseDouble(amount));

            commission = bundle.getString("commission");
            fessTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + Double.parseDouble(commission));

            totalTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + (Double.parseDouble(amount) + Double.parseDouble(commission)));

            reference = bundle.getString("message");
            referenceTextView.setText(reference);
            waitingTime = bundle.getString("waitingTime");

            name = bundle.getString("name");
            nid = bundle.getString("NID");

        }
    }

    private void initializeViews() {
        fessTextView = getRobotoRegularTextView(R.id.fessTextView);
        totalTextView = getRobotoRegularTextView(R.id.totalTextView);
        toTextView = getRobotoRegularTextView(R.id.toTextView);
        amountTextView = getRobotoRegularTextView(R.id.amountTextView);
        referenceTextView = getRobotoRegularTextView(R.id.referenceTextView);
        pinCodeOtpView = findViewById(R.id.pinCodeOtpView);

        confirmButton = getRobotoRegularButton(R.id.confirmTransferButton);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmTransferButton:
                boolean done = true;

                pinCode =pinCodeOtpView.getOTP();

                if (Validation.isTextEmpty(pinCode)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if (type.equals("user")) {
                            service.setConfirmTransfer(accountNumber, amount, reference, pinCode, waitingTime);
                        } else if (type.equals("nouser")) {
                            service.setTransferToNonUser(accountNumber, amount, nid, name, waitingTime, reference, pinCode);
                        }
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("TRANSFER_MONEY")) {
                TransferMoney transfer = (TransferMoney) dtoBase;
                if (transfer.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("TO");
                    value.add(accountNumber.substring(2));

                    title.add("Amount");
                    value.add(amount);

                    title.add("Reference");
                    value.add(reference);


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.payment_done));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                } else if (transfer.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(transfer.getMessage());
                }
            } else if (method.equals("TRANSFER_MONEY_NONUSER")) {
                TransferMoney transfer = (TransferMoney) dtoBase;
                if (transfer.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("TO");
                    value.add(accountNumber.substring(2));

                    title.add("Amount");
                    value.add(amount);

                    title.add("Reference");
                    value.add(reference);


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.payment_done));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                } else if (transfer.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(transfer.getMessage());
                }
            }
        }
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //to show soft keyboard
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //to show soft keyboard
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
        //to hide it, call the method again
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
