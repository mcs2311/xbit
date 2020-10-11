//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.text.*;

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

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.helpers.*;
import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter.crawler.*;

//-------------------------------------------------------------------------------------
public class TwitterSignal extends Signal {
//	private String exchange;
//	private CurrencyPair repositoryCurrencyPair;

	private String account;
	private float thresholdBuy;
	private float thresholdSell;

	private TwitterConfiguration configuration;


	private int eventCounter;

	//---statics:
//	private final static DecimalFormat df = new DecimalFormat("#.##");
	private static TwitterCrawler twitterCrawler;

//-------------------------------------------------------------------------------------
    public TwitterSignal(Debug _debug, Resolver _resolver, SignalConfiguration _configuration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _configuration, _currencyPair, _args);

        eventCounter = 0;
		debug.outln(Debug.WARNING, "TwitterSignal._args: "+args);

//		exchange = args.get("exchange");
//		repositoryCurrencyPair = new CurrencyPair(args.get("currencyPair"));

		account = args.get("account");
		if(account == null){
			debug.outln(Debug.ERROR, "Cannot start TwitterSignal with null account!");
			return;
		}

		String _s = args.get("thresholdBuy");
		if(_s != null){
			thresholdBuy = Float.parseFloat(_s);
		}

		_s = args.get("thresholdSell");
		if(_s != null){
			thresholdSell = Float.parseFloat(_s);
		}

    	load();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {

//	try{

/*
	} catch(TwitterException _e1){
		debug.outln(Debug.ERROR, "TwitterException: " + _e1.getMessage());
	} catch(IOException _e2){
		debug.outln(Debug.ERROR, "IOException: " + _e2.getMessage());
	}


		resolver.getMarketModels()
			.getMarketModel(currencyPair)
			.getMarketModelEvent()
			.subscribeOn(Schedulers.io())
			.observeOn(Schedulers.computation(), false)
			.subscribe(_marketModelEvent -> onNext(_marketModelEvent),
						_throwable -> onError(_throwable),
    					() -> onCompleted());	
*/
    }


//-------------------------------------------------------------------------------------
    public void save(){
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "DepthChartSignal:" + 
		currencyPair + 
		" , " + 
		args;
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
