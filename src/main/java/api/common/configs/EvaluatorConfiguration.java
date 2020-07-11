//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

import com.fasterxml.jackson.annotation.*;

import org.knowm.xchange.currency.CurrencyPair;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public class EvaluatorConfiguration extends AbstractConfiguration {
    private String mode;
	private List<String> types;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private BigDecimal totalProfit;
    private List<EvaluatorConfiguration.EntryEvaluatorConfiguration> evaluators;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public EvaluatorConfiguration(){
    }

//-------------------------------------------------------------------------------------
    public String getMode(){
    	return mode;
    }

//-------------------------------------------------------------------------------------
    public List<String> getTypes(){
    	return types;
    }

//-------------------------------------------------------------------------------------
    public List<EvaluatorConfiguration.EntryEvaluatorConfiguration> getEvaluators(){
    	return evaluators;
    }
    
//-------------------------------------------------------------------------------------
    public BigDecimal getTotalProfit(){
        return totalProfit;
    }
    
//-------------------------------------------------------------------------------------
    public void setTotalProfit(BigDecimal _totalProfit){
        totalProfit = _totalProfit;
    }
    

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public static class EntryEvaluatorConfiguration extends AbstractConfiguration {
	    private int id;
	    @JsonFormat(shape=JsonFormat.Shape.STRING)
	    private BigDecimal finalProfit;
//-------------------------------------------------------------------------------------
	    public int getId(){
	    	return id;
	    }
//-------------------------------------------------------------------------------------
	    public BigDecimal getFinalProfit(){
	    	return finalProfit;
	    }
//-------------------------------------------------------------------------------------
    }
    
//-------------------------------------------------------------------------------------
    public static class OrderbookEvaluatorConfiguration extends EntryEvaluatorConfiguration {
	    private int id;
	    private String tactic;
	    private String exchange;
	    private CurrencyPair pair;
	    @JsonFormat(shape=JsonFormat.Shape.STRING)
	    private BigDecimal finalProfit;
//-------------------------------------------------------------------------------------
	    public OrderbookEvaluatorConfiguration(int _id, String _tactic, String _exchange, CurrencyPair _pair){
	    	id = _id;
	    	tactic = _tactic;
	    	exchange = _exchange;
	    	pair = _pair;
	    }
//-------------------------------------------------------------------------------------
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public static class TacticEvaluatorConfiguration extends EntryEvaluatorConfiguration {
	    private String tactic;
	    private int numberOfOrderbooks;
	    private List<CurrencyPair> pairs;
	    @JsonFormat(shape=JsonFormat.Shape.STRING)
	    private BigDecimal finalProfit;
//-------------------------------------------------------------------------------------
	    public TacticEvaluatorConfiguration(String _tactic, int _numberOfOrderbooks, List<CurrencyPair> _pairs){
	    	tactic = _tactic;
	    	numberOfOrderbooks = _numberOfOrderbooks;
	    	pairs = _pairs;
	    }
//-------------------------------------------------------------------------------------
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public static class PairEvaluatorConfiguration extends EntryEvaluatorConfiguration {
	    private CurrencyPair pair;
	    @JsonFormat(shape=JsonFormat.Shape.STRING)
	    private BigDecimal finalProfit;
//-------------------------------------------------------------------------------------
	    public PairEvaluatorConfiguration(CurrencyPair _pair){
	    	pair = _pair;
	    }
//-------------------------------------------------------------------------------------
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
