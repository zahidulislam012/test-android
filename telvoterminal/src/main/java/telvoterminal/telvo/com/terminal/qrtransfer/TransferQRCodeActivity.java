package telvoterminal.telvo.com.terminal.qrtransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.generalscreen.SuccessActivity;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.qrtransfer.QRReceive;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class TransferQRCodeActivity extends BaseActivity {


    private ImageView qrCodeImageView;
    private Bitmap myBitmap;
    private String result;

    private BroadcastReceiver qrCodeBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_qrcode);

        setToolbar(R.id.toolbar_transfer_qr, getString(R.string.transfer_qr_code));
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            result = bundle.getString("DATA");
            myBitmap = QRCode.from(result).bitmap();
            qrCodeImageView.setImageBitmap(myBitmap);
        }

        qrCodeBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.QRCODE_PUSH)) {
                    String message = intent.getStringExtra("message");
                    if (!TextUtils.isEmpty(message)) {
                        QRReceive qrReceive = (QRReceive) AppManager.getClassObject(message, new QRReceive());


                        intent = new Intent(context, SuccessActivity.class);
                        bundle = new Bundle();
                        List<String> title = new ArrayList<>();
                        List<String> value = new ArrayList<>();


                        title.add("Amount");
                        value.add(qrReceive.getAmount());

                        title.add("Transfer");
                        value.add("QR Code Transfer");


                        Success done = new Success();
                        done.setTitle(title);
                        done.setValue(value);

                        if (!preferences.getValue("USER").equals("")) {
                            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
                            user.setBalance(qrReceive.getBalance());
                            preferences.setValue("USER", AppManager.getClassString(user));
                        }

                        bundle.putSerializable(Constant.success, done);
                        bundle.putString(Constant.ACTIVITY_NAME, getStringResources(R.string.payment_done));
                        intent.putExtras(bundle);
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(qrCodeBroadcastReceiver, new IntentFilter(Constant.QRCODE_PUSH));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(qrCodeBroadcastReceiver);
    }

    private void initializeViews() {
        qrCodeImageView = getImageView(R.id.qrCodeImageView);
    }

    private void shareImage(Bitmap bitmap) throws IOException {
        File cachePath = new File(context.getCacheDir(), "images");
        if (!cachePath.exists()) {
            cachePath.mkdirs();
        }

        FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();

        File newFile = new File(cachePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(context, "telvoterminal.telvo.com.terminal.fileprovider", newFile);

        if (contentUri != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);

            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.putExtra(Intent.EXTRA_TEXT,"http://telvoterminal.com/transferqrcode?code=12345");
            shareIntent.setType("image/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
          //  startActivity(Intent.createChooser(shareIntent,"Share images..."));


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qr_transfer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.share_image:
                try {
                    shareImage(myBitmap);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
