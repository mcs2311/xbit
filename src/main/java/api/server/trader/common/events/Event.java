//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.events;
//-------------------------------------------------------------------------------------
import java.time.*;
import java.math.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;


import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
public interface Event {
	public final static int KIND_TRADE 						= 1;
	public final static int KIND_TICKER 					= 2;
	public final static int KIND_ORDERBOOK 					= 3;
	public final static int KIND_STRATEGY 					= 4;
	public final static int KIND_MARKETMODEL 				= 5;
	public final static int KIND_SIGNAL 					= 6;
	public final static int KIND_PILOT 						= 7;

	public final static int KIND_USER_TRADE 				= 100;
	public final static int KIND_USER_BALANCE 				= 101;
	public final static int KIND_USER_ORDER 				= 102;
	public final static int KIND_USER_ORDERS 				= 103;
	public final static int KIND_USER_ORDERBOOK 			= 104;

//-------------------------------------------------------------------------------------
    public int getKind();
//-------------------------------------------------------------------------------------
    public float getConfidence();
//-------------------------------------------------------------------------------------
    public String getName();
//-------------------------------------------------------------------------------------
    public String toString();
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
