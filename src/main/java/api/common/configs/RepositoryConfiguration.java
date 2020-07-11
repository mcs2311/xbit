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
public class RepositoryConfiguration extends AbstractConfiguration {

	private List<String> exchanges;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
	private List<CurrencyPair> pairs;
//    private String mode;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private HashMap<String, List<String>> types;
//    private List<String> barDurations;

    @JsonIgnore
	private String exchange;
    @JsonIgnore
	private CurrencyPair pair;
    @JsonIgnore
    private String type;
    @JsonIgnore
    private String barDuration;
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public RepositoryConfiguration(){
    }

//-------------------------------------------------------------------------------------
    public RepositoryConfiguration(RepositoryConfiguration _repositoryConfiguration, String _exchange, CurrencyPair _pair, String _type, String _barDuration){
    	enabled = _repositoryConfiguration.enabled;
    	id = _repositoryConfiguration.id;
    	name = _repositoryConfiguration.name;
    	exchanges = _repositoryConfiguration.exchanges;
    	exchange = _exchange;
    	pairs = _repositoryConfiguration.pairs;
    	pair = _pair;
    	types = _repositoryConfiguration.types;
    	type = _type;
//    	barDurations = _repositoryConfiguration.barDurations;
    	barDuration = _barDuration;
    }

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public CurrencyPair getCurrencyPair(){
		return pair;
	}

//-------------------------------------------------------------------------------------
	public String getExchange(){
		return exchange;
	}

//-------------------------------------------------------------------------------------
/*	public String getMode(){
		return mode;
	}
*/
//-------------------------------------------------------------------------------------
	public HashMap<String, List<String>> getTypes(){
		return types;
	}

//-------------------------------------------------------------------------------------
	public String getType(){
		return type;
	}

//-------------------------------------------------------------------------------------
/*	public List<String> getBarDurations(){
		return barDurations;
	}*/

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public String getBarDuration(){
		return barDuration;
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public int getBarDurationAsInt(){
		return TimeUtils.getSeconds(barDuration);
/*		if(barDuration.trim().equals("instant")){
			return 0;
		}
		int _len = barDuration.length();
		String _barDuration = barDuration.substring(0, _len - 1);
		String _multiplierString = barDuration.substring(_len - 1, _len);
		int _multiplier = 0;
		int _barDurationInt = 0;
		switch (_multiplierString) {
			case "s": _multiplier = 1; break;
			case "m": _multiplier = 60; break;
			case "h": _multiplier = 3600; break;
			case "d": _multiplier = 24*3600; break;
			case "w": _multiplier = 7*24*3600; break;
			case "l": _multiplier = 30*24*3600; break;//month
			default : _multiplier = 0; break;
		}
		try{
			_barDurationInt = Integer.parseInt(_barDuration) * _multiplier;
		}catch(Exception _e){}
		return _barDurationInt;
*/		
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public void setBarDuration(String _barDuration){
		barDuration = _barDuration;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    @JsonIgnore
	public List<RepositoryConfiguration> getInputConfigurations(){
		List<RepositoryConfiguration> _list = new ArrayList<RepositoryConfiguration>();
		for (int e = 0 ; e < exchanges.size(); e++) {
			String _exchange = exchanges.get(e);
//			System.out.println("_exchange="+_exchange);
			for (int c = 0 ; c < pairs.size(); c++) {
				CurrencyPair _pair = pairs.get(c);
				for (Map.Entry<String, List<String>> _entry : types.entrySet()) {
					String _type = _entry.getKey();
					RepositoryConfiguration _repositoryConfiguration = new RepositoryConfiguration(this, _exchange, _pair, _type, null);
					_list.add(_repositoryConfiguration);
				}
			}
		}
		return _list;
	}

//-------------------------------------------------------------------------------------
    @JsonIgnore
	public List<RepositoryConfiguration> getAllConfigurations(){
		List<RepositoryConfiguration> _list = new ArrayList<RepositoryConfiguration>();
		for (int e = 0 ; e < exchanges.size(); e++) {
			String _exchange = exchanges.get(e);
//			System.out.println("_exchange="+_exchange);
			for (int c = 0 ; c < pairs.size(); c++) {
				CurrencyPair _pair = pairs.get(c);
//				for (int i = 0 ; i < types.size(); i++) {
				for (Map.Entry<String, List<String>> _entry : types.entrySet()) {
					String _type = _entry.getKey();
					List<String> _barDurations = _entry.getValue();
					for (int j = 0 ; j < _barDurations.size(); j++) {
						String _barDuration = _barDurations.get(j);
						RepositoryConfiguration _repositoryConfiguration = new RepositoryConfiguration(this, _exchange, _pair, _type, _barDuration);
						_list.add(_repositoryConfiguration);
					}
				}
			}
		}
		return _list;
	}

//-------------------------------------------------------------------------------------
	public String toString(){
		return exchange  + "-" + pair + "-" + type + "-" + barDuration;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------