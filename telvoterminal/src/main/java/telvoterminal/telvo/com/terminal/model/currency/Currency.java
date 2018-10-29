package telvoterminal.telvo.com.terminal.model.currency;


import java.util.List;

import telvoterminal.telvo.com.terminal.service.DTOBase;

/**
 * Created by Dell on 09-Mar-17.
 */

public class Currency extends DTOBase {
    List<CurrencyData> CcyNtry;

    public List<CurrencyData> getCcyNtry() {
        return CcyNtry;
    }

    public void setCcyNtry(List<CurrencyData> ccyNtry) {
        CcyNtry = ccyNtry;
    }
}
