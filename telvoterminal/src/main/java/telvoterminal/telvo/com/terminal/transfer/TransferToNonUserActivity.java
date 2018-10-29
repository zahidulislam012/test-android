package telvoterminal.telvo.com.terminal.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class TransferToNonUserActivity extends BaseActivity implements View.OnClickListener {

    private RobotoRegularEditText accountNumberEditText;
    private RobotoRegularEditText amountEditText;
    private RobotoRegularEditText nidEditText;
    private RobotoRegularEditText nameEditText;

    private RobotoRegularButton proceedButton;


    private String nid;
    private String name;

    private String accountNumber;
    private String amount;
    private String waitingTime;
    private String reference;
    private String commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_to_non_user);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_transfer_to_non_user, getStringResources(R.string.transfer));

        initializeViews();

        bundle = getIntent().getExtras();
        if(bundle!=null){
            accountNumber= bundle.getString("receiver");
            accountNumberEditText.setText(accountNumber.substring(3));
            accountNumberEditText.setEnabled(false);

            amount = bundle.getString("amount");
            commission = bundle.getString("commission");
            amountEditText.setText(amount);
            amountEditText.setEnabled(false);
            reference = bundle.getString("message");
            waitingTime = bundle.getString("waitingTime");
        }
    }

    private void initializeViews() {
        accountNumberEditText = getRobotoRegularEditText(R.id.accountNumberEditText);
        amountEditText = getRobotoRegularEditText(R.id.amountEditText);
        nidEditText = getRobotoRegularEditText(R.id.nidEditText);
        nameEditText = getRobotoRegularEditText(R.id.nameEditText);
        proceedButton = getRobotoRegularButton(R.id.proceedButton);
        proceedButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedButton:
                boolean done = true;
                accountNumber = accountNumberEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();
                nid = nidEditText.getText().toString().trim();
                name = nameEditText.getText().toString().trim();

                if (Validation.isTextEmpty(accountNumber)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.account_number_empty));
                } else if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else if (Validation.isTextEmpty(nid)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.natinal_id_empty));
                } else if (Validation.isTextEmpty(name)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.nameEmpty));
                } else if (Validation.isTextEmpty(reference)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.reference_empty));
                }

                if (done) {
                    intent = new Intent(context,ConfirmTransferActivity.class);
                    bundle = new Bundle();
                    bundle.putString("TYPE","nouser");
                    bundle.putString("receiver","880"+accountNumber);
                    bundle.putString("amount",amount);
                    bundle.putString("message",reference);
                    bundle.putString("waitingTime",waitingTime);
                    bundle.putString("commission",commission);
                    bundle.putString("name",name);
                    bundle.putString("NID",nid);
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }


}
