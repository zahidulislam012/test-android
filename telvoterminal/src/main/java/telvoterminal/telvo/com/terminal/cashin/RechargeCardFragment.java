package telvoterminal.telvo.com.terminal.cashin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.cashin.RechargeCard;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeCardFragment extends BaseFragment implements View.OnClickListener ,IServiceResultListener{

    private RobotoRegularEditText cardNumberEditText;
    private RobotoRegularButton proceedButton;

    private String cardNumber;

    public RechargeCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recharge_card, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        proceedButton = view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedButton:
                cardNumber = cardNumberEditText.getText().toString().trim();
                if (Validation.isTextEmpty(cardNumber)) {
                    customAlertDialog.showDialog(getStringResources(R.string.card_number_empty));
                } else if (!(cardNumber.length() == 16)) {
                    customAlertDialog.showDialog(getStringResources(R.string.card_number_should_be_16_digit));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        if(AppManager.AccountVerify(context)){
                            service = new TerminalService(context,this);
                            service.setRechargeCard(cardNumber);
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
            if(method.equals("RECHARGE_BALANCE")){
                RechargeCard rechargeCard = (RechargeCard) dtoBase;
                if(rechargeCard.getStatus().equals(Constant.success)){
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("Amount");
                    value.add(rechargeCard.getDepositAmount());
                    title.add("Card Number");
                    value.add(cardNumber);
                    title.add("Balance");
                    value.add(rechargeCard.getCurrentBalance()+"");


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success,done);
                    bundle.putString(Constant.ACTIVITY_NAME, getActivity().getString(R.string.cash_in));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    if(!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(rechargeCard.getCurrentBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
                }else if(rechargeCard.getStatus().equals(Constant.error)){
                   customAlertDialog.showDialog(rechargeCard.getMessage());
                }
            }
        }
    }
}
