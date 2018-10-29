package telvoterminal.telvo.com.terminal.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class NotificationsActivity extends BaseActivity implements View.OnClickListener {

    private CheckBox soundCheckBox;
    private CheckBox vibrationCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_notifications, getStringResources(R.string.notifications));

        initializeViews();
    }

    private void initializeViews() {
        soundCheckBox = getCheckBox(R.id.soundCheckBox);
        soundCheckBox.setOnClickListener(this);
        soundCheckBox.setChecked(preferences.getValue("SOUND", true));
        vibrationCheckBox = getCheckBox(R.id.vibrationCheckBox);
        vibrationCheckBox.setOnClickListener(this);
        vibrationCheckBox.setChecked(preferences.getValue("VIBRATION", true));
    }

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        switch(view.getId()) {
            case R.id.soundCheckBox:
                if (checked) {
                    preferences.setValue("SOUND", true);
                } else {
                    preferences.setValue("SOUND", false);
                }
                break;
            case R.id.vibrationCheckBox:
                if (checked) {
                    preferences.setValue("VIBRATION", true);
                } else {
                    preferences.setValue("VIBRATION", false);
                }
                break;
            default:
        }
    }
}
