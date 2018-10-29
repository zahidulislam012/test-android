package telvoterminal.telvo.com.terminal.mainscreen;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.testfairy.TestFairy;

import java.text.DecimalFormat;
import java.util.Currency;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.RobotoBoldTextView;
import telvoterminal.telvo.com.terminal.customviews.RobotoLightTextView;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.UserLogin;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener , IServiceResultListener {

    private CircularImageView userPhotoImageView;
    private RobotoBoldTextView userNameTextView;
    private RobotoBoldTextView balanceAmountTextView;
    private LinearLayout cashInLinearLayout;
    private LinearLayout cashOutLinearLayout;
    private LinearLayout transferLinearLayout;
    private LinearLayout pendingLinearLayout;
    private RobotoLightTextView lastLoginTextView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private User user = null;
    private BroadcastReceiver balanceBroadcastReceiver;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
            if (user != null) {
                TestFairy.setUserId(user.getMobileNumber());
                userNameTextView.setText(user.getName());
                balanceAmountTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(user.getBalance()));
                Picasso.with(context).load(user.getKyc().getPersonalInfo().getImage().equals("") ? "NO images" : BaseUrl.IMAGE_BASE + user.getKyc().getPersonalInfo().getImage()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(userPhotoImageView);
                lastLoginTextView.setText(getString(R.string.last_login) + " " + AppManager.getLastLogin(user.getLastLogin()).replace("a.m.", "AM").replace("p.m.", "PM"));
            }
        }

        balanceBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constant.BALANCE_UPDATE_PUSH)) {
                    balanceAmountTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(intent.getDoubleExtra("Balance", 0.00)));
                }
            }
        };

        return rootView;
    }

    private void initializeViews(View view) {
        userPhotoImageView = view.findViewById(R.id.userPhotoImageView);
        userNameTextView = view.findViewById(R.id.userNameTextView);
        balanceAmountTextView = view.findViewById(R.id.balanceAmountTextView);
        cashInLinearLayout = view.findViewById(R.id.cashInLinearLayout);
        cashInLinearLayout.setOnClickListener(this);
        cashOutLinearLayout = view.findViewById(R.id.cashOutLinearLayout);
        cashOutLinearLayout.setOnClickListener(this);
        transferLinearLayout = view.findViewById(R.id.transferLinearLayout);
        transferLinearLayout.setOnClickListener(this);
        pendingLinearLayout = view.findViewById(R.id.pendingLinearLayout);
        pendingLinearLayout.setOnClickListener(this);
        lastLoginTextView = view.findViewById(R.id.lastLoginTextView);
        swipeRefreshLayout = view.findViewById(R.id.fragment_home);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(context, "Refresh refresh refresh", Toast.LENGTH_SHORT).show();
                //swipeRefreshLayout.setRefreshing(false);
                if (AppManager.hasInternetConnection(context)) {
                    service = new TerminalService(context,HomeFragment.this);
                    service.getBalance(preferences.getValue("USER_ID"));
                } else {
                    customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(context).registerReceiver(balanceBroadcastReceiver, new IntentFilter(Constant.BALANCE_UPDATE_PUSH));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(balanceBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        switch (view.getId()) {
            case R.id.cashInLinearLayout:
                ((MainActivity) getActivity()).setNavigationItem(R.id.nav_cash_in);
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            case R.id.cashOutLinearLayout:
                ((MainActivity) getActivity()).setNavigationItem(R.id.nav_cash_out);
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
            case R.id.transferLinearLayout:
                ((MainActivity) getActivity()).setNavigationItem(R.id.nav_transfer);
                navigationView.getMenu().getItem(4).setChecked(true);
                break;
            case R.id.pendingLinearLayout:
                ((MainActivity) getActivity()).setNavigationItem(R.id.nav_pending_transfer);
                navigationView.getMenu().getItem(5).setChecked(true);
                break;
            default:
                return;
        }
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        swipeRefreshLayout.setRefreshing(false);
        if(success){
            if(method.equals("USER_BALANCE")){
                User user = (User) dtoBase;
                if (user.getStatus().equals(Constant.success)) {
                    balanceAmountTextView.setText(AppManager.getCurrencySymbol(user.getCurrency()) + " " + new DecimalFormat("#.##").format(user.getBalance()));
                }
            }
        }
    }
}
