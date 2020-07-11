//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1.rawbalances;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.List;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashSet;

//import info.bitrich.xchangestream.core.*;
//import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.dto.account.*;
import org.knowm.xchange.dto.Order;
//import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.service.trade.params.orders.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;

//-------------------------------------------------------------------------------------
public class RawBalances extends AbstractMeta<Configuration, RawBalancesByExchange> {

    //---cache:

//-------------------------------------------------------------------------------------
    public RawBalances(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public RawBalancesByExchange getRawBalancesByExchange(String _exchange) {
    	Key _key = new Key(_exchange);
//		debug.out("Balances.addBalance.exchanges.add:"+_exchange.getShortName());
		RawBalancesByExchange _rawBalancesByExchange = getEntity(_key);
		if(_rawBalancesByExchange == null){
			_rawBalancesByExchange = new RawBalancesByExchange(debug, resolver, _exchange);
			addEntity(_key, _rawBalancesByExchange);
		}
		return _rawBalancesByExchange;
	}

/*
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void refresh(UserExchange _exchange) {
        ExchangeConfiguration _exchangeConfiguration = _exchange.getExchangeConfiguration();

    	synchronized(_exchange){

	        String _text = "["+_exchange.getShortName()+"]: Balances: ";
	        AccountInfo _accountInfo = null;
	        try{
	            _accountInfo = _exchange.getAccountService().getAccountInfo();
	        } catch(IOException _e1){
	            debug.outln(Debug.IMPORTANT2, _text +" : IOException" + _e1.getMessage());
	            return;
	        } catch(Exception _e2){
	            debug.outln(Debug.IMPORTANT2, _text +" : Exception:" + _e2.getMessage());
	            _e2.printStackTrace();
	            return;
	        }
	        BigDecimal _tradingFee =  _accountInfo.getTradingFee();
	        if(_tradingFee != null){
	            _text += "tradingFee="+_tradingFee.floatValue()+" ";
	        }
	        Map<String, Wallet> _wallets0 = _accountInfo.getWallets();
	        String _tradingWalletName = _exchangeConfiguration.getTradingWalletName();
            Wallet _wallet = _wallets0.get(_tradingWalletName);
            if(_wallet != null){
            	_text += updateBalance(_exchange, _wallet);           
	        	debug.outln(Debug.IMPORTANT3, _text);
            } else {
	        	debug.outln(Debug.ERROR, "Cannot find a wallet name: " + _tradingWalletName + " on " + _exchange.getShortName());            	
            }
    	}

    }

//-------------------------------------------------------------------------------------
    public String updateBalance(UserExchange _exchange, Wallet _wallet) {
    	String _text = "";
        Map<Currency, Balance>  _balances = _wallet.getBalances();
//    	debug.outln(Debug.WARNING, "Balances.updateBalance..."+_exchange);//+", "+_balances);
        ExchangeConfiguration _exchangeConfiguration = _exchange.getExchangeConfiguration();
		Map<Currency, Currency> _currencyMap = _exchangeConfiguration.getCurrencyMap();
		for (Map.Entry<Currency, Currency> _entry : _currencyMap.entrySet()) {
		    Currency _currency = _entry.getKey();
		    Currency _currencyCode = _entry.getValue();
			ShadowBalance _balanceX = getBalance(_exchange, _currency);
			if(_balanceX != null){
			    Balance _balance = _balances.get(_currencyCode);
			    if(_balance != null){
				    BigDecimal _amount = _balance.getAvailable();
				    if(_amount != null){
						_text +=_balanceX.setBalance(_amount);			    	
				    }
			    }
//				_balanceX = new ShadowBalance(debug, resolver, _exchange, _currency);
//				addEntity(_key, _balanceX);
			}
//    		debug.outln(Debug.WARNING, "Balances shadowBalance:"+_balanceX+", "+_currency+", "+_currencyCode);
		}


		return _text;
    }

//-------------------------------------------------------------------------------------
    public void listenEvent(Event _event){
//		debug.outln(Debug.INFO, "Balances _eventId="+_event.getId()+", counterExchangesReady="+counterExchangesReady+", counterExchangesRegistered="+counterExchangesRegistered);
    	switch(_event.getId()){
    		case Event.EXCHANGE_READY: {
    			UserExchange _exchange = (UserExchange)_event.getObject();
    			refresh(_exchange);
    			counterExchangesReady++;
    			if(counterExchangesReady == counterExchangesRegistered){
			    	Event _event1 = new Event(Event.BALANCES_READY, null, null);
			    	pushEvent(_event1);
    			}
    			break;
    		} 
    		default: {
				debug.outln(Debug.ERROR, "Balances: unknown event id _eventId="+_event.getId());
    			break;
    		} 
		}
	}
*/
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    public RawBalance loadEntity(Configuration _configuration) {
    	return null;
    }*/

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    }

//-------------------------------------------------------------------------------------
    public void save(){
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
