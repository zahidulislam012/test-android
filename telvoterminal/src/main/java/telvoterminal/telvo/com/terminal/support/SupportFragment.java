package telvoterminal.telvo.com.terminal.support;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment implements View.OnClickListener {

    private LinearLayout callLinearLayout;
    private LinearLayout helpLinearLayout;
    private LinearLayout privacyPolicyLinearLayout;
    private LinearLayout termsLinearLayout;
    private LinearLayout feedbackLinearLayout;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1028;

    public SupportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        callLinearLayout = view.findViewById(R.id.callLinearLayout);
        callLinearLayout.setOnClickListener(this);
        helpLinearLayout = view.findViewById(R.id.helpLinearLayout);
        helpLinearLayout.setOnClickListener(this);
        privacyPolicyLinearLayout = view.findViewById(R.id.privacyPolicyLinearLayout);
        privacyPolicyLinearLayout.setOnClickListener(this);
        termsLinearLayout = view.findViewById(R.id.termsLinearLayout);
        termsLinearLayout.setOnClickListener(this);
        feedbackLinearLayout = view.findViewById(R.id.feedbackLinearLayout);
        feedbackLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callLinearLayout:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+88029887646"));
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else {
                    //You already have permission
                    try {
                        startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.helpLinearLayout:
                Intent helpIntent = new Intent(getActivity(), HelpAndSupportActivity.class);
                helpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(helpIntent);
                break;
            case R.id.privacyPolicyLinearLayout:
                Intent privacyIntent = new Intent(getActivity(), PrivacyPolicyActivity.class);
                privacyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(privacyIntent);
                break;
            case R.id.termsLinearLayout:
                Intent termsIntent = new Intent(getActivity(), TermsAndConditionsActivity.class);
                termsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(termsIntent);
                break;
            case R.id.feedbackLinearLayout:
                Intent feedbackIntent = new Intent(Intent.ACTION_SEND);
                feedbackIntent.setType("plain/text");
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "terminal@telvo.com" });
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: ");
                //feedbackIntent.putExtra(Intent.EXTRA_TEXT, "mail body");
                startActivity(Intent.createChooser(feedbackIntent, ""));
                break;
            default:
                return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+88029887646"));
                    startActivity(callIntent);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
