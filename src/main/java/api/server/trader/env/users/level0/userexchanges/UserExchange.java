//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level0.userexchanges;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import info.bitrich.xchangestream.core.*;
import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.*;
import org.knowm.xchange.dto.account.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.meta.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.exceptions.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class UserExchange extends AbstractExchange<AccountConfiguration> {
    
//----
    private Exchange exchange;
    private TradeService tradeService;
    private AccountService accountService;
    private MarketDataService marketDataService;
    private List<CurrencyPair> allCurrencyPairs;
    private ExchangeMetaData exchangeMetaData;

//-------------------------------------------------------------------------------------
    public UserExchange(Debug _debug, Resolver _resolver, AccountConfiguration _accountConfiguration, ConfigurationLoader<AccountConfiguration> _configurationLoader) {
        super(_debug, _resolver, _accountConfiguration);
//        log.info("qqqqq");
        if(_accountConfiguration != null){
	        String _exchangeName = _accountConfiguration.getName();
	        exchangeConfiguration = resolver.getInfluxExchanges().getExchange(_exchangeName).getConfiguration();
	//        state = STATE_INIT;
        }
        load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected void stateConnecting(){
		if(configuration.isEnabled()){
			initExchange();
		}
    }

//-------------------------------------------------------------------------------------
    protected void stateReady(){
    	while(true){
    		try{
    			Thread.sleep(1000);    			
    		}catch(Exception _e){

    		}
    	}
//    	setState(STATE_CONNECTING);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void initExchange() {
//		debug.outln("initExchange: " + exchangeConfiguration.getShortName() + " ---> " + exchangeConfiguration.getExchangeName());
//		debug.outln("userExchange: " + exchangeConfiguration.getShortName());

        ExchangeSpecification _exchangeSpecification = new ExchangeSpecification(exchangeConfiguration.getExchangeName());
        //(new UserExchange()).getDefaultExchangeSpecification(configuration.getExchangeName());
        _exchangeSpecification.setUserName(configuration.getUserName());
        _exchangeSpecification.setApiKey(configuration.getApiKey());
        _exchangeSpecification.setSecretKey(configuration.getSecretKey());

		//exchangeSpecification.setShouldLoadRemoteMetaData(true);

        while(exchange == null){
            try{
                exchange = ExchangeFactory.INSTANCE.createExchange(_exchangeSpecification);
//                break;
            } catch(Throwable _t){
        		debug.outln(Debug.WARNING, "createExchange failed: " + configuration.getName() + " : "+ _t.getMessage());
				onError(_t);            	
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException _e1){}
                exchange = null;
            }
        }

        tradeService = exchange.getTradeService();
        accountService = exchange.getAccountService();
        marketDataService = exchange.getMarketDataService();
        allCurrencyPairs = exchange.getExchangeSymbols();
       	exchangeMetaData = exchange.getExchangeMetaData();
//        debug.outln("exchangeMetaData: " + exchangeMetaData);

//file:///Users/marius/Documents/JAVA/xchange-core/org/knowm/xchange/dto/account/Fee.html
//file:///Users/marius/Documents/JAVA/xchange-core/org/knowm/xchange/dto/meta/CurrencyPairMetaData.html
//        debug.outln("All currencies from ["+getShortName()+"] exchange: " + allCurrencyPairs.toString());

//        debug.outln("All currencies from ["+getShortName()+"] exchange: " + allCurrencyPairs.toString());


		String _streamingName = exchangeConfiguration.getStreamingName();
        ExchangeSpecification _streamingExchangeSpecification = new ExchangeSpecification(_streamingName);
        //(new UserExchange()).getDefaultExchangeSpecification(configuration.getExchangeName());
        _streamingExchangeSpecification.setUserName(configuration.getUserName());
        _streamingExchangeSpecification.setApiKey(configuration.getApiKey());
        _streamingExchangeSpecification.setSecretKey(configuration.getSecretKey());

        while(streamingExchange == null){
            try{
	    		streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(_streamingExchangeSpecification);
	    		streamingExchange.useCompressedMessages(exchangeConfiguration.useCompression());
				streamingExchange.connect().blockingAwait();        	
            } catch(Throwable _t){
        		debug.outln(Debug.WARNING, "createStreamingExchange failed: " + _streamingName + " : "+ _t.getMessage());
        		onError(_t);
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException _e1){}
            }
        }

        try {
	    	streamingTradeService = streamingExchange.getStreamingTradeService();
        } catch(NotYetImplementedForExchangeException _e){

        }

        try {
	    	streamingAccountService = streamingExchange.getStreamingAccountService();
        } catch(NotYetImplementedForExchangeException _e){

        }

        try {
	    	streamingMarketDataService = streamingExchange.getStreamingMarketDataService();
        } catch(NotYetImplementedForExchangeException _e){

        }
        setState(STATE_READY);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public AccountConfiguration getAccountConfiguration(){
        return configuration;
    }

//-------------------------------------------------------------------------------------
    public String getShortName(){
        return configuration.getName();
    }

//-------------------------------------------------------------------------------------
    public TradeService getTradeService(){
    	waitUntilIsReady();
        return tradeService;
    }

//-------------------------------------------------------------------------------------
    public AccountService getAccountService(){
    	waitUntilIsReady();
        return accountService;
    }

//-------------------------------------------------------------------------------------
    public MarketDataService getMarketDataService(){
    	waitUntilIsReady();
        return marketDataService;
    }

//-------------------------------------------------------------------------------------
    public ExchangeMetaData getExchangeMetaData(){
    	waitUntilIsReady();
        return exchangeMetaData;
    }

//-------------------------------------------------------------------------------------
    public CurrencyMetaData getCurrencyMetaData(org.knowm.xchange.currency.Currency _currency){
    	waitUntilIsReady();
//        return exchangeMetaData.getCurrencyPairs().get(_currencyPair);
//    	debug.outln(">>>1>"+exchangeMetaData.getCurrencies());
//    	debug.outln(">>>2>"+exchangeMetaData.getCurrencyPairs());
    	org.knowm.xchange.currency.Currency _currencyCode = exchangeConfiguration.getCurrencyCode(_currency);
        return exchangeMetaData.getCurrencies().get(_currencyCode);
    }

//-------------------------------------------------------------------------------------
    public CurrencyPairMetaData getCurrencyPairMetaData(CurrencyPair _currencyPair){
    	waitUntilIsReady();
//        return exchangeMetaData.getCurrencyPairs().get(_currencyPair);
//    	debug.outln(">>>1>"+exchangeMetaData.getCurrencies());
//    	debug.outln(">>>2>"+exchangeMetaData.getCurrencyPairs());
    	CurrencyPair _currencyPairCode = exchangeConfiguration.getCurrencyPairCode(_currencyPair);
        return exchangeMetaData.getCurrencyPairs().get(_currencyPairCode);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public io.reactivex.Observable<Balance> getBalanceChanges(org.knowm.xchange.currency.Currency _currency){
    	waitUntilIsReady();
		org.knowm.xchange.currency.Currency _currencyCode = exchangeConfiguration.getCurrencyCode(_currency);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currency + " in " + getShortName());
			System.exit(0);
		}
		if(streamingAccountService == null){
			return null;
		}
        return streamingAccountService.getBalanceChanges(_currencyCode);
	}


