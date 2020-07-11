//-------------------------------------------------------------------------------------
package codex.xbit.api.common.configs;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
//import java.lang.reflect.*;

import org.knowm.xchange.currency.*;
import com.fasterxml.jackson.annotation.*;

import codex.common.utils.*;
import codex.xbit.api.common.aspects.*;
import codex.xbit.api.common.loaders.*;


//-------------------------------------------------------------------------------------
public interface Configuration extends OperableAspect {
//-------------------------------------------------------------------------------------
    public void initiate(Debug _debug, String _name, ConfigurationLoader _configurationLoader);
//-------------------------------------------------------------------------------------
    public void setDebug(Debug _debug);
//-------------------------------------------------------------------------------------
	public boolean isEnabled();
//-------------------------------------------------------------------------------------
	public void setEnabled(boolean _enabled);
//-------------------------------------------------------------------------------------
	public int getId();
//-------------------------------------------------------------------------------------
	public void setId(int _id);
//-------------------------------------------------------------------------------------
	public String getName();
//-------------------------------------------------------------------------------------
	public void setName(String _name);
//-------------------------------------------------------------------------------------
    @JsonIgnore
	public String getDescriptor();
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
