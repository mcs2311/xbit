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

import codex.xbit.api.server.trader.common.events.users.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;

//-------------------------------------------------------------------------------------
public class RawBalancesByExchange extends AbstractCluster<Configuration, RawBalance> implements Runnable {
	private String exchange;

    //---cache:
	private UserExchange userExchange;
	private ExchangeConfiguration exchangeConfiguration;
	private TradeService tradeService;
	private Thread thisThread;

//-------------------------------------------------------------------------------------
    public RawBalancesByExchange(Debug _debug, Resolver _resolver, String _exchange) {
    	super(_debug, _resolver);
    	exchange = _exchange;

		userExchange = resolver.getUserExchanges().getUserExchange(exchange);
    	exchangeConfiguration = userExchange.getExchangeConfiguration();

		if(!exchangeConfiguration.supportForStreamingOrderChanges()){
	        tradeService = userExchange.getTradeService();
	        try{
		    	thisThread = new Thread(this);
		    	thisThread.start();
	    	}catch(Throwable _t){
	    		onError(_t);
	    	}
		}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public RawBalance getRawBalance(Currency _currency) {
    	Key _key = new Key(_currency);
		RawBalance _balance = getEntity(_key);
		if(_balance == null){
			_balance = new RawBalance(debug, resolver, exchange, _currency);
			addEntity(_key, _balance);
		}
		return _balance;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
    	int _delay = 0;
        while(isEnabled()){
            try{
//                Thread.sleep(1800000);
                Thread.sleep(_delay);
            }catch(InterruptedException _e){
//            	break;
            }
            checkForNewBalances();
            _delay = 300000;// + (_refreshCounter * 500)
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void checkForNewBalances() {
//	       	debug.outln(Debug.INFO, "checkForNewBalances....");
	        String _text = "["+exchange+"]: RawBalances: ";
	        AccountInfo _accountInfo = null;
	        try{
	            _accountInfo = userExchange.getAccountService().getAccountInfo();
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
	        String _tradingWalletName = exchangeConfiguration.getTradingWalletName();
            Wallet _wallet = _wallets0.get(_tradingWalletName);
            if(_wallet != null){
            	_text += updateBalance(_wallet);           
	        	debug.outln(Debug.IMPORTANT3, _text);
            } else {
	        	debug.outln(Debug.ERROR, "Cannot find a wallet name: " + _tradingWalletName + " on " + exchange);
            }
    }

//-------------------------------------------------------------------------------------
    public String updateBalance(Wallet _wallet) {
    	String _text = "";
        Map<Currency, Balance> _balances = _wallet.getBalances();
//    	debug.outln(Debug.WARNING, "Balances.updateBalance..."+_exchange);//+", "+_balances);
        List<RawBalance> _rawBalances = getEntities();
		for (int i = 0; i < _rawBalances.size(); i++ ) {
			RawBalance _rawBalance = _rawBalances.get(i);
			Currency _currency = _rawBalance.getCurrency();
			Currency _currencyCode = exchangeConfiguration.getCurrencyCode(_currency);
			Balance _balance = _wallet.getBalance(_currencyCode);
			_rawBalance.onNext(_balance);
			_text += "[" + _currency.toString() + ":" + _balance.getAvailable() + "] ";
//				_balanceX = new ShadowBalance(debug, resolver, _exchange, _currency);
//				addEntity(_key, _balanceX);
//    		debug.outln(Debug.WARNING, "Balances shadowBalance:"+_balanceX+", "+_currency+", "+_currencyCode);
		}
		return _text;
    }

//-------------------------------------------------------------------------------------
    public void triggerBalanceCheck(){
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}    	
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
    	super.load();
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
		setEnabled(false);
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}    	
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
