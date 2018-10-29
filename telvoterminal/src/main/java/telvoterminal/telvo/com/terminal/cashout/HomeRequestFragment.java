package telvoterminal.telvo.com.terminal.cashout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.cashout.HomeRequest;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeRequestFragment extends BaseFragment implements View.OnClickListener ,IServiceResultListener{

    private RobotoRegularTextView currentBalanceTextView;
    private RobotoRegularEditText amountEditText;
    private Spinner districtSpinner;
    private Spinner areaSpinner;
    private RobotoRegularEditText addressEditText;
    private RobotoRegularButton proceedButton;

    //private String currentBalance;
    private String amount;
    private String district;
    private String area;
    private String address;

    public HomeRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home_request, container, false);

        context = getActivity();
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
        amountEditText = view.findViewById(R.id.amountEditText);
        districtSpinner = view.findViewById(R.id.districtSpinner);
        areaSpinner = view.findViewById(R.id.areaSpinner);
        addressEditText = view.findViewById(R.id.addressEditText);
        proceedButton = view.findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(this);

        setupDistrictSpinner();
        setupAreaSpinner();
    }

    private void setupDistrictSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.district_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        districtSpinner.setAdapter(adapter);
    }

    private void setupAreaSpinner() {
        ArrayList<String> adminPoints = new ArrayList<>();
        Gson gson = new Gson();
        String adminPointsJson = preferences.getValue("ADMIN_POINTS");
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        adminPoints = gson.fromJson(adminPointsJson, type);
        adminPoints.add(0, getString(R.string.select_area));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, adminPoints);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        areaSpinner.setAdapter(adapter);
    }
 
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.proceedButton:
                boolean done = true;
                //currentBalance = currentBalanceTextView.getText().toString().trim().substring(1);
                amount = amountEditText.getText().toString().trim();
                district = districtSpinner.getSelectedItem().toString();
                area = areaSpinner.getSelectedItem().toString();
                address = addressEditText.getText().toString().trim();

                if (Validation.isTextEmpty(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.amount_empty));
                } else if (districtSpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.district_not_selected));
                } else if (areaSpinner.getSelectedItemPosition() < 1) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.area_not_selected));
                } else if (Validation.isTextEmpty(address)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.area_empty));
                }/* else if (Double.parseDouble(currentBalance) < Double.parseDouble(amount)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.insufficient_balance));
                }*/

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        if(AppManager.AccountVerify(context)){
                            service = new TerminalService(context,this);
                            service.setPreHomeRequest(amount);
                        }else{
                            customAlertDialog.showDialogWithYes(getStringResources(R.string.account_verify), new ButtonClick() {
                                @Override
                                public void Do() {
                                    MainActivity.callAccount();
                                }
                            });
                        }

                    }else {
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
        if(success) {
            if(method.equals("PRE_HOME_REQUEST")){
                HomeRequest homeRequest = (HomeRequest) dtoBase;
                if(homeRequest.getStatus().equals(Constant.success)){
                    intent = new Intent(context, ConfirmRequestActivity.class);
                    bundle = new Bundle();
                    bundle.putString("Amount",amount);
                    bundle.putString("District",district);
                    bundle.putString("Area",area);
                    bundle.putString("Address",address);
                    bundle.putString("commission",homeRequest.getCommission());
                    intent.putExtras(bundle);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivityForResult(intent, Constant.RESULT_CODE);
                }else if(homeRequest.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(homeRequest.getMessage());
                }
            }
        }
    }
}
