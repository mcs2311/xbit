//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level4.marketmodels;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

//import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.input.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;
import codex.xbit.api.server.trader.env.single.level4.marketmodels.helpers.*;

//import codex.xbit.api.server.trader.env.users.level2.orders.*;

//-------------------------------------------------------------------------------------
public class MarketModel extends AbstractEntity<Configuration> implements FlowableOnSubscribe<MarketModelEvent> {
    private CurrencyPair currencyPair;

	private TradeMarketModel tradeMarketModel;
	private TickerMarketModel tickerMarketModel;
	private OrderbookMarketModel orderbookMarketModel;


	private int eventCounter;
	private MarketModelEvent lastMarketModelEvent;

    //---rx:
	private Flowable<MarketModelEvent> flowable;
	private FlowableEmitter<MarketModelEvent> emitter;


    //---cache:

    //---statics:

//-------------------------------------------------------------------------------------
    public MarketModel(Debug _debug, Resolver _resolver, CurrencyPair _currencyPair) {
        super(_debug, _resolver, null, null);

//        exchange = resolver.getInfluxExchanges().getExchange(_name);
		currencyPair = _currencyPair;
//		type = _type;

    	flowable = Flowable.create(this, BackpressureStrategy.DROP)
    				.share();
//    			.onBackpressureBuffer(16, () -> { },
//              		BackpressureOverflowStrategy.DROP_OLDEST);


    	debug.outln(Debug.INFO, "MarketModel started for  : "+currencyPair + " ...");
/*
        if(type == AbstractOrder.TYPE_BUY){
			marketWisdom = new BidMarketWisdom(_debug);
        } else{
			marketWisdom = new AskMarketWisdom(_debug);
        }
*/

		tradeMarketModel = new TradeMarketModel(_debug);
		tickerMarketModel = new TickerMarketModel(_debug);
		orderbookMarketModel = new OrderbookMarketModel(_debug);


//    	InputRepositories _inputRepositories = resolver.getRepositories().getInputRepositories();//.getRepository(currencyPair);
/*    	if(repositoryTicker != null){
    		Event _event = new Event(Event.EXCHANGE_PUSH_TICKER, currencyPair);
    		repositoryTicker.register(_event, this, AbstractEventPusher.PRIORITY_HIGH);
    	} else {
    		debug.outln(Debug.WARNING, "MarketModel could not register to "+currencyPair+", type="+type);
    	}
*/
    	eventCounter = 0;
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*	public InfluxExchange getExchange(){
		return exchange;
	}
*/
//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPair(){
		return currencyPair;
	}

//-------------------------------------------------------------------------------------
	public BigDecimal getQuote(BigDecimal _baseAmount){
//		BigDecimal _bidPrice = lastAskPrice
//		return _baseAmount.divide(lastAskPrice, RoundingMode.HALF_UP);
		return _baseAmount.multiply(getLastAskPrice());
	}

//-------------------------------------------------------------------------------------
	public BigDecimal getLastAskPrice(){
		return tickerMarketModel.getLastAskPrice();
	}

//-------------------------------------------------------------------------------------
	public BigDecimal getLastBidPrice(){
		return tickerMarketModel.getLastBidPrice();
	}

//-------------------------------------------------------------------------------------
	public int getDirection(){
		if(lastMarketModelEvent != null){
			return lastMarketModelEvent.getDirection();
		} else {
			return MarketModelEvent.MARKET_STALL;
		}
	}

//-------------------------------------------------------------------------------------
	public float getConfidence(){
		if(lastMarketModelEvent != null){
			return lastMarketModelEvent.getConfidence();
		} else {
			return 0f;
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	@Override
	public void subscribe(FlowableEmitter<MarketModelEvent> _emitter) throws Exception {
		emitter = _emitter;
    }

//-------------------------------------------------------------------------------------
	public Flowable<MarketModelEvent> getMarketModelEvent() {
		return flowable;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private synchronized void onNextTrade(BeanEvent _beanEvent){
    	TradeEvent _tradeEvent = (TradeEvent)_beanEvent;

		MarketModelEvent _marketModelEvent = tradeMarketModel.onNextTrade(_tradeEvent);

		sendMarketModelEvent(_marketModelEvent);
	}

//-------------------------------------------------------------------------------------
    private synchronized void onNextTicker(BeanEvent _beanEvent){
    	TickerEvent _tickerEvent = (TickerEvent)_beanEvent;

		MarketModelEvent _marketModelEvent = tickerMarketModel.onNextTicker(_tickerEvent);

		sendMarketModelEvent(_marketModelEvent);
	}

//-------------------------------------------------------------------------------------
    private synchronized void onNextOrderbook(BeanEvent _beanEvent){
    	OrderbookEvent _orderbookEvent = (OrderbookEvent)_beanEvent;

		MarketModelEvent _marketModelEvent = orderbookMarketModel.onNextOrderbook(_orderbookEvent);

		sendMarketModelEvent(_marketModelEvent);
	}

//-------------------------------------------------------------------------------------
    private void sendMarketModelEvent(MarketModelEvent _marketModelEvent){
		if(_marketModelEvent != null){
			lastMarketModelEvent = _marketModelEvent;

			if(emitter != null){
				if(eventCounter > 200){
					if(_marketModelEvent.getConfidence() > 0.75f){
						debug.outln(">>>["+currencyPair+"]" + _marketModelEvent);
					}
					emitter.onNext(_marketModelEvent);
				}
				eventCounter++;
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	InputRepositories _inputRepositories = resolver
				.getRepositories()
				.getInputRepositories();

    		_inputRepositories
    			.getRepository(currencyPair, "trade")
    			.getBean()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation(), false)
    			.subscribe(_beanEvent -> onNextTrade(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

			_inputRepositories
				.getRepository(currencyPair, "ticker")
    			.getBean()
				.subscribeOn(Schedulers.io())
				.observeOn(Schedulers.computation(), false)
    			.subscribe(_beanEvent -> onNextTicker(_beanEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

    		CurrencyPair _currencyPair;
    		if(currencyPair.toString().equals("ETH/USDT")){
    			_currencyPair = new CurrencyPair("ETH/USD");
    		} else if(currencyPair.toString().equals("BTC/USDT")){
    			_currencyPair = new CurrencyPair("BTC/USD");
    		} else {
    			_currencyPair = currencyPair;
    		}
    		OutputRepository _outputRepository = 
    			(OutputRepository)resolver.getRepositories()
		    	.getOutputRepositories()
		    	.getRepository(_currencyPair, "orderbook", "instant");
		    	if(_outputRepository != null){
					_outputRepository.getBean()
					.subscribeOn(Schedulers.io())
					.observeOn(Schedulers.computation(), false)
					.subscribe(_orderbookEvent -> onNextOrderbook(_orderbookEvent),
								_throwable -> onError(_throwable),
								() -> onCompleted());
		    	} else {
    				debug.outln(Debug.ERROR, "Cannot find OrderbookRepository for currencyPair: " + currencyPair);
		    	}
	}

//-------------------------------------------------------------------------------------
    public void save() {
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "MarketModel:" + 
		currencyPair;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
