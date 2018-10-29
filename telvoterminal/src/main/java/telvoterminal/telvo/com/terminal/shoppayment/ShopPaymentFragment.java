package telvoterminal.telvo.com.terminal.shoppayment;


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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopPaymentFragment extends BaseFragment implements ZXingScannerView.ResultHandler {

    private static final String TAG = "ShopPaymentFragment";

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1101;

    private ZXingScannerView mScannerView;
    private View rootView;

    public ShopPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_shop_payment, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        return rootView;
    }

    private void initializeViews() {
        mScannerView = rootView.findViewById(R.id.zxingScannerView);
        mScannerView.setResultHandler(this);
    }

    @Override
    public void onPause() {
        mScannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void handleResult(Result result) {
        intent = new Intent(context, ConfirmPaymentActivity.class);
        intent.putExtra(Constant.BARCODE_VALUE, result.getText());
        getActivity().startActivityForResult(intent,Constant.RESULT_CODE);
    }
}
