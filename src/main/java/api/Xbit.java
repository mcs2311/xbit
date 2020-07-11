//-------------------------------------------------------------------------------------
package codex.xbit.api;
//-------------------------------------------------------------------------------------
//import org.slf4j.*;
//import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import org.apache.commons.cli.*;

import codex.common.utils.*;
import codex.xbit.api.client.*;
import codex.xbit.api.server.*;


//-------------------------------------------------------------------------------------
public class Xbit {

//-------------------------------------------------------------------------------------
    public static void main(String _args[]) throws Exception {
    	// Sets the application name on the menu bar
        System.setProperty("Xdock:name", "Xbit");

        //dummy call just for SLF4J initialization
		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.INFO);
        
        Options _options = new Options();
        _options.addRequiredOption("m", "mode", true, "client or server mode");
        _options.addOption("u", "use", true, "command(only for client). Initial command for client");
        _options.addOption("g", "gui", false, "gui mode(only for client). Initial command for client");
        _options.addOption("host", true, "hostname(only for client). Default is localhost");
        _options.addOption("port", true, "port number (for client and server). Default is 40000");
        _options.addOption("p", false, "ignore");
        _options.addOption("maxNumberOfThreads", true, "Maximum number of threads(only for server)");

        CommandLineParser _parser = new DefaultParser();
        CommandLine _cmd = _parser.parse(_options, _args);

        if(!_cmd.hasOption("mode")) { 
            System.out.println("Missing argument: mode");
            printUsage(_options);
        } else {
            String _mode = _cmd.getOptionValue("m");
            switch(_mode) { 
                case "client": { 
                    Client _client = new Client(_cmd); 
                    break;
                }
                case "server": {
                    Server _server = new Server(_cmd);
                    break;
                }
                default: {
                    System.out.println("Unknown xbit mode");
                    printUsage(_options);
                }            
            }
        }
    }
//-------------------------------------------------------------------------------------
    public static void printUsage(Options _options) {
        HelpFormatter _formatter = new HelpFormatter();
        _formatter.printHelp("xbit", _options);
    }

//-------------------------------------------------------------------------------------
}
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
