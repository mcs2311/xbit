//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.input;
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

//-------------------------------------------------------------------------------------
public class InputRepositories extends AbstractCluster<RepositoryConfiguration, Repository> {

//-------------------------------------------------------------------------------------
	@SuppressWarnings({"rawtypes", "unchecked"})
    public InputRepositories(Debug _debug, Resolver _resolver, ConfigurationLoader _configurationLoader) {
    	super(_debug, _resolver);
        configurationLoader = _configurationLoader;
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Repository getRepository(CurrencyPair _currencyPair, String _type) {
//    	debug.outln(Debug.IMPORTANT3, "Seeking repository " + _currencyPair +"...");
		Key _key = new Key(_currencyPair, _type);
		return getEntity(_key);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Repository loadEntity(RepositoryConfiguration _repositoryConfiguration) {
    	InputRepository _inputRepository = null;
    	List<RepositoryConfiguration> _inputRepositoryConfigurations = _repositoryConfiguration.getInputConfigurations();
    	for (int i = 0; i < _inputRepositoryConfigurations.size(); i++) {
    		RepositoryConfiguration _repositoryConfiguration0 = _inputRepositoryConfigurations.get(i);
    		String _exchangeName = _repositoryConfiguration0.getExchange();
    		CurrencyPair _currencyPair = _repositoryConfiguration0.getCurrencyPair();
    		String _type = _repositoryConfiguration0.getType();
    		_inputRepository = (InputRepository)getRepository(_currencyPair, _type);
	    	if(_inputRepository == null){
//    			debug.outln(Debug.IMPORTANT3, "Repository not found. Create repository for: " + _repositoryConfiguration0 +"...");
    			_inputRepository = new InputRepository(debug, resolver, _repositoryConfiguration0, configurationLoader);
				Key _key = new Key(_currencyPair, _type);
				addEntity(_key, _inputRepository);
	    	}
    	}
//    	debug.outln(Debug.IMPORTANT3, "InputRepositories " + entities.size() +" loaded ...");
    	return null;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
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
    	super.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
