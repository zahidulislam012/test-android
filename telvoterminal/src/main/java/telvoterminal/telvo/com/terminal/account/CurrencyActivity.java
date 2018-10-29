package telvoterminal.telvo.com.terminal.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.UserLogin;
import telvoterminal.telvo.com.terminal.model.currency.Currency;
import telvoterminal.telvo.com.terminal.model.currency.CurrencyData;
import telvoterminal.telvo.com.terminal.model.kyc.CurrencyInfo;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class CurrencyActivity extends BaseActivity implements View.OnClickListener,IServiceResultListener {

    private Spinner currencySpinner;
    private RobotoRegularButton submitButton;

    private String currencyString;
    private String selectedCurrency;
    private ArrayAdapter<String> adapter;
    private List<String> currencyTypes = new ArrayList<>();
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_currency, getString(R.string.currency));
        initializeViews();
        setupCurrencySpinner();

    }

    private void setupCurrencySpinner() {
        currencyString = AppManager.loadJSONFromAsset(context, "currency");
        currency = (Currency) new AppManager().getClassObject(currencyString, new Currency());

        currencyTypes.add("Select Currency");

        for (CurrencyData currencyData : currency.getCcyNtry()) {
            String result = currencyData.getCcy();
            currencyTypes.add(result);
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_item, currencyTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        currencySpinner.setAdapter(adapter);
    }

    private void initializeViews() {
        currencySpinner = getSpinner(R.id.currencySpinner);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                selectedCurrency = currencySpinner.getSelectedItem().toString();
                if (currencySpinner.getSelectedItemPosition() > 0) {
                    if (AppManager.hasInternetConnection(context)) {
                            service.setCurrency(selectedCurrency);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                } else {
                    customAlertDialog.showDialog(getStringResources(R.string.currency_not_selected));
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("CURRENCY_INFORMATION")){
                CurrencyInfo currencyInfo = (CurrencyInfo) dtoBase;
                if(currencyInfo.getStatus().equals(Constant.success)){
                    customAlertDialog.showDialogWithYes(currencyInfo.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            if(!preferences.getValue("USER").equals("")) {
                                user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                                user.setCurrency(selectedCurrency);
                                preferences.setValue("USER", AppManager.getClassString(user));
                                finish();
                            }else{
                                finish();
                            }
                        }
                    });
                }else if(currencyInfo.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(currencyInfo.getMessage());
                }
            }
        }
    }
}