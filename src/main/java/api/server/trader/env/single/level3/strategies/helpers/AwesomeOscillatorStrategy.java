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
public class AwesomeOscillatorStrategy extends StrategyX {
	private String barDuration;
	private String variation;

	//---cache:
	private BigDecimal variationBD;
	
	//---statics:

//-------------------------------------------------------------------------------------
    public AwesomeOscillatorStrategy(Debug _debug, Resolver _resolver, StrategyConfiguration _strategyConfiguration, CurrencyPair _currencyPair, Map<String, String> _args) {
    	super(_debug, _resolver, _strategyConfiguration, null, _currencyPair, _args);

    	barDuration = _args.get("barDuration");
    	variation = _args.get("variation");
    	variationBD = new BigDecimal(variation);

    	List<String> _indicator = Arrays.asList("awsome_oscillator", barDuration);

    	configuration.addIndicator(_indicator);
//    	configuration.setIndicators(_indicators);
    	load();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void buildStrategy(BeanEvent _beanEvent) {
		TradeEvent _tradeEvent = (TradeEvent)_beanEvent;

    	String _name = configuration.getName();
    	CachedIndicator<Num> _ao = indicatorsMatrix.get(0);

//    	BeanEvent _beanEvent = (BeanEvent)_event.getObject();
    	BigDecimal _price = _tradeEvent.getPrice();

    	BigDecimal _percentP = _price.multiply(variationBD);
    	BigDecimal _percentM = BigDecimal.ZERO.subtract​(_percentP);
		Num _plus100 = PrecisionNum.valueOf(_percentP);
		Num _minus100 = PrecisionNum.valueOf(_percentM);


		Rule _entryRule = new UnderIndicatorRule(_ao, _minus100);
		Rule _exitRule = new OverIndicatorRule(_ao, _plus100);
		strategy = new BaseStrategy(_name, _entryRule, _exitRule);
    	debug.outln(Debug.INFO, "["+currencyPair+"]: AwsomeOscillatorStrategy builded. Waiting for +/-["+_percentP+"] variation...");
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
