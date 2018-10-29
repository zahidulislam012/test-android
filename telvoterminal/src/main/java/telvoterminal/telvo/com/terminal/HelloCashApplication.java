package telvoterminal.telvo.com.terminal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class HelloCashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(new ApplicationLifecycleTracker(this));
    }
}


class ApplicationLifecycleTracker implements Application.ActivityLifecycleCallbacks {

    private int numStarted = 0;
    private Context context;

    ApplicationLifecycleTracker(Context context) {
        this.context = context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            new ApplicationPreferences(context).setValue(Constant.APPLICATION_FOREGROUND, true);
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            new ApplicationPreferences(context).setValue(Constant.APPLICATION_FOREGROUND, false);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
