package telvoterminal.telvo.com.terminal.service;

import java.io.Serializable;
import java.util.ArrayList;


public class DTOBase implements Serializable {
    protected String status;
    protected String message;
    protected String token;
    protected ArrayList<String> adminPoints;

    public ArrayList<String> getAdminPoints() {
        return adminPoints;
    }

    public void setAdminPoints(ArrayList<String> adminPoints) {
        this.adminPoints = adminPoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
