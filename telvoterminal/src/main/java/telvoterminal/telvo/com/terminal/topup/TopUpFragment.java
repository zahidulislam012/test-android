package telvoterminal.telvo.com.terminal.topup;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.OtpView.OtpView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularRadioButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.TopUp;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopUpFragment extends BaseFragment implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText phoneNoEditText;
    private RobotoRegularRadioButton prepaidRadioButton;
    private RobotoRegularRadioButton postpaidRadioButton;
    private Spinner operatorSpinner;
    private RobotoRegularEditText amountEditText;
    /*private RobotoRegularEditText pinCodeEditText1;
    private RobotoRegularEditText pinCodeEditText2;
    private RobotoRegularEditText pinCodeEditText3;
    private RobotoRegularEditText pinCodeEditText4;*/
    private RobotoRegularButton submitButton;
    private RelativeLayout contactsImageView;

    private String currentBalance;
    private String phoneNumber;
    private String phoneType;
    private String operator;
    private String amount;
  /*  private String pinCode1;
    private String pinCode2;
    private String pinCode3;
    private String pinCode4;
    private String pinCode;
*/

    private OtpView pinCodeOtpView;

    private static final int RESULT_PICK_CONTACT = 1000;

    private String[] operatorList = {"", "Airtel", "Banglalink", "GP", "Robi", "Teletalk"};

    public TopUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_top_up, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);
        setupOperatorSpinner();

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            currentBalanceTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(user.getBalance()));

        }

        return rootView;
    }

    private void initializeViews(View view) {
        currentBalanceTextView = view.findViewById(R.id.currentBalanceTextView);
        phoneNoEditText = view.findViewById(R.id.phoneNoEditText);
        contactsImageView = view.findViewById(R.id.contactsImageView);
        contactsImageView.setOnClickListener(this);
        prepaidRadioButton = view.findViewById(R.id.prepaidRadioButton);
        prepaidRadioButton.setOnClickListener(this);
        postpaidRadioButton = view.findViewById(R.id.postpaidRadioButton);
        postpaidRadioButton.setOnClickListener(this);
        operatorSpinner = view.findViewById(R.id.operatorSpinner);
        amountEditText = view.findViewById(R.id.amountEditText);
        pinCodeOtpView = view.findViewById(R.id.pinCodeOtpView);
       /* pinCodeEditText1 = view.findViewById(R.id.pinCodeEditText1);
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
        pinCodeEditText2 = view.findViewById(R.id.pinCodeEditText2);
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
        pinCodeEditText3 = view.findViewById(R.id.pinCodeEditText3);
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
        pinCodeEditText4 = view.findViewById(R.id.pinCodeEditText4);
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
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                //to show soft keyboard
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //to hide it, call the method again
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });*/
        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    private void setupOperatorSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.operator_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        operatorSpinner.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        boolean checked;
        switch (view.getId()) {
            case R.id.contactsImageView:
                pickContact();
                break;
            case R.id.prepaidRadioButton:
                checked = ((RobotoRegularRadioButton) view).isChecked();
                if (checked) {
                    phoneType = "0";
                }
                break;
            case R.id.postpaidRadioButton:
                checked = ((RobotoRegularRadioButton) view).isChecked();
                if (checked) {
                    phoneType = "1";
                }
                break;
            case R.id.submitButton:
                boolean done = true;
                currentBalance = currentBalanceTextView.getText().toString().trim().substring(1);
                phoneNumber = phoneNoEditText.getText().toString().trim();
                operator = operatorList[operatorSpinner.getSelectedItemPosition()];
                amount = amountEditText.getText().toString().trim();
              /*  pinCode1 = pinCodeEditText1.getText().toString().trim();
                pinCode2 = pinCodeEditText2.getText().toString().trim();
                pinCode3 = pinCodeEditText3.getText().toString().trim();
                pinCode4 = pinCodeEditText4.getText().toString().trim();
                pinCode = pinCode1 + pinCode2 + pinCode3 + pinCode4;*/

                if (!TextUtils.isEmpty(phoneNumber)) {
                    if (Character.isDigit(phoneNumber.charAt(0))) {
                        phoneNumber = Integer.parseInt(Character.toString(phoneNumber.charAt(0))) == 0 ? (phoneNumber.substring(1, phoneNumber.length())) : phoneNumber;
                    }
                }

                Log.i("DATA", phoneNumber.length() + "");
                if (Validation.isTextEmpty(phoneNumber)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.phone_number_empty));
                } else if (phoneNumber.length() < 10) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.phone_number_ten));
                } else if (phoneNumber.length() > 10) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.phone_number_less_ten));
                } else if (Validation.isTextEmpty(phoneType)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.select_phone_no_type));
                } else if (operatorSpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.select_operator));
                } else if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else if (Validation.isTextEmpty(pinCodeOtpView.getOTP())) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.enter_pin_code));
                }else if(!pinCodeOtpView.hasValidOTP()){
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.invalid_pin_size));
                } else if (phoneType.equals("0")) {
                    if (Double.parseDouble(amount) < 10.0) {
                        done = false;
                        customAlertDialog.showDialog(getStringResources(R.string.less_prepaid_recharge_amount));
                    }
                } else if (phoneType.equals("1")) {
                    if (Double.parseDouble(amount) < 50.0) {
                        done = false;
                        customAlertDialog.showDialog(getStringResources(R.string.less_postpaid_recharge_amount));
                    }
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if (AppManager.AccountVerify(context)) {
                            service = new TerminalService(context, this);
                            service.setTopUp("880" + phoneNumber, phoneType, operator,
                                    amount, pinCodeOtpView.getOTP() );
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
        }
    }

    public void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            phoneNo = phoneNo.replaceAll("\\D", "");
            phoneNo = formatPhoneNo(phoneNo);
            name = cursor.getString(nameIndex);
            // Set the value to the views
            phoneNoEditText.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatPhoneNo(String phoneNo) {
        if (phoneNo.startsWith("8")) {
            phoneNo = phoneNo.substring(3);
        } else if (phoneNo.startsWith("0")) {
            phoneNo = phoneNo.substring(1);
        }
        return phoneNo;
    }


    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("MOBILE_TOPUP")) {
                TopUp topUp = (TopUp) dtoBase;
                if (topUp.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add(getString(R.string.to));
                    value.add("0" + phoneNumber);

                    title.add(getString(R.string.recharge_amount));
                    value.add("৳ " + topUp.getAmount());

                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    bundle.putSerializable(Constant.success, done);
                    bundle.putString(Constant.ACTIVITY_NAME, getActivity().getString(R.string.top_up));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    if (!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(topUp.getBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    getActivity().startActivityForResult(intent, Constant.RESULT_CODE);
                } else if (topUp.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(topUp.getMessage());
                }
            }
        }
    }
}
