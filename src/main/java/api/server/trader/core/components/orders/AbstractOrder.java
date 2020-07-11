//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components.orders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public abstract class AbstractOrder extends AbstractEntity<Configuration> {
//	protected int id;
	protected String exchange;
	protected CurrencyPair currencyPair;
	protected long time;
	protected BigDecimal price;
	protected BigDecimal amount;
	protected int type;
	protected int position;

	protected Scales scales;

	//---cache:

	//---statics:
	public static final int TYPE_ALL 	= -1;
	public static final int TYPE_BUY  	= 0;
	public static final int TYPE_SELL 	= 1;


	public static final int POSITION_ALL 	= -1;
	public static final int POSITION_LONG  	= 0;
	public static final int POSITION_SHORT 	= 1;


//-------------------------------------------------------------------------------------
    public AbstractOrder(Debug _debug, Resolver _resolver, AbstractOrder _order) {
		this(_debug, 
			_resolver, 
			_order.id, 
			_order.exchange,
			_order.currencyPair,
			_order.time,
			_order.price,
			_order.amount,
			_order.type,
			_order.position);
    }

//-------------------------------------------------------------------------------------
    public AbstractOrder(Debug _debug, Resolver _resolver, long _id, String _exchange, CurrencyPair _currencyPair, long _time, BigDecimal _price, BigDecimal _amount, int _type, int _position) {
		super(_debug, _resolver);
		id = _id;
        exchange = _exchange;
        currencyPair = _currencyPair;
        time = _time;
        price = _price;
        amount = _amount;
        type = _type;
        position = _position;
    }

//-------------------------------------------------------------------------------------
    public String getIdAsString() {
        return StringUtils.getLongAsString(id);
//        return StringUtils.getHex((int)id);
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
    public void setTime(long _time) {
        time = _time;
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
        return convertTypeFromInt(type);
    }

//-------------------------------------------------------------------------------------
    public int getPosition() {
        return position;
    }

//-------------------------------------------------------------------------------------
    public String getPositionAsString() {
        return convertPositionFromInt(position);
    }

//-------------------------------------------------------------------------------------
    public void setPosition(int _position) {
        position = _position;
    }

//-------------------------------------------------------------------------------------
    public Scales getScales() {
        return scales;
    }

//-------------------------------------------------------------------------------------
    public void setScales(Scales _scales) {
        scales = _scales;
		price = price.setScale(_scales.getPrice(), RoundingMode.HALF_UP);
		amount = amount.setScale(_scales.getAmount(), RoundingMode.HALF_UP);
    }
//-------------------------------------------------------------------------------------
    public void resetScales() {
		price = price.setScale(scales.getPrice(), RoundingMode.HALF_UP);
		amount = amount.setScale(scales.getAmount(), RoundingMode.HALF_UP);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void updateChanges(Order _order){
//		id = 0L;//_order.getId();
//        currencyPair = _order.getCurrencyPair();
        price = _order.getAveragePrice();
        amount = _order.getRemainingAmount();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public static int getReverseType(int _type) {
        switch(_type){
        	case TYPE_BUY: return TYPE_SELL;
        	case TYPE_SELL: return TYPE_BUY;
        	default: return -1;
        }
	}

//-------------------------------------------------------------------------------------
    public static String convertTypeFromInt(int _type) {
        switch(_type){
        	case TYPE_BUY: return "B";
        	case TYPE_SELL: return "S";
        	default: return "ERROR";
        }
	}

//-------------------------------------------------------------------------------------
    public static String convertTypeFromIntLongForm(int _type) {
        switch(_type){
        	case TYPE_BUY: return "BUY";
        	case TYPE_SELL: return "SELL";
        	case TYPE_ALL: return "ALL";
        	default: return "ERROR";
        }
	}

//-------------------------------------------------------------------------------------
    public static int convertTypeFromString(String _type) {
        switch(_type){
        	case "B": return TYPE_BUY;
        	case "S": return TYPE_SELL;
        	default: return -1;
        }
	}

//-------------------------------------------------------------------------------------
    public static String convertPositionFromInt(int _position) {
        switch(_position){
        	case POSITION_SHORT : return "S";
        	case POSITION_LONG	: return "L";
        	default: return "ERROR";
        }
	}

//-------------------------------------------------------------------------------------
    public static String convertPositionFromIntLongForm(int _position) {
        switch(_position){
        	case POSITION_SHORT	: return "SHORT";
        	case POSITION_LONG 	: return "LONG";
        	case POSITION_ALL 	: return "ALL";
        	default: return "ERROR";
        }
	}

//-------------------------------------------------------------------------------------
    public static int convertPositionFromString(String _position) {
        switch(_position){
        	case "S": return POSITION_SHORT;
        	case "L": return POSITION_LONG;
        	default: return -1;
        }
	}


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Order: "+id+", "+time+", "+exchange+", "+currencyPair+", "+price+", "+amount+", "+convertPositionFromInt(type)+", "+convertPositionFromInt(position);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
