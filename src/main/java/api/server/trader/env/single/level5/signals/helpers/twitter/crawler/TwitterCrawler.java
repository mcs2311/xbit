//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter.crawler;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;

import twitter4j.*;
import twitter4j.conf.*;

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
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter.*;
//import codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter.crawler.objects.*;
//import codex.xbit.api.server.trader.env.single.level5.signals.helpers.twitter.crawler.analyzers.*;

//https://alvinalexander.com/java/java-apache-httpclient-restful-client-examples
//https://docs.whale-alert.io/#introduction
//@SuppressWarnings("serial")
@SuppressWarnings({"serial", "deprecation", "unchecked"})
//-------------------------------------------------------------------------------------
public class TwitterCrawler extends AbstractCluster<TwitterConfiguration, TwitterSignal> implements StatusListener  {
	private SignalConfiguration signalConfiguration;

	private List<CurrencyPair> currencyPairs;

	//---cache:
	private TwitterConfiguration configuration;

	//---statics:
	private static final int STATE_NONE = 0;
	private static final int STATE_INIT = 1;
	private static final int STATE_RUNNING = 2;
	private static final int STATE_STOPPED = 3;

//	private static final double RATE_LIMIT = 0.05;

//-------------------------------------------------------------------------------------
    public TwitterCrawler(Debug _debug, Resolver _resolver, SignalConfiguration _signalConfiguration) {
    	super(_debug, _resolver);
    	signalConfiguration = _signalConfiguration;

		String _path = SystemUtils.getHomeDirectory() + "/" + resolver.getServerConfiguration().getPathToSettings();
    	configurationLoader = new ConfigurationLoader(debug, _path, TwitterConfiguration.class);
    	configuration = configurationLoader.load("twitter");

    	load();
    }

//-------------------------------------------------------------------------------------
	public TwitterSignal getSignal(CurrencyPair _currencyPair, Map<String, String> _arg){
		Key _key = new Key(_currencyPair);
		TwitterSignal _twitterSignal = getEntity(_key);
		if(_twitterSignal == null){
			_twitterSignal = new TwitterSignal(debug, resolver, signalConfiguration, _currencyPair, null);
			addEntity(_key, _twitterSignal);
			currencyPairs.add(_currencyPair);
		}
		return _twitterSignal;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public void onStatus(Status _status) {
	    debug.outln("TwitterSignal: " + _status.getUser().getName() + " : " + _status.getText());
	    onNext(_status);
	}

//-------------------------------------------------------------------------------------
	public void onNext(Status _status) {
/*		BeanEvent _beanEvent = _marketModelEvent.getBeanEvent();
		if(!(_beanEvent instanceof OrderbookEvent)){
			return;
		}
		int _direction = _marketModelEvent.getDirection();
		float _confidence = _marketModelEvent.getConfidence();


		_confidence = _confidence * confidence;
		_confidence = Math.min(_confidence, 0.95f);

		SignalEvent _newEvent = null;
		if((_direction == MarketModelEvent.MARKET_GOING_UP) && (_confidence > thresholdBuy)){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_BUY, _confidence, SignalEvent.SOURCE_DEPTHCHART);
	//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		} else if((_direction == MarketModelEvent.MARKET_GOING_DOWN)  && (_confidence > thresholdSell)){
			_newEvent = new SignalEvent(SignalEvent.SIGNAL_SELL, _confidence, SignalEvent.SOURCE_DEPTHCHART);
//				debug.outln(Debug.WARNING, "["+currencyPair+"]: "+_newEvent);
		}



		
		if(_newEvent != null){
			if(emitter != null){
				debug.outln(Debug.WARNING, "DepthChartSignal.onNext: " + _marketModelEvent + ", _confidence=" + Float.valueOf(df.format(_confidence)));
				emitter.onNext(_newEvent);
			}
		}
		*/
	}

//-------------------------------------------------------------------------------------
	public void onDeletionNotice(StatusDeletionNotice _statusDeletionNotice) {

	}

//-------------------------------------------------------------------------------------
	public void onTrackLimitationNotice(int _numberOfLimitedStatuses) {

	}

//-------------------------------------------------------------------------------------
	public void onStallWarning(StallWarning _stallWarning) {

	}

//-------------------------------------------------------------------------------------
	public void onScrubGeo(long _x, long _y) {

	}

//-------------------------------------------------------------------------------------
	public void onException(Exception _e) {
	    _e.printStackTrace();
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_WHALES: {
//				return analyzer.getSummary();
			}
			default: {
				return (Object)(new ArrayList<String>(Arrays.asList("Command not found:" + _netCommandParameter)));
			}
		}
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void load() {
		super.load();
		ConfigurationBuilder _configurationBuilder = new ConfigurationBuilder();
		_configurationBuilder.setOAuthConsumerKey(configuration.getApiKey());
		_configurationBuilder.setOAuthConsumerSecret(configuration.getApiKeySecret());
		_configurationBuilder.setOAuthAccessToken(configuration.getAccessToken());
		_configurationBuilder.setOAuthAccessTokenSecret(configuration.getAccessTokenSecret());

	    TwitterStream _twitterStream = new TwitterStreamFactory(_configurationBuilder.build()).getInstance();
	    _twitterStream.addListener(this);
	    // sample() method internally creates a thread which manipulates TwitterStream and calls these adequate listener methods continuously.
	    _twitterStream.sample();		
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
		return "TwitterCrawler:";
    }
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
