package telvoterminal.telvo.com.terminal.model.history;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 21-Sep-17.
 */

public class History  extends DTOBase{
    private List<Deposits> deposits;
    private List<Transfers> transfers;
    private List<Withdraws> withdraws;
    private List<Topups> topups;
    private List<Payments> payments;

    public List<Deposits> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<Deposits> deposits) {
        this.deposits = deposits;
    }

    public List<Transfers> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<Transfers> transfers) {
        this.transfers = transfers;
    }

    public List<Withdraws> getWithdraws() {
        return withdraws;
    }

    public void setWithdraws(List<Withdraws> withdraws) {
        this.withdraws = withdraws;
    }

    public List<Topups> getTopups() {
        return topups;
    }

    public void setTopups(List<Topups> topups) {
        this.topups = topups;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void setPayments(List<Payments> payments) {
        this.payments = payments;
    }
}
