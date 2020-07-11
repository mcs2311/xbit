//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.single.level3.strategies.helpers;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.util.*;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.*;

import org.ta4j.core.*;
import org.ta4j.core.num.*;
import org.ta4j.core.trading.rules.*;
import org.ta4j.core.indicators.*;
import org.ta4j.core.indicators.helpers.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.loaders.*;


import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

import codex.xbit.api.common.aspects.*;
import codex.xbit.api.server.trader.core.components.*;

import codex.xbit.api.server.trader.env.single.level2.indicators.*;
import codex.xbit.api.server.trader.env.single.level3.strategies.*;

//-------------------------------------------------------------------------------------
public class GlobalExtremaStrategy extends StrategyX {
	private String barDuration;
	private int barCount;
	private BigDecimal longMultiplier;
	private BigDecimal shortMultiplier;

	//---cache:

	//---statics:
	
//-------------------------------------------------------------------------------------
    public GlobalExtremaStrategy(Debug _debug, Resolver _resolver, StrategyConfiguration _strategyConfiguration, CurrencyPair _currencyPair, Map<String,String> _args) {
    	super(_debug, _resolver, _strategyConfiguration, null, _currencyPair, _args);

    	barDuration = _args.get("barDuration");
    	barCount = StringUtils.getNumber(_args.get("barCount"));
    	longMultiplier = new BigDecimal(_args.get("longMultiplier"));
    	shortMultiplier = new BigDecimal(_args.get("shortMultiplier"));

    	List<String> _indicator0 = Arrays.asList("close_price", barDuration);
    	configuration.addIndicator(_indicator0);

    	List<String> _indicator1 = Arrays.asList("high_price", barDuration);
    	configuration.addIndicator(_indicator1);

    	List<String> _indicator2 = Arrays.asList("low_price", barDuration);
    	configuration.addIndicator(_indicator2);

//    	List<String> _indicator3 = Arrays.asList("hma", barDuration);
//    	configuration.addIndicator(_indicator3);


//    	configuration.setIndicators(_indicators);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void buildStrategy(BeanEvent _beanEvent) {
		TradeEvent _tradeEvent = (TradeEvent)_beanEvent;

    	String _name = configuration.getName();
    	int _index = _tradeEvent.getIndex();
		
		CachedIndicator<Num> _closePrices = indicatorsMatrix.get(0);
//		CachedIndicator<Num> _hma = indicatorsMatrix.get(3);

		// Getting the max price over the past week
    	CachedIndicator<Num> _hiPrice = indicatorsMatrix.get(1);
 		HighestValueIndicator _maxPrice = new HighestValueIndicator(_hiPrice, barCount);

 		// Getting the min price over the past week
     	CachedIndicator<Num> _loPrice = indicatorsMatrix.get(2);
		LowestValueIndicator _minPrice = new LowestValueIndicator(_loPrice, barCount);

        // Going long if the close price goes below the min price
        MultiplierIndicator _downWeek = new MultiplierIndicator(_minPrice, longMultiplier.doubleValue());
        Rule _buyingRule = new UnderIndicatorRule(_closePrices, _downWeek);

        // Going short if the close price goes above the max price
        MultiplierIndicator _upWeek = new MultiplierIndicator(_maxPrice, shortMultiplier.doubleValue());
        Rule _sellingRule = new OverIndicatorRule(_closePrices, _upWeek);

		strategy = new BaseStrategy(_buyingRule, _sellingRule);

    	debug.outln(Debug.INFO, "["+currencyPair+"]: GlobalExtremaStrategy builded. Waiting for hi=["+_upWeek.getValue(_index)+"], lo=["+_downWeek.getValue(_index)+"] variation...barCount="+barCount);
//    	debug.outln(Debug.INFO, "["+currencyPair+"]: GlobalExtremaStrategy builded. Waiting for hi=["+_hiPrice.getValue(_index)+"], lo=["+_loPrice.getValue(_index)+"] variation...");
//    	debug.outln(Debug.INFO, "["+currencyPair+"]: GlobalExtremaStrategy builded. Waiting for hi=["+_maxPrice.getValue(_index)+"], lo=["+_minPrice.getValue(_index)+"] variation...");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
