//-------------------------------------------------------------------------------------
package codex.xbit.api.common.datas;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import java.time.format.DateTimeFormatter;

import org.knowm.xchange.currency.Currency;
import com.fasterxml.jackson.annotation.*;


import codex.common.utils.*;

//import codex.xbit.api.server.trader.core.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class WhaleAlertTransactionData extends AbstractData {
    private String blockchain;
    private String symbol;
    private String id;
    private String transaction_type;
    private Address from;
    private Address to;
    private long timestamp;
    private double amount;
    private long amount_usd;
    private int transaction_count;


    private int type;
    private org.knowm.xchange.currency.Currency currency;

    
    //---statics:
    public static final int TYPE_INCOMING = 1;
    public static final int TYPE_OUTGOING = 2;
    public static final int TYPE_UNKNOWN = 3;
    public static final int TYPE_EXCHANGE = 4;
	public static final int TYPES[] = {TYPE_INCOMING, TYPE_OUTGOING, TYPE_UNKNOWN, TYPE_EXCHANGE}; 

	public static DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("dd.HH:mm:ss.SSS");

//-------------------------------------------------------------------------------------
    public WhaleAlertTransactionData() {
    }

//-------------------------------------------------------------------------------------
    public String getBlockchain() {
        return blockchain;
    }

//-------------------------------------------------------------------------------------
    public String getSymbol() {
        return symbol;
    }

//-------------------------------------------------------------------------------------
    public String getIdAsString() {
        return id;
    }

//-------------------------------------------------------------------------------------
    public String getTransactionType() {
        return transaction_type;
    }

//-------------------------------------------------------------------------------------
    public Address getFrom() {
        return from;
    }

//-------------------------------------------------------------------------------------
    public Address getTo() {
        return to;
    }

//-------------------------------------------------------------------------------------
    public long getTimestamp() {
        return timestamp;
    }

//-------------------------------------------------------------------------------------
    public String getTimeAsString() {
        return TimeUtils.getTime(timestamp, formatter);
    }

//-------------------------------------------------------------------------------------
    public double getAmount() {
        return amount;
    }

//-------------------------------------------------------------------------------------
    public long getAmountUsd() {
        return amount_usd;
    }

//-------------------------------------------------------------------------------------
    public int getTransactionCount() {
        return transaction_count;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
    	return type;
    }

//-------------------------------------------------------------------------------------
    public String getTypeAsString() {
    	return convertIntTypeToString(type);
    }

//-------------------------------------------------------------------------------------
    public org.knowm.xchange.currency.Currency getCurrency() {
    	return currency;
    }

//-------------------------------------------------------------------------------------
	public List<Object> getAsArrayList() {
//"currency", "type", "from", "to", "time", "amount_usd", "transaction_count"
		ArrayList<Object> _list = new ArrayList<Object>(Arrays.asList(currency.toString(), getTypeAsString(), from.toString(), to.toString(), getTimeAsString(), Long.valueOf(amount_usd), Integer.valueOf(transaction_count)));
		return (List<Object>)(_list);
	}

//-------------------------------------------------------------------------------------
    public static String convertIntTypeToString(int _type) {
    	switch(_type){
    		case TYPE_INCOMING: return "in";
    		case TYPE_OUTGOING: return "out";
    		case TYPE_UNKNOWN:  return "unk";
    		case TYPE_EXCHANGE:  return "ex";
    		default: return "err";
    	}
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void pack() {
        if(!from.isExchange() && !to.isExchange()){
        	type = TYPE_UNKNOWN;
        } else if(!from.isExchange() && to.isExchange()){
        	type = TYPE_INCOMING;
		} else if(from.isExchange() && !to.isExchange()){
        	type = TYPE_OUTGOING;
        } else {
        	type = TYPE_EXCHANGE;
        }
    	String _symbol = getSymbol();
    	if(_symbol.startsWith("usd")){
    		_symbol = "usd";
    	}
        currency = new org.knowm.xchange.currency.Currency(_symbol);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
        return null;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
