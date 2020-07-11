//-------------------------------------------------------------------------------------
package codex.common.utils;
//-------------------------------------------------------------------------------------
import java.util.*;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;

//-------------------------------------------------------------------------------------
public class CurrencyUtils {
    
//-------------------------------------------------------------------------------------
/*	public List<CurrencyPair> getAllCurrencies(){
		Set<Event> _set = eventListeners.keySet();
		HashSet<CurrencyPair> _pairs = new HashSet<CurrencyPair>();
		Iterator<Event> _iterator = _set.iterator();
		while (_iterator.hasNext()) {
            Event _event = _iterator.next();
            _pairs.add(_event.getCurrencyPair());
		}
		List<CurrencyPair> _list = new ArrayList<CurrencyPair>();
		_list.addAll(_pairs);
		return _list;
	}
*/

//-------------------------------------------------------------------------------------
	public static String easyToReadFormat(long _number){
		double _n = (double)_number/(double)1000000;
		return String.format("%.2fm", _n);
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
