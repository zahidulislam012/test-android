package telvoterminal.telvo.com.terminal.fragmentadapter;

import android.support.v4.app.Fragment;

/**
 * Created by Invariant on 10/23/17.
 */

public class FragmentHolder {
    private Fragment fragment;
    private String title;

    public FragmentHolder(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
