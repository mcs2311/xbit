//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level0.influxexchanges;
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
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class InfluxExchange extends AbstractExchange<ExchangeConfiguration> {

    //---rx:

    //---statics:

//-------------------------------------------------------------------------------------
    public InfluxExchange(Debug _debug, Resolver _resolver, ExchangeConfiguration _exchangeConfiguration) {
        super(_debug, _resolver, _exchangeConfiguration);
//        log.info("qqqqq");
        exchangeConfiguration = _exchangeConfiguration;
        load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    protected void stateConnecting(){
		try{
		    String _streamingName = configuration.getStreamingName();
		    if((_streamingName != null) && (!_streamingName.isEmpty())){
		    	initExchangeStreams(_streamingName);
		    }   			
		} catch(Throwable _t){
			onError(_t);
//        	debug.outln(Debug.ERROR, "StreamingExchangeX.exception:"+getShortName()+" : "+_t.getMessage());
//        	_e1.printStackTrace();
        	return;
		}
//        debug.outln(Debug.ERROR, "InfluxExchange "+getShortName()+" connected...");
//        setState(STATE_READY);
    }

//-------------------------------------------------------------------------------------
    protected void stateReady(){
    	while(streamingExchange.isAlive()){
    		try{
    			Thread.sleep(1000);    			
    		}catch(Throwable _t){
//				onError(_t);
    		}
    	}
//    	setState(STATE_CONNECTING);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//https://github.com/bitrich-info/xchange-stream
//-------------------------------------------------------------------------------------
    private void initExchangeStreams(String _streamingName) {
//    	debug.outln("streamingExchange.1: " + configuration.getShortName());

        while(streamingExchange == null){
//    	debug.outln("streamingExchange.2: " + configuration.getShortName());
            try{
//	    		streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(_exchangeSpecification);
	    		streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(_streamingName);
	    		streamingExchange.useCompressedMessages(configuration.useCompression());
				// Connect to the StreamingExchangeX WebSocket API. Blocking wait for the connection.
//				streamingExchange.connect(productSubscription).blockingAwait();        	
				streamingExchange.connect().blockingAwait();        	

//                break;
            } catch(Exception _e){
        		debug.outln(Debug.WARNING, "createExchange failed: " + _streamingName + " : "+ _e.getMessage());
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException _e1){}
                streamingExchange = null;
            }
        }
//    	debug.outln("streamingExchange.3: " + configuration.getShortName());
// for binance
//        if(configuration.needsToSubscribeBeforeConnect()){
//        }

		streamingMarketDataService = streamingExchange.getStreamingMarketDataService();
		setState(STATE_READY);
//        assignProductSubscription();
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public io.reactivex.Observable<Trade> getTrades(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = configuration.getCurrencyPairCode(_currencyPair);
//		debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyPairCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
			System.exit(0);
		}
    	return streamingMarketDataService.getTrades(_currencyPairCode);
	}

//-------------------------------------------------------------------------------------
    public io.reactivex.Observable<Ticker> getTicker(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = configuration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyPairCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
			System.exit(0);
		}
    	return streamingMarketDataService.getTicker(_currencyPairCode);
	}

//-------------------------------------------------------------------------------------
    public io.reactivex.Observable<OrderBook> getOrderBook(CurrencyPair _currencyPair){
    	waitUntilIsReady();
		CurrencyPair _currencyPairCode = configuration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
		if(_currencyPairCode == null){
			debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
			System.exit(0);
		}
    	return streamingMarketDataService.getOrderBook(_currencyPairCode, 5);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ExchangeConfiguration getExchangeConfiguration(){
        return configuration;
    }

//-------------------------------------------------------------------------------------
    public String getShortName(){
        return configuration.getShortName();
    }

//-------------------------------------------------------------------------------------
    public List<String> getExchangesInfo() {
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
// 		debug.outln("Exchange.load ....0");
    	super.load();
//		debug.outln("Exchange.loas ....1");
//        List<String> _strategiesList = configuration.getStrategiesList();
//        trader.getMonitoring().getTacticCollections().addStrategies(_strategiesList);
    }

//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln("StreamingExchangeX save...0");
    	super.save();
        // Unsubscribe from data order book.
//		debug.outln("StreamingExchangeX save...1");
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "StreamingExchangeX:" + 
		configuration.getShortName();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
