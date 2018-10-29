package telvoterminal.telvo.com.terminal.generalscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.model.Success;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class SuccessActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;

    private RobotoRegularTextView titleTextView1;
    private RobotoRegularTextView titleTextView2;
    private RobotoRegularTextView titleTextView3;
    private RobotoRegularTextView titleTextView4;
    private RobotoRegularTextView valueTextView1;
    private RobotoRegularTextView valueTextView2;
    private RobotoRegularTextView valueTextView3;
    private RobotoRegularTextView valueTextView4;

    private RobotoRegularButton doneButton;

    private String activityName;
    private Success done = null;

    private List<String> title;
    private List<String> value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews();

        bundle = getIntent().getExtras();
        if(bundle!=null){
            activityName = bundle.getString(Constant.ACTIVITY_NAME);
            done = (Success) bundle.getSerializable(Constant.success);
        }
        setToolbar(R.id.toolbar_success, activityName);

        if(!preferences.getValue("USER").equals("")){
            user= (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }


        if(done!=null){
            title = done.getTitle();
            value = done.getValue();

            int i = 1;
            for (String aValue:value){
                if(i==1){
                    linearLayout1.setVisibility(View.VISIBLE);
                    titleTextView1.setText(title.get(i-1));
                    if(title.get(i-1).equals("Amount")){
                        valueTextView1.setText(new DecimalFormat("#.##").format(Double.parseDouble(aValue)));
                    }else{
                        valueTextView1.setText(aValue);
                    }

                }else if(i==2){
                    linearLayout2.setVisibility(View.VISIBLE);
                    titleTextView2.setText(title.get(i-1));
                    valueTextView2.setText(aValue);
                }else if(i==3){
                    linearLayout3.setVisibility(View.VISIBLE);
                    titleTextView3.setText(title.get(i-1));
                    if(title.get(i-1).equals("Balance")){
                        valueTextView3.setText(AppManager.getCurrencySymbol(user.getCurrency())+" "+new DecimalFormat("#.##").format(Double.parseDouble(aValue)));
                    }else{
                        valueTextView3.setText(aValue);
                    }

                }else if(i==4){
                    linearLayout4.setVisibility(View.VISIBLE);
                    titleTextView4.setText(title.get(i-1));
                    valueTextView4.setText(aValue);
                }
                i++;
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeViews() {
        linearLayout1 = getLinearLayout(R.id.linearLayout1);
        linearLayout2 = getLinearLayout(R.id.linearLayout2);
        linearLayout3 = getLinearLayout(R.id.linearLayout3);
        linearLayout4 = getLinearLayout(R.id.linearLayout4);

        titleTextView1 = getRobotoRegularTextView(R.id.titleTextView1);
        titleTextView2 = getRobotoRegularTextView(R.id.titleTextView2);
        titleTextView3 = getRobotoRegularTextView(R.id.titleTextView3);
        titleTextView4 = getRobotoRegularTextView(R.id.titleTextView4);

        valueTextView1 = getRobotoRegularTextView(R.id.valueTextView1);
        valueTextView2 = getRobotoRegularTextView(R.id.valueTextView2);
        valueTextView3 = getRobotoRegularTextView(R.id.valueTextView3);
        valueTextView4 = getRobotoRegularTextView(R.id.valueTextView4);

        doneButton = getRobotoRegularButton(R.id.doneButton);
        doneButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}
