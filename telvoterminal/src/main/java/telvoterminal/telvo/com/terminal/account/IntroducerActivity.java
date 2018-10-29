package telvoterminal.telvo.com.terminal.account;

import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.hbb20.CountryCodePicker;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightCheckBox;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.kyc.IntroducerInfo;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class IntroducerActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularEditText nameEditText;
    private RobotoRegularEditText relationEditText;
    private CountryCodePicker countryCodePicker;
    private RobotoRegularEditText mobileNumberEditText;
    private RobotoLightCheckBox hasAccountCheckBox;
    private RobotoRegularButton submitButton;

    private String name;
    private String relationShip;
    private String mobileNumber;
    private Boolean hasAccount = false;
    private IntroducerInfo introducerInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introducer);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_introducer, getString(R.string.introducer));
        initializeViews();

        bundle = getIntent().getExtras();
        if(bundle!=null){
            introducerInfo = (IntroducerInfo) bundle.getSerializable("INTRODUCER");
            if(!introducerInfo.getName().equals(Constant.VALUE_CHECK)){
                nameEditText.setText(introducerInfo.getName());
            }
            if(!introducerInfo.getRelation().equals(Constant.VALUE_CHECK)){
                relationEditText.setText(introducerInfo.getRelation());
            }
            if(!introducerInfo.getMobileNumber().equals(Constant.VALUE_CHECK)){
                mobileNumberEditText.setText(introducerInfo.getMobileNumber());
            }
        }

    }

    private void initializeViews() {
        nameEditText = getRobotoRegularEditText(R.id.nameEditText);
        relationEditText= getRobotoRegularEditText(R.id.relationEditText);
        countryCodePicker = getCountryCodePicker(R.id.countryCodePicker);
        mobileNumberEditText = getRobotoRegularEditText(R.id.mobileNumberEditText);
        hasAccountCheckBox = getRobotoLightCheckBox(R.id.hasAccountCheckBox);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                name = nameEditText.getText().toString().trim();
                relationShip=relationEditText.getText().toString().trim();
                mobileNumber = mobileNumberEditText.getText().toString().trim();
                Boolean done = true;
                if (Validation.isTextEmpty(name)) {
                    customAlertDialog.showDialog(getStringResources(R.string.nameEmpty));
                    done = false;
                } else if(Validation.isTextEmpty(relationShip)){
                    customAlertDialog.showDialog(getStringResources(R.string.relationEmpty));
                    done = false;
                }else if (Validation.isTextEmpty(mobileNumber)) {
                    customAlertDialog.showDialog(getStringResources(R.string.mobile_numberEmpty));
                    done = false;
                }

                if (Validation.isCheckBoxChecked(hasAccountCheckBox)) {
                    hasAccount = true;
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setIntroducer(name, relationShip,countryCodePicker.getSelectedCountryCode(),mobileNumber, hasAccount);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("INTRODUCER_INFORMATION")) {
                introducerInfo = (IntroducerInfo) dtoBase;
                if(introducerInfo.getStatus().equals(Constant.success)){
                    customAlertDialog.showDialogWithYes(introducerInfo.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            if(!preferences.getValue("USER").equals("")) {
                                user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                                user.getKyc().setAllVerified(false);
                                user.getKyc().getIntroducerInfo().setVerified(false);
                                user.getKyc().getIntroducerInfo().setDeclined(false);
                                user.getKyc().getIntroducerInfo().setName(name);
                                user.getKyc().getIntroducerInfo().setCountryCode(countryCodePicker.getSelectedCountryCode());
                                user.getKyc().getIntroducerInfo().setMobileNumber(mobileNumber);
                                user.getKyc().getIntroducerInfo().setInserted(true);
                                preferences.setValue("USER", AppManager.getClassString(user));
                                finish();
                            }else{
                                finish();
                            }
                        }
                    });
                }else if(introducerInfo.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(introducerInfo.getMessage());
                }
            }
        }
    }
}
