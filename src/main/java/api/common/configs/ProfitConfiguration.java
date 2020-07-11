//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import codex.common.utils.*;

import org.knowm.xchange.currency.CurrencyPair;
import com.fasterxml.jackson.annotation.*;

import org.jline.utils.*;

//import codex.xbit.api.server.trader.common.*;


//-------------------------------------------------------------------------------------
public class ProfitConfiguration extends AbstractConfiguration {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal targetProfit;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal minimumProfit;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal currentProfit;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal finalProfit;

    @JsonIgnore
    private BigDecimal openingPrice;

//-------------------------------------------------------------------------------------
    public ProfitConfiguration(Debug _debug, BigDecimal _targetProfit, BigDecimal _minimumProfit, BigDecimal _openingPrice) {
        debug = _debug;
        targetProfit = _targetProfit;
        minimumProfit = _minimumProfit;
        openingPrice = _openingPrice;
        currentProfit = BigDecimal.ZERO;
        finalProfit = BigDecimal.ZERO;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void initiate(Debug _debug) {
        super.initiate(_debug, "");
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public BigDecimal getTargetProfit() {
        return targetProfit;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setTargetProfit(BigDecimal _targetProfit) {
        targetProfit = _targetProfit;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public BigDecimal getMinimumProfit() {
        return minimumProfit;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setMinimumProfit(BigDecimal _minimumProfit) {
        minimumProfit = _minimumProfit;
    }


//-------------------------------------------------------------------------------------
    @JsonIgnore
    public float getOpeningPrice() {
        return openingPrice.floatValue();
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setOpeningPrice(float _openingPrice) {
        openingPrice = new BigDecimal(_openingPrice);
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void setOpeningPrice(BigDecimal _openingPrice) {
        openingPrice = _openingPrice;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    @JsonIgnore
    public void computeFinalProfit(BigDecimal _closingPrice) {
//        float _profit = computeProfit(_closingPrice.floatValue());
//        finalProfit = BigDecimal.valueOf(_profit);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    @JsonIgnore
    public String toString(){
        String _info;
//        _info = "Id: "+id+",State: "+state;
        return _info;
    }*/

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------