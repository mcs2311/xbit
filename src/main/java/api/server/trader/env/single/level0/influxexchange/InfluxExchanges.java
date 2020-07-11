//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level0.influxexchanges;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level1.repositories.*;


//-------------------------------------------------------------------------------------
public class InfluxExchanges extends AbstractCluster<ExchangeConfiguration, InfluxExchange> {

//-------------------------------------------------------------------------------------
    public InfluxExchanges(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "exchanges");
    	String _path = SystemUtils.getHomeDirectory() + "/" + resolver.getServerConfiguration().getPathToExchanges();
        configurationLoader = new ConfigurationLoader<ExchangeConfiguration>(_debug, _path, ".yaml", ExchangeConfiguration.class);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<InfluxExchange> getExchanges(List<String> _names) {
    	List<InfluxExchange> _list = new ArrayList<InfluxExchange>();
    	for (int i = 0; i < _names.size(); i++) {
    		_list.add(getExchange(_names.get(i)));
    	}
    	return _list;
	}

//-------------------------------------------------------------------------------------
    public synchronized InfluxExchange getExchange(String _name) {
		Key _key = new Key(_name);
		InfluxExchange _influxExchange = getEntity(_key);
		if(_influxExchange == null){
			ExchangeConfiguration _exchangeConfiguration = configurationLoader.load(_name);
			_influxExchange = new InfluxExchange(debug, resolver, _exchangeConfiguration);
		}
    	return _influxExchange;
//		return _influxExchange;
	}
/*
//-------------------------------------------------------------------------------------
    public List<String> getExchangesInfo() {
        List<String> _list = new ArrayList<String>();
        for (int i = 0; i < entities.size(); i++) {
            InfluxExchange _influxExchange = getEntity.get(i);
            _list.addAll(_influxExchange.getExchangesInfo());
        }
        return _list;
    }
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public InfluxExchange loadEntity(ExchangeConfiguration _exchangeConfiguration) {
		InfluxExchange _influxExchange = new InfluxExchange(debug, resolver, _exchangeConfiguration);
		Key _key = new Key(_exchangeConfiguration.getShortName());
//		debug.outln("Exchanges.loadEntity ...."+_exchangeConfiguration.getShortName());
		addEntity(_key, _influxExchange);
		return _influxExchange;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void initiate() {
    	super.initiate();
    	
*/
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
