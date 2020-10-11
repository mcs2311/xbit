//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.crawler.analyzers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.datas.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.crawler.objects.*;

//-------------------------------------------------------------------------------------
public class Analyzer extends AbstractItem {
	private TransactionMap transactionMap;
	private WhaleAlertConfiguration whaleAlertConfiguration;

	//---cache:
	private float whaleAlertSizeTrigger;
	private Map<String, Float> buyTriggerWords;
	private Map<String, Float> sellTriggerWords;
	private float triggerWordsSize;

	//---statics:

//-------------------------------------------------------------------------------------
    public Analyzer(Debug _debug, Resolver _resolver, WhaleAlertConfiguration _whaleAlertConfiguration) {
    	super(_debug, _resolver);
    	whaleAlertConfiguration = _whaleAlertConfiguration;
    	
    	transactionMap = new TransactionMap(_debug, _resolver);
    	whaleAlertSizeTrigger = (float)_whaleAlertConfiguration.getTriggerSize();
    	buyTriggerWords = _whaleAlertConfiguration.getTriggerWords("buy");
    	sellTriggerWords = _whaleAlertConfiguration.getTriggerWords("sell");
    	triggerWordsSize = (float)_whaleAlertConfiguration.getTriggerWordsSize();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public List<SignalEvent> load(Result _result) {
//    	List<Event> _events = new ArrayList<Event>();
//		debug.outln("_result:" + _result.getResult() + ", _count="+_result.getCount());
		List<WhaleAlertTransactionData> _transactions = _result.getTransactions();
		List<SignalEvent> _signalEvents = new ArrayList<SignalEvent>();
		if(_transactions != null){
			_transactions.forEach(_transaction -> {
				SignalEvent _signalEvent = checkForTriggers(_transaction);
				if(_signalEvent != null){
					_signalEvents.add(_signalEvent);
				}
				transactionMap.add(_transaction);

		    	Address _from = _transaction.getFrom();
		    	Address _to = _transaction.getTo();
				debug.outln("symbol="+_transaction.getSymbol()+", amount=" + CurrencyUtils.easyToReadFormat(_transaction.getAmountUsd()) +
					", from:"+_from.getOwner()+"/"+_from.getOwnerType()+", to:"+_to.getOwner()+"/"+_to.getOwnerType());

			});
		}

//		debug.outln("_transaction:" + _transaction.getBlockchain() + ", _symbol="+_transaction.getSymbol()+", _amount="+_transaction.getAmount());

		return _signalEvents;
    }

//-------------------------------------------------------------------------------------
	public SignalEvent checkForTriggers(WhaleAlertTransactionData _transaction){
		float _weightSell = _transaction.getFrom().matchAnyTriggerWords(sellTriggerWords);
		if(_weightSell > (float)0.1){
			long _amountUsd = _transaction.getAmountUsd();
			if(_amountUsd > triggerWordsSize){
				float _confidence = (float)1.0 - triggerWordsSize/_amountUsd;
				debug.outln("checkForTriggers.sell: _amountUsd=" + _amountUsd + ", _confidence="+_confidence+", _weightSell="+_weightSell);
				_confidence = _confidence * _weightSell;
		        return new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_WHALEALERT);				
			}
		} 
		float _weightBuy = _transaction.getFrom().matchAnyTriggerWords(sellTriggerWords);
		if(_weightBuy > (float)0.1){
			long _amountUsd = _transaction.getAmountUsd();
			if(_amountUsd > triggerWordsSize){
				float _confidence = (float)1.0 - triggerWordsSize/_amountUsd;
				debug.outln("checkForTriggers.buy: _amountUsd=" + _amountUsd + ", _confidence="+_confidence+", _weightBuy="+_weightBuy);
				_confidence = _confidence * _weightBuy;
	        	return new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_WHALEALERT);
	        }
		}
		return null;			
	}

//-------------------------------------------------------------------------------------
	public void remove(long _fromTime) {
		transactionMap.removeAndSum(_fromTime);		
	}

//-------------------------------------------------------------------------------------
	public SignalEvent scan(CurrencyPair _currencyPair) {
		org.knowm.xchange.currency.Currency _base = _currencyPair.base;
		org.knowm.xchange.currency.Currency _counter = _currencyPair.counter;
		long _sumBase = scan(_base);
		long _sumCounter = scan(_counter);

		long _difference = _sumBase - _sumCounter;
		if(Math.abs(_difference) > whaleAlertSizeTrigger/5) {
			debug.outln("WhaleAlertSignal.Analyzing.scan ["+_currencyPair+"]: difference= " + CurrencyUtils.easyToReadFormat(_difference) + "...");
		}
		if(_difference > whaleAlertSizeTrigger){
			debug.outln("Analyzer: ["+_currencyPair+"]"+CurrencyUtils.easyToReadFormat(_sumBase)+"/"+CurrencyUtils.easyToReadFormat(_sumCounter)+": difference=" + CurrencyUtils.easyToReadFormat(_difference));
			float _confidence = (float)1.0 - whaleAlertSizeTrigger/Math.abs(_difference);
//			SignalEvent _signalSource = new SignalEvent(SignalEvent.SIGNAL_WHALE_ALERT, _confidence);
			debug.outln("Analyzer: ["+_currencyPair+"]--->SIGNAL_SELL...:_confidence="+_confidence);
	        return new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_WHALEALERT);
		} else if(_difference < (-whaleAlertSizeTrigger)){
			float _confidence = (float)1.0 - whaleAlertSizeTrigger/Math.abs(_difference);
//			SignalEvent _signalSource = new SignalEvent(SignalEvent.SIGNAL_WHALE_ALERT, _confidence);
			debug.outln("Analyzer: ["+_currencyPair+"]--->SIGNAL_BUY..._confidence="+_confidence);
	        return new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_WHALEALERT);
		} else {
			return null;
		}
	}

//-------------------------------------------------------------------------------------
	private long scan(org.knowm.xchange.currency.Currency _currency) {
//		debug.outln("Scan 0 "+_currency+"....");
		long _incoming = transactionMap.getSum(_currency, WhaleAlertTransactionData.TYPE_INCOMING);
//		debug.outln("Scan 1...");
//		long _incoming = _sums.get(WhaleAlertTransactionData.TYPE_INCOMING);
		long _outgoing = 0;//_sums.get(WhaleAlertTransactionData.TYPE_OUTGOING);
//		debug.outln("Scan 2...");
		return _incoming - _outgoing;
	}

//-------------------------------------------------------------------------------------
	public Object getSummary() {
		return (Object)transactionMap.getSummary();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void save() {
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "Analyzer:";
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
