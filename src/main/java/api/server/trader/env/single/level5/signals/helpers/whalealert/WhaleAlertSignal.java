//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;


import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpConnectionParams;

import com.google.common.util.concurrent.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.datas.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level5.signals.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.crawler.*;

//-------------------------------------------------------------------------------------
public class WhaleAlertSignal extends Signal {
	private CurrencyPair currencyPair;

	//---cache:

	//---statics:
	private static WhaleAlertCrawler whaleAlertCrawler;

//-------------------------------------------------------------------------------------
    public WhaleAlertSignal(Debug _debug, Resolver _resolver, SignalConfiguration _signalConfiguration, CurrencyPair _currencyPair, List<String> _arg) {
    	super(_debug, _resolver, _signalConfiguration);
    	currencyPair = _currencyPair;
    	load();
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public CurrencyPair getCurrencyPair() {
    	return currencyPair;
    }

//-------------------------------------------------------------------------------------
	public void onNext(SignalEvent _signalEvent){
		float _confidence = _signalEvent.getConfidence();
		_signalEvent.setConfidence(confidence * _confidence);
		super.onNext(_signalEvent);
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
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String toString() {
		return "WhaleAlertSignal:";
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
