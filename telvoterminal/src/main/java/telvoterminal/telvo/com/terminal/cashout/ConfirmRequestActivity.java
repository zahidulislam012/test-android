package telvoterminal.telvo.com.terminal.cashout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;
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
import telvoterminal.telvo.com.terminal.model.cashout.HomeRequest;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class ConfirmRequestActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView amountTextView;
    private RobotoRegularTextView areaTextView;
    private RobotoRegularTextView addressTextView;
    private RobotoRegularButton confirmRequestButton;
    private RobotoRegularTextView fessTextView;

    private String amount;
    private String district;
    private String area;
    private String address;
    private String commission;

    private OtpView pinCodeOtpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_confirm_request, getStringResources(R.string.confirm_request));

        initializeViews();

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }

        bundle = getIntent().getExtras();
        if (bundle != null) {
            amount = bundle.getString("Amount");
            amountTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + amount);
            district = bundle.getString("District");
            area = bundle.getString("Area");
            areaTextView.setText(district + " - " + area);
            address = bundle.getString("Address");
            addressTextView.setText(address);
            commission = bundle.getString("commission");
            fessTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + commission);
        }
    }

    private void initializeViews() {
        fessTextView = getRobotoRegularTextView(R.id.fessTextView);
        amountTextView = getRobotoRegularTextView(R.id.amountTextView);
        areaTextView = getRobotoRegularTextView(R.id.areaTextView);
        addressTextView = getRobotoRegularTextView(R.id.addressTextView);
        pinCodeOtpView = findViewById(R.id.pinCodeOtpView);
        confirmRequestButton = getRobotoRegularButton(R.id.confirmRequestButton);
        confirmRequestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmRequestButton:

                if(Validation.isTextEmpty(pinCodeOtpView.getOTP())){
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                }else if(!pinCodeOtpView.hasValidOTP()){
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_pin_size));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setHomeRequest(amount, district, area, address, pinCodeOtpView.getOTP());
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
            if (method.equals("HOME_REQUEST")) {
                HomeRequest homeRequest = (HomeRequest) dtoBase;
                if (homeRequest.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("Amount");
                    value.add(amount);
                    title.add("Area");
                    value.add(district + " - " + area);
                    title.add("Address");
                    value.add(address);

                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.confirm_request));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

                    if (!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(homeRequest.getBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }
                    startActivity(intent);
                    finish();
                } else if (homeRequest.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(homeRequest.getMessage());
                }
            }
        }
    }
}
