package telvoterminal.telvo.com.terminal.activitylog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.model.ActivityLog;
import telvoterminal.telvo.com.terminal.utility.AppManager;

/**
 * Created by invar on 21-Sep-17.
 */

public class ActivityLogAdapter extends RecyclerView.Adapter<ActivityLogAdapter.ActivityLogViewHolder> {

    private List<ActivityLog> activityLogList;
    private Context context;

    public class ActivityLogViewHolder extends RecyclerView.ViewHolder {
        public RobotoRegularTextView timeTextView, activityTextView;

        public ActivityLogViewHolder(View view) {
            super(view);

            timeTextView = view.findViewById(R.id.timeTextView);
            activityTextView = view.findViewById(R.id.activityTextView);
        }
    }

    public ActivityLogAdapter(Context context,List<ActivityLog> activityLogList) {
        this.activityLogList = activityLogList;
        this.context=context;
    }

    @Override
    public ActivityLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_log_row, parent, false);

        return new ActivityLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivityLogViewHolder holder, int position) {
        ActivityLog ActivityLog = activityLogList.get(position);

        holder.timeTextView.setText(ActivityLog.getMessage());
        holder.activityTextView.setText(AppManager.getLastLogin(ActivityLog.getDate()).replace("a.m.", "AM").replace("p.m.","PM"));
    }

    @Override
    public int getItemCount() {
        return activityLogList.size();
    }
}
