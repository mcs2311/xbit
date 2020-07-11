//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

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
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.orders.*;

//-------------------------------------------------------------------------------------
public abstract class AbstractTrade extends AbstractEntity<Configuration> {
//	protected int id;
	protected String exchange;
	protected CurrencyPair currencyPair;
	protected long time;
	protected BigDecimal price;
	protected BigDecimal amount;
	protected int type;
	protected long orderId;

	//---cache:


//-------------------------------------------------------------------------------------
    public AbstractTrade(Debug _debug, Resolver _resolver, AbstractTrade _trade) {
		this(_debug, 
			_resolver, 
			_trade.id, 
			_trade.exchange,
			_trade.currencyPair,
			_trade.time,
			_trade.price,
			_trade.amount,
			_trade.type,
			_trade.orderId);
    }
/*
//-------------------------------------------------------------------------------------
    public AbstractTrade(Debug _debug, Resolver _resolver, AbstractTrade _trade) {
		this(_debug, 
			_resolver, 
			_trade.id, 
			_trade.exchange,
			_trade.currencyPair,
			_trade.time,
			_trade.price,
			_trade.amount,
			_trade.type,
			_trade.orderId);
    }
*/
//-------------------------------------------------------------------------------------
    public AbstractTrade(Debug _debug, Resolver _resolver, long _id, String _exchange, CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, long _orderId) {
		super(_debug, _resolver);
		id = _id;
        exchange = _exchange;
        currencyPair = _currencyPair;
        time = _time;
        price = _price;
        amount = _amount;
        type = _type;
        orderId = _orderId;
    }

//-------------------------------------------------------------------------------------
    public String getIdAsString() {
        return StringUtils.getHex((int)id);
    }

//-------------------------------------------------------------------------------------
    public String getExchange() {
        return exchange;
    }

//-------------------------------------------------------------------------------------
    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

//-------------------------------------------------------------------------------------
    public long getTime() {
        return time;
    }

//-------------------------------------------------------------------------------------
    public Date getTimestamp() {
        return new Date(time * 1000);
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getPrice() {
        return price;
    }

//-------------------------------------------------------------------------------------
    public void setPrice(BigDecimal _price) {
        price = _price;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getAmount() {
        return amount;
    }

//-------------------------------------------------------------------------------------
    public void setAmount(BigDecimal _amount) {
        amount = _amount;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsAString() {
        return AbstractOrder.convertTypeFromInt(type);
    }

//-------------------------------------------------------------------------------------
    public long getOrderId() {
        return orderId;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return " "+id+", "+time+", "+exchange+", "+currencyPair+", "+price+", "+amount+", "+type;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
