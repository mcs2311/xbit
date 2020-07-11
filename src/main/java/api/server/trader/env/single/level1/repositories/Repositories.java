//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level1.repositories.input.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;

//-------------------------------------------------------------------------------------
public class Repositories extends AbstractCluster<RepositoryConfiguration, Repository> {
	private InputRepositories inputRepositories;
	private OutputRepositories outputRepositories;

//-------------------------------------------------------------------------------------
    public Repositories(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "repositories");
    	String _path = SystemUtils.getHomeDirectory() + "/" + resolver.getServerConfiguration().getPathToRepositories();
        configurationLoader = new ConfigurationLoader<RepositoryConfiguration>(_debug, _path, ".yaml", RepositoryConfiguration.class);
        inputRepositories = new InputRepositories(_debug, _resolver, configurationLoader);
        outputRepositories = new OutputRepositories(_debug, _resolver, configurationLoader);
	}

//-------------------------------------------------------------------------------------
    public InputRepositories getInputRepositories() {
    	return inputRepositories;
    }

//-------------------------------------------------------------------------------------
    public OutputRepositories getOutputRepositories() {
    	return outputRepositories;
    }

//-------------------------------------------------------------------------------------
    public Repository loadEntity(RepositoryConfiguration _repositoryConfiguration) {
    	if(_repositoryConfiguration.isEnabled()){
	    	inputRepositories.loadEntity(_repositoryConfiguration);
	    	outputRepositories.loadEntity(_repositoryConfiguration);
    	}
    	return null;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//		debug.outln("Load repositories...");
    	super.load();
//    	inputRepositories.load();
//    	outputRepositories.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
		debug.outln("Save repositories...");
    	super.save();
    	inputRepositories.save();
    	outputRepositories.save();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
