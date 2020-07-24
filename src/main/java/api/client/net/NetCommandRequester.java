//-------------------------------------------------------------------------------------
package codex.xbit.api.client.net;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.functions.*;
import io.reactivex.rxjava3.schedulers.*;

import codex.common.utils.*;

import codex.xbit.api.common.configs.*;
import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;

//-------------------------------------------------------------------------------------
public class NetCommandRequester implements Consumer<Throwable>{
    private Debug debug;
    private ServerConfiguration serverConfiguration;
    private NetClient netClient;
    private NetStream netStream;

//-------------------------------------------------------------------------------------
    public NetCommandRequester(Debug _debug, ServerConfiguration _serverConfiguration, NetClient _netClient) {
        debug = _debug;
//        _debug.outln("Create NetCommandRequester for ["+_serverConfiguration+"]...");
        serverConfiguration = _serverConfiguration;
        netClient = _netClient;
        netStream = new NetStream(_debug, _serverConfiguration.getHost(), _serverConfiguration.getPort());
//        start();
    }

//-------------------------------------------------------------------------------------
/*    public void run(){
        receive();
    }*/

//-------------------------------------------------------------------------------------
    public void send(NetPacket _packet){
        send(_packet, 0);
    }

//-------------------------------------------------------------------------------------
    public void send(NetPacket _packet, int _timeout){
        netStream.writePacket(_packet, _timeout);
    }

//-------------------------------------------------------------------------------------
    public void send(NetPacket _packet, Consumer<NetPacket> _consumer){
		Flowable.fromCallable(() -> {
		    send(_packet);
		    return receive();
		})
		.subscribeOn(Schedulers.io())
		.observeOn(Schedulers.single())
		.subscribe(_consumer, 
			this);
	}

//-------------------------------------------------------------------------------------
    public NetPacket receive(){
    	return receive(0);
    }

//-------------------------------------------------------------------------------------
    public NetPacket receive(int _timeout){
        NetPacket _packet;
        _packet = netStream.readPacket(_timeout);
        if(_packet != null){
            long _startTime = _packet.getStartTime();
            serverConfiguration.setStartTime(_startTime);
        }
        return _packet;
    }

//-------------------------------------------------------------------------------------
    public void accept(Throwable _t) {
        ExceptionUtils.printException(debug, this, _t);
    }

//-------------------------------------------------------------------------------------
    public void close() {
        netStream.close();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
