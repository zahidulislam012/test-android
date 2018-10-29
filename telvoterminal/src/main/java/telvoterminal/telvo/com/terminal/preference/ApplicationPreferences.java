package telvoterminal.telvo.com.terminal.preference;

import android.content.Context;
import android.content.SharedPreferences;

import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * Created by Invariant on 9/17/17.
 */

public class ApplicationPreferences {
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public ApplicationPreferences(Context context) {
        this.appSharedPrefs = context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    public void setValue(String keyName, String value) {
        prefsEditor.putString(keyName, value);
        prefsEditor.commit();
    }

    public String getValue(String keyName) {
        return appSharedPrefs.getString(keyName, "");
    }

    public void setValue(String keyName, Boolean value) {
        prefsEditor.putBoolean(keyName, value);
        prefsEditor.commit();
    }

    public Boolean getValue(String keyName, Boolean defaulValue) {
        return appSharedPrefs.getBoolean(keyName, defaulValue);
    }

    public void setIntroPageVisitStatus(boolean isIntoPageVisit) {
        prefsEditor.putBoolean(Constant.VISIT_INTRO_PAGE, isIntoPageVisit);
        prefsEditor.commit();
    }

    public Boolean getIntroPageVisitStatus() {
        return appSharedPrefs.getBoolean(Constant.VISIT_INTRO_PAGE, false);
    }


    public void Clear() {
        prefsEditor.clear();
    }

}
