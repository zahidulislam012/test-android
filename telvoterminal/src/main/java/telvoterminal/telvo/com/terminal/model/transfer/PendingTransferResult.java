package telvoterminal.telvo.com.terminal.model.transfer;

import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/31/17.
 */

public class PendingTransferResult extends DTOBase {
    private String userId;
    private String pendingId;
    private List<PendingTransfer> tempTransfers;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPendingId() {
        return pendingId;
    }

    public void setPendingId(String pendingId) {
        this.pendingId = pendingId;
    }

    public List<PendingTransfer> getTempTransfers() {
        return tempTransfers;
    }

    public void setTempTransfers(List<PendingTransfer> tempTransfers) {
        this.tempTransfers = tempTransfers;
    }
}
