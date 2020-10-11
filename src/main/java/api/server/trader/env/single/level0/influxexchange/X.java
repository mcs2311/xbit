//-------------------------------------------------------------------------------------
package xbit.api.server.trader.env.single.level0.influxexchanges;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;

import io.reactivex.disposables.*;
import info.bitrich.xchangestream.core.*;
import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;

import xbit.api.utils.*;
import xbit.api.common.configs.*;
import xbit.api.common.loaders.*;

import xbit.api.server.trader.common.objects.*;

import xbit.api.server.trader.core.aspects.*;
import xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public class InfluxExchange extends AbstractEntity<ExchangeConfiguration> implements Runnable {

    private List<Disposable> subscriptions;

    //streaming
    private String streamsExchangeClassName;
    private StreamingExchange streamingExchange;
    private ProductSubscription productSubscription;

//    private Object oLock = new Object();
    private boolean startStreams = false;
    private LinkedTransferQueue<Event> queue;

    private int state = STATE_INIT;
    private Object oLock = new Object();


    //---statics:
    public static final int STATE_INIT = 0;
    public static final int STATE_READY = 1;

//-------------------------------------------------------------------------------------
    public InfluxExchange(Debug _debug, Resolver _resolver, ExchangeConfiguration _exchangeConfiguration, ConfigurationLoader<ExchangeConfiguration> _configurationLoader) {
        super(_debug, _resolver, _exchangeConfiguration, _configurationLoader);
//        log.info("qqqqq");
        subscriptions = new ArrayList<Disposable>();
        queue = new LinkedTransferQueue<Event>();
        state = STATE_INIT;
    }

//-------------------------------------------------------------------------------------
    public void run() {
    	while (true) {
    		try{
			    String _streamingName = configuration.getStreamingName();
			    if((_streamingName != null) && (!_streamingName.isEmpty())){
			    	initExchangeStreams(_streamingName);
			    	flushQueue();
			    } else {
		        	try{
		        		Thread.sleep(1000000000000L);
		        	}catch(InterruptedException _e){
		        		break;
		        	}
			    }    			
    		} catch(Exception _e1){
	        	debug.outln(Debug.ERROR, "StreamingExchangeX.exception:"+getShortName()+" : "+_e1.getMessage());
	        	_e1.printStackTrace();
	        	return;
    		}
    	}
    }



//-------------------------------------------------------------------------------------
    private void initExchangeStreams(String _streamingName) {
/*        ExchangeSpecification _exchangeSpecification = new ExchangeSpecification(configuration.getStreamingName());
        _exchangeSpecification.setUserName(configuration.getUserName());
        _exchangeSpecification.setApiKey(configuration.getApiKey());
        _exchangeSpecification.setSecretKey(configuration.getSecretKey());
*/
		while(!startStreams){
			try{
				Thread.sleep(100);
			}catch(InterruptedException _e){}
		}
//    	debug.outln("streamingExchange: " + configuration.getShortName());

        while(streamingExchange == null){
            try{
//	    		streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(_exchangeSpecification);
	    		streamingExchange = StreamingExchangeFactory.INSTANCE.createExchange(_streamingName);
	    		streamingExchange.useCompressedMessages(true);
				// Connect to the StreamingExchangeX WebSocket API. Blocking wait for the connection.
				streamingExchange.connect(productSubscription).blockingAwait();        	

//                break;
            } catch(Exception _e){
        		debug.outln(Debug.WARNING, "createExchange failed: " + _streamingName + " : "+ _e.getMessage());
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException _e1){}
            }
        }
// for binance
        if(configuration.needsToSubscribeBeforeConnect()){
			createProductSubscription();
        }



        assignProductSubscription();


        synchronized(oLock){
        	state = STATE_READY;
        	oLock.notifyAll();
        }
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public void register(Event _event, EventListenerAspect _eventListenerAspect){
    	if(_event.getId() == Event.EXCHANGE_PUSH_ORDER){
    		waitUntil(STATE_STREAMS_READY);
    		CurrencyPair _currencyPair = _event.getCurrencyPair();
        	StreamingTradeService _streamingTradeService = streamingExchange.getStreamingTradeService();

            Disposable _subscription = _streamingTradeService
            .getOrderChanges(_currencyPair)
            .subscribe(order -> {
//                        	debug.outln("subscribe.orderBook...");
				pushEvent(Event.EXCHANGE_PUSH_ORDER, _currencyPair, order);
//                                            debug.outln("Incoming orderBook: {}"+ orderBook);
            }, _throwable -> {
				debug.outln("Error in subscribing order." + _throwable);
            });
            subscriptions.add(_subscription);

    	}
    	super.register(_event, _eventListenerAspect);
    }*/

//-------------------------------------------------------------------------------------
    private void createProductSubscription(){
        ProductSubscription.ProductSubscriptionBuilder productSubscriptionBuilder = ProductSubscription.create();
/*
                .addTicker(CurrencyPair.ETH_BTC)
                .addTicker(CurrencyPair.LTC_BTC)
                .addOrderbook(CurrencyPair.LTC_BTC)
                .addTrades(CurrencyPair.BTC_USDT)
                .build();
*/
        List<CurrencyPair> _currencyPairs = getCurrencyPairs();
        _currencyPairs.forEach(_currencyPair -> {
        			CurrencyPair _currencyPairCode = configuration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
        			if(_currencyPairCode == null){
        				debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
        				System.exit(0);
        			}

        			Event _EXCHANGE_PUSH_TRADE_EVENT = new Event(Event.EXCHANGE_PUSH_TRADE, _currencyPair);
        			Event _EXCHANGE_PUSH_TICKER_EVENT = new Event(Event.EXCHANGE_PUSH_TICKER, _currencyPair);
        			Event _EXCHANGE_PUSH_ORDERBOOK_EVENT = new Event(Event.EXCHANGE_PUSH_ORDERBOOK, _currencyPair);

                    if(getNumberOfListeners(_EXCHANGE_PUSH_TRADE_EVENT) > 0){
//                        	debug.outln("subscribe.trade...0");
                    	productSubscriptionBuilder.addTrades(_currencyPairCode);
                    }

                    if(getNumberOfListeners(_EXCHANGE_PUSH_TICKER_EVENT) > 0){
                    	productSubscriptionBuilder.addTicker(_currencyPairCode);
                    }

                    // Subscribe order book data with the reference to the productSubscription.
                    if(getNumberOfListeners(_EXCHANGE_PUSH_ORDERBOOK_EVENT) > 0){
                    	productSubscriptionBuilder.addOrderbook(_currencyPairCode);

                    }
                });	 
        productSubscription = productSubscriptionBuilder.build();
	}

//-------------------------------------------------------------------------------------
    private void assignProductSubscription(){

        StreamingMarketDataService _streamingMarketDataService = streamingExchange.getStreamingMarketDataService();
//        Disposable _subscription;

//		_streamingMarketDataService.getTrades(_currencyPairCode);


        // Subscribe to live trades update.
        List<CurrencyPair> _currencyPairs = getCurrencyPairs();
        _currencyPairs.forEach(_currencyPair -> {

//        			Event _EXCHANGE_PUSH_TRADE_EVENT = new Event(Event.EXCHANGE_PUSH_TRADE, _currencyPair);
//        			Event _EXCHANGE_PUSH_TICKER_EVENT = new Event(Event.EXCHANGE_PUSH_TICKER, _currencyPair);
//        			Event _EXCHANGE_PUSH_ORDERBOOK_EVENT = new Event(Event.EXCHANGE_PUSH_ORDERBOOK, _currencyPair);

        			CurrencyPair _currencyPairCode = configuration.getCurrencyPairCode(_currencyPair);
//        			debug.outln("subscribing to ..."+getShortName()+" , -->_currencyPair="+_currencyPair+", _currencyPairCode="+_currencyPairCode);
        			if(_currencyPairCode == null){
        				debug.outln(Debug.ERROR, "There is no mapped symbol for "+_currencyPair + " in " + getShortName());
        				System.exit(0);
        			}
//        			debug.outln(Debug.INFO, "_currencyPairCode= "+_currencyPairCode);

//                    if(getNumberOfListeners(_EXCHANGE_PUSH_TRADE_EVENT) > 0){
//                        	debug.outln("subscribe.trade...0");
                        Disposable _subscription0 = _streamingMarketDataService
                        .getTrades(_currencyPairCode)
                        .subscribe(trade -> {
//                        	debug.outln("subscribe.trade...1");
							pushEvent(Event.EXCHANGE_PUSH_TRADE, _currencyPair, trade);
                        }, throwable -> {
                            debug.outln("Error in subscribing trades." + throwable + ": "+_currencyPairCode);
                        });
                        subscriptions.add(_subscription0);
//                    }

//                    if(getNumberOfListeners(_EXCHANGE_PUSH_TICKER_EVENT) > 0){
//                       	debug.outln("subscribe.ticker...0:"+ getShortName()+", "+ _currencyPairCode);
                        Disposable _subscription1 = _streamingMarketDataService
                        .getTicker(_currencyPairCode)
                        .subscribe(ticker -> { 
//                        	debug.outln("subscribe.ticker...1:"+ getShortName()+", "+ _currencyPairCode);
							pushEvent(Event.EXCHANGE_PUSH_TICKER, _currencyPair, ticker);
                        }, _throwable -> {
							debug.outln("["+this+"]["+_currencyPair+"]: Error in subscribing tickers." + _throwable);
                        });
                        subscriptions.add(_subscription1);
//                    }

                    // Subscribe order book data with the reference to the productSubscription.
//                    if(getNumberOfListeners(_EXCHANGE_PUSH_ORDERBOOK_EVENT) > 0){
/*                        Disposable _subscription2 = _streamingMarketDataService
                        .getOrderBook(_currencyPairCode)
                        .subscribe(orderBook -> {
//                        	debug.outln("subscribe.orderBook...");
							pushEvent(Event.EXCHANGE_PUSH_ORDERBOOK, _currencyPair, orderBook);
    //                                            debug.outln("Incoming orderBook: {}"+ orderBook);
                        }, _throwable -> {
							debug.outln("["+this+"]["+_currencyPair+"]: Error in subscribing orderBooks." + _throwable);
                        });
                        subscriptions.add(_subscription2);
//                    }
*/
/*
                    if(getNumberOfListeners(_EXCHANGE_PUSH_ORDERBOOK_EVENT) > 0){
                        Disposable _subscription = _streamingMarketDataService
                        .getOrderBook(_currencyPair)
                        .subscribe(orderBook -> {
//                        	debug.outln("subscribe.orderBook...");
							pushEvent(Event.EXCHANGE_PUSH_ORDERBOOK, _currencyPair, orderBook);
    //                                            debug.outln("Incoming orderBook: {}"+ orderBook);
                        }, _throwable -> {
							debug.outln("["+this+"]["+_currencyPair+"]: Error in subscribing orderBooks." + _throwable);
                        });
                        subscriptions.add(_subscription);
                    }
*/
                });	 
	}   
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void pushEvent(int _eventId, CurrencyPair _currencyPair, Object _object){
    	Event _event = new Event(_eventId, _currencyPair, _object);
//		debug.outln("StreamingExchangeX.pushEvent0:"+ this +" : " + _event);	        
    	queue.put(_event);
    }

//-------------------------------------------------------------------------------------
    private void flushQueue(){
    	while(true){
	    	try{
	        	Event _event = queue.take();
//        		debug.outln("StreamingExchangeX.pushEvent1:"+ this +" : " + _event);	        
	        	pushEvent(_event);
	    	}catch(InterruptedException _e){ 
	    		return;
	    	}
    	}
	}

//-------------------------------------------------------------------------------------
    public void printThrowable(Throwable _throwable){
        if(_throwable == null){
            debug.outln("_throwable=null");
        } else {
            debug.outln(_throwable.getMessage());
            _throwable.printStackTrace();
        }
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
    public boolean isEnabled(){
        return configuration.isEnabled();
    }

//-------------------------------------------------------------------------------------
    public List<String> getExchangesInfo() {
        List<String> _list = new ArrayList<String>();
//        _list.add();
        _list.addAll(configuration.getExchangesInfo());
        return _list;
    }

//-------------------------------------------------------------------------------------
	public void waitUntil(int _stateWaitedFor){
		while(state < _stateWaitedFor){
			synchronized(oLock){
				try{
					oLock.wait();
				} catch(InterruptedException _e){
					return;
				}
			}
		}
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
    public void initiate() {
// 		debug.outln("StreamingExchange.init:"+configuration.getShortName()+" ...");
    	super.initiate();
//        if(configuration.isEnabled()){
//		debug.outln("Exchange.initiate ....2");
        	Thread _thread = new Thread(this);
            _thread.start();
//        }
    	
    	startStreams = true;
    }

//-------------------------------------------------------------------------------------
    public void terminate() {
//		debug.outln("StreamingExchangeX terminate...0");
    	super.terminate();
        // Unsubscribe from data order book.
        for (int i = 0; i < subscriptions.size() ; i++) {
            subscriptions.get(i).dispose();            
        }
        if(streamingExchange != null){
            // Disconnect from exchange (non-blocking)
            streamingExchange.disconnect().subscribe(() -> debug.outln("Disconnected from the StreamingExchangeX"));
        }
//		debug.outln("StreamingExchangeX terminate...1");
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "StreamingExchangeX:" + 
		configuration.getShortName();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
