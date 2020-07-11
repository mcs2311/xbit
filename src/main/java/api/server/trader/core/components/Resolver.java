//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.core.components;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.time.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;

import codex.xbit.api.common.aspects.*;

import codex.xbit.api.server.trader.*;

//single
import codex.xbit.api.server.trader.env.single.*;
import codex.xbit.api.server.trader.env.single.level0.influxexchanges.*;
import codex.xbit.api.server.trader.env.single.level1.repositories.*;
import codex.xbit.api.server.trader.env.single.level2.indicators.*;
import codex.xbit.api.server.trader.env.single.level3.strategies.*;
import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;
import codex.xbit.api.server.trader.env.single.level5.signals.*;
import codex.xbit.api.server.trader.env.single.level6.pilots.*;


//users
import codex.xbit.api.server.trader.env.users.*;
import codex.xbit.api.server.trader.env.users.level0.userexchanges.*;
import codex.xbit.api.server.trader.env.users.level1.rawbalances.*;
import codex.xbit.api.server.trader.env.users.level1.raworders.*;
import codex.xbit.api.server.trader.env.users.level1.rawtrades.*;
import codex.xbit.api.server.trader.env.users.level2.shadowbalances.*;
import codex.xbit.api.server.trader.env.users.level2.shadoworders.*;
import codex.xbit.api.server.trader.env.users.level3.agents.*;
import codex.xbit.api.server.trader.env.users.level3.mixers.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.*;
import codex.xbit.api.server.trader.env.users.level5.brokers.*;
import codex.xbit.api.server.trader.env.users.level6.pairs.*;
import codex.xbit.api.server.trader.env.users.level7.tactics.*;


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
public class Resolver {
	private Debug debug;
	private Trader trader;
	private ServerConfiguration serverConfiguration;
    private PilotConfiguration pilotConfiguration;
    private TraderConfiguration traderConfiguration;
    private Map<String, Configuration> cachedConfigurations;

    private ZoneId zoneId;


    private InfluxExchanges influxExchanges;

    private Repositories repositories;

    private Indicators indicators;

    private Strategies strategies;

    private MarketModels marketModels;

    private Signals signals;

    private Pilots pilots;

//----

    private User user;

    private UserExchanges userExchanges;

    private RawBalances rawBalances;
    private RawOrders rawOrders;
    private RawTrades rawTrades;

    private ShadowBalances shadowBalances;
    private ShadowOrders shadowOrders;

    private Agents agents;
    private Mixers mixers;

    private Orderbooks orderbooks;

    private Brokers brokers;

    private Pairs pairs;

