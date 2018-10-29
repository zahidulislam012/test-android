package telvoterminal.telvo.com.terminal.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.testfairy.TestFairy;

import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.introscreen.IntroScreenActivity;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * Created by invar on 05-Oct-17.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());
        Intent mainIntent = getIntent();
        if(Intent.ACTION_VIEW.equals(mainIntent.getAction())){
            Uri uri = mainIntent.getData();
            Log.i("deep_link",uri.getQueryParameter("code"));
           // String post_id = uri.getQueryParameter("post_id");
        }

        TestFairy.begin(this, "386ffcd0bc36b0a6b5962264204b2342f4274ce2");
        if(!preferences.getValue("LNG").equals("")){
            AppManager.setLocale(context,preferences.getValue("LNG"));
        }

        if(preferences.getIntroPageVisitStatus()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, IntroScreenActivity.class);
            startActivity(intent);
            finish();
        }




    }
}
