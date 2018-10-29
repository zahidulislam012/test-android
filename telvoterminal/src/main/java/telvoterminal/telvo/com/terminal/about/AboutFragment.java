package telvoterminal.telvo.com.terminal.about;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {

    private RobotoRegularTextView appVersionTextView;
    private LinearLayout rateLinearLayout;
    private LinearLayout inviteLinearLayout;
    private LinearLayout walkthroughLinearLayout;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());
        getVersionName();
        initializeViews(rootView);

        return rootView;
    }

    private String getVersionName() {
        String versionName = "";
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    private void initializeViews(View view) {
        appVersionTextView = view.findViewById(R.id.appVersionTextView);
        appVersionTextView.setText(getVersionName());
        rateLinearLayout = view.findViewById(R.id.rateLinearLayout);
        rateLinearLayout.setOnClickListener(this);
        inviteLinearLayout = view.findViewById(R.id.inviteLinearLayout);
        inviteLinearLayout.setOnClickListener(this);
        walkthroughLinearLayout = view.findViewById(R.id.walkthroughLinearLayout);
        walkthroughLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rateLinearLayout:
                openPlayStore();
                break;
            case R.id.inviteLinearLayout:
                inviteFriends();
                break;
            case R.id.walkthroughLinearLayout:
                break;
            default:
                return;
        }
    }

    private void inviteFriends() {
        final String appPackageName = getActivity().getPackageName();
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject test");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
        startActivity(Intent.createChooser(i,"Share via"));
    }

    private void openPlayStore() {
        final String appPackageName = getActivity().getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
}
