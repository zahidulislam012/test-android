package telvoterminal.telvo.com.terminal.pendingtransfer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.alertdialog.ButtonClick;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.customviews.DividerItemDecoration;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.transfer.PendingTransfer;
import telvoterminal.telvo.com.terminal.model.transfer.PendingTransferResult;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.BaseUrl;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingTransferFragment extends BaseFragment implements IServiceResultListener{

    private RecyclerView pendingTransferRecyclerView;
    private PendingTransferAdapter pendingTransferAdapter;
    private List<PendingTransfer> tempTransfers;

    public PendingTransferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_transfer, container, false);

        Crashlytics.setString(Constant.CRASH_OCCURRED_PAGE_NAME, getClass().getSimpleName());

        initializeViews(rootView);
        service = new TerminalService(context,this);

        if(!preferences.getValue("USER").equals("")){
            user= (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }

        if (AppManager.hasInternetConnection(context)) {
            service.setPendingTransfer();
        } else {
            customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
        }

        return rootView;
    }


    private void initializeViews(View view) {
        pendingTransferRecyclerView = view.findViewById(R.id.pendingTransferRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        pendingTransferRecyclerView.setLayoutManager(mLayoutManager);
        pendingTransferRecyclerView.setItemAnimator(new DefaultItemAnimator());
        pendingTransferRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("PENDING_TRANSFER")){
                PendingTransferResult transferResult = (PendingTransferResult) dtoBase;
                if(transferResult.getStatus().equals(Constant.success)){
                    tempTransfers = transferResult.getTempTransfers();
                    pendingTransferAdapter = new PendingTransferAdapter(context,tempTransfers,AppManager.getCurrencySymbol(user.getCurrency()));
                    pendingTransferRecyclerView.setAdapter(pendingTransferAdapter);
                    new ItemTouchHelper(simpleCallback).attachToRecyclerView(pendingTransferRecyclerView);
                }else if(transferResult.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(transferResult.getMessage());
                }
            }else if(method.equals("PENDING_TRANSFER_DELETE")){
                PendingTransferResult transferResult = (PendingTransferResult) dtoBase;
                if(transferResult.getStatus().equals(Constant.success)){
                    tempTransfers = transferResult.getTempTransfers();
                    pendingTransferAdapter = new PendingTransferAdapter(context,tempTransfers,AppManager.getCurrencySymbol(user.getCurrency()));
                    pendingTransferRecyclerView.setAdapter(pendingTransferAdapter);
                    new ItemTouchHelper(simpleCallback).attachToRecyclerView(pendingTransferRecyclerView);
                }else if(transferResult.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(transferResult.getMessage());
                }
            }
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            Toast.makeText(context, "on Move", Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            final int itemPosition = viewHolder.getAdapterPosition();
            final PendingTransfer pendingTransfer = tempTransfers.get(itemPosition);
            customAlertDialog.showDialogWithYesNo("Warring!", "Do you want to cancel this Transfer. Mobile Number: "+pendingTransfer.getReceiver().getMobileNumber(), new ButtonClick() {
                @Override
                public void Do() {
                    service.setDeletePendingTransfer(pendingTransfer.get_id());
                }
            }, new ButtonClick() {
                @Override
                public void Do() {
                    pendingTransferAdapter.setnoRemove();
                }
            });
            Toast.makeText(context, "onSwiped", Toast.LENGTH_SHORT).show();
        }
    };
}
