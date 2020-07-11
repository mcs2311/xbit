//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.objects;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;

import org.knowm.xchange.currency.Currency;

import codex.xbit.api.common.datas.*;
import codex.common.utils.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;


//-------------------------------------------------------------------------------------
//@SuppressWarnings("serial")
public class TransactionMap extends AbstractItem {
	private Map<org.knowm.xchange.currency.Currency, TransactionBook> transactionBooks;

	//---cache:

	//--- statics:
	private static final org.knowm.xchange.currency.Currency USD = new org.knowm.xchange.currency.Currency("USD");
//-------------------------------------------------------------------------------------
    public TransactionMap(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    	transactionBooks = new HashMap<org.knowm.xchange.currency.Currency, TransactionBook>();
    }

//-------------------------------------------------------------------------------------
    public TransactionBook get(org.knowm.xchange.currency.Currency _currency) {
    	TransactionBook _transactionBook = transactionBooks.get(_currency);
    	if(_transactionBook == null){
    		_transactionBook = new TransactionBook(debug, resolver);
    		transactionBooks.put(_currency, _transactionBook);
    	}
    	return _transactionBook;
    }

//-------------------------------------------------------------------------------------
    public void add(WhaleAlertTransactionData _transaction) {
    	org.knowm.xchange.currency.Currency _currency = _transaction.getCurrency();
//    	int _type = _transaction.getType();
		if(_currency.getSymbol().contains("USD")){
			_currency = USD;
		}
    	TransactionBook _transactionBook = get(_currency);
    	_transactionBook.add(_transaction);
    }

//-------------------------------------------------------------------------------------
	public void removeAndSum(long _fromTime) {
		Collection<TransactionBook> _c = transactionBooks.values();
		Iterator<TransactionBook> _iterator = _c.iterator();
		while (_iterator.hasNext()) {
            TransactionBook _transactionBook = _iterator.next();
            _transactionBook.removeAndSum(_fromTime);
		}
	}

//-------------------------------------------------------------------------------------
	public long getSum(org.knowm.xchange.currency.Currency _currency, int _type) {
		if(_currency.getSymbol().contains("USD")){
			_currency = USD;
		}
//		debug.outln("TransactionMap.Scan  "+_currency+"....");
//		TransactionBook _transactionBook = transactionBooks.get(_currency);
		TransactionBook _transactionBook = get(_currency);
		return _transactionBook.getSum(_type);
	}

//-------------------------------------------------------------------------------------
	public WhaleAlertSummaryData getSummary() {
		Set<org.knowm.xchange.currency.Currency> _s = transactionBooks.keySet();
		Iterator<org.knowm.xchange.currency.Currency> _iterator = _s.iterator();
		List<String> _summary = new ArrayList<String>();
		WhaleAlertSummaryData _whaleAlertSummaryData = new WhaleAlertSummaryData();
		while (_iterator.hasNext()) {
//			String _summary0 = "";
            org.knowm.xchange.currency.Currency _currency = _iterator.next();
//            _summary0 += _currency.toString().toUpperCase() + ": [";
            TransactionBook _transactionBook = transactionBooks.get(_currency);
//            String _summary0 = _transactionBook.getSummary();
            WhaleAlertSummaryEntryData _whaleAlertSummaryEntryData = new WhaleAlertSummaryEntryData(_currency);
            _transactionBook.loadSummary(_whaleAlertSummaryEntryData);
            _whaleAlertSummaryData.add(_whaleAlertSummaryEntryData);
            	//_currency.toString().toUpperCase() + ": [" + _transactionBook.getSummary() + " ]");
//			debug.outln(_summary);
		}
//		debug.outln(_whaleAlertSummaryData.toString());
		return _whaleAlertSummaryData;
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
