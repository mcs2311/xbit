//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;

import com.fasterxml.jackson.annotation.*;

import codex.common.utils.*;

import org.knowm.xchange.currency.CurrencyPair;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class StrategyConfiguration extends AbstractConfiguration {
//    private String repository;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private List<List<String>> indicators;

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

    public StrategyConfiguration(){
    	indicators = new ArrayList<List<String>>();
    }

//-------------------------------------------------------------------------------------
	public void initiate(Debug _debug){
    	debug = _debug;
	}

/*
//-------------------------------------------------------------------------------------
	public String getRepository(){
		return repository;
	}*/

//-------------------------------------------------------------------------------------
	public List<List<String>> getIndicators(){
		return indicators;
	}

//-------------------------------------------------------------------------------------
	public void setIndicators(List<List<String>> _indicators){
		indicators = _indicators;
	}

//-------------------------------------------------------------------------------------
	public void addIndicator(List<String> _indicator){
		indicators.add( _indicator);
	}

//-------------------------------------------------------------------------------------
	public String toString(){
		String _s = "";
		if(indicators == null){
			return "[indicators = null!!!]";
		}
		for (int i = 0;  i < indicators.size(); i++) {
			_s += "i"+i+"="+indicators.get(i).toString() + ",";
		}
		return _s;
	}


//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
