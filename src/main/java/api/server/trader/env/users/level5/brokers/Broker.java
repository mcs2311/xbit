//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level5.brokers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.meta.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;


import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.output.*;
import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;


import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;
//import codex.xbit.api.server.trader.env.users.level3.agents.helpers.pricemanager.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers.*;
import codex.xbit.api.server.trader.env.users.level5.brokers.brokerorderscontainer.*;

//-------------------------------------------------------------------------------------
public class Broker extends AbstractEntity<Configuration> {
	private PilotConfiguration pilotConfiguration;
	private TacticConfiguration tacticConfiguration;
    private CurrencyPair currencyPair;
    private String exchange;
    private int type;

	private List<Map<String, String>> signalsNames;


	private BrokerOrdersContainer brokerOrdersContainer;
	private int maxNumberOfOpenDealsPerPair;


//	private Scales scales;
    private MarketModel marketModel;
//    private PriceManager priceManager;

    //---cache:
	private TraderConfiguration traderConfiguration;
	private ShadowBalance shadowBalance;

    //---statics:


//-------------------------------------------------------------------------------------
    public Broker(Debug _debug, Resolver _resolver, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair, String _exchange, int _type) {
        super(_debug, _resolver, null, null);
        tacticConfiguration = _tacticConfiguration;
		currencyPair = _currencyPair;
		exchange = _exchange;
		type = _type;
  		
  		signalsNames = _tacticConfiguration.getSignals();
//		debug.outln(Debug.INFO, "Broker signalsNames=["+signalsNames+"]...");
  		pilotConfiguration = _resolver.getPilotConfiguration();
		traderConfiguration = _resolver.getTraderConfiguration();

		maxNumberOfOpenDealsPerPair = _tacticConfiguration.getMaxNumberOfOpenDealsPerPair();

		load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public synchronized void onNextOrdersEvent(UserOrdersEvent _userOrdersEvent){
    	int _type = _userOrdersEvent.getType();
		debug.outln(Debug.INFO, "Broker.onNextOrdersEvent..." + _type);
    	switch(_type){
    		case UserOrdersEvent.ORDERS_UPDATE: {
//			case UserOrdersEvent.ORDERS_DETECTED_NEW:
//    		case UserOrdersEvent.ORDERS_NUMBER_OF_OPEN_DEALS_CHANGED: {
		    	int _numberOfOpenDealsPerPair = _userOrdersEvent.getNumberOfOpenDeals();
				debug.outln(Debug.INFO, "Broker._numberOfOpenDealsPerPair=" + _numberOfOpenDealsPerPair);
		    	setNumberOfOpenDealsPerPair(_numberOfOpenDealsPerPair);
		    	break;
    		}
    		default: {
				debug.outln(Debug.ERROR, "Unknown UserOrdersEvent in Broker.onNextOrdersEvent.type=" + _type);
				break;
			}
    	}
    }
*/


//-------------------------------------------------------------------------------------
    public synchronized void onNextPilotEvent(PilotEvent _pilotEvent){
    	OrderX _currentOrder = null;

    	switch(_pilotEvent.getType()){
    		case PilotEvent.PILOT_BEAR_END: 
    		case PilotEvent.PILOT_BULL_BEGIN: {
				//debug.outln(Debug.INFO, "WFS:" + _signalEvent);
				if(type == OrderX.TYPE_BUY){
					brokerOrdersContainer.attemptToCancelAll();
				} else if(type == OrderX.TYPE_SELL){
					debug.outln(Debug.INFO, "WFS.pilot:" + _pilotEvent);
//					if(traderConfiguration.isNotBearish()){
					if(isNotManualBearish()){
						_currentOrder = fetchBuyOrder(_pilotEvent);
						if(_currentOrder != null){
							if(checkForFunds(_currentOrder)){
								if(_currentOrder.shallIBuy(_pilotEvent)){
		    						debug.outln(Debug.WARNING, "Broker...deciding to buy...");
									brokerOrdersContainer.sendOrderToOrderBook();
								}
							}
						}
					}
				}
    			break;
    		} 
    		case PilotEvent.PILOT_BEAR_BEGIN: 
    		case PilotEvent.PILOT_BULL_END: {
				//debug.outln(Debug.INFO, "WFS: " + _signalEvent);
				if(type == OrderX.TYPE_SELL){
					brokerOrdersContainer.attemptToCancelAll();
				} else if(type == OrderX.TYPE_BUY){
					debug.outln(Debug.INFO, "WFS.pilot:" + _pilotEvent);
//					if(traderConfiguration.isNotBullish()){
					if(isNotManualBullish()){
						_currentOrder = fetchSellOrder(_pilotEvent);
						if(_currentOrder != null){
							if(checkForFunds(_currentOrder)){
								if(_currentOrder.shallISell(_pilotEvent)){
		    						debug.outln(Debug.WARNING, "Broker...deciding to sell...");
									brokerOrdersContainer.sendOrderToOrderBook();
								}
							}
						}
					}
				}
    			break;
    		} 
			default: {
				debug.outln(Debug.ERROR, "Unknown event in Broker: " + _pilotEvent.getName());
				break;
			}
		}
    }


//-------------------------------------------------------------------------------------
    public synchronized void onNextSignalEvent(SignalEvent _signalEvent){
    	OrderX _currentOrder = null;

    	switch(_signalEvent.getType()){
    		case SignalEvent.SIGNAL_BUY: {
				//debug.outln(Debug.INFO, "WFS:" + _signalEvent);
				if(type == OrderX.TYPE_BUY){
					brokerOrdersContainer.attemptToCancelAll();
				} else if(type == OrderX.TYPE_SELL){
					debug.outln(Debug.INFO, "WFS.signal:" + _signalEvent);
					if(traderConfiguration.isNotBearish()){
//					if(isNotManualBearish()){
						_currentOrder = fetchBuyOrder(_signalEvent);
						if(_currentOrder != null){
							if(checkForFunds(_currentOrder)){
								if(_currentOrder.shallIBuy(_signalEvent)){
		    						debug.outln(Debug.WARNING, "Broker...deciding to buy...");
									brokerOrdersContainer.sendOrderToOrderBook();
								}
							}
						}
					}
				}
    			break;
    		} 
    		case SignalEvent.SIGNAL_SELL: {
				//debug.outln(Debug.INFO, "WFS: " + _signalEvent);
				if(type == OrderX.TYPE_SELL){
					brokerOrdersContainer.attemptToCancelAll();
				} else if(type == OrderX.TYPE_BUY){
					debug.outln(Debug.INFO, "WFS.signal:" + _signalEvent);
					if(traderConfiguration.isNotBullish()){
//					if(isNotManualBullish()){
						_currentOrder = fetchSellOrder(_signalEvent);
						if(_currentOrder != null){
							if(checkForFunds(_currentOrder)){
								if(_currentOrder.shallISell(_signalEvent)){
		    						debug.outln(Debug.WARNING, "Broker...deciding to sell...");
									brokerOrdersContainer.sendOrderToOrderBook();
								}
							}
						}
					}
				}
    			break;
    		} 
			default: {
				debug.outln(Debug.ERROR, "Unknown event in Broker: " + _signalEvent.getName());
				break;
			}
		}
    }

//-------------------------------------------------------------------------------------
    public OrderX fetchBuyOrder(Event _event){
		int _numberOfOpenDealsPerPair = brokerOrdersContainer.getNumberOfOpenDealsPerPair();
		debug.outln(Debug.WARNING, "Number of open deals = " + _numberOfOpenDealsPerPair + ", maxNumberOfOpenDealsPerPair="+maxNumberOfOpenDealsPerPair);
		if(_numberOfOpenDealsPerPair >= maxNumberOfOpenDealsPerPair){
				return null;
		}
		OrderX _currentOrder = brokerOrdersContainer.getCurrentOrder();
		if(_currentOrder == null){
			_currentOrder = brokerOrdersContainer.generateNewOrder();
			if(_currentOrder == null){
				return null;
			}
		} 
//		OrderX _currentOrder = brokerOrdersContainer.generateNewOrder();
		debug.outln(Debug.WARNING, "Broker.fetchBuyOrder..._currentOrder="+_currentOrder);
//		priceManager.setOrder(_currentOrder);

		return _currentOrder;
	}

//-------------------------------------------------------------------------------------
    public OrderX fetchSellOrder(Event _event){
		OrderX _currentOrder = brokerOrdersContainer.getCurrentOrder();
		debug.outln(Debug.WARNING, "Broker.fetchSellOrder..._currentOrder="+_currentOrder);
		if(_currentOrder == null){
			return null;
		} 
//		priceManager.setOrder(_currentOrder);
		return _currentOrder;
	}

//-------------------------------------------------------------------------------------
	private boolean checkForFunds(OrderX _order){
		BigDecimal _orderAmount = _order.getAmount();
		if(_order.getType() == AbstractOrder.TYPE_BUY){
			_orderAmount = marketModel.getQuote(_orderAmount);
		}
		boolean _success = shadowBalance.check(_orderAmount);;
		debug.outln(Debug.IMPORTANT4, "Broker.checkForFunds: shadowBalance="+shadowBalance+", _orderAmount=" + _orderAmount+", _success=" + _success);
		return _success;
	}

//-------------------------------------------------------------------------------------
	private boolean isNotManualBearish(){
		return !((pilotConfiguration.getMode() == PilotConfiguration.MODE_MANUAL) && 
			traderConfiguration.isBearish());
	}

//-------------------------------------------------------------------------------------
	private boolean isNotManualBullish(){
		return !((pilotConfiguration.getMode() == PilotConfiguration.MODE_MANUAL) && 
			traderConfiguration.isBullish());
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
//		debug.outln(Debug.INFO, "Broker.load...");
    	super.load();
    	UserExchange _userExchange = resolver.getUserExchanges()
    			.getUserExchange(exchange);

    	org.knowm.xchange.currency.Currency _currency = (type == OrderX.TYPE_BUY) ? currencyPair.base : currencyPair.counter;

    	shadowBalance = resolver.getShadowBalances()
    			.getShadowBalance(exchange, _currency);


    	Scales _scales = _userExchange.getScales(currencyPair);

    	MarketModels _marketModels = resolver.getMarketModels();
    	marketModel = _marketModels.getMarketModel(currencyPair);
		brokerOrdersContainer = new BrokerOrdersContainer(debug, resolver, marketModel, _scales, tacticConfiguration, currencyPair, exchange, type);

/*
        if(type == OrderX.TYPE_BUY){
			priceManager = new AskPriceManager(debug, resolver, marketModel, scales, tacticConfiguration, null, (float)0.10);        	
        } else {
			priceManager = new BidPriceManager(debug, resolver, marketModel, scales, tacticConfiguration, null, (float)0.10);
        }
*/

/*
    	SidesOrdersContainer _typesOrdersContainer =  resolver.getOrderbooks()
    			.getOrderbook(currencyPair, exchange)
    			.getSides()
    			.getSidesOrdersContainer();

				setNumberOfOpenDealsPerPair(_typesOrdersContainer.calculateNumberOfOpenDeals());

    			_typesOrdersContainer.getOrdersEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_userOrdersEvent -> onNextOrdersEvent(_userOrdersEvent),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

*/
/*
    	resolver.getShadowBalances()
    			.getShadowBalance(_userExchange, currencyPair.counter)
    			.getShadowBalance()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_amountCounter -> onNextAmountCounter(_amountCounter),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());

    	resolver.getShadowBalances()
    			.getShadowBalance(_userExchange, currencyPair.base)
    			.getShadowBalance()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_amountBase -> onNextAmountBase(_amountBase),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());
*/

    	List<Signal> _signals = resolver.getSignals()
    			.getSignals(currencyPair, signalsNames);
				
				_signals.forEach(_signal -> {
					debug.outln(Debug.INFO, "Registering broker for signal =["+_signal+"]...");
					_signal.getSignalEvent()
					.subscribeOn(Schedulers.computation())
	    			.observeOn(Schedulers.newThread(), false)
	    			.subscribe(_signalEvent -> onNextSignalEvent(_signalEvent),
	    						_throwable -> onError(_throwable),
	        					() -> onCompleted());
	    		});

    	resolver.getPilots()
    			.getPilotEvent()
    			.subscribeOn(Schedulers.io())
    			.observeOn(Schedulers.computation(), false)
    			.subscribe(_amountBase -> onNextPilotEvent(_amountBase),
    						_throwable -> onError(_throwable),
        					() -> onCompleted());	    		
    }


//-------------------------------------------------------------------------------------
    public void save() {
//		debug.outln(Debug.INFO, "Broker save...");
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Broker:" + 
		currencyPair +
		" , " + 
		signalsNames;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
