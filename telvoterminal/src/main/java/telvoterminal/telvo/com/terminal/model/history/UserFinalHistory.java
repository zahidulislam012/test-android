package telvoterminal.telvo.com.terminal.model.history;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Invariant on 11/2/17.
 */

public class UserFinalHistory extends DTOBase {
    private String title;
    private String subtile;
    private String dateTime;
    private String images;
    private Integer resource;
    private String currency;
    private String amount;

    public UserFinalHistory(String title, String subtile, String dateTime, String images, Integer resource, String currency, String amount) {
        this.title = title;
        this.subtile = subtile;
        this.dateTime = dateTime;
        this.images = images;
        this.resource = resource;
        this.currency = currency;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtile() {
        return subtile;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getImages() {
        return images;
    }

    public Integer getResource() {
        return resource;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }
}
