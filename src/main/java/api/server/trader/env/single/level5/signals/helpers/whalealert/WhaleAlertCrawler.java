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
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.objects.*;
import codex.xbit.api.server.trader.env.single.level5.signals.helpers.whalealert.analyzers.*;

//https://alvinalexander.com/java/java-apache-httpclient-restful-client-examples
//https://docs.whale-alert.io/#introduction
//@SuppressWarnings("serial")
@SuppressWarnings({"serial", "deprecation", "unchecked"})
//-------------------------------------------------------------------------------------
public class WhaleAlertCrawler extends AbstractCluster<WhaleAlertConfiguration, WhaleAlertSignal> implements Runnable {
	private SignalConfiguration signalConfiguration;


    private ObjectMapper mapper;
	private Analyzer analyzer;
	private DefaultHttpClient httpclient;
    private HttpHost target;
    private HttpGet getRequest;
 	private RateLimiter throttle;
	private int state;
	private byte[] buffer = new byte[4096];


	private List<CurrencyPair> currencyPairs;

	//---cache:
	private Thread thisThread;
	private long startupTimestamp;
	private String cursor;
	private WhaleAlertConfiguration configuration;

	private String whalealertServer;
	private String whalealertKey;

	//---statics:
	private static final int STATE_NONE = 0;
	private static final int STATE_INIT = 1;
	private static final int STATE_RUNNING = 2;
	private static final int STATE_STOPPED = 3;

//	private static final double RATE_LIMIT = 0.05;

//-------------------------------------------------------------------------------------
    public WhaleAlertCrawler(Debug _debug, Resolver _resolver, SignalConfiguration _signalConfiguration) {
    	super(_debug, _resolver);
    	signalConfiguration = _signalConfiguration;
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
    	httpclient = new DefaultHttpClient();
        //The following parameter configurations are not
        //neccessary for this example, but they show how
        //to further tweak the HttpClient
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 2900);
        HttpConnectionParams.setSoTimeout(params, 2900);
        HttpConnectionParams.setTcpNoDelay(params, true);
        HttpConnectionParams.setLinger(params, 0);

    	String _thisServerName = _resolver.getServerConfiguration().getAlias();

		String _path = SystemUtils.getHomeDirectory() + "/" + resolver.getServerConfiguration().getPathToSettings();
    	configurationLoader = new ConfigurationLoader(debug, _path, WhaleAlertConfiguration.class);
    	configuration = configurationLoader.load("whalealert");

      	whalealertServer = configuration.getServer();
      	whalealertKey = configuration.getKey(_thisServerName);
//      	whaleAlertTimeWindow = _configuration.getTimeWindow();
//      	whaleAlertSizeTrigger = _configuration.getTriggerSize();

    	analyzer = new Analyzer(_debug, _resolver, configuration);

      	if(whalealertServer.isEmpty() || (whalealertKey == null) || whalealertKey.isEmpty()){
      		debug.outln(Debug.ERROR, "WhaleAlertServer cannot be empty. _thisServerName= "+_thisServerName+".\nPlease check configurations...");
      		System.exit(0);
      	}
      	// specify the host, protocol, and port
    	target = new HttpHost(whalealertServer, 443, "https");
      	// specify the get request
//		getRequest = new HttpGet("/v1/status?api_key=ncQNB9su54R0eeRcctcc2lDk2plL859E");
//		getRequest = new HttpGet("/v1/transactions?api_key=ncQNB9su54R0eeRcctcc2lDk2plL859E&min_value=500000");

    	throttle = RateLimiter.create(configuration.getRateLimit());
    	state = STATE_INIT;
    	currencyPairs = new ArrayList<CurrencyPair>();
    	load();
    }

