package telvoterminal.telvo.com.terminal.mainscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.about.AboutFragment;
import telvoterminal.telvo.com.terminal.account.ProfileFragment;
import telvoterminal.telvo.com.terminal.activitylog.ActivityLogFragment;
import telvoterminal.telvo.com.terminal.auth.LoginActivity;
import telvoterminal.telvo.com.terminal.baseactivity.BaseActivity;
import telvoterminal.telvo.com.terminal.cashin.CashInFragment;
import telvoterminal.telvo.com.terminal.cashout.CashOutFragment;
import telvoterminal.telvo.com.terminal.history.HistoryFragment;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.pendingtransfer.PendingTransferFragment;
import telvoterminal.telvo.com.terminal.qrtransfer.QRTransferFragment;
import telvoterminal.telvo.com.terminal.settings.SettingsFragment;
import telvoterminal.telvo.com.terminal.shoppayment.ShopPaymentFragment;
import telvoterminal.telvo.com.terminal.support.SupportFragment;
import telvoterminal.telvo.com.terminal.topup.TopUpFragment;
import telvoterminal.telvo.com.terminal.transfer.TransferFragment;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private static AppCompatActivity activity;
    private int Menuid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.activity = this;
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0f);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Get the header view
        View headerView = navigationView.getHeaderView(0);

        // Make the header view below status bar
        LinearLayout linearLayoutStatusBar = headerView.findViewById(R.id.linear_layout_status_bar);
        linearLayoutStatusBar.getLayoutParams().height = getStatusBarHeight();

        // Initialize the navigation header items
        LinearLayout shareLinearLayout = headerView.findViewById(R.id.linear_layout_share);
        shareLinearLayout.setOnClickListener(this);
        LinearLayout rateLinearLayout = headerView.findViewById(R.id.linear_layout_rating);
        rateLinearLayout.setOnClickListener(this);
        LinearLayout aboutLinearLayout = headerView.findViewById(R.id.linear_layout_about);
        aboutLinearLayout.setOnClickListener(this);

        if (!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());

            //set crashlytics logged in user
            if(user.getMobileNumber() !=null)
                Crashlytics.setUserIdentifier(user.getMobileNumber());


            if (user.getKyc().getAllVerified()) {
                setNavigationItem(R.id.nav_home);
                navigationView.getMenu().getItem(0).setChecked(true);
            } else {
                setNavigationItem(R.id.nav_account);
                navigationView.getMenu().getItem(1).setChecked(true);
            }
        }


        // service.setData("Android App");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                setNavigationItem(R.id.nav_home);
                navigationView.getMenu().getItem(0).setChecked(true);
            }
        }
    }

    public void setNavigationItem(int id) {
        Menuid = id;
        Fragment fragment;
        Class fragmentClass = null;

        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.telvo_terminal));
        } else if (id == R.id.nav_account) {
            fragmentClass = ProfileFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.account));
        } else if (id == R.id.nav_cash_in) {
            fragmentClass = CashInFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.cash_in));
        } else if (id == R.id.nav_cash_out) {
            fragmentClass = CashOutFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.cash_out));
        } else if (id == R.id.nav_transfer) {
            fragmentClass = TransferFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.transfer));
        } else if (id == R.id.nav_qr_transfer) {
            fragmentClass = QRTransferFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.qr_transfer));
        } else if (id == R.id.nav_pending_transfer) {
            fragmentClass = PendingTransferFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.pending_transfer));
        } else if (id == R.id.nav_top_up) {
            fragmentClass = TopUpFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.top_up));
        } else if (id == R.id.nav_history) {
            fragmentClass = HistoryFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.history));
        } else if (id == R.id.nav_activity_log) {
            fragmentClass = ActivityLogFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.activity_log));
        } else if (id == R.id.nav_shop_payment) {
            fragmentClass = ShopPaymentFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.shop_payment));
        } else if (id == R.id.nav_support) {
            fragmentClass = SupportFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.support));
        } else if (id == R.id.nav_settings) {
            fragmentClass = SettingsFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.settings));
        } else if (id == R.id.nav_logout) {
            logOut();
        } else if (id == R.id.linear_layout_about) {
            fragmentClass = AboutFragment.class;
            getSupportActionBar().setTitle(getStringResources(R.string.about));
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_screen, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callAccount() {
        Fragment fragment;
        Class fragmentClass = ProfileFragment.class;
        activity.getSupportActionBar().setTitle(activity.getResources().getString(R.string.account));
        try {
            fragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_screen, fragment).commit();
            navigationView.getMenu().getItem(1).setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logOut() {
        preferences.Clear();
        preferences.setValue(Constant.USER_LOGGED_IN, false);
        intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (Menuid == R.id.nav_home) {
                showExitDialog();
            } else {
                setNavigationItem(R.id.nav_home);
                navigationView.getMenu().getItem(0).setChecked(true);
            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        if(!user.getKyc().getAllVerified()){
//            setNavigationItem(R.id.nav_account);
//            navigationView.getMenu().getItem(1).setChecked(true);
//        }else{
//            setNavigationItem(id);
//        }
        setNavigationItem(id);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_layout_share:
                inviteFriends();
                break;
            case R.id.linear_layout_rating:
                showDialogToOpenPlayStore();
                break;
            case R.id.linear_layout_about:
                setNavigationItem(R.id.linear_layout_about);
                break;
            default:
                return;
        }
    }

    private void showDialogToOpenPlayStore() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Open Play Store to rate our app")
                .setTitle("Rate us");

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                openPlayStore();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void inviteFriends() {
        final String appPackageName = getPackageName();
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject test");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + appPackageName);
        startActivity(Intent.createChooser(i, "Share via"));
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        //} else {
        builder = new AlertDialog.Builder(context);
        //}
        builder.setTitle(R.string.exit)
                .setMessage(R.string.exit_message)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        preferences.setValue(Constant.USER_LOGGED_IN, false);
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
