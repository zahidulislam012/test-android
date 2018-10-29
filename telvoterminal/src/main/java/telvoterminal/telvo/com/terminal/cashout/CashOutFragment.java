package telvoterminal.telvo.com.terminal.cashout;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.cashin.CreditCardFragment;
import telvoterminal.telvo.com.terminal.cashin.RechargeCardFragment;
import telvoterminal.telvo.com.terminal.fragmentadapter.FragmentCategoryAdapter;
import telvoterminal.telvo.com.terminal.fragmentadapter.FragmentHolder;
import telvoterminal.telvo.com.terminal.utility.Constant;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashOutFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;

    private FragmentCategoryAdapter fragmentCategoryAdapter;
    private List<FragmentHolder> fragmentHolderList;

    public CashOutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cash_out, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        // Find the view pager that will allow the user to swipe between fragments
        viewPager = view.findViewById(R.id.viewpager);

        fragmentHolderList = new ArrayList<>();
        fragmentHolderList.add(new FragmentHolder(new AtmWithdrawFragment(),getActivity().getString(R.string.atm)));
        fragmentHolderList.add(new FragmentHolder(new AgentWithdrawFragment(),getActivity().getString(R.string.agent)));
        fragmentHolderList.add(new FragmentHolder(new HomeRequestFragment(),getActivity().getString(R.string.home)));

        fragmentCategoryAdapter = new FragmentCategoryAdapter(getChildFragmentManager(),fragmentHolderList);

        // Set the adapter onto the view pager
        viewPager.setAdapter(fragmentCategoryAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float offset, int offsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                        INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                }
            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
