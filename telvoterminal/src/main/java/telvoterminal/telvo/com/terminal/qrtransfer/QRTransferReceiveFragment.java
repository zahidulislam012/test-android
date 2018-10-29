package telvoterminal.telvo.com.terminal.qrtransfer;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.mainscreen.MainActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.qrtransfer.QRReceive;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class QRTransferReceiveFragment extends BaseFragment implements IServiceResultListener, ZXingScannerView.ResultHandler {

    private static final String TAG = "QRTransferReceiveFrgmnt";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1101;

    private ZXingScannerView mScannerView;
    private View rootView;

    public QRTransferReceiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_qrtransfer_receive, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();
        service = new TerminalService(context, this);

        return rootView;
    }

    private void initializeViews() {
        mScannerView = rootView.findViewById(R.id.zxingScannerView);
        mScannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        mScannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume");

        onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        Log.e(TAG, "setUserVisibleHint");

        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onVisible();
        }
    }

    public void onVisible()
    {
        Log.e(TAG, "onVisible");

        if (!getUserVisibleHint())
        {
            return;
        }

        initializeViews();
        checkForCameraPermission();
    }

    private void checkForCameraPermission() {
        Log.e(TAG, "checkForCameraPermission");

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);

            // MY_PERMISSIONS_REQUEST_CAMERA is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        } else {
            //You already have permission
            Log.e(TAG, "already have permission");

            mScannerView.startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "permission was granted");

                    mScannerView.startCamera();
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

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("QR_RECEIVER")){
                QRReceive qrReceive = (QRReceive) dtoBase;
                if(qrReceive.getStatus().equals(Constant.success)){
                    intent = new Intent(context, SuccessActivity.class);
                    bundle = new Bundle();
                    List<String> title = new ArrayList<>();
                    List<String> value = new ArrayList<>();

                    title.add("From");
                    value.add(qrReceive.getSender());

                    title.add("Receive Amount");
                    value.add(AppManager.getCurrencySymbol(qrReceive.getCurrency())+" "+qrReceive.getAmount());


                    Success done = new Success();
                    done.setTitle(title);
                    done.setValue(value);

                    if(!preferences.getValue("USER").equals("")) {
                        user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                        user.setBalance(qrReceive.getBalance());
                        preferences.setValue("USER", AppManager.getClassString(user));
                    }

                    bundle.putSerializable(Constant.success,done);
                    bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.payment_done));
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
                }else if(qrReceive.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialogWithYes(qrReceive.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            onVisible();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void handleResult(Result result) {
        if (AppManager.hasInternetConnection(context)) {
            if(AppManager.AccountVerify(context)){
                service.setQRReceive(result.getText());
            }else{
                customAlertDialog.showDialogWithYes(getStringResources(R.string.account_verify), new ButtonClick() {
                    @Override
                    public void Do() {
                        MainActivity.callAccount();
                    }
                });
            }

        } else {
            customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
        }
    }
}
