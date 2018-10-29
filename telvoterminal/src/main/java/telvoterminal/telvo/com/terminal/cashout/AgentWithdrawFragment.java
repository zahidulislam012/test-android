package telvoterminal.telvo.com.terminal.cashout;


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
import android.widget.RelativeLayout;

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
import telvoterminal.telvo.com.terminal.model.cashout.AgentWithdraw;
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
public class AgentWithdrawFragment extends BaseFragment implements View.OnClickListener, IServiceResultListener {

    private static final String TAG = AgentWithdrawFragment.class.getSimpleName();

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText agentNumberEditText;
    private RelativeLayout contactPicker;
    private RobotoRegularEditText amountEditText;
    private RobotoRegularButton proceedButton;

    private String agentNumber;
    private String amount;

    private static final int RESULT_PICK_CONTACT = 1000;

    public AgentWithdrawFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_withdraw, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        context = getActivity();
        initializeViews(rootView);

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            currentBalanceTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(user.getBalance()));
        }

        return rootView;
    }

    private void initializeViews(View view) {
        currentBalanceTextView = view.findViewById(R.id.currentBalanceTextView);
        agentNumberEditText = view.findViewById(R.id.agentNumberEditText);
        contactPicker = view.findViewById(R.id.contactsPicker);
        contactPicker.setOnClickListener(this);
        amountEditText = view.findViewById(R.id.amountEditText);
        proceedButton = view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.contactsPicker:
                pickContact();
                break;
            case R.id.proceedButton:
                agentNumber = agentNumberEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();

                if (Validation.isTextEmpty(agentNumber)) {
                    customAlertDialog.showDialog(getStringResources(R.string.agent_number_empty));
                } else if (agentNumber.length() < 10) {
                    customAlertDialog.showDialog(getStringResources(R.string.agent_number_invalid));
                } else if (Validation.isTextEmpty(amount)) {
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        if (AppManager.AccountVerify(context)) {
                            agentNumber = Integer.parseInt(Character.toString(agentNumber.charAt(0))) == 0 ? (agentNumber.substring(1, agentNumber.length())) : agentNumber;
                            agentNumber = "880" + agentNumber;
                            service = new TerminalService(context, this);
                            service.setPreAgentWithdraw( agentNumber, amount);
                        } else {
                            customAlertDialog.showDialogWithYes(getStringResources(R.string.account_verify), new ButtonClick() {
                                @Override
                                public void Do() {
                                    MainActivity.callAccount();
                                }
                            });
                        }
                    } else {
                        customAlertDialog.showDialog(getString(R.string.no_internet_connection));
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
            Log.e(TAG, "Failed to pick contact");
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
            if (cursor != null) {
                cursor.moveToFirst();
            }
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            phoneNo = phoneNo.replaceAll("\\D", "");
            phoneNo = formatPhoneNo(phoneNo);
            name = cursor.getString(nameIndex);
            // Set the value to the views
            agentNumberEditText.setText(phoneNo);
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
            if (method.equals("PRE_AGENT_WITHDRAW")) {
                AgentWithdraw agentWithdraw = (AgentWithdraw) dtoBase;
                if (agentWithdraw.getStatus().equals(Constant.success)) {
                    intent = new Intent(context, ConfirmAgentWithdrawActivity.class);
                    bundle = new Bundle();
                    bundle.putString("agentNumber", agentNumber);
                    bundle.putString("amount", amount);
                    bundle.putString("fees", String.valueOf(agentWithdraw.getCommission()));
                    bundle.putString("total", String.valueOf(Double.valueOf(amount) + agentWithdraw.getCommission()));
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivityForResult(intent, Constant.RESULT_CODE);
                } else if (agentWithdraw.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(agentWithdraw.getMessage());
                }
            }
        }
    }
}