    private Tactics tactics;

//    private Evaluators evaluators;*/

//-------------------------------------------------------------------------------------
    public Resolver(Debug _debug, Trader _trader, ServerConfiguration _serverConfiguration) {
    	debug = _debug;
    	trader = _trader;
    	serverConfiguration = _serverConfiguration;
//        traderConfiguration = _traderConfiguration;
        cachedConfigurations = new HashMap<String, Configuration>();
        addCachedConfiguration("filters", FiltersConfiguration.class);
        preload();
    }

//-------------------------------------------------------------------------------------
    public Resolver(Resolver _resolver, User _user) {
    	debug = _resolver.debug;
    	trader = _resolver.trader;
    	serverConfiguration = _resolver.serverConfiguration;
        pilotConfiguration = _resolver.pilotConfiguration;
        traderConfiguration = _resolver.traderConfiguration;
        cachedConfigurations = _resolver.cachedConfigurations;

        influxExchanges = _resolver.influxExchanges;
    	repositories = _resolver.repositories;
    	indicators = _resolver.indicators;
    	strategies = _resolver.strategies;
    	marketModels = _resolver.marketModels;
    	signals = _resolver.signals;
    	pilots = _resolver.pilots;

        user = _user;

        preload();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Trader getTrader(){
        return trader;
    }

//-------------------------------------------------------------------------------------
    public ServerConfiguration getServerConfiguration(){
        return serverConfiguration;
    }

//-------------------------------------------------------------------------------------
    public PilotConfiguration getPilotConfiguration(){
        return pilotConfiguration;
    }

//-------------------------------------------------------------------------------------
    public void setPilotConfiguration(PilotConfiguration _pilotConfiguration){
        pilotConfiguration = _pilotConfiguration;
    }

//-------------------------------------------------------------------------------------
    public TraderConfiguration getTraderConfiguration(){
        return traderConfiguration;
    }

//-------------------------------------------------------------------------------------
    public void setTraderConfiguration(TraderConfiguration _traderConfiguration){
        traderConfiguration = _traderConfiguration;
    }

//-------------------------------------------------------------------------------------
    public Configuration getCachedConfiguration(String _configurationName){
    	Configuration _configuration = cachedConfigurations.get(_configurationName);
    	return _configuration;
    }

//-------------------------------------------------------------------------------------
    public User getUser(){
    	return user;
    }

//-------------------------------------------------------------------------------------
    public String getUserName(){
    	return user.getUserName();
    }

//-------------------------------------------------------------------------------------
    public String getUserHome(){
    	String _path = SystemUtils.getHomeDirectory() + "/" + serverConfiguration.getPathToUsers();
    	_path = _path + "/" + user.getUserName();
    	return _path;
    }

//-------------------------------------------------------------------------------------
    public ZoneId getZoneId(){
        return zoneId;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private void preload(){
        String _zoneIdString = getServerConfiguration().getZoneId();
//        debug.outln("ZoneId: "+ _zoneIdString);
//        ZoneId _zoneId;
        if(_zoneIdString.isEmpty()){
        	zoneId = ZoneId.systemDefault();
        	debug.outln(Debug.WARNING, "Resolver.ZoneId is empty. Loading default ZoneId: "+ zoneId);
        } else {
        	zoneId = ZoneId.of(_zoneIdString);
        }
//        debug.outln(Debug.WARNING, "Resolver.ZoneId = "+ _zoneId);
    }

//-------------------------------------------------------------------------------------
    public void load(){
        influxExchanges = trader.getSingle().getLevel0().getInfluxExchanges();

        repositories = trader.getSingle().getLevel1().getRepositories();

        indicators = trader.getSingle().getLevel2().getIndicators();

        strategies = trader.getSingle().getLevel3().getStrategies();

        marketModels = trader.getSingle().getLevel4().getMarketModels();

        signals = trader.getSingle().getLevel5().getSignals();

        pilots = trader.getSingle().getLevel6().getPilots();


//---
        if(user != null){
	        userExchanges = user.getLevel0().getUserExchanges();

	        rawBalances = user.getLevel1().getRawBalances();
	        rawOrders = user.getLevel1().getRawOrders();
	        rawTrades = user.getLevel1().getRawTrades();

	        shadowBalances = user.getLevel2().getShadowBalances();
	        shadowOrders = user.getLevel2().getShadowOrders();

	        agents = user.getLevel3().getAgents();
	        mixers = user.getLevel3().getMixers();

	        orderbooks = user.getLevel4().getOrderbooks();

	        brokers = user.getLevel5().getBrokers();

	        pairs = user.getLevel6().getPairs();

	        tactics = user.getLevel7().getTactics();
	    }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public InfluxExchanges getInfluxExchanges(){
    	return influxExchanges;
    }


//-------------------------------------------------------------------------------------
    public Repositories getRepositories(){
    	return repositories;
    }

//-------------------------------------------------------------------------------------
    public Indicators getIndicators(){
    	return indicators;
    }

//-------------------------------------------------------------------------------------
    public Strategies getStrategies(){
    	return strategies;
    }

//-------------------------------------------------------------------------------------
    public MarketModels getMarketModels(){
    	return marketModels;
    }

//-------------------------------------------------------------------------------------
    public Signals getSignals(){
    	return signals;
    }

//-------------------------------------------------------------------------------------
    public Pilots getPilots(){
    	return pilots;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserExchanges getUserExchanges(){
    	return userExchanges;
    }

//-------------------------------------------------------------------------------------
    public RawBalances getRawBalances(){
    	return rawBalances;
    }

//-------------------------------------------------------------------------------------
    public RawOrders getRawOrders(){
    	return rawOrders;
    }

//-------------------------------------------------------------------------------------
    public RawTrades getRawTrades(){
    	return rawTrades;
    }

//-------------------------------------------------------------------------------------
    public ShadowBalances getShadowBalances(){
    	return shadowBalances;
    }

//-------------------------------------------------------------------------------------
    public ShadowOrders getShadowOrders(){
    	return shadowOrders;
    }

//-------------------------------------------------------------------------------------
    public Agents getAgents(){
    	return agents;
    }

//-------------------------------------------------------------------------------------
    public Mixers getMixers(){
    	return mixers;
    }

//-------------------------------------------------------------------------------------
    public Orderbooks getOrderbooks(){
    	return orderbooks;
    }

//-------------------------------------------------------------------------------------
    public Brokers getBrokers(){
    	return brokers;
    }

//-------------------------------------------------------------------------------------
    public Pairs getPairs(){
    	return pairs;
    }

//-------------------------------------------------------------------------------------
    public Tactics getTactics(){
    	return tactics;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
@SuppressWarnings("unchecked")
//-------------------------------------------------------------------------------------
    private void addCachedConfiguration(String _configurationName, Class _clazz){
		String _path = SystemUtils.getHomeDirectory() + "/" + serverConfiguration.getPathToSettings();
    	Configuration _configuration = new ConfigurationLoader(debug, _path, _clazz).load(_configurationName);
    	cachedConfigurations.put(_configurationName, _configuration);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
