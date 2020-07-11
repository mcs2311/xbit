//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.rawtrades;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import info.bitrich.xchangestream.core.*;
import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.service.trade.params.*;
import org.knowm.xchange.service.trade.params.orders.*;
import org.knowm.xchange.exceptions.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.common.loaders.orders.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level1.raworders.*;

//-------------------------------------------------------------------------------------
public class RawTradesByExchangeAndCurrencyPair extends AbstractCluster<Configuration, RawTrade> implements FlowableOnSubscribe<UserTradeEvent>, Runnable  {
	private String exchange;
	private CurrencyPair currencyPair;

	private long highestTradeId;
	private long latestTradeTime;

	//---cache:
	private TradeService tradeService;
//	private DefaultTradeHistoryParamCurrencyPair​ defaultTradeHistoryParamCurrencyPair​;
	private TradeHistoryParamsAll tradeHistoryParamsAll;
	private Thread thisThread;

	private RawOrdersByExchangeAndCurrencyPair rawOrdersByExchangeAndCurrencyPair;

    //---rx:
	private Flowable<UserTradeEvent> flowable;
	private FlowableEmitter<UserTradeEvent> emitter;


//-------------------------------------------------------------------------------------
    public RawTradesByExchangeAndCurrencyPair(Debug _debug, Resolver _resolver, String _exchange, CurrencyPair _currencyPair) {
    	super(_debug, _resolver);
    	exchange = _exchange;
    	currencyPair = _currencyPair;

		setHighestTradeId(-1L);
		setLastestTradeTime(0L);

		UserExchange _userExchange = resolver.getUserExchanges().getUserExchange(exchange);

    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);

    	ExchangeConfiguration _exchangeConfiguration = _userExchange.getExchangeConfiguration();
		CurrencyPair _currencyPairCode = _exchangeConfiguration.getCurrencyPairCode(_currencyPair);

