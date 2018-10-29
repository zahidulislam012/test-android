package telvoterminal.telvo.com.terminal.qrtransfer;


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
import telvoterminal.telvo.com.terminal.model.qrtransfer.QRTransfer;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRTransferSendFragment extends BaseFragment implements View.OnClickListener ,IServiceResultListener{

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText amountEditText;
   /* private RobotoRegularEditText pinCodeEditText1;
    private RobotoRegularEditText pinCodeEditText2;
    private RobotoRegularEditText pinCodeEditText3;
    private RobotoRegularEditText pinCodeEditText4;
   */ private RobotoRegularButton generateQrButton;


    private String amount;
    /*private String pinCode1;
    private String pinCode2;
    private String pinCode3;
    private String pinCode4;
    private String pinCode;*/
    private OtpView pinCodeOtpView;
    public QRTransferSendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qrtransfer_send, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

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
                amount = amountEditText.getText().toString().trim();

                if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                }
                else if(Validation.isTextEmpty(pinCodeOtpView.getOTP())){
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
                            service.setQRTransfer(amount,pinCodeOtpView.getOTP());
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
        if(success){
            if(method.equals("QR_TRANSFER")){
                QRTransfer qrTransfer = (QRTransfer) dtoBase;
                if (qrTransfer.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, TransferQRCodeActivity.class);
                    bundle = new Bundle();
                    bundle.putString("DATA",qrTransfer.getQrSecret());
                    intent.putExtras(bundle);
                    getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
                } else if (qrTransfer.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(qrTransfer.getMessage());
                }
            }
        }
    }
}
