package telvoterminal.telvo.com.terminal.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.List;

import telvoterminal.telvo.com.terminal.BuildConfig;
import telvoterminal.telvo.com.terminal.Exception.TerminalError;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.alertdialog.CustomAlertDialog;
import telvoterminal.telvo.com.terminal.model.kyc.PersonalInfo;
import telvoterminal.telvo.com.terminal.preference.ApplicationPreferences;


public class BaseService {
    private Context context;
    private IServiceResultListener serviceResultListener;
    private ProgressDialog progressDialog;
    private String response;
    private CustomAlertDialog alertDialog;
    protected ApplicationPreferences preferences;

    public BaseService(Context context) {
        this(context, ((context instanceof IServiceResultListener) ? (IServiceResultListener) context : null));

    }

    public BaseService(Context context, IServiceResultListener serviceResultListener) {
        this.context = context;
        this.serviceResultListener = serviceResultListener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        alertDialog= new CustomAlertDialog(context);
        preferences=new ApplicationPreferences(context);
    }

    protected String getDtoBase(DTOBase dtoBase){
        return new Gson().toJson(dtoBase);
    }

    protected void setPostRequest(final String url, final DTOBase requestBase, final DTOBase dtoBase, final String method, final boolean progress, final String requestMethod, final Boolean tryAgain) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(progressDialog!=null && progress){
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    if(method.equals("PERSONAL_INFORMATION")){
                        try {
                            PersonalInfo personalInfo= (PersonalInfo) requestBase;
                            String request = getDtoBase(personalInfo);
                            Log.i("Request Body",request);
                            response = new HttpMultipartRequest().multipartRequest(url,personalInfo,requestBase.getToken(),new String[]{personalInfo.getImage(),personalInfo.getSignImage()},new String[]{"userImage","signImage"},new String[]{"image/png","image/png"});
                            Log.i("Response Body",response);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else{
                        String request = getDtoBase(requestBase);
                        Log.i("Request Body",request);
                        response = new HttpRequest().getRequest(url,request,requestMethod,requestBase.getToken());
                        Log.i("Response Body",response);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
                return "Done";
            }

            @Override
            protected void onPostExecute(final String result) {
                super.onPostExecute(result);
                if (progressDialog.isShowing() && progress)
                    progressDialog.dismiss();

                if(result.equals("Done")){
                    try {
                        if(dtoBase!=null){
                            Gson gson = new Gson();
                            DTOBase dtoBaseResult = gson.fromJson(new String(response), dtoBase.getClass());
                            if(serviceResultListener!=null){
                                serviceResultListener.OnServiceResult(method, dtoBaseResult, true);
                            }

                        }
                    }catch (Exception ex) {
                        ex.printStackTrace();
                        TerminalError error = new TerminalError();
                        error.setMessage(ex.getMessage());
                        if(serviceResultListener!=null){
                            serviceResultListener.OnServiceResult(method, error, false);
                        }

                    }
                }else {
                    if(tryAgain){
                        alertDialog.showDialogWithYesNo("Connection unsuccessful", "Confirm that the network signal (3G, 4G or Wi-Fi ) is strong. Try to access from an area where there is a strong signal.", new ButtonClick() {
                            @Override
                            public void Do() {
                                setPostRequest(url,requestBase,dtoBase,method,progress,requestMethod,tryAgain);
                            }
                        }, new ButtonClick() {
                            @Override
                            public void Do() {
                                if(serviceResultListener!=null){
                                    DTOBase base = new DTOBase();
                                    base.setMessage(result);
                                    serviceResultListener.OnServiceResult(method, base, false);
                                }

                            }
                        });

                    }else  if(BuildConfig.DEBUG){
                        alertDialog.showDialog(result);
                    }

                }
            }
        }.execute();
    }


}
