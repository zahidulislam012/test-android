package telvoterminal.telvo.com.terminal.pushservice;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * Created by Invariant on 10/25/17.
 */

public class RegistrationIntentService extends IntentService {
    private static final String TAG = "Intent Service";
    private String token;
    private ApplicationPreferences preferences;
    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            preferences=new ApplicationPreferences(this);
            token = FirebaseInstanceId.getInstance().getToken();
            Log.i("Intent Service",token);
            if(!preferences.getValue(Constant.PUSHTOKEN).equals("")){
                if(!preferences.getValue(Constant.PUSHTOKEN).equals(token)){
                    preferences.setValue(Constant.PUSHTOKEN_OLD,preferences.getValue(Constant.PUSHTOKEN));
                    preferences.setValue(Constant.PUSHTOKEN,token);
                }
            }else{
                preferences.setValue(Constant.PUSHTOKEN,token);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
