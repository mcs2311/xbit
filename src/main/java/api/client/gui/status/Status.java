//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui.status;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;

import codex.xbit.api.common.packets.*;
import codex.xbit.api.common.streams.*;

import codex.common.utils.*;
import codex.xbit.api.client.gui.*;

//-------------------------------------------------------------------------------------
public class Status extends JPanel{
    private Debug debug;
    private Gui gui;

    private String taskName;
    private JLabel jlabel;

//-------------------------------------------------------------------------------------
    public Status(Debug _debug, Gui _gui) {
        debug = _debug;
        gui = _gui;

        jlabel = new JLabel("", JLabel.LEFT);
        setLayout(new BorderLayout());
        add(jlabel);
        add(getButtonsPanel(), BorderLayout.EAST);
        setMaximumSize( new Dimension(Integer.MAX_VALUE, 20));     
//        updateNetworkStatus(connectStatus, port);  
    }




//-------------------------------------------------------------------------------------
    private JPanel getButtonsPanel() {
        JPanel _p = new JPanel();
        _p.setLayout(new FlowLayout(FlowLayout.RIGHT));
/*
        portField = new JTextField(Integer.toString(port), 4);
        portField.setHorizontalAlignment(JTextField.RIGHT);

        portLeftButton = new BasicArrowButton(BasicArrowButton.WEST);
        portLeftButton.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e)
              {
                connectToPreviousPort();
              }
            });

        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e)
              {
                int _portInt = getPortNumber();
                switch(connectStatus){
                    case NetStream.STATUS_NOTCONNECTED:
                    case NetStream.STATUS_DISCONNECTED_ON_ERROR:
                    case NetStream.STATUS_DISCONNECTED_ON_DEMAND:{
                        gui.connectToPort();
                        return;
                    }
                    case NetStream.STATUS_CONNECTING:{
                        gui.disconnect();
                        return;
                    }
                    case NetStream.STATUS_CONNECTED:{
                        gui.disconnect();
                        return;
                    }
                }                
              }
            });

        portRightButton = new BasicArrowButton(BasicArrowButton.EAST);
        portRightButton.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e)
              {
                connectToNextPort();
              }
            });
*/

        JButton _b2 = new JButton("Exit");
        _b2.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent e)
              {
                gui.exit();
              }
            });
/*        _p.add(portLeftButton);
        _p.add(portField);
        _p.add(portRightButton);
        _p.add(connectButton);*/
        _p.add(_b2);
        return _p;
    }


//-------------------------------------------------------------------------------------
/*    private int getPortNumber() {
        String _port = portField.getText();
        int _portInt = 0;
        try{
            _portInt = Integer.parseInt(_port);
        }catch(Exception _e){
            debug.outln(Debug.ERROR, "Illegal port number:"+_port);
        }
        return _portInt;
    }*/

//-------------------------------------------------------------------------------------
/*    private void connectToNextPort() {
        int _portInt = getPortNumber();
        _portInt++;
        if(_portInt > NetStream.DEFAULT_MAX_PORT){
            _portInt = NetStream.DEFAULT_MIN_PORT;
        }
        portField.setText(Integer.toString(_portInt));
        gui.connectToPort(_portInt, NetStream.DIRECTION_NEXT);
    }

//-------------------------------------------------------------------------------------
    private void connectToPreviousPort() {
        int _portInt = getPortNumber();
        _portInt--;
        if(_portInt < NetStream.DEFAULT_MIN_PORT){
            _portInt = NetStream.DEFAULT_MAX_PORT;
        }
        portField.setText(Integer.toString(_portInt));
        gui.connectToPort(_portInt, NetStream.DIRECTION_PREV);
    }
*/
//-------------------------------------------------------------------------------------
    public void setTaskName(String _taskName) {
        taskName = _taskName;
    }


//-------------------------------------------------------------------------------------
    public void out(String _text) {
        jlabel.setText(_text);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
