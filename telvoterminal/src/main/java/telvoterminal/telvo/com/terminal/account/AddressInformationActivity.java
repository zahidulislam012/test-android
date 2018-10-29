package telvoterminal.telvo.com.terminal.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightCheckBox;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.currency.Currency;
import telvoterminal.telvo.com.terminal.model.currency.CurrencyData;
import telvoterminal.telvo.com.terminal.model.kyc.AddressInfo;
import telvoterminal.telvo.com.terminal.model.kyc.PermanentAddress;
import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;
import telvoterminal.telvo.com.terminal.model.kyc.PresentAddress;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;


public class AddressInformationActivity extends BaseActivity implements View.OnClickListener, IServiceResultListener {

    private RobotoRegularEditText presentAddressLine1EditText;
    private RobotoRegularEditText presentAddressLine2EditText;
    private RobotoRegularEditText presentCityEditText;
    private RobotoRegularEditText presentPostalCodeEditText;
    private Spinner presentCountrySpinner;
    private RobotoRegularEditText permanentAddressLine1EditText;
    private RobotoRegularEditText permanentAddressLine2EditText;
    private RobotoRegularEditText permanentCityEditText;
    private RobotoRegularEditText permanentPostalCodeEditText;
    private Spinner permanentCountrySpinner;
    private RobotoLightCheckBox sameAsPresentAddressCheckBox;
    private RobotoRegularButton submitButton;

    private String countryString;
    private Currency currency;
    private List<String> countries = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String presentAddressLine1;
    private String presentAddressLine2 = "";
    private String presentCity;
    private String presentPostalCode;
    private String presentCountry;

    private String permanentAddressLine1;
    private String permanentAddressLine2 = "";
    private String permanentCity;
    private String permanentPostalCode;
    private String permanentCountry;

