package telvoterminal.telvo.com.terminal.model.kyc;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by invar on 28-Sep-17.
 */

public class CurrencyInfo extends DTOBase{
    private String userId;
    private String currency;
    private Boolean inserted;

    public Boolean getInserted() {
        return inserted;
    }

    public void setInserted(Boolean inserted) {
        this.inserted = inserted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
