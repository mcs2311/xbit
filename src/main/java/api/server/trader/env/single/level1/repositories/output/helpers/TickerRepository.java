//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.repositories.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;

//-------------------------------------------------------------------------------------
public class TickerRepository extends OutputRepository {

    //---cache:

//-------------------------------------------------------------------------------------
    public TickerRepository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration, _configurationLoader);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(BeanEvent _beanEvent) {
		if(emitter != null) {
			emitter.onNext(_beanEvent);
		}
	}

//-------------------------------------------------------------------------------------
    public BarSeries getSeries(String _type){
        return null;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	resolver.getRepositories()
    			.getInputRepositories()
    			.getRepository(currencyPair, "ticker")
    			.getBean()
    			.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation())
				.subscribe(_beanEvent -> onNext(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
    public void save() {

    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
