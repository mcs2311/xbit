//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;
import java.util.concurrent.*;

import org.knowm.xchange.dto.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


import codex.xbit.api.server.trader.*;
import codex.xbit.api.server.trader.common.events.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;
import codex.xbit.api.server.trader.core.components.*;
import codex.xbit.api.server.trader.core.components.orders.*;

import codex.xbit.api.server.trader.env.single.level4.marketmodels.*;
//import codex.xbit.api.server.trader.env.single.level4.marketmodels.helpers.layer1.marketwisdom.*;


import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.*;
import codex.xbit.api.server.trader.env.users.level4.orderbooks.sides.orders.decisionchain.chains.*;

//-------------------------------------------------------------------------------------
public class DecisionChain {
	protected Debug debug;

	protected OrderX order;
    protected Decision decision;

    protected S0_PriceExtractor 				s0_priceExtractorChain;
    protected S1_SignalFilterChain 				s1_signalFilterChain;
    protected S2_ProfitExtractorChain 			s2_profitExtractorChain;
    protected S3_TrailingProcentChain 			s3_trailingProcentChain;
    protected S4_ProfitPredictionChain 			s4_profitPredictionChain;
    protected S5_WisenessCalculatorChain 		s5_wisenessCalculatorChain;
    protected S6_SignalFilterChain 				s6_signalFilterChain;
    protected S7_PriceCalculatorChain 			s7_priceCalculatorChain;
    protected S8_VariationFilterChain 			s8_variationFilterChain;

    //---cache:

    //---statics:
    public static final int SCALE_TRAILING_STOP_LOSS_PROCENT = 1;

//-------------------------------------------------------------------------------------
    public DecisionChain(Debug _debug, Resolver _resolver, int _type, MarketModel _marketModel, Scales _scales, TacticConfiguration _tacticConfiguration, OrderX _order) {
    	debug = _debug;

    	order = _order;
    	BigDecimal _price = _order.getPrice();
        decision = new Decision(_scales);

        s0_priceExtractorChain 		= new S0_PriceExtractor(_debug, _marketModel);
        s1_signalFilterChain 		= new S1_SignalFilterChain(_debug, _resolver, _type);
    	s2_profitExtractorChain 	= new S2_ProfitExtractorChain(_debug, _price);
    	s3_trailingProcentChain 	= new S3_TrailingProcentChain(_debug, _type, _tacticConfiguration);
    	s4_profitPredictionChain 	= new S4_ProfitPredictionChain(_debug, _type, _marketModel);
    	s5_wisenessCalculatorChain 	= new S5_WisenessCalculatorChain(_debug, _type, _tacticConfiguration);
		s6_signalFilterChain 		= new S6_SignalFilterChain(_debug, _resolver, _type, _tacticConfiguration, _order);
    	s7_priceCalculatorChain 	= new S7_PriceCalculatorChain(_debug, _type);
		s8_variationFilterChain 	= new S8_VariationFilterChain(_debug, _type, decision, 0.001f, 0.002f, 0.002f, 0.001f);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public boolean shallIBuy(Event _event){
    	float _currentPrice 	= s0_priceExtractorChain.extract(_event);
    	Event _event0 			= s1_signalFilterChain.filter(_event);
    	float _currentProfit 	= s2_profitExtractorChain.profit(_currentPrice);
    	float _trailingProcent 	= s3_trailingProcentChain.trail(_currentProfit);
    	float _predictedProfit 	= s4_profitPredictionChain.predict(_event0, _currentProfit);
    	float _wiseness 		= s5_wisenessCalculatorChain.wiseness(_predictedProfit, _currentProfit, _trailingProcent);
    	int _result				= s6_signalFilterChain.filter(S6_SignalFilterChain.BROKER_MODE, _event0, _wiseness);
//		debug.outln(Debug.ERROR, "shallIBuy: _result=" + _result);
    	return (_result == Decision.DECISION_CHANGE_ORDER);
    }

//-------------------------------------------------------------------------------------
    public boolean shallISell(Event _event){
    	float _currentPrice 	= s0_priceExtractorChain.extract(_event);
    	Event _event0 			= s1_signalFilterChain.filter(_event);
    	float _currentProfit 	= s2_profitExtractorChain.profit(_currentPrice);
    	float _trailingProcent 	= s3_trailingProcentChain.trail(_currentProfit);
    	float _predictedProfit 	= s4_profitPredictionChain.predict(_event0, _currentProfit);
    	float _wiseness 		= s5_wisenessCalculatorChain.wiseness(_predictedProfit, _currentProfit, _trailingProcent);
    	int _result 			= s6_signalFilterChain.filter(S6_SignalFilterChain.BROKER_MODE, _event0, _wiseness);
//		debug.outln(Debug.ERROR, "shallISell: _result=" + _result);
    	return (_result == Decision.DECISION_CHANGE_ORDER);
    }

//-------------------------------------------------------------------------------------
    public Decision recalculate(Event _event){
    	float _currentPrice 	= s0_priceExtractorChain.extract(_event);
    	Event _event0 			= s1_signalFilterChain.filter(_event);
    	float _currentProfit 	= s2_profitExtractorChain.profit(_currentPrice);
    	float _trailingProcent 	= s3_trailingProcentChain.trail(_currentProfit);
    	float _predictedProfit 	= s4_profitPredictionChain.predict(_event0, _currentProfit);
    	float _wiseness 		= s5_wisenessCalculatorChain.wiseness(_predictedProfit, _currentProfit, _trailingProcent);
    	int _result 			= s6_signalFilterChain.filter(S6_SignalFilterChain.AGENT_MODE, _event0, _wiseness);
    	if(_result == Decision.DECISION_ABANDON_ORDER){
    		return Decision.ABANDON;
    	} else if(_result == Decision.DECISION_DONT_TOUCH_ORDER){
    		return Decision.DONT_TOUCH;
    	} else {
	    	float _price 		= s7_priceCalculatorChain.price(_event0, _currentPrice, _wiseness);
	    	Decision _decision  = s8_variationFilterChain.filter(_price);
    		return _decision;
    	}
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
/*    protected float loadAmountBaseCurrency() {
//    	OrderXConfiguration _closingOrderXConfiguration = orderbookConfiguration.getClosingOrderX();
		BigDecimal _amountBaseBD = order.getAmount();
		float _amountBase = _amountBaseBD.floatValue();
		return _amountBase;
	}
*/

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
