package telvoterminal.telvo.com.terminal.cashin;


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
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.fragmentadapter.FragmentCategoryAdapter;
import telvoterminal.telvo.com.terminal.fragmentadapter.FragmentHolder;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.utility.Constant;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashInFragment extends Fragment {

    private View rootView;
    private ViewPager viewPager;
    private FragmentCategoryAdapter fragmentCategoryAdapter;
    private List<FragmentHolder> fragmentHolderList;

    public CashInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cash_in, container, false);
        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);
        return rootView;
    }

    private void initializeViews(View view) {
        viewPager = view.findViewById(R.id.viewpager);
        fragmentHolderList = new ArrayList<>();
        fragmentHolderList.add(new FragmentHolder(new RechargeCardFragment(),getActivity().getString(R.string.recharge_card)));
        fragmentHolderList.add(new FragmentHolder(new CreditCardFragment(),getActivity().getString(R.string.credit_card)));

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
                imm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
            }
        });

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
