package telvoterminal.telvo.com.terminal.history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.model.history.History;
import telvoterminal.telvo.com.terminal.model.history.UserFinalHistory;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;

/**
 * Created by invar on 21-Sep-17.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<UserFinalHistory> historyList;
    private Context context;

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public CircularImageView ImageView1;
        public ImageView ImageView2;
        public RobotoRegularTextView accountNumberTextView, eventTextView, dateTextView, amountTextView;

        public HistoryViewHolder(View view) {
            super(view);

            ImageView1 = view.findViewById(R.id.ImageView1);
            ImageView2 = view.findViewById(R.id.ImageView2);
            accountNumberTextView = view.findViewById(R.id.accountNumberTextView);
            eventTextView = view.findViewById(R.id.eventTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
            amountTextView = view.findViewById(R.id.amountTextView);
        }
    }

    public HistoryAdapter(Context context,List<UserFinalHistory> historyList) {
        this.context=context;
        this.historyList = historyList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);

        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        UserFinalHistory history = historyList.get(position);

        holder.accountNumberTextView.setText(isTitleNUll(history.getTitle()));
        holder.eventTextView.setText(isSubTitleNUll(history.getSubtile()));
        holder.dateTextView.setText(AppManager.getLastLogin(history.getDateTime()));
        holder.amountTextView.setText(AppManager.getCurrencySymbol(history.getCurrency())+" "+history.getAmount());
        if(history.getImages()!=null){
            holder.ImageView1.setVisibility(View.VISIBLE);
            holder.ImageView2.setVisibility(View.GONE);
          //  Picasso.with(context).load(BaseUrl.IMAGE_BASE+history.getImages()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(holder.ImageView1);
            Picasso.with(context).load(history.getImages().equals("") ? "NO images" : BaseUrl.IMAGE_BASE+history.getImages()).placeholder(R.drawable.ic_home_user_photo).error(R.drawable.ic_home_user_photo).into(holder.ImageView1);
        }else if(history.getResource()!=null){
            holder.ImageView1.setVisibility(View.GONE);
            holder.ImageView2.setVisibility(View.VISIBLE);
            holder.ImageView2.setImageResource(history.getResource());
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    private String isTitleNUll(String title){
        if(title==null){
            return "Eempty";
        }else{
            return title;
        }
    }

    private String isSubTitleNUll(String Subtitle){
        if(Subtitle==null){
            return "Eempty";
        }else{
            return Subtitle;
        }
    }
}
