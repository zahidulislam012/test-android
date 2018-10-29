package telvoterminal.telvo.com.terminal.baseactivity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.HashMap;

import telvoterminal.telvo.com.terminal.alertdialog.CustomAlertDialog;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;

/**
 * Created by Dell on 02-Feb-17.
 */

public class BaseFragment extends Fragment {
    protected Context context;
    protected Intent intent;
    protected Bundle bundle=null;
    protected ApplicationPreferences preferences;
    protected TerminalService service;
    protected CustomAlertDialog customAlertDialog;
    protected User user=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        preferences=new ApplicationPreferences(context);
       ///service = new TerminalService(context,this);
        customAlertDialog = new CustomAlertDialog(context);

    }

    protected void ToastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    protected String getStringResources(int id)
    {
        return getResources().getString(id);
    }

}