		if(!_exchangeConfiguration.supportForStreamingTradeChanges()){
	        tradeService = _userExchange.getTradeService();
//	        defaultTradeHistoryParamCurrencyPair​ = new DefaultTradeHistoryParamCurrencyPair​(_currencyPairCode);
	        tradeHistoryParamsAll = new TradeHistoryParamsAll();
	        tradeHistoryParamsAll.setCurrencyPair(_currencyPairCode);
			tradeHistoryParamsAll.setStartId("0");
	        tradeHistoryParamsAll.setOffset(0L);
	        tradeHistoryParamsAll.setPageLength(10);
//			lastTradeId
	        try{
		    	Thread thisThread = new Thread(this);
		    	thisThread.start();
		    }catch(Throwable _t){
    			onError(_t);
    		}
		} else {
			io.reactivex.Observable<UserTrade> _userTrades = _resolver.getUserExchanges().getUserExchange(_exchange)
	    			.getUserTrades(_currencyPairCode);

	    	if(_userTrades != null){
	    			_userTrades.subscribeOn(Schedulers.io())
	    			.observeOn(Schedulers.computation(), false)
	    			.subscribe(_userTrade -> onNext(_userTrade),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
	    	}
		}
		load();
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized long getHighestTradeId() {
    	return highestTradeId;
    }

//-------------------------------------------------------------------------------------
    public synchronized void setHighestTradeId(long _highestTradeId) {
//    	highestTradeId = Math.max(highestTradeId, _highestTradeId);
    	highestTradeId = _highestTradeId;
    }

//-------------------------------------------------------------------------------------
    public synchronized long getLastestTradeTime() {
    	return latestTradeTime;
    }

//-------------------------------------------------------------------------------------
    public synchronized void setLastestTradeTime(long _latestTradeTime) {
//    	latestTradeTime = Math.max(latestTradeTime, _latestTradeTime);
    	latestTradeTime = _latestTradeTime;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
    	int _refreshCounter = 0;
    	int _delay = 1000;
        while(isEnabled()){
            try{
                Thread.sleep(_delay);
            }catch(InterruptedException _e){
//            	break;
            }
//            if(_refreshCounter%10 == 0){
//				debug.outln("Check for new trades... highestTradeId="+highestTradeId + ", latestTradeTime="+latestTradeTime);
//            }
            checkForNewTrades();
            _delay = 40000;// + (_refreshCounter * 1000);
            _refreshCounter = (_refreshCounter + 1)%100;
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void checkForNewTrades() {
    	if(getLastestTradeTime() != 0){
//    		tradeHistoryParamsAll.setStartTime(new Date(getLastestTradeTime()));
			tradeHistoryParamsAll.setStartId(Long.toString(getHighestTradeId()));
	    	tradeHistoryParamsAll.setOffset(1L);
    	}

//    	lastTradeTime = System.currentTimeMillis();
//		debug.outln("Check for new trades...0: lastTradeId="+lastTradeId + ", lastTradeTime="+lastTradeTime);
		UserTrades _userTrades = null;
		try{
			_userTrades = tradeService.getTradeHistory​(tradeHistoryParamsAll);			
		} catch(IOException _e){
			debug.outln(Debug.ERROR, "Cannot retreive trades...");
			return;
		}
//		debug.outln("Check for new trades...1: _userTrades.getlastID()="+_userTrades.getlastID());
//		if(_userTrades.getlastID() > lastTradeId){
			checkForNewTrades(_userTrades.getUserTrades());
//		}
    }

//-------------------------------------------------------------------------------------
    private void checkForNewTrades(List<UserTrade> _userTrades) {
//		debug.outln("checkForNewTrades:"+_userTrades.size());
		for (int i = 0; i < _userTrades.size(); i++) {
			UserTrade _userTrade = _userTrades.get(i);
//			long _tradeId = StringUtils.getLongNumber(_userTrade.getId());
//			if(getLastTradeId() < _tradeId){
			long _tradeTime = _userTrade.getTimestamp().getTime();
//			if(_tradeTime > getLastestTradeTime()){
				checkIfUserTradeAlreadyExists(_userTrade);
//			}
		}
    }

//-------------------------------------------------------------------------------------
    private void checkIfUserTradeAlreadyExists(UserTrade _userTrade) {
//		debug.outln("Check for new trade...:"+_userTrade);
		String _tradeIdString = _userTrade.getId();
		long _tradeId = StringUtils.getLongNumber(_tradeIdString);

		Key _key = new Key(_tradeId);
		RawTrade _rawTrade = getEntity(_key);
		if(_rawTrade != null){
			return;
		}

		setHighestTradeId(_tradeId);
		setLastestTradeTime(_userTrade.getTimestamp().getTime());		

					
		_rawTrade = new RawTrade(debug, 
					resolver,
					exchange, 
					currencyPair,
					_userTrade);
		
		addRawTrade(_rawTrade);

//					addEntity(_key, _rawTrade);

//		debug.outln("New trade detected..." + _rawTrade);

		if(emitter != null){
				UserTradeEvent _userOrdersEvent = 
				new UserTradeEvent(UserTradeEvent.TRADE_DETECTED_NEW, _rawTrade);
				emitter.onNext(_userOrdersEvent);
		}
//		}
    }

//-------------------------------------------------------------------------------------
    public synchronized void addRawTrade(RawTrade _rawTrade) {
	    long _orderId = _rawTrade.getId();
    	Key _key = new Key(_orderId);
		addEntity(_key, _rawTrade);
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onNext(UserTrade _userTrade){
		checkIfUserTradeAlreadyExists(_userTrade);        
	}

//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<UserTradeEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<UserTradeEvent> getRawTrade(long _highestTradeId, long _latestTradeTime) {
		setHighestTradeId(_highestTradeId);
		setLastestTradeTime(_latestTradeTime);
		return flowable;
    }

//-------------------------------------------------------------------------------------
    public void triggerTradesCheck(){
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}    	
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    	rawOrdersByExchangeAndCurrencyPair = 
    		resolver.getRawOrders()
    				.getRawOrdersByExchangeAndCurrencyPair(exchange, currencyPair);
	}

//-------------------------------------------------------------------------------------
    public void save() {
    	super.save();
		setEnabled(false);
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}    	
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "RawTradesByExchangeAndCurrencyPair:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
