package telvoterminal.telvo.com.terminal.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import telvoterminal.telvo.com.terminal.R;
import telvoterminal.telvo.com.terminal.baseactivity.BaseFragment;
import telvoterminal.telvo.com.terminal.model.User;
import telvoterminal.telvo.com.terminal.model.history.Deposits;
import telvoterminal.telvo.com.terminal.model.history.History;
import telvoterminal.telvo.com.terminal.model.history.Payments;
import telvoterminal.telvo.com.terminal.model.history.Topups;
import telvoterminal.telvo.com.terminal.model.history.Transfers;
import telvoterminal.telvo.com.terminal.model.history.UserFinalHistory;
import telvoterminal.telvo.com.terminal.model.history.UserHistory;
import telvoterminal.telvo.com.terminal.model.history.Withdraws;
import telvoterminal.telvo.com.terminal.service.DTOBase;
import telvoterminal.telvo.com.terminal.service.IServiceResultListener;
import telvoterminal.telvo.com.terminal.service.TerminalService;
import telvoterminal.telvo.com.terminal.utility.AppManager;
import telvoterminal.telvo.com.terminal.utility.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends BaseFragment implements IServiceResultListener{

    private RecyclerView histroyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<UserFinalHistory> finalHistories;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        String test=null;
        test.toString();

        initializeViews(rootView);

        if (AppManager.hasInternetConnection(context)) {
            service = new TerminalService(context,this);
            service.getUserHistory();
        } else {
            customAlertDialog.showDialog(getStringResources(R.string.no_internet_connection));
        }
        if(!preferences.getValue("USER").equals("")) {
            user = (User) AppManager.getClassObject(preferences.getValue("USER"), new User());
        }


        return rootView;
    }


    private void initializeViews(View view) {
        histroyRecyclerView = view.findViewById(R.id.historyRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        histroyRecyclerView.setLayoutManager(mLayoutManager);
        histroyRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void OnServiceResult(String method, DTOBase dtoBase, boolean success) {
        if(success){
            if(method.equals("USER_History")){
                UserHistory userHistory= (UserHistory) dtoBase;
                if(userHistory.getStatus().equals(Constant.success)){
                   History history= userHistory.getHistory();
                     List<Deposits> deposits = history.getDeposits();
                     List<Transfers> transfers = history.getTransfers();
                     List<Withdraws> withdraws = history.getWithdraws();
                     List<Topups> topups = history.getTopups();
                     List<Payments> payments = history.getPayments();
                    finalHistories = new ArrayList<>();
                    for (Deposits deposit:deposits){
                        if(deposit.getServiceType()==0){
                            finalHistories.add(new UserFinalHistory(deposit.getCardType(),deposit.getCardNumber(),deposit.getCreatedAt(),null,R.drawable.ic_history_recharge_card,deposit.getCurrency().getFrom(),deposit.getAmount()));
                        }else if(deposit.getServiceType()==1){
                            finalHistories.add(new UserFinalHistory(deposit.getCardType(),deposit.getCardNumber(),deposit.getCreatedAt(),null,R.drawable.ic_history_master_card,deposit.getCurrency().getFrom(),deposit.getAmount()));
                        }else if(deposit.getServiceType()==2){
                            finalHistories.add(new UserFinalHistory(deposit.getCardType(),deposit.getCardNumber(),deposit.getCreatedAt(),null,R.drawable.ic_home_user_photo,deposit.getCurrency().getFrom(),deposit.getAmount()));
                        }
                    }

                    for (Transfers transfer:transfers){
                            if(user.getMobileNumber().equals(transfer.getReceiver().getMobileNumber())){
                                /// Receive money
                                finalHistories.add(new UserFinalHistory("Receive Money from "+transfer.getUser().getName(),transfer.getUser().getMobileNumber(),transfer.getCreatedAt(),transfer.getUser().getImage(),null,transfer.getCurrency().getFrom(),transfer.getAmount()));
                            }else{
                                /// send money
                                finalHistories.add(new UserFinalHistory("Send Money to "+transfer.getReceiver().getName(),transfer.getReceiver().getMobileNumber(),transfer.getCreatedAt(),transfer.getReceiver().getImage(),null,transfer.getCurrency().getFrom(),transfer.getAmount()));
                            }
                    }

                    for (Withdraws withdraw:withdraws){
                        if(withdraw.getServiceType()==0){
                            finalHistories.add(new UserFinalHistory(withdraw.getDetail(),withdraw.getName(),withdraw.getCreatedAt(),null,R.drawable.ic_history_home,withdraw.getCurrency().getFrom(),withdraw.getAmount()));
                        }else if(withdraw.getServiceType()==1){
                            finalHistories.add(new UserFinalHistory(withdraw.getDetail(),"From "+withdraw.getName(),withdraw.getCreatedAt(),null,R.drawable.ic_history_atm_withdraw,withdraw.getCurrency().getFrom(),withdraw.getAmount()));
                        }else if(withdraw.getServiceType()==2){
                            finalHistories.add(new UserFinalHistory(withdraw.getDetail(),withdraw.getName(),withdraw.getCreatedAt(),null,R.drawable.ic_home_user_photo,withdraw.getCurrency().getFrom(),withdraw.getAmount()));
                        }
                    }

                    for (Topups anTopups:topups){
                        finalHistories.add(new UserFinalHistory("Mobile Recharge to "+anTopups.getReceiver(),anTopups.getUser().getName()+"( "+anTopups.getUser().getMobileNumber()+" )",anTopups.getCreatedAt(),null,R.drawable.ic_history_recharge_card,"BDT",anTopups.getAmount()));
                    }

                    for (Payments payment:payments){
                        finalHistories.add(new UserFinalHistory(payment.getShop().getName(),payment.getShop().getMobileNumber(),payment.getCreatedAt(),null,R.drawable.ic_history_shop_pyament,payment.getCurrency().getFrom(),payment.getAmount()));
                    }

                    Collections.sort(finalHistories, new Comparator<UserFinalHistory>() {
                        public int compare(UserFinalHistory history1, UserFinalHistory history2) {
                            return history2.getDateTime().compareTo(history1.getDateTime());
                        }
                    });
                    historyAdapter = new HistoryAdapter(context,finalHistories);
                    histroyRecyclerView.setAdapter(historyAdapter);

                }else if(userHistory.getStatus().equals(Constant.error)){
                    customAlertDialog.showDialog(userHistory.getMessage());
                }

            }
        }
    }
}
