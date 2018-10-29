package telvoterminal.telvo.com.terminal.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;
import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 9/17/17.
 */

public class AppManager {

    public static boolean hasInternetConnection(Context context) {

        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            return cm.getActiveNetworkInfo().isConnected();

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
            return false;
        }
    }

    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public static DTOBase getClassObject(String response, DTOBase dtoBase){
        Gson gson = new Gson();
        DTOBase result= gson.fromJson(response,dtoBase.getClass());
        return result;
    }

    public static String getClassString(DTOBase dtoBase){
        Gson gson = new Gson();
        return  gson.toJson(dtoBase);
    }

    public static String getDeviceId(Context context){
        return  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getCurrencySymbol(String currency){
        if(currency.equals("BDT")){
            return "৳";
        }else if(currency.equals("USD")){
            return "$";
        }else if(currency.equals("EUR")){
            return "€";
        }else if(currency.equals("GBP")){
            return "£";
        }else if(currency.equals("AUD")){
            return "$";
        }else if(currency.equals("JPY")){
            return "￥";
        }else if(currency.equals("CAD")){
            return "$";
        }else if(currency.equals("SEK")){
            return "kr";
        }else if(currency.equals("SGD")){
            return "$";
        }else if(currency.equals("CNY")){
            return "CN¥";
        }else if(currency.equals("INR")){
            return "₹";
        }else {
            return "";
        }
    }

    public static String getLastLogin(String inputDate){
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm aa");
        try {
            return output.format(input.parse(inputDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate;
        }
    }

    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,context.getResources().getDisplayMetrics());

    }

    public static void setAlert(final Context context, final ApplicationPreferences preferences, final ButtonClick buttonClick){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        CharSequence items[] = new CharSequence[] {"English", "Bengali"};
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                if(position==0){
                    preferences.setValue("LNG","en");
                   setLocale(context,"en");
                }else if(position==1){
                    preferences.setValue("LNG","bn");
                   setLocale(context,"bn");
                }
                buttonClick.Do();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.cancel),null);

        builder.setTitle(context.getResources().getString(R.string.change_your_language));
        builder.show();
    }

    public static boolean AccountVerify(Context context){
        return new ApplicationPreferences(context).getValue("AllVerified", false);
    }

}
