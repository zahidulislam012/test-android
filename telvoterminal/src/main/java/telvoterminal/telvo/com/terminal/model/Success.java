package telvoterminal.telvo.com.terminal.model;

import java.util.HashMap;
import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 10/24/17.
 */

public class Success extends DTOBase {
    private List<String> title;
    private List<String> value;

    public List<String> getTitle() {
        return title;
    }

    public void setTitle(List<String> title) {
        this.title = title;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
