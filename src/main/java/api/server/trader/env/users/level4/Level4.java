//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;

import codex.xbit.api.server.trader.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;

//-------------------------------------------------------------------------------------
public class Level4 extends AbstractLevel {

    private Orderbooks orderbooks;

//-------------------------------------------------------------------------------------
    public Level4(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver, "L4");
        orderbooks = new Orderbooks(_debug, _resolver);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public Orderbooks getOrderbooks(){
		return orderbooks;
	}

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter, String _tactic){
//		return brokers.getInfo(_netCommandParameter, _tactic);
		return null;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
        orderbooks.load();
	}

//-------------------------------------------------------------------------------------
    public void save(){
//    	debug.outln(Debug.IMPORTANT3, "Level2.save...");
    	if(orderbooks != null){
        	orderbooks.save();
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------