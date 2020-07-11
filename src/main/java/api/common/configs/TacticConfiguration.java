//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;
import java.lang.reflect.*;
import codex.common.utils.*;

import com.fasterxml.jackson.annotation.*;

import org.knowm.xchange.currency.CurrencyPair;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class TacticConfiguration extends AbstractConfiguration {
    @JsonFormat(shape=JsonFormat.Shape.STRING)
	private List<CurrencyPair> pairs;
	private List<String> exchanges;
	private String type;
    
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal targetProfit;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal minimumProfit;


    private boolean trailing;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal trailingDeviation;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal useCounterCurrencyPercent;

    private int numberOfBrokers;

    private int maxNumberOfOpenDealsPerPair;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private List<Map<String, String>> signals;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<String, String> flags;


    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<org.knowm.xchange.currency.Currency, List<BigDecimal>> amounts;

    @JsonIgnore
    public static final BigDecimal DEFAULT_MINIMUM_PROFIT = BigDecimal.valueOf(0.42);

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public TacticConfiguration(){
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void initiate(Debug _debug, String _name){
		super.initiate(_debug, _name);
		if(minimumProfit == null){
			_debug.outln(Debug.WARNING, "["+this+"]: minimumProfit is not set. Setting to default=" + DEFAULT_MINIMUM_PROFIT);
			minimumProfit = DEFAULT_MINIMUM_PROFIT;
		}
	}

//-------------------------------------------------------------------------------------
    public List<CurrencyPair> getPairs(){
    	return pairs;
    }

//-------------------------------------------------------------------------------------
    public List<String> getExchanges(){
    	return exchanges;
    }

//-------------------------------------------------------------------------------------
    public String getType(){
    	return type;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getTargetProfit(){
        return targetProfit;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getMinimumProfit(){
        return minimumProfit;
    }

//-------------------------------------------------------------------------------------
    public boolean trailingEnabled(){
        return trailing;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getTrailingDeviation(){
        return trailingDeviation;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getUseCounterCurrencyPercent(){
        return useCounterCurrencyPercent;
    }
    
//-------------------------------------------------------------------------------------
    public int getNumberOfBrokers(){
    	return numberOfBrokers;
    }

//-------------------------------------------------------------------------------------
    public int getMaxNumberOfOpenDealsPerPair(){
    	return maxNumberOfOpenDealsPerPair;
    }
    
//-------------------------------------------------------------------------------------
    public List<Map<String, String>> getSignals(){
    	return signals;
    }
    
//-------------------------------------------------------------------------------------
    public boolean isFlagEnabled(String _flagName){
    	String _flagValue = flags.get(_flagName);
    	if(_flagValue == null){
    		return false;
    	} else {
    		return _flagValue.equals("true");
    	}
    }

//-------------------------------------------------------------------------------------
    public Map<org.knowm.xchange.currency.Currency, List<BigDecimal>> getAmounts(){
    	return amounts;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
