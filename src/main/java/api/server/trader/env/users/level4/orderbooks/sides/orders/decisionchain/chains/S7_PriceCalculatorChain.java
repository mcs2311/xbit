//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.Order;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;

import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;

//-------------------------------------------------------------------------------------
public class S7_PriceCalculatorChain {
	protected Debug debug;
	protected int type;

    //---cache:
	protected float priceIncrementValue;
    protected int counter;

    //---cache:

    //---statics:
    private static final float PRICE_INCREMENT_PROCENT = 0.2f;

//-------------------------------------------------------------------------------------
    public S7_PriceCalculatorChain(Debug _debug, int _type) {
    	debug = _debug;
    	type = _type;

    	priceIncrementValue = 0.0f;
    	counter = 0;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public float price(Event _event, float _newPrice, float _wiseness){
//		debug.outln(Debug.ERROR, "filter: _traderState=" + _traderState + ", _source=" + _source + ", _confidence=" + _confidence + ", _wiseness=" + _wiseness);
    	if(counter%1000 == 0){
    		calculatePriceIncrementValue(_newPrice);
    	}
		counter++;

    	if(type == AbstractOrder.TYPE_BUY){//Order.OrderType.ASK){
    		return priceBid(_event, _newPrice, _wiseness);
    	} else {
    		return priceAsk(_event, _newPrice, _wiseness);    		
    	}
    }

//-------------------------------------------------------------------------------------
    private float priceBid(Event _event, float _newPrice, float _wiseness) {

		float _currentPriceIncrementValue = priceIncrementValue * Math.abs(((float)1.0 - _wiseness));
		float _newPrice1 = _newPrice - _currentPriceIncrementValue;
    	debug.outln(Debug.INFO, "%%%%->_newPrice1=" + _newPrice1+", _newPrice="+_newPrice+", _wiseness="+_wiseness);

    	return _newPrice1;
	}

//-------------------------------------------------------------------------------------
    private float priceAsk(Event _event, float _newPrice, float _wiseness) {

		float _currentPriceIncrementValue = priceIncrementValue * Math.abs(((float)1.0 - _wiseness));
		float _newPrice1 = _newPrice + _currentPriceIncrementValue;
//    	debug.outln(Debug.INFO, "%%%%->_newPrice=" + _newPrice);

    	return _newPrice1;
	}

//-------------------------------------------------------------------------------------
    protected void calculatePriceIncrementValue(float _newPrice) {
    	if(_newPrice > 0.0f){
    		priceIncrementValue = (_newPrice * PRICE_INCREMENT_PROCENT) / 100;
    	}
//		debug.outln(Debug.WARNING, "JumperPriceCalculatorHelper.calculatePriceIncrementValue=" + priceIncrementValue);
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