    private AddressInfo addressInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_information);

        setToolbar(R.id.toolbar_address_information, getString(R.string.address_information));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            addressInfo = (AddressInfo) bundle.getSerializable("ADDRESS_INFORMATION");
            PresentAddress presentAddress = addressInfo.getPresentAddress();
            PermanentAddress permanentAddress = addressInfo.getPermanentAddress();
            setPresentAddress(presentAddress);
            setPermanentAddress(permanentAddress);

        }
    }

    private void setPermanentAddress(PermanentAddress permanentAddress) {
        if (!TextUtils.isEmpty(permanentAddress.getAddress())) {
            String[] perAddress = permanentAddress.getAddress().split("\\$\\$");
            if (!perAddress[0].equals(Constant.VALUE_CHECK)) {
                permanentAddressLine1EditText.setText(perAddress[0]);
            }
            if (perAddress.length > 1) {
                if (!perAddress[1].equals(Constant.VALUE_CHECK)) {
                    permanentAddressLine2EditText.setText(perAddress[1]);
                }
            }

        }

        if (!permanentAddress.getCity().equals(Constant.VALUE_CHECK)) {
            permanentCityEditText.setText(permanentAddress.getCity());
        }

        if (!permanentAddress.getPostalCode().equals(Constant.VALUE_CHECK)) {
            permanentPostalCodeEditText.setText(permanentAddress.getPostalCode());
        }

        if (!permanentAddress.getCountry().equals(Constant.VALUE_CHECK)) {
            setupPermanentCountry(permanentAddress.getCountry());
        } else {
            setupPermanentCountry(null);
        }
    }

    private void setPresentAddress(PresentAddress presentAddress) {
        if (!TextUtils.isEmpty(presentAddress.getAddress())) {
            String[] pAddress = presentAddress.getAddress().split("\\$\\$");

            if (!pAddress[0].equals(Constant.VALUE_CHECK)) {
                presentAddressLine1EditText.setText(pAddress[0]);
            }
            if (pAddress.length > 1) {
                if (!pAddress[1].equals(Constant.VALUE_CHECK)) {
                    presentAddressLine2EditText.setText(pAddress[1]);
                }
            }

        }

        if (!presentAddress.getCity().equals(Constant.VALUE_CHECK)) {
            presentCityEditText.setText(presentAddress.getCity());
        }

        if (!presentAddress.getPostalCode().equals(Constant.VALUE_CHECK)) {
            presentPostalCodeEditText.setText(presentAddress.getPostalCode());
        }

        if (!presentAddress.getCountry().equals(Constant.VALUE_CHECK)) {
            setupPresentCountry(presentAddress.getCountry());
        } else {
            setupPresentCountry(null);
        }
    }

    private void setupPresentCountry(String countryName) {
        int selected = getCountryList(countryName);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_item, countries);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        presentCountrySpinner.setAdapter(adapter);
        presentCountrySpinner.setSelection(selected);
    }

    private void setupPermanentCountry(String countryName) {
        int selected = getCountryList(countryName);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_item, countries);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        permanentCountrySpinner.setAdapter(adapter);
        permanentCountrySpinner.setSelection(selected);
    }

    private int getCountryList(String countryName) {
        int selected = 0;
        countryString = AppManager.loadJSONFromAsset(context, "currency");
        currency = (Currency) new AppManager().getClassObject(countryString, new Currency());


        countries.add(getString(R.string.select_country));

        for (CurrencyData currencyData : currency.getCcyNtry()) {
            String result = currencyData.getCtryNm();
            countries.add(result);
            if (result.equals(countryName)) {
                selected = countries.size() - 1;
            }
        }
        return selected;
    }

    private void initializeViews() {
        presentAddressLine1EditText = getRobotoRegularEditText(R.id.presentAddressLine1EditText);
        presentAddressLine2EditText = getRobotoRegularEditText(R.id.presentAddressLine2EditText);
        presentCityEditText = getRobotoRegularEditText(R.id.presentCityEditText);
        presentPostalCodeEditText = getRobotoRegularEditText(R.id.presentPostalCodeEditText);
        presentCountrySpinner = getSpinner(R.id.presentCountrySpinner);
        permanentAddressLine1EditText = getRobotoRegularEditText(R.id.permanentAddressLine1EditText);
        permanentAddressLine2EditText = getRobotoRegularEditText(R.id.permanentAddressLine2EditText);
        permanentCityEditText = getRobotoRegularEditText(R.id.permanentCityEditText);
        permanentPostalCodeEditText = getRobotoRegularEditText(R.id.permanentPostalCodeEditText);
        permanentCountrySpinner = getSpinner(R.id.permanentCountrySpinner);
        sameAsPresentAddressCheckBox = getRobotoLightCheckBox(R.id.sameAsPresentAddressCheckBox);
        sameAsPresentAddressCheckBox.setOnClickListener(this);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                boolean done = true;
                presentAddressLine1 = presentAddressLine1EditText.getText().toString().trim();
                presentAddressLine2 = presentAddressLine2EditText.getText().toString().trim();
                presentCity = presentCityEditText.getText().toString().trim();
                presentPostalCode = presentPostalCodeEditText.getText().toString().trim();
                presentCountry = presentCountrySpinner.getSelectedItem().toString().trim();
                permanentAddressLine1 = permanentAddressLine1EditText.getText().toString().trim();
                permanentAddressLine2 = permanentAddressLine2EditText.getText().toString().trim();
                permanentCity = permanentCityEditText.getText().toString().trim();
                permanentPostalCode = permanentPostalCodeEditText.getText().toString().trim();
                permanentCountry = permanentCountrySpinner.getSelectedItem().toString().trim();

                if (Validation.isTextEmpty(presentAddressLine1)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_address_line_1_empty));
                } else if (presentAddressLine1.length() > 64) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_address_character));
                } else if (presentAddressLine2.length() > 64) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_address_character));
                } else if (Validation.isTextEmpty(presentCity)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_city_empty));
                } else if (Validation.isTextEmpty(presentPostalCode)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_postal_code_empty));
                } else if (presentCountrySpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_country_was_not_selected));
                } else if (Validation.isTextEmpty(permanentAddressLine1)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.permanent_address_line_1_empty));
                } else if (permanentAddressLine1.length() > 64) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_address_character));
                } else if (permanentAddressLine2.length() > 64) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.present_address_character));
                } else if (Validation.isTextEmpty(permanentCity)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.permanent_city_empty));
                } else if (Validation.isTextEmpty(permanentPostalCode)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.permanent_postal_code_empty));
                } else if (permanentCountrySpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.permanent_country_was_not_selected));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setAddress(presentAddressLine1, presentAddressLine2, presentCity, presentPostalCode, presentCountry, permanentAddressLine1, permanentAddressLine2, permanentCity, permanentPostalCode, permanentCountry);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            case R.id.sameAsPresentAddressCheckBox:
                boolean checked = ((CheckBox) view).isChecked();
                if (checked) {
                    permanentAddressLine1EditText.setText(presentAddressLine1EditText.getText().toString());
                    permanentAddressLine2EditText.setText(presentAddressLine2EditText.getText().toString());
                    permanentCityEditText.setText(presentCityEditText.getText().toString());
                    permanentPostalCodeEditText.setText(presentPostalCodeEditText.getText().toString());
                    permanentCountrySpinner.setSelection(presentCountrySpinner.getSelectedItemPosition());
                } else {
                    if (addressInfo != null) {
                        PermanentAddress permanentAddress = addressInfo.getPermanentAddress();
                        setPermanentAddress(permanentAddress);
                    } else {
                        permanentAddressLine1EditText.setText("");
                        permanentAddressLine2EditText.setText("");
                        permanentCityEditText.setText("");
                        permanentPostalCodeEditText.setText("");
                        permanentCountrySpinner.setSelection(0);
                    }

                }
                break;
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if (success) {
            if (method.equals("ADDRESS_INFORMATION")) {
                addressInfo = (AddressInfo) dtoBase;
                if (addressInfo.getStatus().equals(Constant.success)) {
                    customAlertDialog.showDialogWithYes(addressInfo.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            if (!preferences.getValue("USER").equals("")) {
                                user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                                user.getKyc().setAllVerified(false);
                                user.getKyc().getAddressInfo().getPresentAddress().setVerified(false);
                                user.getKyc().getAddressInfo().getPermanentAddress().setVerified(false);
                                user.getKyc().getAddressInfo().getPresentAddress().setDeclined(false);
                                user.getKyc().getAddressInfo().getPermanentAddress().setDeclined(false);

                                user.getKyc().getAddressInfo().getPresentAddress().setInserted(true);
                                user.getKyc().getAddressInfo().getPermanentAddress().setInserted(true);

                                user.getKyc().getAddressInfo().getPresentAddress().setAddress(presentAddressLine1 + "$$" + presentAddressLine2);
                                user.getKyc().getAddressInfo().getPresentAddress().setCity(presentCity);
                                user.getKyc().getAddressInfo().getPresentAddress().setPostalCode(presentPostalCode);
                                user.getKyc().getAddressInfo().getPresentAddress().setCountry(presentCountry);

                                user.getKyc().getAddressInfo().getPermanentAddress().setAddress(permanentAddressLine1 + "$$" + permanentAddressLine2);
                                user.getKyc().getAddressInfo().getPermanentAddress().setCity(permanentCity);
                                user.getKyc().getAddressInfo().getPermanentAddress().setPostalCode(permanentPostalCode);
                                user.getKyc().getAddressInfo().getPermanentAddress().setCountry(permanentCountry);

                                preferences.setValue("USER", AppManager.getClassString(user));
                                finish();
                            } else {
                                finish();
                            }
                        }
                    });
                } else if (addressInfo.getStatus().equals(Constant.error)) {
                    customAlertDialog.showDialog(addressInfo.getMessage());
                }
            }
        }
    }
}
