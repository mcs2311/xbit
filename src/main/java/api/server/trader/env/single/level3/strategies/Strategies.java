//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level3.strategies;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


//import codex.xbit.api.server.trader.common.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level3.strategies.helpers.*;

//-------------------------------------------------------------------------------------
public class Strategies extends AbstractCluster<StrategyConfiguration, StrategyX> {
	private List<StrategyConfiguration> templateStrategies;

//-------------------------------------------------------------------------------------
    public Strategies(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//    	String _path = resolver.getServerConfiguration().getPathToStrategies();
//        configurationLoader = new ConfigurationLoader<StrategyConfiguration>(_debug, _path, ".yaml", StrategyConfiguration.class);
        templateStrategies = new ArrayList<StrategyConfiguration>();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public List<StrategyX> getStrategies(List<String> _names) {
    	List<StrategyX> _list = new ArrayList<StrategyX>();
    	for (int i = 0; i < _names.size(); i++) {
    		_list.add(getStrategy(_names.get(i)));
    	}
    	return _list;
	}
*/

//-------------------------------------------------------------------------------------
    public void changeStrategy(String[] _args) {
    	String _name = _args[1];
    	String _barDuration = _args[2];
    	String _variation = _args[3];

//    	BigDecimal _targetProfit = new BigDecimal(_profit);
        debug.out(Debug.IMPORTANT3, "Change strategy "+_name+" with _barDuration="+_barDuration +" and _variation="+_variation);
		for (Map.Entry<Key, StrategyX> _entry : entities.entrySet()) {
//		    System.out.println(entry.getKey() + "/" + entry.getValue());
		    Key _key = _entry.getKey();
		    StrategyX _strategy = _entry.getValue();
//		    StrategyConfiguration _strategyConfiguration = _strategy.getConfiguration();
		}    	
        debug.outln(Debug.IMPORTANT3, "", false);		
	}

//-------------------------------------------------------------------------------------
    public synchronized StrategyX getStrategy(CurrencyPair _currencyPair, Map<String, String> _strategyData) {
		debug.outln(Debug.INFO, "_strategyData= "+_strategyData);
		Key _key = new Key(_currencyPair, _strategyData);
		StrategyX _strategy = getEntity(_key);
		if(_strategy == null){
			_strategy = createNewStrategyFromName(_currencyPair, _strategyData);
			if(_strategy == null){
				debug.outln(Debug.ERROR, "No strategy associated with name: "+_strategyData);
				return null;
			}
			_strategy.link();
			addEntity(_key, _strategy);
		}
		return _strategy;
	}

//-------------------------------------------------------------------------------------
	private StrategyX createNewStrategyFromName(CurrencyPair _currencyPair, Map<String, String> _strategyData){
		StrategyX _strategy = null;
		String _name = _strategyData.get("name");
		StrategyConfiguration _strategyConfiguration = new StrategyConfiguration();
    	_strategyConfiguration.setEnabled(true);
    	_strategyConfiguration.setName(_name);
		if(_name.equals("awesome_oscillator")){
			_strategy = new AwesomeOscillatorStrategy(debug, resolver, _strategyConfiguration, _currencyPair, _strategyData);
		} else if(_name.equals("global_extrema")){
			_strategy = new GlobalExtremaStrategy(debug, resolver, _strategyConfiguration, _currencyPair, _strategyData);
		}
		return _strategy;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void loadEntity(StrategyConfiguration _strategyConfiguration) {
//        StrategyX _strategy = new StrategyX(debug, resolver, _strategyConfiguration, configurationLoader);
//        templateStrategies.add(_strategyConfiguration);
//    	Key _key = new Key(_strategyConfiguration.getName());
//		addEntity(_key, _strategy);
    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
