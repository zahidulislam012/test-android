package telvoterminal.telvo.com.terminal.model;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/20/17.
 */

public class Encrypt extends DTOBase {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
