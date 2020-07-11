//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level1.repositories.input;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.sql.*;
import java.math.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;


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

//-------------------------------------------------------------------------------------
public class InputRepository extends Repository {
	private String exchange;
	private CurrencyPair currencyPair;
	private String type;

    //---cache:


//-------------------------------------------------------------------------------------
    public InputRepository(Debug _debug, Resolver _resolver, RepositoryConfiguration _repositoryConfiguration, ConfigurationLoader<RepositoryConfiguration> _configurationLoader) {
    	super(_debug, _resolver, _repositoryConfiguration, _configurationLoader);
    	exchange = configuration.getExchange();
    	currencyPair = configuration.getCurrencyPair();
    	type = configuration.getType();
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void onNext(Trade _trade){
    	TradeEvent _tradeEvent = new TradeEvent(_trade);
//    	debug.outln(Debug.INFO, "InputRepository.onNext - trade["+configuration.getCurrencyPair()+"]: "+_beanEvent);
		if(emitter != null){
    		emitter.onNext(_tradeEvent);
    	}
    }

//-------------------------------------------------------------------------------------
    public void onNext(Ticker _ticker){
    	TickerEvent _tickerEvent = new TickerEvent(_ticker);
//    	debug.outln(Debug.INFO, "InputRepository.onNext - ticker: "+_beanEvent);
		if(emitter != null){
    		emitter.onNext(_tickerEvent);
    	}
    }

//-------------------------------------------------------------------------------------
    public void onNext(OrderBook _orderBook){
//    	debug.outln(Debug.INFO, "InputRepository.onNext - _orderBook: asks: "+_orderBook.getAsks().size() + ", bids: "+_orderBook.getBids().size());
    	OrderbookEvent _orderbookEvent = new OrderbookEvent(_orderBook);
		if(emitter != null){
    		emitter.onNext(_orderbookEvent);
		}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();

    	if(type.equals("trade")){
	    	resolver.getInfluxExchanges()
	    			.getExchange(exchange)
			    	.getTrades(currencyPair)
					.toFlowable(BackpressureStrategy.BUFFER)
	//				.onBackpressureBuffer(16, () -> { },
	//              		BackpressureOverflowStrategy.DROP_OLDEST)
	              	.subscribeOn(Schedulers.io())
	//				.observeOn(Schedulers.computation())
	//				.onBackpressureBuffer(16, () -> { },
	//              		BackpressureOverflowStrategy.DROP_OLDEST)
	//		    	.onBackpressureDrop()
					.observeOn(Schedulers.computation(), false)
	//				.observeOn(Schedulers.newThread(), false)
					.subscribe(_trade -> onNext(_trade),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());

    	} else if(type.equals("ticker")){
	    	resolver.getInfluxExchanges()
	    			.getExchange(exchange)
			    	.getTicker(currencyPair)
					.toFlowable(BackpressureStrategy.DROP)
					.onBackpressureBuffer(16, () -> { },
	              		BackpressureOverflowStrategy.DROP_OLDEST)
			    	.subscribeOn(Schedulers.io())
					.observeOn(Schedulers.computation(), false)
	//				.observeOn(Schedulers.newThread(), false)
					.subscribe(_ticker -> onNext(_ticker),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
		} else if(type.equals("orderbook")){
	    	resolver.getInfluxExchanges()
	    			.getExchange(exchange)
			    	.getOrderBook(currencyPair)
					.toFlowable(BackpressureStrategy.DROP)
					.onBackpressureBuffer(16, () -> { },
	              		BackpressureOverflowStrategy.DROP_OLDEST)		    	
//					.toFlowable(BackpressureStrategy.BUFFER)
			    	.subscribeOn(Schedulers.io())
					.observeOn(Schedulers.computation(), false)
					.subscribe(_orderBook -> onNext(_orderBook),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());    	
		} else {
			debug.outln(Debug.ERROR, "Error 333: Unknown type of input repository: " + type);
		}
    }

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
