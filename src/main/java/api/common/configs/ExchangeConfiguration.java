//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.math.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.*;
import codex.common.utils.*;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import com.fasterxml.jackson.annotation.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class ExchangeConfiguration extends AbstractConfiguration {
	private String shortName;
	private String exchangeName;
	private String streamingName;
	private String userName;
    private String apiKey;
    private String secretKey;
    private boolean needsToSubscribeBeforeConnect;
    private boolean useCompression;
    private boolean supportForStreamingBalanceChanges;
    private boolean supportForStreamingOrderChanges;
    private boolean supportForStreamingTradeChanges;

	private String mainWalletName;
	private String tradingWalletName;

    @JsonFormat(shape=JsonFormat.Shape.STRING)
    private Map<Currency, BigDecimal> minimums;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
	private Map<Currency, Currency> currenciesmap;
    @JsonFormat(shape=JsonFormat.Shape.STRING)
	private Map<CurrencyPair, CurrencyPair> pairsmap;
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ExchangeConfiguration(){
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public String getShortName(){
		if(shortName != null){
			return shortName;
		}
		return getName();
	}

//-------------------------------------------------------------------------------------
	public String getExchangeName(){
		return exchangeName;
	}

//-------------------------------------------------------------------------------------
	public String getStreamingName(){
		return streamingName;
	}

//-------------------------------------------------------------------------------------
	public String getUserName(){
		return userName;
	}

//-------------------------------------------------------------------------------------
	public String getApiKey(){
		return apiKey;
	}

//-------------------------------------------------------------------------------------
	public String getSecretKey(){
		return secretKey;
	}

//-------------------------------------------------------------------------------------
	public boolean needsToSubscribeBeforeConnect(){
		return needsToSubscribeBeforeConnect;
	}

//-------------------------------------------------------------------------------------
	public boolean useCompression(){
		return useCompression;
	}

//-------------------------------------------------------------------------------------
	public boolean supportForStreamingBalanceChanges(){
		return supportForStreamingBalanceChanges;
	}

//-------------------------------------------------------------------------------------
	public boolean supportForStreamingOrderChanges(){
		return supportForStreamingOrderChanges;
	}

//-------------------------------------------------------------------------------------
	public boolean supportForStreamingTradeChanges(){
		return supportForStreamingTradeChanges;
	}

//-------------------------------------------------------------------------------------
	public String getMainWalletName(){
		return mainWalletName;
	}

//-------------------------------------------------------------------------------------
	public String getTradingWalletName(){
		return tradingWalletName;
	}

//-------------------------------------------------------------------------------------
	public Map<Currency, BigDecimal> getMinimums(){
		return minimums;
	}

//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPairCode(CurrencyPair _currencyPair){
		return pairsmap.get(_currencyPair);
	}

//-------------------------------------------------------------------------------------
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Map.Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}

//-------------------------------------------------------------------------------------
	public CurrencyPair getCurrencyPair(CurrencyPair _currencyPairCode){
		return getKeyByValue(pairsmap, _currencyPairCode);
	}

//-------------------------------------------------------------------------------------
	public Currency getCurrency(Currency _currencyCode){
		return getKeyByValue(currenciesmap, _currencyCode);
	}

//-------------------------------------------------------------------------------------
	public Currency getCurrencyCode(Currency _currency){
		return currenciesmap.get(_currency);
	}

//-------------------------------------------------------------------------------------
	public Map<Currency, Currency> getCurrencyMap(){
		return currenciesmap;
	}

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    @JsonIgnore
    public List<String> getExchangesInfo() {
        List<String> _list = new ArrayList<String>();
        String _info;
        _info = shortName+": enabled:"+enabled;
        _list.add(_info);
        return _list;
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