//-------------------------------------------------------------------------------------
	public io.reactivex.Observable<Order> getOrderChanges(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = exchangeConfiguration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyPairCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
			System.exit(0);
		}
		if(streamingTradeService == null){
			return null;
		}
        return streamingTradeService.getOrderChanges(_currencyPairCode);
	}


//-------------------------------------------------------------------------------------
	public io.reactivex.Observable<UserTrade> getUserTrades(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = exchangeConfiguration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyPairCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
			System.exit(0);
		}
		if(streamingTradeService == null){
			return null;
		}
        return streamingTradeService.getUserTrades(_currencyPairCode);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Scales getScales(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = exchangeConfiguration.getCurrencyPairCode(_currencyPair);
		CurrencyPairMetaData _currencyPairMetaData = getCurrencyPairMetaData(_currencyPair);
		CurrencyMetaData _currencyMetaData = getCurrencyMetaData(_currencyPairCode.base);
		if(_currencyMetaData == null){
    		debug.outln(Debug.INFO, "Cannot find CurrencyMetaData for  : "+_currencyPairCode+"/"+_currencyPair);
    		System.exit(0);
		}
		if(_currencyPairMetaData == null){
    		debug.outln(Debug.INFO, "Cannot find CurrencyPairMetaData for  : "+_currencyPairCode+"/"+_currencyPair);
    		System.exit(0);
		}
//    		debug.outln(Debug.INFO, "CurrencyPairMetaData for  : "+_currencyPairCode+"="+_currencyPairMetaData+", _currencyMetaData="+_currencyMetaData);
    	int _amountScale = 12;
    	Integer _scale = _currencyMetaData.getScale();
    	if(_scale != null){
    		_amountScale = _scale;
    	} else {
    		Integer _baseScale = _currencyPairMetaData.getBaseScale();
    		if(_baseScale != null){
    			_amountScale = _baseScale;
    		}
    	}

    	int _priceScale = _currencyPairMetaData.getPriceScale();
    	Scales _scales = new Scales(_amountScale, _priceScale);
//    	debug.outln(Debug.INFO, "Pair _amountScale="+_amountScale+", _priceScale="+_priceScale);
    	return _scales;
	}
  
//-------------------------------------------------------------------------------------
    public List<String> getInfo() {
        List<String> _list = new ArrayList<String>();
//        _list.add();
        _list.addAll(configuration.getExchangesInfo());
        return _list;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln("UserExchange save...0");
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "UserExchange:" + getName();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
