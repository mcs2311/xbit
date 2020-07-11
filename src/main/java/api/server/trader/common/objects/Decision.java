//-------------------------------------------------------------------------------------
package codex.xbit.api.server.trader.common.objects;
//-------------------------------------------------------------------------------------
import java.math.*;
import java.util.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;


//-------------------------------------------------------------------------------------
public class Decision {
	private BigDecimal price;
	private BigDecimal amount;
	private int decision;
	private Scales scales;

    //---statics:
    public static final int DECISION_NONE 				= 0;
    public static final int DECISION_DONT_TOUCH_ORDER 	= 1;
    public static final int DECISION_CHANGE_ORDER 		= 2;
    public static final int DECISION_ABANDON_ORDER 		= 3;

    public static final Decision DONT_TOUCH = new Decision(BigDecimal.ZERO, BigDecimal.ZERO, DECISION_DONT_TOUCH_ORDER, Scales.DEFAULT);
    public static final Decision ABANDON = new Decision(BigDecimal.ZERO, BigDecimal.ZERO, DECISION_ABANDON_ORDER, Scales.DEFAULT);
//-------------------------------------------------------------------------------------
    public Decision() {
    	this(BigDecimal.ZERO, BigDecimal.ZERO, DECISION_DONT_TOUCH_ORDER, new Scales());
    }


//-------------------------------------------------------------------------------------
    public Decision(Decision _decision) {
    	this(_decision.price, _decision.amount, DECISION_NONE, new Scales());
    }

//-------------------------------------------------------------------------------------
    public Decision(Scales _scales) {
    	this(BigDecimal.ZERO, BigDecimal.ZERO, DECISION_DONT_TOUCH_ORDER, _scales);
    }

 //-------------------------------------------------------------------------------------
    public Decision(BigDecimal _price, BigDecimal _amount, int _decision, Scales _scales) {
    	price = _price.setScale(_scales.getPrice(), RoundingMode.HALF_UP);
    	amount = _amount.setScale(_scales.getAmount(), RoundingMode.HALF_UP);
    	decision = _decision;
    	scales = _scales;
    }

 //-------------------------------------------------------------------------------------
    public Decision clone() {
    	return new Decision(this.price, this.amount, this.decision, this.scales);
    }

//-------------------------------------------------------------------------------------
    public int getDecision() {
    	return decision;
    }

//-------------------------------------------------------------------------------------
    public void setDecision(int _decision) {
    	decision = _decision;
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getPrice() {
    	return price;
    }

//-------------------------------------------------------------------------------------
    public void setPrice(BigDecimal _price) {
    	if (_price.compareTo(BigDecimal.ZERO) < 0) {
    		new Exception("Price cannot be negative!").printStackTrace();
    	}
    	price = _price.setScale(scales.getPrice(), RoundingMode.HALF_UP);
    }

//-------------------------------------------------------------------------------------
    public void setPrice(float _price) {
    	setPrice(BigDecimal.valueOf(_price));
    }

//-------------------------------------------------------------------------------------
    public BigDecimal getAmount() {
    	return amount;
    }

//-------------------------------------------------------------------------------------
    public void setAmount(BigDecimal _amount) {
    	if (_amount.compareTo(BigDecimal.ZERO) < 0) {
    		new Exception("Amount cannot be negative!").printStackTrace();
    	}
    	amount = _amount.setScale(scales.getAmount(), RoundingMode.HALF_UP);
    }

//-------------------------------------------------------------------------------------
    public void setAmount(float _amount) {
    	setAmount(BigDecimal.valueOf(_amount));
    }

//-------------------------------------------------------------------------------------
    public void setScales(int _amount, int _price) {
    	scales = new Scales(_amount, _price);
    	amount.setScale(_amount, RoundingMode.HALF_UP);
    	price.setScale(_price, RoundingMode.HALF_UP);
    }


//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public String getDecisionAsString() {
    	switch(decision){
    		case DECISION_NONE 				: return "DECISION_NONE";
    		case DECISION_DONT_TOUCH_ORDER 	: return "DECISION_DONT_TOUCH_ORDER";
    		case DECISION_CHANGE_ORDER 		: return "DECISION_CHANGE_ORDER";
    		case DECISION_ABANDON_ORDER		: return "DECISION_ABANDON_ORDER";
    		default : return "DECISION_UNKNOWN";
    	}
    }

//-------------------------------------------------------------------------------------
    public String toString() {
    	return getDecisionAsString() + " : " + price + " : " + amount;
    }
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
