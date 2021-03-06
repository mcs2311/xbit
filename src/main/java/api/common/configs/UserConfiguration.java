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
public class UserConfiguration extends AbstractConfiguration implements Serializable {
	private String userName;
    @JsonIgnore
    private List<AccountConfiguration> accounts;
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public UserConfiguration(String _userName){
    	userName = _userName;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
	public String getName(){
		return getUserName();
	}

//-------------------------------------------------------------------------------------
	public String getUserName(){
		return userName;
	}

//-------------------------------------------------------------------------------------
	public List<AccountConfiguration> getAccounts(){
		return accounts;
	}

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
