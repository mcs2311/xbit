//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level5.brokers.brokerorderscontainer;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.disposables.*;
import io.reactivex.schedulers.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.Order;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;


import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.sidesorderscontainers.*;

//-------------------------------------------------------------------------------------
public class BrokerOrdersContainer extends AbstractCluster<Configuration, OrderX> {
    private MarketModel marketModel;
    private Scales scales;
	private TacticConfiguration tacticConfiguration;
    private CurrencyPair currencyPair;
    private String exchange;
    private int type;

//    private boolean thereIsAChangeInOrderBook;
	private OrderX currentOrder;

	private codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.scheduler.Scheduler scheduler;
	private Sides sides;

	private int numberOfOpenDealsPerPair;
	private BigDecimal lastNewGeneratedOrderAmount;

    //---cache:
    protected BigDecimal maximumAmount, initialAmount, incrementingAmount, minimumAmount;
//	private Scales scales;

    //---statics:

//-------------------------------------------------------------------------------------
    public BrokerOrdersContainer(Debug _debug, Resolver _resolver, MarketModel _marketModel, Scales _scales, TacticConfiguration _tacticConfiguration, CurrencyPair _currencyPair, String _exchange, int _type) {
        super(_debug, _resolver);
        marketModel = _marketModel;
        scales = _scales;
        tacticConfiguration = _tacticConfiguration;
		currencyPair = _currencyPair;
		exchange = _exchange;
		type = _type;

//		thereIsAChangeInOrderBook = true;
		currentOrder = null;

        org.knowm.xchange.currency.Currency _currency = currencyPair.base;
        Map<org.knowm.xchange.currency.Currency, List<BigDecimal>> _amountsMap = _tacticConfiguration.getAmounts();
//        debug.out(Debug.IMPORTANT3, "_amountsMap: "+_amountsMap);
        if(_amountsMap != null){
	        List<BigDecimal> _amounts = _amountsMap.get(_currency);
        	if(_amounts != null){
	//        	debug.out(Debug.IMPORTANT3, "_amounts for  "+_currency+": "+_amounts);
	//#maximumAmount, initialAmount, incrementingAmount, minimumAmount
				maximumAmount = _amounts.get(0);
				initialAmount = _amounts.get(1);
				incrementingAmount = _amounts.get(2);
				minimumAmount = _amounts.get(3);
        	} else {
        		loadDefaultAmountsMap();
        	}
        } else {
        	loadDefaultAmountsMap();
		}
//    	flowable = Flowable.create(this, BackpressureStrategy.BUFFER);
//		lastAmountForNewOrders = initialAmount;
		setNumberOfOpenDealsPerPair(Integer.MAX_VALUE);
		lastNewGeneratedOrderAmount = BigDecimal.ZERO;
		load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public synchronized void onNextOrdersEvent(UserOrdersEvent _userOrdersEvent){
    	if(_userOrdersEvent.isALockSwitch()){
    		return;
    	}
    	int _numberOfOpenDealsPerPair = _userOrdersEvent.getNumberOfOpenDeals();
		debug.outln(Debug.INFO, "BrokerOrdersContainer.onNextOrdersEvent..."+_userOrdersEvent + ", _numberOfOpenDealsPerPair=" + _numberOfOpenDealsPerPair);
//		OrderX _currentOrder = brokerOrdersContainer.getCurrentOrder();
//		debug.outln(Debug.INFO, "BrokerOrdersContainer._numberOfOpenDealsPerPair=" + _numberOfOpenDealsPerPair);
    	setNumberOfOpenDealsPerPair(_numberOfOpenDealsPerPair);
    	refreshCurrentOrder();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	private synchronized void setNumberOfOpenDealsPerPair(int _numberOfOpenDealsPerPair){
		numberOfOpenDealsPerPair = _numberOfOpenDealsPerPair;
	}

//-------------------------------------------------------------------------------------
	public synchronized int getNumberOfOpenDealsPerPair(){
		return numberOfOpenDealsPerPair;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized OrderX getCurrentOrder(){
		if((currentOrder == null) || 
			(currentOrder.isConsumed()) || 
			(currentOrder.isLocked())) {
				refreshCurrentOrder();
		}
		return currentOrder;
	}
/*
//-------------------------------------------------------------------------------------
    public OrderX openNewOrder(SignalEvent _signalEvent){
				return brokerOrdersContainer.generateNewOrder();
		} else {
			return null;
		}
	}
*/
//-------------------------------------------------------------------------------------
	public synchronized OrderX generateNewOrder(){
		long _id;
		long _time;
		BigDecimal _price, _amount;
		//---
		
		debug.outln(Debug.WARNING, "BrokerOrdersContainer.generateNewOrder...");
		_id = -1;
		_time = System.currentTimeMillis();
		_price = new BigDecimal(Integer.MAX_VALUE);
		if(lastNewGeneratedOrderAmount.equals(BigDecimal.ZERO)){
			_amount = initialAmount;
		} else {
			_amount = lastNewGeneratedOrderAmount.add(incrementingAmount);			
		}
		lastNewGeneratedOrderAmount = _amount;
//		initialAmount.add(incrementingAmount.multiply(BigDecimal.valueOf(_numberOfOpenDealsPerPair)));
//		lastAmountForNewOrders = _amount;
		currentOrder = new OrderX(debug, resolver, _id, exchange, currencyPair, _time, _price, _amount, AbstractOrder.getReverseType(type), AbstractOrder.POSITION_LONG);
		currentOrder.initiateDecisionChain(debug, marketModel, scales, tacticConfiguration);
//		currentOrder.setTacticConfiguration(tacticConfiguration);
		return currentOrder;
	}

//-------------------------------------------------------------------------------------
	public synchronized boolean sendOrderToOrderBook(){
//		incNumberOfOpenOrders();
		debug.outln(Debug.WARNING, "BrokerOrdersContainer.sendOrderToOrderBook...");
		scheduler.placeOrder(currentOrder);
//		thereIsAChangeInOrderBook = true;
		currentOrder = null;
/*		BigDecimal _baseAmount = currentOrder.getBaseAmount();
		shadowBalanceBase.lock(_baseAmount);

    	BigDecimal _amountInitial = _order.getAmount();
    	BigDecimal _amountAlreadySpent = _order.getAlreadySpent();//amountAlreadySpentFromOrder(_order);
    	BigDecimal _remainingToBeSpent = _amountInitial.subtract(_amountAlreadySpent);
    	BigDecimal _amount = _remainingToBeSpent.min(maximumAmount);

		OrderX _orderNew = _order.getOrderWithAmount(_amount);
		_orderNew.setBackwardPeer(_order.getId());
		schedulerInputQueue.addNewOrder(_orderNew);*/
		return false;
	}

//-------------------------------------------------------------------------------------
	private synchronized void refreshCurrentOrder(){
		OrderX _order = scheduler.nextOrder();
		debug.outln(Debug.WARNING, "BrokerOrdersContainer["+type+"].refreshCurrentOrder.0:");
		if(currentOrder != _order){
			replaceCurrentOrder(_order);
		}
	}


//-------------------------------------------------------------------------------------
	private void replaceCurrentOrder(OrderX _order){
		debug.outln(Debug.WARNING, "BrokerOrdersContainer["+type+"].refreshCurrentOrder.0:");
		if(currentOrder != null){
			currentOrder.unlockParentOrder();
		}
		debug.outln(Debug.WARNING, "BrokerOrdersContainer["+type+"].refreshCurrentOrder.2: " + _order);
		if(_order == null){
			currentOrder = null;
		} else {
			currentOrder = _order.getOrderWithReverseType();					
			BigDecimal _amountToConsume = _order.getAvailableToConsume();
			currentOrder.initiateDecisionChain(debug, marketModel, scales, tacticConfiguration);
			currentOrder.setAmount(_amountToConsume);
			currentOrder.setId(-1);
/*			if(currentOrder.getType() == AbstractOrder.TYPE_BUY){
				currentOrder.setPrice(BigDecimal.ZERO);
			}*/
//					currentOrder.setTacticConfiguration(tacticConfiguration);

			debug.out(Debug.ERROR, "Locking "+_amountToConsume + " out of "+_amountToConsume + " in order " + _order.getId() + "....");
			BigDecimal _amountLocked = _order.addToLocked(_amountToConsume);
			debug.outln(Debug.ERROR, " =  "+_amountLocked, false);
			if((_amountLocked == null) || (!_amountLocked.equals(_amountToConsume))){
				currentOrder = null;
			}
		}
	}	
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public synchronized void attemptToCancelAll(){
		List<OrderX> _orders = getEntities();
		for (int i = 0; i < _orders.size(); i++) {
			OrderX _order = _orders.get(i);
			_order.attemptToCancel();
/*			if(_order.attemptToCancel()){
				BigDecimal _counterAmount = currentOrder.getCounterAmount();
				shadowBalanceBase.unlock(_baseAmount);
			}*/
		}
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    private void chopOrderAndFillTheQueue(OrderX _order){
    	synchronized(_order){
//			debug.outln(Debug.INFO, "SchedulerOutputQueue.chopOrderAndFillTheQueue.0..._order="+_order);
	    	BigDecimal _amountInitial = _order.getAmount();
	    	BigDecimal _amountAlreadySpent = _order.getAlreadySpent();//amountAlreadySpentFromOrder(_order);
	    	BigDecimal _remainingToBeSpent = _amountInitial.subtract(_amountAlreadySpent);
	    	BigDecimal _amount = _remainingToBeSpent.min(maximumAmount);

			OrderX _orderNew = _order.getOrderWithAmount(_amount);
			_orderNew.setBackwardPeer(_order.getId());
			schedulerInputQueue.addNewOrder(_orderNew);
//			debug.outln(Debug.INFO, "SchedulerOutputQueue.chopOrderAndFillTheQueue.1..._amountInitial="+_amountInitial+", _amountAlreadySpent="+_amountAlreadySpent+", _remainingToBeSpent="+_remainingToBeSpent+", _orderNew="+_orderNew);
	//		_amount = _amount.add(incrementingAmount);
			_order.addToAlreadySpent(_amount);
    	}
    }
*/
    
//-------------------------------------------------------------------------------------
    private void loadDefaultAmountsMap() {
		debug.outln(Debug.ERROR, "Cannot find amounts map for currency: " + currencyPair);
		maximumAmount = new BigDecimal(Integer.MAX_VALUE);
		initialAmount = new BigDecimal(Integer.MIN_VALUE);
		incrementingAmount = new BigDecimal(Integer.MIN_VALUE);
		minimumAmount = BigDecimal.valueOf(0.000001);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {

		sides = resolver.getOrderbooks()
				.getOrderbook(currencyPair, exchange)
				.getSides();

		Side _type = sides.getSide(type);

		scheduler = _type.getScheduler();

		SidesOrdersContainer _typesOrdersContainer = sides.getSidesOrdersContainer();
    	setNumberOfOpenDealsPerPair(_typesOrdersContainer.calculateNumberOfOpenDeals());

		_typesOrdersContainer.getOrdersEvent()
		.subscribeOn(Schedulers.computation())
		.observeOn(Schedulers.computation(), false)
		.subscribe(_userOrdersEvent -> onNextOrdersEvent(_userOrdersEvent),
					_throwable -> onError(_throwable),
					() -> onCompleted());
    }

//-------------------------------------------------------------------------------------
    public void save() {
		StateOrder _parentOrder = (StateOrder)currentOrder.getParentOrder();
		if(_parentOrder != null){
			_parentOrder.removeFromLocked(currentOrder.getAmount());
		}
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "BrokerOrdersContainer:";
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
