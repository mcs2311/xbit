//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.output;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


//import codex.xbit.api.server.trader.common.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
//import codex.xbit.api.server.trader.core.components.keys.*;

//import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers.*;

//-------------------------------------------------------------------------------------
public class OutputRepositories extends AbstractCluster<RepositoryConfiguration, Repository> implements Runnable {

//-------------------------------------------------------------------------------------
	@SuppressWarnings({"rawtypes", "unchecked"})
    public OutputRepositories(Debug _debug, Resolver _resolver, ConfigurationLoader _configurationLoader) {
    	super(_debug, _resolver);
        configurationLoader = _configurationLoader;
        try{
	    	Thread _thread = new Thread(this);
	    	_thread.start();
    	}catch(Throwable _t){
    		onError(_t);
    	}
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public Repository getRepository(String _exchange, CurrencyPair _currencyPair, String _type) {
    	return getRepository(_exchange, _currencyPair, _type, null);
    }

//-------------------------------------------------------------------------------------
    public Repository getRepository(CurrencyPair _currencyPair, String _type, String _barDuration) {
		return getRepository(null, _currencyPair, _type, _barDuration);
	}
*/
//-------------------------------------------------------------------------------------
    public Repository getRepository(CurrencyPair _currencyPair, String _type, String _barDuration) {
//    	debug.outln(Debug.IMPORTANT3, "Seeking OutputRepository... " + _currencyPair + " , "+_type+" , " + _barDuration);
		Key _key = new Key(_currencyPair, _type, _barDuration);
		return getEntity(_key);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Repository loadEntity(RepositoryConfiguration _repositoryConfiguration) {
		Repository _repository = null;
    	List<RepositoryConfiguration> _allRepositoryConfigurations = _repositoryConfiguration.getAllConfigurations();
    	for (int i = 0; i < _allRepositoryConfigurations.size(); i++) {
    		RepositoryConfiguration _repositoryConfiguration0 = _allRepositoryConfigurations.get(i);
    		switch(_repositoryConfiguration0.getType()){
    			case "ticker" : {
    				_repository = new TickerRepository(debug, resolver, _repositoryConfiguration0, configurationLoader);
    				break;
    			}
    			case "trade" : {
    				_repository = new TradeRepository(debug, resolver, _repositoryConfiguration0, configurationLoader);
    				break;
    			}
    			case "orderbook" : {
    				_repository = new OrderbookRepository(debug, resolver, _repositoryConfiguration0, configurationLoader);
    				break;
    			}
    			default : {
    				debug.outln(Debug.ERROR, "Unknown repository type: " + _repositoryConfiguration0.getType());
    				break;
    			}
    		}
//	    	Repository _repository = new Repository(debug, resolver, _repositoryConfiguration0, configurationLoader);
//    		debug.outln(Debug.IMPORTANT3, "Adding repository " + _repositoryConfiguration0.getExchange() +" , " + _repositoryConfiguration0.getCurrencyPair() + " , "+_repositoryConfiguration0.getType()+" , " + _repositoryConfiguration0.getBarDuration());
	    	Key _key = new Key(_repositoryConfiguration0.getCurrencyPair(),
	    		_repositoryConfiguration0.getType(),
	    		_repositoryConfiguration0.getBarDuration());
	    	if(getEntity(_key) != null){
	    		debug.outln(Debug.WARNING, "Duplicate repository: "+ _repository);
	    		System.exit(0);
	    	} else {
				addEntity(_key, _repository);	    		
	    	}
    	}
//    	debug.outln(Debug.IMPORTANT3, "OutputRepositories " + entities.size() +" loaded ...");
    	return _repository;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
        while(true){
            try{
                Thread.sleep(1800000);
            }catch(InterruptedException _e){
            	break;
            }
			debug.outln("Save repositories...");
            save();
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln("Save repositories...");
    	super.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
