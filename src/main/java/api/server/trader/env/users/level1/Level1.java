//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level1;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;


import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.env.users.level1.rawbalances.*;
import codex.xbit.api.server.trader.env.users.level1.raworders.*;
import codex.xbit.api.server.trader.env.users.level1.rawtrades.*;

//-------------------------------------------------------------------------------------
public class Level1 extends AbstractLevel {
	private RawBalances rawBalances;
	private RawOrders rawOrders;
	private RawTrades rawTrades;

//-------------------------------------------------------------------------------------
    public Level1(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "L1");
        rawBalances = new RawBalances(debug, _resolver);
        rawOrders = new RawOrders(debug, _resolver);
        rawTrades = new RawTrades(debug, _resolver);
    }

//-------------------------------------------------------------------------------------
    public RawBalances getRawBalances() {
    	return rawBalances;
	}

//-------------------------------------------------------------------------------------
    public RawOrders getRawOrders() {
    	return rawOrders;
	}

//-------------------------------------------------------------------------------------
    public RawTrades getRawTrades() {
    	return rawTrades;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
        rawBalances.load();
        rawOrders.load();
        rawTrades.load();
	}

//-------------------------------------------------------------------------------------
    public void save(){
    	if(rawBalances != null){
        	rawBalances.save();
    	}
    	if(rawOrders != null){
        	rawOrders.save();
    	}
    	if(rawTrades != null){
        	rawTrades.save();
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
