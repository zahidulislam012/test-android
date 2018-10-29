package telvoterminal.telvo.com.terminal.shoppayment;

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
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.ShopPayment;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class ConfirmPaymentActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView accountNameTextView;
    private RobotoRegularTextView amountTextView;
    private RobotoRegularButton confirmPaymentButton;

    private String barcodeValue;

    private ShopPayment shopPayment = null;
    private OtpView pinCodeOtpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_confirm_payment, getStringResources(R.string.shop_payment));

        initializeViews();

        barcodeValue = getIntent().getStringExtra(Constant.BARCODE_VALUE);
        if (!barcodeValue.isEmpty()) {
            shopPayment = (ShopPayment) AppManager.getClassObject(barcodeValue, new ShopPayment());
            if (shopPayment != null) {
                accountNameTextView.setText(shopPayment.getShopName());
                amountTextView.setText("৳ " + shopPayment.getAmount());
            }
        }
        /// Toast.makeText(this, barcodeValue, Toast.LENGTH_SHORT).show();


    }

    private void initializeViews() {
        accountNameTextView = getRobotoRegularTextView(R.id.accountNameTextView);
        amountTextView = getRobotoRegularTextView(R.id.amountTextView);
        pinCodeOtpView = findViewById(R.id.pinCodeOtpView);
        confirmPaymentButton = getRobotoRegularButton(R.id.confirmPaymentButton);
        confirmPaymentButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirmPaymentButton:
                boolean done = true;

                if (Validation.isTextEmpty(pinCodeOtpView.getOTP())) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                } else if (!pinCodeOtpView.hasValidOTP()) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_pin_size));

                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if (AppManager.AccountVerify(context)) {
                            service.setConfirmShopPayment(pinCodeOtpView.getOTP(), shopPayment.getSecretCode());
                        } else {
                            customAlertDialog.showDialogWithYes(getStringResources(R.string.account_verify), new ButtonClick() {
                                @Override
                                public void Do() {
                                    MainActivity.callAccount();
                                }
                            });
                        }

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
            if (method.equals("SHOP_PAYMENT")) {
                ShopPayment shopPayment = (ShopPayment) dtoBase;
                if (shopPayment.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("Shop Name");
                    value.add(accountNameTextView.getText().toString().trim());

                    title.add("Amount");
                    value.add(amountTextView.getText().toString().trim());


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    if (!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(shopPayment.getBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.payment_done));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                } else if (shopPayment.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(shopPayment.getMessage());
                }
            }
        }
    }
}
