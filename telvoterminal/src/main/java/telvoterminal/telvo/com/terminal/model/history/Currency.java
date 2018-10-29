package telvoterminal.telvo.com.terminal.model.history;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/2/17.
 */

public class Currency extends DTOBase {
    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