//-------------------------------------------------------------------------------------
	public WhaleAlertSignal getWhaleAlertSignal(CurrencyPair _currencyPair){
		Key _key = new Key(_currencyPair);
		WhaleAlertSignal _whaleAlertSignal = getEntity(_key);
		if(_whaleAlertSignal == null){
			_whaleAlertSignal = new WhaleAlertSignal(debug, resolver, signalConfiguration, _currencyPair, null);
			addEntity(_key, _whaleAlertSignal);
			currencyPairs.add(_currencyPair);
		}
		return _whaleAlertSignal;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void run() {
    	startupTimestamp = System.currentTimeMillis()/1000 - 3500;
    	while(state != STATE_STOPPED){
    		throttle.acquire();
    		process();
    	}
    }

//-------------------------------------------------------------------------------------
    private void process() {
    	HttpEntity _entity = fetch();
		if(_entity != null){
			Result _result = adapt(_entity);
			if(_result != null){
				analyze(_result);
			}
		}
    }

//-------------------------------------------------------------------------------------
    private HttpEntity fetch() {
    	HttpResponse _httpResponse = null;
    	HttpEntity _entity = null;
    	try{
			String _pointers = "";
			if(state == STATE_INIT) {
				_pointers += "&start="+startupTimestamp;//+"&cursor="+cursor;
			} else {
				_pointers += "&start="+startupTimestamp+"&cursor="+cursor;				
			}
			String _path = "https://"+whalealertServer+"/v1/transactions?api_key="+whalealertKey+"&min_value=500000" + _pointers;
//			debug.outln("GET: "+_path);
			getRequest = new HttpGet(_path);

//			debug.outln("executing request to " + target);

			_httpResponse = httpclient.execute(getRequest);
			_entity = _httpResponse.getEntity();
		} catch (Exception _e) {
        	debug.outln(Debug.ERROR, "WhaleAlertSignal error0: " + _e.getMessage());
//			_e.printStackTrace();
		} finally {
//			_httpResponse.disconnect();

/*

			if(_httpResponse != null) {
				try { _httpResponse.disconnect(); } catch (Exception ignore) {}
			}

*/

		}
		return _entity;
	}

//-------------------------------------------------------------------------------------
    private Result adapt(HttpEntity _entity) {
/*
			debug.outln(httpResponse.getStatusLine().toString());
			Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				debug.outln(headers[i].toString());
			}
*/
		String chunk = "";
        InputStream inputStream = null;
        try {
        	inputStream = _entity.getContent();
			int bytesRead = 0;
        	BufferedInputStream bis = new BufferedInputStream(inputStream);
        	while ((bytesRead = bis.read(buffer)) != -1) {
            	chunk += new String(buffer, 0, bytesRead);
          	}
        } catch (Exception _e) {
//          e.printStackTrace();
        	debug.outln(Debug.ERROR, "WhaleAlertSignal error0: " + _e.getMessage());
        } finally {
          try { inputStream.close(); } catch (Exception ignore) {

          }
        }
        
//            System.out.println(chunk);
  		if(chunk.trim().equals("Service Unavailable")){
			debug.outln(Debug.ERROR, "Warning: Service Unavailable");
  			return null;
  		}

  		try{
			Result _result = mapper.readValue(chunk, Result.class);
			_result.pack();
			return _result;
		} catch (Exception _e) {
        	debug.outln(Debug.ERROR, "WhaleAlertSignal error1: " + _e.getMessage());
//        	onError(_e);
//			_e.printStackTrace();
		}
		return null;
	}    		

//-------------------------------------------------------------------------------------
    private void analyze(Result _result) {
    	try{
    		String _newCursor = _result.getCursor();
    		if(_newCursor != null){
				if((cursor == null) || (!cursor.equals(_newCursor))){
//					debug.outln("Count="+_result.getCount()+", new cursor="+_newCursor);
					check(analyzer.load(_result));
					cursor = _newCursor;
				}
				if(state == STATE_INIT){
					analyzer.remove(0);
					displaySummary();
					state = STATE_RUNNING;
				} else {
					long _currentTimestamp = System.currentTimeMillis()/1000;
	//				debug.outln(Debug.ERROR, "_currentTimestamp= " + _currentTimestamp);
					analyzer.remove(_currentTimestamp - configuration.getTimeWindow());
					scan();	
				}
    		}
		} catch (Exception _e) {
        	debug.outln(Debug.ERROR, "WhaleAlertSignal error2: " + _e.getMessage());
        	onError(_e);
//			_e.printStackTrace();
		}
  }

//-------------------------------------------------------------------------------------
	private void check(List<SignalEvent> _signalEvents) {
		for (int i = 0; i < _signalEvents.size(); i++) {
			SignalEvent _signalEvent = _signalEvents.get(i);
			if(_signalEvent != null){
				debug.outln(Debug.INFO, "WhaleAlertSignal.load:" + _signalEvent);
				for (int j = 0; j < currencyPairs.size(); j++) {
					CurrencyPair _currencyPair = currencyPairs.get(j);
					getWhaleAlertSignal(_currencyPair).onNext(_signalEvent);
				}
			}			
		}
	}

//-------------------------------------------------------------------------------------
	private void scan() {
//		List<CurrencyPair> _currencyPairs = getCurrencyPairs();
//		debug.outln("Scanning."+_currencyPairs.size()+" pairs...");
		for (int i = 0; i < currencyPairs.size(); i++) {
			CurrencyPair _currencyPair = currencyPairs.get(i);
//			debug.outln("Scanning_______:"+_currencyPair);
			SignalEvent _signalEvent = analyzer.scan(_currencyPair);
			if(_signalEvent != null){
//				_event.setObject();
				debug.outln(Debug.INFO, "WhaleAlertSignal.scan:" + _signalEvent);
//				pushEvent(_event);
				getWhaleAlertSignal(_currencyPair).onNext(_signalEvent);
			}
		}
	}

//-------------------------------------------------------------------------------------
	private void displaySummary() {
		/*List<String> _summary = (List<String>)analyzer.getSummary();
		debug.outln(Debug.IMPORTANT1, _summary);*/
		WhaleAlertSummaryData _summary = (WhaleAlertSummaryData)analyzer.getSummary();
		for (int i = 0; i < _summary.size(); i++) {
			WhaleAlertSummaryEntryData _entry = _summary.get(i);
			Set<String> _keys = _entry.getKeys();
			Iterator<String> _iterator = _keys.iterator();
			String _line = "[" + _entry.getCurrency().toString().toUpperCase() + "]: ";
			while(_iterator.hasNext()){
            	String _key = _iterator.next();
            	long _value = _entry.get(_key);
            	_line += _key +"=" + CurrencyUtils.easyToReadFormat(_value);
            	if(_iterator.hasNext()){
            		_line += ", ";
            	}
			}
			debug.outln(_line);
		}
	}

//-------------------------------------------------------------------------------------
	public Object getInfo(int _netCommandParameter){
		switch(_netCommandParameter){
			case NetCommand.COMMAND_SHOW_WHALES: {
				return analyzer.getSummary();
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
//		debug.outln(Debug.ERROR, "repository.load:" + getName());
    	state = STATE_INIT;
    	try{
	    	thisThread = new Thread(this);
	    	thisThread.start();
    	}catch(Throwable _t){
    		onError(_t);
    	}
    }

//-------------------------------------------------------------------------------------
    public void save(){
    	super.save();
    	state = STATE_STOPPED;
		if(thisThread != null){
    		thisThread.interrupt(); 	
		}
		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources
		httpclient.getConnectionManager().shutdown();
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
