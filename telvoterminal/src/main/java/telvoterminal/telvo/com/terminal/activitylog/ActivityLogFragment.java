package telvoterminal.telvo.com.terminal.activitylog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.model.ActivityLog;
import telvoterminal.telvo.com.terminal.model.ActivityLogResult;
import telvoterminal.telvo.com.terminal.model.history.UserFinalHistory;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityLogFragment extends BaseFragment implements IServiceResultListener{

    private RecyclerView activityLogRecyclerView;
    private ActivityLogAdapter adapter;
    private List<ActivityLog> userLog;

    public ActivityLogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity_log, container, false);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);


        if (AppManager.hasInternetConnection(context)) {
            service = new TerminalService(context,this);
            service.getActivityLogs();
        } else {
            customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
        }

        return rootView;
    }


    private void initializeViews(View view) {
        activityLogRecyclerView = view.findViewById(R.id.activityLogRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        activityLogRecyclerView.setLayoutManager(mLayoutManager);
        activityLogRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("ACTIVITY_LOGS")){
                ActivityLogResult activityLogResult = (ActivityLogResult) dtoBase;
                if(activityLogResult.getStatus().equals(Constant.success)){
                    userLog=activityLogResult.getUserLog();
                    Collections.sort(userLog, new Comparator<ActivityLog>() {
                        public int compare(ActivityLog activityLog1, ActivityLog activityLog2) {
                            return activityLog2.getDate().compareTo(activityLog1.getDate());
                        }
                    });
                    adapter = new ActivityLogAdapter(context,userLog);
                    activityLogRecyclerView.setAdapter(adapter);
                }else if(activityLogResult.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(activityLogResult.getMessage());
                }

            }
        }
    }
}
