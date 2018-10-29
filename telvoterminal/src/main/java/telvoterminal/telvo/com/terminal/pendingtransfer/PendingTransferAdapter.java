package telvoterminal.telvo.com.terminal.pendingtransfer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.customviews.RobotoRegularTextView;
import telvoterminal.telvo.com.terminal.model.transfer.PendingTransfer;
import telvoterminal.telvo.com.terminal.utility.AppManager;

/**
 * Created by invar on 21-Sep-17.
 */

public class PendingTransferAdapter extends RecyclerView.Adapter<PendingTransferAdapter.TransferViewHolder> {

    private List<PendingTransfer> pendingTransferList;
    private Context context;
    private String currency;

    public class TransferViewHolder extends RecyclerView.ViewHolder {
        public RobotoRegularTextView accountNumberTextView, transferTypeTextView, timeTextView, amountTextView, waitingTimeTextView;

        public TransferViewHolder(View view) {
            super(view);

            accountNumberTextView = view.findViewById(R.id.accountNumberTextView);
            transferTypeTextView = view.findViewById(R.id.transferTypeTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            amountTextView = view.findViewById(R.id.amountTextView);
            waitingTimeTextView = view.findViewById(R.id.waitingTimeTextView);
        }
    }

    public PendingTransferAdapter(Context context,List<PendingTransfer> pendingTransferList,String currency) {
        this.context=context;
        this.pendingTransferList = pendingTransferList;
        this.currency=currency;
    }

    @Override
    public TransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_transfer_row, parent, false);

        return new TransferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransferViewHolder holder, int position) {
        PendingTransfer pendingTransfer = pendingTransferList.get(position);

        holder.accountNumberTextView.setText(pendingTransfer.getReceiver().getMobileNumber());
        holder.transferTypeTextView.setText(pendingTransfer.getMessage());
        holder.timeTextView.setText(AppManager.getLastLogin(pendingTransfer.getCreatedAt()).replace("a.m.", "am").replace("p.m.","pm"));
        holder.amountTextView.setText(currency+" "+pendingTransfer.getAmount());
        holder.waitingTimeTextView.setText(pendingTransfer.getDelayTime()+" min");
    }

    public void setnoRemove(){
       // pendingTransferList.add(adapterPosition,pendingTransfer);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return pendingTransferList.size();
    }
}
