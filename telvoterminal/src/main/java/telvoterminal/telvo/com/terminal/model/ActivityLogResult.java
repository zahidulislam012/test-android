package telvoterminal.telvo.com.terminal.model;

import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 21-Sep-17.
 */

public class ActivityLogResult extends DTOBase {
    private String  userId;
    private List<ActivityLog> userLog;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<ActivityLog> getUserLog() {
        return userLog;
    }

    public void setUserLog(List<ActivityLog> userLog) {
        this.userLog = userLog;
    }
}
