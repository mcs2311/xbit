//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.nio.file.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import info.bitrich.xchangestream.core.*;
import org.knowm.xchange.*;
import org.knowm.xchange.currency.*;
import org.knowm.xchange.dto.*;
import org.knowm.xchange.dto.account.*;
import org.knowm.xchange.dto.marketdata.*;
import org.knowm.xchange.dto.meta.*;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.service.account.*;
import org.knowm.xchange.service.marketdata.*;
import org.knowm.xchange.service.trade.*;
import org.knowm.xchange.exceptions.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

//-------------------------------------------------------------------------------------
public abstract class AbstractExchange<T1 extends Configuration> extends AbstractEntity<T1> implements Runnable {
    
    protected ExchangeConfiguration exchangeConfiguration;

	//----
    protected StreamingExchange streamingExchange;
    protected StreamingTradeService streamingTradeService;
    protected StreamingAccountService streamingAccountService;
    protected StreamingMarketDataService streamingMarketDataService;


	//----
    private int state;
    private Thread thisThread;
    private Object oLock = new Object();


    //---statics:
    public static final int STATE_CONNECTING 	= 0;
    public static final int STATE_READY 		= 1;

//-------------------------------------------------------------------------------------
//    public AbstractExchange(Debug _debug, Resolver _resolver, T1 _configuration, ConfigurationLoader<T1> _configurationLoader) {
    public AbstractExchange(Debug _debug, Resolver _resolver, T1 _configuration) {
        super(_debug, _resolver, _configuration);
        setState(STATE_CONNECTING);
    }

//-------------------------------------------------------------------------------------
    public void run() {
    	try{
	    	while(configuration.isEnabled()) {
	    		switch(getState()){
	    			case STATE_CONNECTING: {
	    				stateConnecting();
	    				break;
	    			}
	    			case STATE_READY: {
	    				stateReady();
	    				break;
	    			}
	    		}
	    	}    		
    	}catch(Throwable _t){
    		onError(_t);
    	}
    }

//-------------------------------------------------------------------------------------
    protected abstract void stateConnecting();

//-------------------------------------------------------------------------------------
    protected abstract void stateReady();

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ExchangeConfiguration getExchangeConfiguration(){
        return exchangeConfiguration;
    }

//-------------------------------------------------------------------------------------
    public synchronized int getState() {
    	return state;
    }

//-------------------------------------------------------------------------------------
    public synchronized void setState(int _state) {
//		debug.outln(Debug.WARNING, "AbstractExchange.setState... state= "+_state);
    	state = _state;
    	synchronized(oLock){
    		oLock.notifyAll();
    	}
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
//-------------------------------------------------------------------------------------
	public void waitUntilIsReady(){
		while((getState() != STATE_READY) && isEnabled()){
//			debug.outln(Debug.WARNING, "AbstractExchange.waitUntilIsReady.0... state= "+getState());
			synchronized(oLock){
				try{
					oLock.wait();
				} catch(InterruptedException _e){
					return;
				}
			}
		}
//		debug.outln(Debug.WARNING, "AbstractExchange.waitUntilIsReady.1... state= "+getState());
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
// 		debug.outln("UserExchange.load ....0");
    	super.load();
//		debug.outln("UserExchange.loas ....1");
//        List<String> _strategiesList = configuration.getStrategiesList();
//        trader.getMonitoring().getTacticCollections().addStrategies(_strategiesList);
    
        if(isEnabled()){
//		debug.outln("UserExchange.init ....2");
			thisThread = new Thread(this);
			thisThread.start();
        }
    }

//-------------------------------------------------------------------------------------
    public void save() {
		// Unsubscribe from data order book.
//		subscription.dispose();

//		debug.outln("UserExchange save...0");
        if(streamingExchange != null){
            // Disconnect from exchange (non-blocking)
            streamingExchange.disconnect().subscribe(() -> debug.outln("Disconnected from the StreamingExchangeX"));
        }
    	super.save();
    	setEnabled(false);
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Exchange:" + getName();
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
