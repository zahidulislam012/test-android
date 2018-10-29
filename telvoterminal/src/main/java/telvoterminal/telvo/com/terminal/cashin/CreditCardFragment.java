package telvoterminal.telvo.com.terminal.cashin;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.Card;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.Calendar;
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
import telvoterminal.telvo.com.terminal.model.cashin.MasterCard;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditCardFragment extends BaseFragment implements View.OnClickListener,IServiceResultListener {

    private RobotoRegularEditText nameEditText;
    private RobotoRegularEditText cardNumberEditText;
    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private ImageView calendarImageView;
    private RobotoRegularEditText csvEditText;
    private RobotoRegularEditText amountEditText;
    private RobotoRegularButton proceedButton;

    private String name;
    private String cardNumber;
    private String date;
    private String csv;
    private String amount;

    private BraintreeFragment mBraintreeFragment;
    private CardBuilder cardBuilder;

    private ProgressDialog progressDialog;

    public CreditCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_credit_card, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);
        setupMonthSpinner();
        setupYearSpinner();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

        try {
            mBraintreeFragment = BraintreeFragment.newInstance(getActivity(), "sandbox_n864zyxn_6hdg7wyj83cys37j");
            mBraintreeFragment.addListener(methodNonceCreatedListener);
            mBraintreeFragment.addListener(braintreeErrorListener);
        } catch (InvalidArgumentException ex) {
            ex.printStackTrace();
        }

        return rootView;
    }

    private void initializeViews(View view) {
        nameEditText = view.findViewById(R.id.nameEditText);
        cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        /*calendarImageView = view.findViewById(R.id.calendarImageView);
        calendarImageView.setOnClickListener(this);*/
        csvEditText = view.findViewById(R.id.csvEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        proceedButton = view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedButton:
                boolean done = true;
                name = nameEditText.getText().toString().trim();
                cardNumber = cardNumberEditText.getText().toString().trim();
                date = monthSpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString();
                csv = csvEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();

                if (Validation.isTextEmpty(name)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.name_empty));
                } else if (Validation.isTextEmpty(cardNumber)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.card_number_empty));
                }
                else if ((monthSpinner.getSelectedItemPosition() < 1) || (yearSpinner.getSelectedItemPosition() < 1)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.date_not_selected));
                } else if (Validation.isTextEmpty(csv)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.csv_empty));
                } else if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                }

               // done =false;

                if (done) {
                    if(AppManager.hasInternetConnection(context)){
                        if(AppManager.AccountVerify(context)){
                            if(progressDialog!=null){
                                progressDialog.setMessage("Please wait...");
                                progressDialog.show();
                            }
                            service = new TerminalService(context,this);
                            cardBuilder = new CardBuilder().cardNumber(cardNumber).expirationMonth(monthSpinner.getSelectedItem().toString().trim()).expirationYear(yearSpinner.getSelectedItem().toString().trim()).cvv(csv).cardholderName(name);
                            Card.tokenize(mBraintreeFragment, cardBuilder);
                        }else{
                            customAlertDialog.showDialogWithYes(getStringResources(R.string.account_verify), new ButtonClick() {
                                @Override
                                public void Do() {
                                    MainActivity.callAccount();
                                }
                            });
                        }

                    }else{
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }else{
                    customAlertDialog.showDialog("service unavailable");
                }
                break;
            default:
                return;
        }
    }

    private void setupMonthSpinner() {
        List<String> monthList = new ArrayList<>();
        monthList.add("MM ");
        for (int i = 1; i <= 12; i++) {
            String month = (i < 10) ? ("0" + i + " ") : ("" + i + " ");
            monthList.add(month);
        }
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, monthList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        monthSpinner.setAdapter(adapter);
    }

    private void setupYearSpinner() {
        //Toast.makeText(getActivity(), "setupYearSpinner", Toast.LENGTH_SHORT).show();
        List<String> yearList = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        yearList.add("YYYY ");
        for (int i = year; i <= (year + 50); i++) {
            yearList.add("" + i + " ");
        }

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, yearList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        yearSpinner.setAdapter(adapter);
    }

    PaymentMethodNonceCreatedListener methodNonceCreatedListener = new PaymentMethodNonceCreatedListener() {
        @Override
        public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
            String nonce = paymentMethodNonce.getNonce();
            if(!Validation.isTextEmpty(nonce)){
                if(AppManager.hasInternetConnection(context)){
                     service.setCreditCard(amount,nonce);
                }else{
                    customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                }
                Log.i("Payment Nonce is : ",nonce);
            }else{
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    };

    BraintreeErrorListener braintreeErrorListener = new BraintreeErrorListener() {
        @Override
        public void onError(Exception error) {

        }
    };

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("CREDIT_CARD")){
                if (progressDialog.isShowing())
                    progressDialog.dismiss();

                MasterCard masterCard = (MasterCard) dtoBase;
                if(masterCard.getStatus().equals(Constant.success)){
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("Amount");
                    value.add(masterCard.getDepositAmount());
                    title.add("Card Number");
                    value.add(cardNumber);
                    title.add("Balance");
                    value.add(masterCard.getCurrentBalance()+"");


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success,done);
                    bundle.putString(Constant.ACTIVITY_NAME, getActivity().getString(R.string.cash_in));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    if(!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(masterCard.getCurrentBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
                }else if(masterCard.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(masterCard.getMessage());
                }
            }
        }
    }
}
