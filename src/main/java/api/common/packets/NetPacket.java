//-------------------------------------------------------------------------------------
package codex.xbit.api.common.packets;
//-------------------------------------------------------------------------------------
import java.io.*;
//import java.time.*;

import codex.common.utils.*;

//-------------------------------------------------------------------------------------
@SuppressWarnings("serial")
public class NetPacket implements Serializable {
// 0. for NetPool 
// !=0 -> netServer
    protected int type;
    protected int counter;
    protected long startTime;

    protected long parameter0;
    protected long parameter1;
    protected long parameter2;
    protected Object message;


    public static final int TYPE_CONTROL = 0;
    public static final int TYPE_COMMAND = 1;

//-------------------------------------------------------------------------------------
    public NetPacket(int _type) {
        this(_type, -1, -1, -1, null);
	}

//-------------------------------------------------------------------------------------
    public NetPacket(int _type, long _parameter0, long _parameter1, long _parameter2, Object _message) {
        type = _type;
        parameter0 = _parameter0;
        parameter1 = _parameter1;
        parameter2 = _parameter2;
        message = _message;
        counter = 0;
    }

//-------------------------------------------------------------------------------------
    public int getType() {
        return type;
    }

//-------------------------------------------------------------------------------------
    public void setCounter(int _counter) {
        counter = _counter;
    }

//-------------------------------------------------------------------------------------
    public long getStartTime() {
        return startTime;
    }

//-------------------------------------------------------------------------------------
    public void setStartTime(long _startTime) {
        startTime = _startTime;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public Object getMessage(){
        return message;
    }

//-------------------------------------------------------------------------------------
    public long getParameter(){
        return parameter0;
    }

//-------------------------------------------------------------------------------------
    public long getParameter0(){
        return parameter0;
    }

//-------------------------------------------------------------------------------------
    public long getParameter1(){
        return parameter1;
    }

//-------------------------------------------------------------------------------------
    public void setMessage(Object _message){
        message = _message;
    }

//-------------------------------------------------------------------------------------
    public String toString() {
//        String _string = "serverId:" + serverId + ", clientId: " + StringUtils.getHex(clientId);
        String _string = "";
        switch(type){
            case TYPE_CONTROL: return _string + ", CONTROL";
            case TYPE_COMMAND: return _string + ", COMMAND";
            default : return "ERROR";
        }
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
