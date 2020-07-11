//-------------------------------------------------------------------------------------
package codex.xbit.api.common.loaders;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.prefs.*;
import java.time.*;

import org.ini4j.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;
import codex.xbit.api.server.trader.common.events.single.*;
import codex.xbit.api.server.trader.common.objects.*;

//-------------------------------------------------------------------------------------
public class ServerConfigurationLoader {
    private Debug debug;
    private ServerConfiguration serverConfiguration;

    private Wini ini;
    private int version;
    
//-------------------------------------------------------------------------------------
    public ServerConfigurationLoader(Debug _debug) {
        debug = _debug;
        serverConfiguration = new ServerConfiguration(debug);
        load();
        String _zoneIdString = serverConfiguration.getZoneId();
        ZoneId _zoneId;
        if(_zoneIdString.isEmpty()){
        	_zoneId = ZoneId.systemDefault();
        } else {
        	_zoneId = ZoneId.of(_zoneIdString);
        }
        debug.outln("ZoneId: "+ _zoneIdString + ", timenow=" + ZonedDateTime.now(_zoneId));

        BeanEvent.setZoneId(_zoneId);
    	TimeUtils.setZoneId(_zoneId);

        long  _startTime = ZonedDateTime.now(_zoneId).toInstant().toEpochMilli();
        serverConfiguration.setStartTime(_startTime);

    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    private String getPath() {
        String _home = SystemUtils.getHomeDirectory();
        String _inifile = _home + "/.xbit/xserver.ini";
        return _inifile;
    }

//-------------------------------------------------------------------------------------
    private void load() {
        String _inifile = getPath();
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
        version = _p0.getInt("version", 0);

        Preferences _p2 = _prefs.node("SERVER");
        serverConfiguration.load(_p2);

    }

//-------------------------------------------------------------------------------------
    public void save() {
        String _inifile = getPath();
        Ini.Section _section;
        debug.outln("Saving "+_inifile+" ...");

        _section = ini.get("GENERAL");
        _section.put("version", version++);

        _section = ini.get("SERVER");
        serverConfiguration.save(_section);
        try{
            FileOutputStream _output = new FileOutputStream(_inifile);
            ini.store(_output);
            _output.close();
        } catch (IOException _e) {
            _e.printStackTrace();
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
