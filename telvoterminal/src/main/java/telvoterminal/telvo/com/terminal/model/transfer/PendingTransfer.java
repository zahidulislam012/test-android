package telvoterminal.telvo.com.terminal.model.transfer;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.model.history.Receiver;
import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 21-Sep-17.
 */

public class PendingTransfer  extends DTOBase{
    private String _id;
    private String amount;
    private Receiver receiver;
    private String delayTime;
    private String createdAt;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
