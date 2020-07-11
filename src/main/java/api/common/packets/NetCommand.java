//-------------------------------------------------------------------------------------
package codex.xbit.api.common.packets;
//-------------------------------------------------------------------------------------
//import java.io.*;
//import java.util.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class NetCommand extends NetPacket {
    private int command;

//---LEVEL 0:
    public static final int COMMAND_NONE = 0;
    public static final int COMMAND_PING = 1;

    public static final int COMMAND_OK      = 2;
    public static final int COMMAND_ERROR   = 3;

    public static final int COMMAND_RELOAD = 1000;

    public static final int COMMAND_SHOW = 2000;
    public static final int COMMAND_STRATEGY = 2001;

    public static final int COMMAND_CHANGE_PILOT_MODE = 2100;
    public static final int COMMAND_CHANGE_PILOT_TYPE = 2101;

    public static final int COMMAND_CHANGE_TRADER_MODE 	= 3000;
    public static final int COMMAND_CHANGE_TRADER_STATE = 3001;


    public static final int COMMAND_DEAL = 100000;
    public static final int COMMAND_PROFIT = 100001;

    public static final int COMMAND_BUY 	= 200000;
    public static final int COMMAND_SELL 	= 200001;


    public static final int COMMAND_QUIT = 9999999;

//---LEVEL 1:
    public static final int COMMAND_SHOW_EXCHANGES = 20000;
    public static final int COMMAND_SHOW_ORDERBOOKS = 20001;
    public static final int COMMAND_SHOW_WHALES = 20002;
    public static final int COMMAND_SHOW_USERS = 20003;
    public static final int COMMAND_SHOW_TACTICS = 20004;
    public static final int COMMAND_SHOW_CURRENCYPAIRS = 20005;

//---LEVEL 2:
    public static final int COMMAND_SHOW_WHALES_SUMMARY = 200020;
    public static final int COMMAND_SHOW_WHALES_TRANSACTIONS = 200021;
//    public static final int COMMAND_SHOW_USER_EXCHANGES = 200030;


//-------------------------------------------------------------------------------------
    public NetCommand(int _command) {
        this(_command, -1, -1);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, long _parameter) {
        this(_command, _parameter, -1);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, long _parameter0, long _parameter1) {
        this(_command, _parameter0, _parameter1, null);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, Object _message) {
        this(_command, -1, -1, _message);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, long _parameter, Object _message) {
        this(_command, _parameter, -1, _message);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, long _parameter0, long _parameter1, Object _message) {
        this(_command, _parameter0, _parameter1, -1, _message);
    }

//-------------------------------------------------------------------------------------
    public NetCommand(int _command, long _parameter0, long _parameter1, long _parameter2, Object _message) {
        super(TYPE_COMMAND, _parameter0, _parameter1, _parameter2, _message);
        command = _command;
    }

//-------------------------------------------------------------------------------------
    public int getCommand(){
        return command;
    }

//-------------------------------------------------------------------------------------
    public void setCommand(int _command){
        command = _command;
    }

//-------------------------------------------------------------------------------------
    public String toString(){
        String _string = super.toString() + ": ";
        switch(command){
            case COMMAND_NONE : return _string + "NONE";

            case COMMAND_PING : return _string + "PING";

            case COMMAND_OK : return _string + "OK";
            case COMMAND_ERROR : return _string + "ERROR";

            case COMMAND_SHOW : return _string + "SHOW";
            case COMMAND_STRATEGY : return _string + "STRATEGY";

            case COMMAND_CHANGE_PILOT_MODE : return _string + "CHANGE_PILOT_MODE";
            case COMMAND_CHANGE_PILOT_TYPE : return _string + "CHANGE_PILOT_TYPE";
            case COMMAND_CHANGE_TRADER_MODE : return _string + "CHANGE_TRADER_MODE";
            case COMMAND_CHANGE_TRADER_STATE : return _string + "CHANGE_TRADER_STATE";

            case COMMAND_DEAL : return _string + "COMMAND_DEAL";
            case COMMAND_PROFIT : return _string + "COMMAND_PROFIT";


            case COMMAND_QUIT : return _string + "QUIT";

            default : return _string + "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
