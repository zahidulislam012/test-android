package telvoterminal.telvo.com.terminal.cashout;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;

import telvoterminal.telvo.com.terminal.OtpView.OtpView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.cashout.ATMWithdraw;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class AtmWithdrawFragment extends BaseFragment implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText amountEditText;
    private RobotoRegularButton generateQrButton;

    //private String currentBalance;
    private String amount;

    private OtpView pinCodeOtpView;

    public AtmWithdrawFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_atm_withdraw, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        context = getActivity();

        initializeViews(rootView);
        if(!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            currentBalanceTextView.setText(AppManager.getCurrencySymbol(user.getCurrency())+" "+new DecimalFormat("#.##").format(user.getBalance()));
        }
        return rootView;
    }

    private void initializeViews(View view) {
        currentBalanceTextView = view.findViewById(R.id.currentBalanceTextView);
        amountEditText = view.findViewById(R.id.amountEditText);
        pinCodeOtpView = view.findViewById(R.id.pinCodeOtpView);

        generateQrButton = view.findViewById(R.id.generateQRButton);
        generateQrButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.generateQRButton:
                boolean done = true;
                //currentBalance = currentBalanceTextView.getText().toString().trim().substring(1);
                amount = amountEditText.getText().toString().trim();

                if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else if(Validation.isTextEmpty(pinCodeOtpView.getOTP())){
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                }else if(!pinCodeOtpView.hasValidOTP()){
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_pin_size));

                }


                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if(AppManager.AccountVerify(context)){
                            service = new TerminalService(context, this);
                            service.setAtmWithdraw(amount, pinCodeOtpView.getOTP());
                        }else{
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
        if (success)
            if (method.equals("ATM_QR")) {
                ATMWithdraw atmWithdraw = (ATMWithdraw) dtoBase;
                if (atmWithdraw.getStatus().equals(Constant.success)) {
                    intent = new Intent(getActivity(), AtmQrCodeActivity.class);
                    bundle = new Bundle();
                    bundle.putString("DATA",atmWithdraw.getQrSecret());
                    intent.putExtras(bundle);
                    getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
                } else if (atmWithdraw.getStatus().equals(Constant.error)) {
                   customAlertDialog.showDialog(atmWithdraw.getMessage());
                }
            }
    }
}
