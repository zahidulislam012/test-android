package telvoterminal.telvo.com.terminal.support;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class PrivacyPolicyActivity extends BaseActivity {

    private WebView privacyPolicyWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_privacy_policy, getStringResources(R.string.privacy_policy));

        initializeViews();
    }

    private void initializeViews() {
        privacyPolicyWebView = getWebView(R.id.privacyPolicyWebView);
        privacyPolicyWebView.loadUrl("file:///android_asset/privacy/privacy.html");
        privacyPolicyWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        privacyPolicyWebView.setLongClickable(false);

// Below line prevent vibration on Long click
        privacyPolicyWebView.setHapticFeedbackEnabled(false);
    }
}
