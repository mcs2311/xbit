//-------------------------------------------------------------------------------------
package codex.xbit.api.common.streams;

//-------------------------------------------------------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import codex.common.utils.*;
import codex.xbit.api.common.packets.*;

//-------------------------------------------------------------------------------------
public class NetStream extends Thread {
    private Debug debug;
    private Socket socket;
    private String host;
    private int port;


    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private int connectStatus;

    public static final String DEFAULT_HOSTNAME = "localhost";

    public static final int STATUS_NOTCONNECTED = 10;
    public static final int STATUS_DISCONNECTED_ON_DEMAND = 11;
    public static final int STATUS_DISCONNECTED_ON_ERROR = 12;
    public static final int STATUS_CONNECTING = 13;
    public static final int STATUS_CONNECTED = 14;
    public static final int STATUS_CLOSED = 15;

    private final Object oLock = new Object();

//-------------------------------------------------------------------------------------
    public NetStream(Debug _debug, String _host, int _port) {
        debug = _debug;
        host = _host;
        port = _port;
        setStatus(STATUS_CONNECTING);
        start();
    }

//-------------------------------------------------------------------------------------
    public NetStream(Debug _debug, Socket _socket) {
        debug = _debug;
        socket = _socket;
        setStatus(STATUS_CONNECTED);
        createStreams();
    }

//-------------------------------------------------------------------------------------
    public synchronized void setStatus(int _connectStatus) {
//        debug.outln("NetStream.setStatus=" + getStatusAsString());
        connectStatus = _connectStatus;
//            updateListenerNetworkStatus(connectStatus, port);
    }

//-------------------------------------------------------------------------------------
    public synchronized int getStatus() {
        return connectStatus;
    }

//-------------------------------------------------------------------------------------
    public String getStatusAsString() {
        switch (connectStatus) {
        case STATUS_NOTCONNECTED: return "NOTCONNECTED";
        case STATUS_DISCONNECTED_ON_DEMAND: return "DISCONNECTED_ON_DEMAND";
        case STATUS_DISCONNECTED_ON_ERROR: return "DISCONNECTED_ON_ERROR";
        case STATUS_CONNECTING: return "CONNECTING";
        case STATUS_CONNECTED: return "CONNECTED";
        default : return "Unknown status!";
        }
    }

//-------------------------------------------------------------------------------------
    public synchronized int getPort() {
        return port;
    }

//-------------------------------------------------------------------------------------
    public void connect() {
        setStatus(STATUS_CONNECTING);
    }

//-------------------------------------------------------------------------------------
    public void reconnect() {
        setStatus(STATUS_DISCONNECTED_ON_ERROR);
        this.interrupt();
        delay(100);
    }

//-------------------------------------------------------------------------------------
    public void disconnect() {
        if (getStatus() == STATUS_CONNECTED) {
            close();
        }
        setStatus(STATUS_DISCONNECTED_ON_DEMAND);
    }

//-------------------------------------------------------------------------------------
    public void run() {
        int _sleepTimer = 1000;
        setStatus(STATUS_CONNECTING);
        while (true) {
            int _status = getStatus();
//            debug.outln("netStream.run.while iteration:" + toString());
            switch (_status) {
            case STATUS_NOTCONNECTED: {
                _sleepTimer = 100;
                setStatus(STATUS_CONNECTING);
                break;
            }
            case STATUS_DISCONNECTED_ON_ERROR: {
                _sleepTimer = 100;
//                changePort();
                setStatus(STATUS_CONNECTING);
                break;
            }
            case STATUS_DISCONNECTED_ON_DEMAND: {
                _sleepTimer = 100;
                break;
            }
            case STATUS_CONNECTING: {
                _sleepTimer = 200;
                try {
                    InetAddress _host = InetAddress.getByName(host);
//                        debug.outln("Connecting to ["+_host.getHostName()+":"+port+"] ...");
                    socket = new Socket(_host.getHostName(), port);
                    //            debug.outln("_socket created!-----0");
                    //                setStatus(STATUS_CONNECTED);
                    createStreams();
                } catch (IOException _e) {
//                        debug.outln("Cannot connect to ["+hostname+":"+port+"] ...");
                    //closeConnection();
                    //return;
                }
                break;
            }
            case STATUS_CONNECTED: {
                _sleepTimer = 1000;
                break;
            }
            case STATUS_CLOSED: {
                return;
            }
            }//end switch
            delay(_sleepTimer);
        }//end while
    }

//-------------------------------------------------------------------------------------
    public boolean isNotConnected() {
        return (connectStatus == STATUS_NOTCONNECTED);
    }

//-------------------------------------------------------------------------------------
    public boolean isConnected() {
        return (connectStatus == STATUS_CONNECTED);
    }

//-------------------------------------------------------------------------------------
    private void createStreams() {
        if (socket == null) {
            setStatus(STATUS_DISCONNECTED_ON_ERROR);
            return;
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
//            debug.outln("_socket created!-----1");
//            writeData(new NetPacket(debug));
//            debug.outln("_socket created!-----2");
            ois = new ObjectInputStream(socket.getInputStream());
//            debug.outln("_socket created!-----3");
            setStatus(STATUS_CONNECTED);
            return;
        } catch (IOException _e) {
//            debug.outln("_socket exception!!!!-----4");
        }
        setStatus(STATUS_DISCONNECTED_ON_ERROR);
    }

//-------------------------------------------------------------------------------------
    public NetPacket readPacket() {
    	return readPacket(0);
    }

//-------------------------------------------------------------------------------------
    public NetPacket readPacket(int _timeout) {
//        debug.outln("netStream.readPacket...");
/*        if(socket == null) {
            debug.outln("netStream.readPacket... socket == null");
            return null;
        }*/
/*        if (!isConnected()) {
            reconnect();
        }*/
        int _index = 0;
        while(true){
            _index++;
            try {
    //            NetPacket _packet = (NetPacket)ois.readUnshared();
                NetPacket _packet = (NetPacket)ois.readObject();
                return _packet;
            } catch (Exception _e) {
//                debug.outln("Exception on readData..." + _e.getMessage());
                reconnect();
            }
            if((_timeout != 0) && (_index >= _timeout)){
//            	debug.outln("NetStream.readData...1..");
            	return null;
            }
//            debug.outln("NetStream.readData...2.. _index=" + _index);
//            _index++;
        }
//        return null;
    }

//-------------------------------------------------------------------------------------
    public void writePacket(NetPacket _packet) {
    	writePacket(_packet, 0);
    }

//-------------------------------------------------------------------------------------
    public void writePacket(NetPacket _packet, int _timeout) {
//        debug.outln("netStream.writePacket...");
/*        if (socket == null) {
            debug.outln("netStream.writePacket... socket == null");
            return;
        }*/
/*        if (!isConnected()) {
            reconnect();
        }*/

//        debug.outln("netStream.writePacket..."+_packet);
        int _index = 0;
        while(true){
            _index++;
            synchronized (oLock) {
                try {
                    //            oos.writeUnshared(_packet);
                    oos.reset();
                    oos.writeObject(_packet);
                    return;
                } catch (Exception _e) {
//                    debug.outln("Exception on writeData..." + _e.getMessage());
                    reconnect();
                }
            }
            if((_timeout != 0) && (_index >= _timeout)){
//            	debug.outln("NetStream.writeData...1..");
            	return;
            }
//            debug.outln("NetStream.writeData...2.. _index=" + _index);
//            _index++;
        }
    }

//-------------------------------------------------------------------------------------
    public void close() {
//        debug.outln("Closing NetStream connection...");
//        new Exception().printStackTrace();
        setStatus(STATUS_DISCONNECTED_ON_DEMAND);
        try {
            if (ois != null) {
                ois.close();
                socket.close();
            }
//            Thread.sleep(100);
        } catch (Exception _e) {}
//        connectToPort(port, direction);
    }


//-------------------------------------------------------------------------------------
    private void delay(int _sleepTimer) {
        try {
            Thread.sleep(_sleepTimer);
        } catch (Exception _e1) {}
    }

//-------------------------------------------------------------------------------------
    public String toString() {
        return "\thostname:" + host + "\tport:" + port + "\tstatus: " + getStatusAsString();
    }

//-------------------------------------------------------------------------------------
    public void printStatus() {
        debug.outln("NetStream: " + toString());
    }


//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
