//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level3.mixers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import io.reactivex.*;
import io.reactivex.schedulers.*;
import io.reactivex.disposables.*;

import org.knowm.xchange.currency.CurrencyPair;

import codex.common.utils.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;

//-------------------------------------------------------------------------------------
public class Mixers extends AbstractCluster<Configuration, Mixer> {
    
    //---cache:

    //---statics:


//-------------------------------------------------------------------------------------
    public Mixers(Debug _debug, Resolver _resolver) {
    	super(_debug, _resolver);
//    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Mixer getMixer(String _exchange, CurrencyPair _currencyPair){
    	Key _key = new Key(_exchange, _currencyPair);
    	Mixer _mixer = getEntity(_key);
    	if(_mixer == null){
    		_mixer = new Mixer(debug, resolver, _exchange, _currencyPair);
    		addEntity(_key, _mixer);    		
    	}
    	return _mixer;
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
    public void save() {
    	super.save();
    }

//-------------------------------------------------------------------------------------
    public String toString() {
		return "Mixers:";
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
