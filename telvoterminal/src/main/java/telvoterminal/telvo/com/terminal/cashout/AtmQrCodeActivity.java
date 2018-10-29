package telvoterminal.telvo.com.terminal.cashout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import net.glxn.qrgen.android.QRCode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.cashout.ATMWithdraw;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class AtmQrCodeActivity extends BaseActivity {

    private ImageView qrCodeImageView;
    private Bitmap myBitmap;
    private String result;

    private BroadcastReceiver atmBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_qr_code);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_atm_qr, getString(R.string.atm_qr_code));

        initializeViews();

        bundle = getIntent().getExtras();
        if(bundle!=null){
            result = bundle.getString("DATA");
            myBitmap = QRCode.from(result).bitmap();
            qrCodeImageView.setImageBitmap(myBitmap);
        }

        if(!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }

        atmBroadcastReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.ATM_PUSH)) {
                    String message = intent.getStringExtra("message");
                    if(!TextUtils.isEmpty(message)){
                        ATMWithdraw atmWithdraw = (ATMWithdraw) AppManager.getClassObject(message, new ATMWithdraw());
                        intent = new Intent(context, SuccessActivity.class);
                        bundle = new Bundle();
                        List<String> title = new ArrayList<>();
                        List<String> value = new ArrayList<>();

                        title.add("ATM Amount");
                        value.add(AppManager.getCurrencySymbol(user.getCurrency())+" "+atmWithdraw.getAmount());
                        title.add("Withdraw");
                        value.add("ATM Network");


                        Success done = new Success();
                        done.setTitle(title);
                        done.setValue(value);

                        bundle.putSerializable(Constant.success,done);
                        bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.cash_in));
                        intent.putExtras(bundle);


                        if(!preferences.getValue("USER").equals("")) {
                            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                            user.setBalance(atmWithdraw.getBalance());
                            preferences.setValue("USER", AppManager.getClassString(user));
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(atmBroadcastReceiver, new IntentFilter(Constant.ATM_PUSH));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(atmBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initializeViews() {
        qrCodeImageView = getImageView(R.id.qrCodeImageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
