//-------------------------------------------------------------------------------------
package codex.xbit.api.client.gui;

//-------------------------------------------------------------------------------------
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.client.cli.*;

import codex.xbit.api.client.gui.selector.*;
import codex.xbit.api.client.gui.selector.tree.nodes.*;
import codex.xbit.api.client.gui.tab.*;
import codex.xbit.api.client.gui.status.*;

//-------------------------------------------------------------------------------------
public class Gui extends JFrame implements Runnable {
    private Debug debug;
    private CommandImplementor commandImplementor;
    private ClientConfiguration clientConfiguration;
//    private String user, server;

    private Selector selector;
    private Tab tab;
    private Status status;
    
//-------------------------------------------------------------------------------------
    public Gui(CommandImplementor _commandImplementor, ClientConfiguration _clientConfiguration) {
//        debug = _debug;
//    	user = _user;
//    	server = _server;
        commandImplementor = _commandImplementor;
        clientConfiguration = _clientConfiguration;
        (new Thread(this)).start();
    }

//-------------------------------------------------------------------------------------
    public void run() {
        String _home = SystemUtils.getHomeDirectory();
        debug = new Debug(_home + "/.xbit/logs/xgui.log", Debug.INFO, true);


		//loading an image from a file
        final Toolkit _defaultToolkit = Toolkit.getDefaultToolkit();
        final URL _imageResource = this.getClass().getClassLoader().getResource("xbit_512.png");
        final Image _image = _defaultToolkit.getImage(_imageResource);

        //this is new since JDK 9
        final Taskbar _taskbar = Taskbar.getTaskbar();

        try {
            //set icon for mac os (and other systems which do support this method)
            _taskbar.setIconImage(_image);
        } catch (final UnsupportedOperationException e) {
            debug.outln("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            debug.outln("There was a security exception for: 'taskbar.setIconImage'");
        }

        //set icon for windows os (and other systems which do support this method)
        setIconImage(_image);
        


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setLayout(new BorderLayout(0, 0));
        
        Dimension screenSize = _defaultToolkit.getScreenSize();
        setPreferredSize(screenSize);

        DefaultXbitTreeNode _xbitRootNode = new DefaultXbitTreeNode(debug, ""); 
        status = new Status(debug, this);
        tab = new Tab(debug, status, _xbitRootNode, clientConfiguration);
 		selector = new Selector(debug, tab, _xbitRootNode, clientConfiguration);  

        add(selector, BorderLayout.WEST);
        add(tab, BorderLayout.CENTER);
        add(status, BorderLayout.PAGE_END);

        setPreferredSize(screenSize);
        pack();
        setResizable(false);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

//-------------------------------------------------------------------------------------
    public void exit() {
        debug.outln("Exiting...");
        commandImplementor.executeGuiStop();
//        System.exit(0);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
