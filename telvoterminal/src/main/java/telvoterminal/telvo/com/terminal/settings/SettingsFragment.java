package telvoterminal.telvo.com.terminal.settings;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import java.util.Locale;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout notificationsLinearLayout;
    private LinearLayout pinCodeLinearLayout;
    private LinearLayout changePasswordLinearLayout;
    private LinearLayout changeLanguageLinearLayout;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        notificationsLinearLayout = view.findViewById(R.id.notificationsLinearLayout);
        notificationsLinearLayout.setOnClickListener(this);
        pinCodeLinearLayout = view.findViewById(R.id.pinCodeLinearLayout);
        pinCodeLinearLayout.setOnClickListener(this);
        changePasswordLinearLayout = view.findViewById(R.id.changePasswordLinearLayout);
        changePasswordLinearLayout.setOnClickListener(this);
        changeLanguageLinearLayout=view.findViewById(R.id.changeLanguageLinearLayout);
        changeLanguageLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notificationsLinearLayout:
                Intent notificationsIntent = new Intent(getActivity(), NotificationsActivity.class);
                notificationsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(notificationsIntent);
                break;
            case R.id.pinCodeLinearLayout:
                Intent pinCodeIntent = new Intent(getActivity(), ChangePinCodeActivity.class);
                pinCodeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(pinCodeIntent);
                break;
            case R.id.changePasswordLinearLayout:
                Intent changePasswordIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                changePasswordIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(changePasswordIntent);
                break;
            case R.id.changeLanguageLinearLayout:
                AppManager.setAlert(context, preferences, new ButtonClick() {
                    @Override
                    public void Do() {
                        getActivity().finish();
                        intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //getActivity().finish();
                    }
                });
                break;
            default:
                return;
        }
    }


}
