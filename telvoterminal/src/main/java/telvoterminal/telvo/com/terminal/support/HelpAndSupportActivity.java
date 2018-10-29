package telvoterminal.telvo.com.terminal.support;

import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.alertdialog.CustomAlertDialog;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularButton;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularEditText;
import telvoterminal.telvo.com.terminal.model.Support;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;
import telvoterminal.telvo.com.terminal.utility.Validation;

public class HelpAndSupportActivity extends BaseActivity implements View.OnClickListener,IServiceResultListener {

    private RobotoRegularEditText subjectEditText;
    private RobotoRegularEditText detailsEditText;
    private RobotoRegularButton submitButton;

    private String subject;
    private String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        setToolbar(R.id.toolbar_help_and_support, getStringResources(R.string.help_and_support));

        initializeViews();
    }

    private void initializeViews() {
        subjectEditText = getRobotoRegularEditText(R.id.subjectEditText);
        detailsEditText = getRobotoRegularEditText(R.id.detailsEditText);
        submitButton = getRobotoRegularButton(R.id.submitButton);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitButton:
                boolean done = true;
                subject = subjectEditText.getText().toString().trim();
                details = detailsEditText.getText().toString().trim();

                if (Validation.isTextEmpty(subject)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.subject_empty));
                } else if (Validation.isTextEmpty(details)) {
                    done = false;
                    customAlertDialog.showDialog(getStringResources(R.string.details_empty));
                }

                if (done) {
                    if (AppManager.hasInternetConnection(context)) {
                        service.setHelpAndSupport(subject, details);
                    } else {
                        customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                    }
                }
                break;
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("SUPPORT")){
                Support support= (Support) dtoBase;
                if(support.getStatus().equals(Constant.success)){
                    customAlertDialog.showDialogWithYes(support.getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            finish();
                        }
                    });
                }else if(support.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(support.getMessage());
                }
            }
        }
    }
}
