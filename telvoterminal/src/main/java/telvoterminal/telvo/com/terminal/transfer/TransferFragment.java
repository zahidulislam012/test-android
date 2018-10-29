package telvoterminal.telvo.com.terminal.transfer;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.transfer.TransferMoney;
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
public class TransferFragment extends BaseFragment implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText accountNumberEditText;
    private RelativeLayout contactsImageView;
    private RobotoRegularEditText amountEditText;
    private Spinner waitingTimeSpinner;
    private RobotoRegularEditText referenceEditText;
    private RobotoRegularButton proceedButton;

    private String accountNumber;
    private String amount;
    private String waitingTime;
    private String reference;

    private TransferMoney transfer;

    private static final int RESULT_PICK_CONTACT = 1000;

    public TransferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transfer, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            currentBalanceTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(user.getBalance()));

        }

        return rootView;
    }

    private void initializeViews(View view) {
        currentBalanceTextView = view.findViewById(R.id.currentBalanceTextView);
        contactsImageView = view.findViewById(R.id.contactsImageView);
        contactsImageView.setOnClickListener(this);
        accountNumberEditText = view.findViewById(R.id.accountNumberEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        waitingTimeSpinner = view.findViewById(R.id.waitingTimeSpinner);
        referenceEditText = view.findViewById(R.id.referenceEditText);
        proceedButton = view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(this);

        setupWaitingTimeSpinner();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactsImageView:
                pickContact();
                break;
            case R.id.proceedButton:
                boolean done = true;
                accountNumber = accountNumberEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();
                waitingTime = waitingTimeSpinner.getSelectedItem().toString().trim();
                reference = referenceEditText.getText().toString().trim();

                if (Validation.isTextEmpty(accountNumber)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
                } else if (accountNumber.length() < 10) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_invalid));
                } else if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else if (waitingTimeSpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.select_waiting_time));
                } else if (Validation.isTextEmpty(reference)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.reference_empty));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if (AppManager.AccountVerify(context)) {
                            if (Character.isDigit(accountNumber.charAt(0))) {
                                accountNumber = Integer.parseInt(Character.toString(accountNumber.charAt(0))) == 0 ? (accountNumber.substring(1, accountNumber.length())) : accountNumber;
                                service = new TerminalService(context, this);
                                service.setPreTransfer("880" + accountNumber, amount);
                            }

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

                /*Intent intent = new Intent(getActivity(), TransferToNonUserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
                break;
            default:
                return;
        }
    }

    private void setupWaitingTimeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.waiting_time_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        waitingTimeSpinner.setAdapter(adapter);
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
            accountNumberEditText.setText(phoneNo);
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
            if (method.equals("TRANSFER_CHECK")) {
                transfer = (TransferMoney) dtoBase;
                if (transfer.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, ConfirmTransferActivity.class);
                    bundle = new Bundle();
                    bundle.putString("TYPE", "user");
                    bundle.putString("receiver", "880" + accountNumber);
                    bundle.putString("amount", amount);
                    bundle.putString("message", reference);
                    bundle.putString("commission", transfer.getCommission());
                    bundle.putString("waitingTime", waitingTimeSpinner.getSelectedItem().toString().replace("Min", ""));
                    intent.putExtras(bundle);
                    getActivity().startActivityForResult(intent, Constant.RESULT_CODE);
                } else if (transfer.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(transfer.getMessage());
                } else if (transfer.getStatus().equals(Constant.nonexist)) {
                    customAlertDialog.showDialogWithYesNo(getActivity().getString(R.string.warning), getStringResources(R.string.w_meessage), new ButtonClick() {
                        @Override
                        public void Do() {
                            intent = new Intent(context, TransferToNonUserActivity.class);
                            bundle = new Bundle();
                            bundle.putString("receiver", "880" + accountNumber);
                            bundle.putString("amount", amount);
                            bundle.putString("message", reference);
                            bundle.putString("commission", transfer.getCommission());
                            bundle.putString("waitingTime", waitingTimeSpinner.getSelectedItem().toString().replace("Min", ""));
                            intent.putExtras(bundle);
                            getActivity().startActivityForResult(intent, Constant.RESULT_CODE);
                        }
                    }, new ButtonClick() {
                        @Override
                        public void Do() {

                        }
                    });
                }
            }
        }
    }
}
