package telvoterminal.telvo.com.terminal.Exception;


import telvoterminal.telvo.com.terminal.service.DTOBase;

public class TerminalError extends DTOBase {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
