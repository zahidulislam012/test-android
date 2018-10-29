package telvoterminal.telvo.com.terminal.support;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class TermsAndConditionsActivity extends BaseActivity {

    private WebView termsAndConditionsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_terms_and_conditions, getStringResources(R.string.TermsAndConditions));

        initializeViews();
    }

    private void initializeViews() {
        termsAndConditionsWebView = getWebView(R.id.termsAndConditionsWebView);
        termsAndConditionsWebView.loadUrl("file:///android_asset/terms/terms.html");
        termsAndConditionsWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        termsAndConditionsWebView.setLongClickable(false);

        termsAndConditionsWebView.setHapticFeedbackEnabled(false);
    }
}
