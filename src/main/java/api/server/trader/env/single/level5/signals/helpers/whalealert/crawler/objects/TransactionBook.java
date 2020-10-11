//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.crawler.objects;
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
public class TransactionBook extends AbstractItem {
	private Map<Integer, TransactionList> transactionLists;
//	private Map<Integer, Long> sums;

	//---cache:
//	private boolean dirtyFlag;

    //---statics:

//-------------------------------------------------------------------------------------
    public TransactionBook(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    	transactionLists = new HashMap<Integer, TransactionList>();
    	get(WhaleAlertTransactionData.TYPE_INCOMING);
    	get(WhaleAlertTransactionData.TYPE_OUTGOING);
    	get(WhaleAlertTransactionData.TYPE_UNKNOWN);
    	get(WhaleAlertTransactionData.TYPE_EXCHANGE);

/*    	sums = new HashMap<Integer, Long>();
    	sums.put(WhaleAlertTransactionData.TYPE_INCOMING, 0L);
    	sums.put(WhaleAlertTransactionData.TYPE_OUTGOING, 0L);
    	sums.put(WhaleAlertTransactionData.TYPE_UNKNOWN, 0L);
    	sums.put(WhaleAlertTransactionData.TYPE_EXCHANGE, 0L);*/
//    	dirtyFlag = true;
    }

//-------------------------------------------------------------------------------------
    public TransactionList get(int _type) {
    	TransactionList _transactionList = transactionLists.get(_type);
    	if(_transactionList == null){
    		_transactionList = new TransactionList(debug, resolver, _type);
    		transactionLists.put(_type, _transactionList);
    	}
    	return _transactionList;
    }

//-------------------------------------------------------------------------------------
    public void add(WhaleAlertTransactionData _transaction) {
    	int _type = _transaction.getType();
    	TransactionList _transactionList = get(_type);
    	_transactionList.add(_transaction);
    }

//-------------------------------------------------------------------------------------
	public void removeAndSum(long _fromTime) {
/*
		Collection<TransactionList> _c = transactionLists.values();
		Iterator<TransactionList> _iterator = _c.iterator();
		while (_iterator.hasNext()) {
            TransactionList _transactionList = _iterator.next();
            _transactionList.removeAndSum(_fromTime);
		}
*/
		Set<Integer> _s = transactionLists.keySet();
		Iterator<Integer> _iterator = _s.iterator();
		while (_iterator.hasNext()) {
            int _type = _iterator.next();
//			debug.outln("TransactionBook...."+_type);
            TransactionList _transactionList = transactionLists.get(_type);
            _transactionList.removeAndSum(_fromTime);
//            sums.put(_type, _transactionList.getSum());
		}
	}

//-------------------------------------------------------------------------------------
	public long getSum(int _type) {
//		debug.outln("TransactionBook.Scan  0 "+_currency+"....");
/*		Set<Integer> _s = transactionLists.keySet();
		Iterator<Integer> _iterator = _s.iterator();
		while (_iterator.hasNext()) {
            int _type = _iterator.next();
//			debug.outln("TransactionBook...."+_type);
            TransactionList _transactionList = transactionLists.get(_type);
            sums.put(_type, _transactionList.getSum());
		}*/
//		debug.outln("TransactionBook.Scan  1 "+_currency+"...."+sums);
    	TransactionList _transactionList = get(_type);
		return _transactionList.getSum();
	}

//-------------------------------------------------------------------------------------
	public void loadSummary(WhaleAlertSummaryEntryData _whaleAlertSummaryEntryData) {
//		String _summary = "";
		Set<Integer> _s = transactionLists.keySet();
		Iterator<Integer> _iterator = _s.iterator();
		while (_iterator.hasNext()) {
            int _type = _iterator.next();
            String _name = WhaleAlertTransactionData.convertIntTypeToString(_type);// + ": ";
            TransactionList _sum = get(_type);
//            _summary += CurrencyUtils.easyToReadFormat(_sum.getSum());
            _whaleAlertSummaryEntryData.add(_name, _sum.getSum());
		}
//		return _summary;
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
