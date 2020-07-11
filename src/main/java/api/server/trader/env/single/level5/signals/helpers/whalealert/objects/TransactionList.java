//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.objects;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

import codex.xbit.api.common.datas.*;
import codex.common.utils.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
//@SuppressWarnings("serial")
public class TransactionList extends AbstractItem {
    private List<WhaleAlertTransactionData> transactions;
    private int type;
    private long sum;
    //---statics:

//-------------------------------------------------------------------------------------
    public TransactionList(Debug _debug, Resolver _resolver, int _type) {
    	super(_debug, _resolver);
    	transactions = new ArrayList<WhaleAlertTransactionData>();
    	type = _type;
    }

//-------------------------------------------------------------------------------------
    public long getSum() {
    	return sum;
    }

//-------------------------------------------------------------------------------------
    public void add(WhaleAlertTransactionData _transaction) {
/*
		debug.outln("Add _transaction: "+_transaction.getSymbol()+", _amount="+CurrencyUtils.easyToReadFormat(_transaction.getAmountUsd())+
			", from:"+_transaction.getFrom().toString()+", to:"+_transaction.getTo().toString()+
			"->:"+WhaleAlertTransactionData.convertIntTypeToString(type)+", t="+_transaction.getTimestamp());
*/
    	transactions.add(_transaction);

    }

//-------------------------------------------------------------------------------------
    public long sum() {
    	long _sum = 0;
        for (int i = 0; i < transactions.size(); i++) {
        	WhaleAlertTransactionData _transaction = transactions.get(i);
        	long _amountUsd = _transaction.getAmountUsd();
        	_sum += _amountUsd;
        }
        return _sum;
    }

//-------------------------------------------------------------------------------------
    public void remove(long _beforeTime) {
        for (int i = 0; i < transactions.size(); i++) {
        	WhaleAlertTransactionData _transaction = transactions.get(i);
        	long _time = _transaction.getTimestamp();
        	if(_time < _beforeTime){
        		transactions.remove(_transaction);
        	}
        }
    }
/*
//-------------------------------------------------------------------------------------
    public long removeAndSum(long _beforeTime) {
    	remove(long _beforeTime);
    	return sum();
    }
*/
//-------------------------------------------------------------------------------------
    public long removeAndSum(long _beforeTime) {
    	sum = 0;
        for (int i = 0; i < transactions.size(); i++) {
        	WhaleAlertTransactionData _transaction = transactions.get(i);
        	long _time = _transaction.getTimestamp();
//			debug.outln("remove _time: "+_time+", _beforeTime="+_beforeTime+", sum="+sum+", size="+transactions.size());
        	if(_time < _beforeTime){
        		transactions.remove(_transaction);
        	} else {
	        	long _amountUsd = _transaction.getAmountUsd();
	        	sum += _amountUsd;
        	}
        }
        return sum;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void link() {
    }

//-------------------------------------------------------------------------------------
    public void initiate() {
   	}

//-------------------------------------------------------------------------------------
    public void save(){
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return null;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
