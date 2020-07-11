//-------------------------------------------------------------------------------------
package codex.xbit.api.common.loaders;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.prefs.*;
import org.ini4j.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;



//-------------------------------------------------------------------------------------
public class ClientConfigurationLoader {
    private Debug debug;
    private ClientConfiguration clientConfiguration;

    private Wini ini;
    private int version;

//-------------------------------------------------------------------------------------
    public ClientConfigurationLoader(Debug _debug) {
        debug = _debug;
        clientConfiguration = new ClientConfiguration(_debug);
        load();
        clientConfiguration.link();
    }
    
//-------------------------------------------------------------------------------------
    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

//-------------------------------------------------------------------------------------
    private void load() {
        String _home = SystemUtils.getHomeDirectory();
        String _inifile = _home + "/.xbit/xclient.ini";
        debug.outln("Loading "+_inifile+" ...");
        ini = new Wini();
        try{
            FileInputStream _input = new FileInputStream(_inifile);
            ini.load(_input);
        } catch (IOException _e) {
            _e.printStackTrace();
        }

        Preferences _prefs = new IniPreferences(ini);
        Preferences _p0 = _prefs.node("GENERAL");
		clientConfiguration.load(_p0);
/*        version = _p0.getInt("version", 0);
        zoneId = _p0.get("zoneId", "");
        lastUsedUser = _p0.get("lastUsedUser", "");
        lastUsedServer = _p0.get("lastUsedServer", "");*/


        int i = 0;
        Preferences _p;
        boolean _exists = false;
        while(true){
            try{
                _exists = _prefs.nodeExists("SERVER"+i);
            }catch(BackingStoreException _e){}
            if(_exists == false){
                if(i > 10){
                    break;
                }
            } else {
                _p = _prefs.node("SERVER"+i);
                ServerConfiguration _serverConfiguration = new ServerConfiguration(debug);
                _serverConfiguration.load(_p);
                clientConfiguration.addServerConfiguration(_serverConfiguration);
//                debug.outln(Debug.IMPORTANT1, "Loading ["+_serverConfiguration.getAlias()+"] -> ["+_serverConfiguration.getHost()+":"+_serverConfiguration.getPort()+"]...");
            }
            i++;
        }

    }

//-------------------------------------------------------------------------------------
    public void save() {
        String _home = SystemUtils.getHomeDirectory();
        String _inifile = _home + "/.xbit/xclient.ini";
        Ini.Section _section;
        debug.outln("Saving "+_inifile+" ...");

        _section = ini.get("GENERAL");
        clientConfiguration.save(_section);
/*        _section.put("version", version++);
        _section.put("lastUsedUser", lastUsedUser);
        _section.put("lastUsedServer", lastUsedServer);*/

        try{
            FileOutputStream _output = new FileOutputStream(_inifile);
            ini.store(_output);
            _output.close();
        } catch (IOException _e) {
            _e.printStackTrace();
        }        
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
