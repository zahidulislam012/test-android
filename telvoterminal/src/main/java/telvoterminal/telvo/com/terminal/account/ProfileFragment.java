package telvoterminal.telvo.com.terminal.account;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.circularprogressbar.CircularProgressBar;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightTextView;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {

    private CircularProgressBar submittedInfoProgressBar;
    private CircularProgressBar acceptedInfoProgressBar;
    private CircularImageView userImageView;
    private RobotoLightTextView profileCompleteTextView;
    private RobotoRegularTextView setCurrencyTextView;
    private RobotoRegularTextView personalInformationTextView;
    private RobotoRegularTextView addressInformationTextView;
    private RobotoRegularTextView introducerInformationTextView;

    private User user = null;
    private int complete;
    private int inserted = 0;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        complete = 0;
        inserted=0;
        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            if (user != null) {
                if (!user.getCurrency().equals("")) {
                    setCurrencyTextView.setEnabled(false);
                    setCurrencyTextView.setTextColor(Color.parseColor("#939597"));
                }

                if (!user.getCurrency().equals("")) {
                    complete = complete + 25;
                    inserted = inserted + 25;
                }
                if (user.getKyc().getPersonalInfo().getVerified()) {
                    complete = complete + 25;
                } else if (user.getKyc().getPersonalInfo().getDeclined()) {
                    personalInformationTextView.setTextColor(Color.parseColor("#D63535"));
                }

                if (user.getKyc().getPersonalInfo().getInserted()) {
                    inserted = inserted + 25;
                }

                if (user.getKyc().getAddressInfo().getPermanentAddress().getVerified() && user.getKyc().getAddressInfo().getPresentAddress().getVerified()) {
                    complete = complete + 25;
                } else if (user.getKyc().getAddressInfo().getPermanentAddress().getDeclined() && user.getKyc().getAddressInfo().getPresentAddress().getDeclined()) {
                    addressInformationTextView.setTextColor(Color.parseColor("#D63535"));
                }

                if (user.getKyc().getAddressInfo().getPresentAddress().getInserted()) {
                    inserted = inserted + 12;
                }
                if (user.getKyc().getAddressInfo().getPermanentAddress().getInserted()) {
                    inserted = inserted + 13;
                }


                if (user.getKyc().getIntroducerInfo().getVerified()) {
                    complete = complete + 25;
                } else if (user.getKyc().getIntroducerInfo().getDeclined()) {
                    introducerInformationTextView.setTextColor(Color.parseColor("#D63535"));
                }

                if (user.getKyc().getIntroducerInfo().getInserted()) {
                    inserted = inserted + 25;
                }

                submittedInfoProgressBar.setProgressWithAnimation(inserted, 3000);
                acceptedInfoProgressBar.setProgressWithAnimation(complete, 3000);

                profileCompleteTextView.setText(complete + "% " + getString(R.string.complete));

                Picasso.with(context).load(user.getKyc().getPersonalInfo().getImage().equals("") ? "NO images" : BaseUrl.IMAGE_BASE + user.getKyc().getPersonalInfo().getImage()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(userImageView);

            }

        }

    }

    private void initializeViews(View view) {
        userImageView = view.findViewById(R.id.userImageView);
        submittedInfoProgressBar = view.findViewById(R.id.submittedInfoProgressBar);
        acceptedInfoProgressBar = view.findViewById(R.id.acceptedInfoProgressBar);
        profileCompleteTextView = view.findViewById(R.id.profileCompleteTextView);
        setCurrencyTextView = view.findViewById(R.id.setCurrencyTextView);
        setCurrencyTextView.setOnClickListener(this);
        personalInformationTextView = view.findViewById(R.id.personalInformationTextView);
        personalInformationTextView.setOnClickListener(this);
        addressInformationTextView = view.findViewById(R.id.addressInformationTextView);
        addressInformationTextView.setOnClickListener(this);
        introducerInformationTextView = view.findViewById(R.id.introducerInformationTextView);
        introducerInformationTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setCurrencyTextView:
                intent = new Intent(context, CurrencyActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.personalInformationTextView:
                intent = new Intent(context, PersonalInformationActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("PERSONAL_INFORMATION", user.getKyc().getPersonalInfo());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (user.getKyc().getPersonalInfo().getDeclined()) {
                    customAlertDialog.showDialogWithYes("Declined Reason: \n" + user.getKyc().getPersonalInfo().getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            startActivity(intent);
                        }
                    });
                } else {
                    startActivity(intent);
                }
                break;
            case R.id.addressInformationTextView:
                intent = new Intent(context, AddressInformationActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("ADDRESS_INFORMATION", user.getKyc().getAddressInfo());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (user.getKyc().getAddressInfo().getPermanentAddress().getDeclined() && user.getKyc().getAddressInfo().getPresentAddress().getDeclined()) {
                    customAlertDialog.showDialogWithYes("Declined Reason: \n" + user.getKyc().getAddressInfo().getPermanentAddress().getMessage() + user.getKyc().getAddressInfo().getPresentAddress().getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            startActivity(intent);
                        }
                    });
                } else {
                    startActivity(intent);
                }

                break;
            case R.id.introducerInformationTextView:
                intent = new Intent(context, IntroducerActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("INTRODUCER", user.getKyc().getIntroducerInfo());
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (user.getKyc().getIntroducerInfo().getDeclined()) {
                    customAlertDialog.showDialogWithYes("Declined Reason: \n" + user.getKyc().getIntroducerInfo().getMessage(), new ButtonClick() {
                        @Override
                        public void Do() {
                            startActivity(intent);
                        }
                    });
                } else {
                    startActivity(intent);
                }
                break;
            default:
        }
    }
}
