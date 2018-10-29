package telvoterminal.telvo.com.terminal.model;

import java.util.ArrayList;
import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 21-Sep-17.
 */

public class ActivityLog extends DTOBase {
    private String date;



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
