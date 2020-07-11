//-------------------------------------------------------------------------------------
package codex.xbit.api.client.cli;
//-------------------------------------------------------------------------------------
import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.time.*;
import java.time.temporal.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import org.jline.builtins.Completers;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.keymap.KeyMap;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.LineReaderImpl;
import org.jline.reader.impl.completer.*;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.*;
import org.jline.utils.*;
import static org.jline.builtins.Completers.TreeCompleter.node;

import codex.common.utils.*;
import codex.xbit.api.common.configs.*;
import codex.xbit.api.client.net.*;

//-------------------------------------------------------------------------------------
public class CommandReader {
  private Debug debug;
  private CommandExecutor commandExecutor;
  private ClientConfiguration clientConfiguration;


  private Terminal terminal;
  private LineReader reader;

  private boolean color;

  public static final String HISTORY_FLE = "xcli.his";
//  private static final ZoneId ZONE_ID = ZoneId.of("Europe/Bucharest");

//-------------------------------------------------------------------------------------
  public CommandReader(Debug _debug, CommandExecutor _commandExecutor, ClientConfiguration _clientConfiguration) {
    debug  = _debug;
    commandExecutor = _commandExecutor;
    clientConfiguration = _clientConfiguration;
    
    File _historyFile;
    try {
      Character mask = null;
      String trigger = null;
      boolean timer = false;
      color = false;

      terminal = TerminalBuilder.builder().build();

      int mouse = 0;
      Completer completer = null;
      Parser parser = null;
//            builder.system(false);
//            Completer _completer0 = new Completers.FileNameCompleter();
//            completer = new StringsCompleter("foo", "bar", "baz");
      DefaultParser p = new DefaultParser();
      p.setEofOnUnclosedQuote(true);
      p.setEofOnEscapedNewLine(false);
      p.setEscapeChars(null);
      parser = p;


      color = true;
//            completer = new StringsCompleter("\u001B[1mfoo\u001B[0m", "bar", "\u001B[32mbaz\u001B[0m", "foobar");
//      NetClient _netClient = commandExecutor.getNetClient();
//      String[] _serversNames = _netClient.getServersNames();


      TreeCompleter _completer1 = new TreeCompleter(
        node("help"),
        node("bear"),
        node("bull"),
        node("buy"),
        node("demo"),
        node("live"),
        node("pause"),
        node("resume"),
        node("normal"),
        node("show"),
        node("sell"),
        node("use"),
        node("exit"),
        node("quit")
      );
//            completer = new AggregateCompleter(_completer0, _completer1);
      completer = _completer1;

      String _home = SystemUtils.getHomeDirectory();
      _historyFile = new File(_home + "/.xbit/logs/" + HISTORY_FLE);
      if (!_historyFile.exists()) {
        _historyFile.createNewFile();
      }
      reader = LineReaderBuilder.builder()
               .variable(LineReader.HISTORY_FILE, _historyFile)
               .history(new DefaultHistory())
               .terminal(terminal)
               .completer(completer)
               .parser(parser)
               .build();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

//-------------------------------------------------------------------------------------
  public String getLeftPrompt() {
//    String _prompt = SystemUtils.getUsername() + "@" + SystemUtils.getMachinename();
//    String _host = "";//commandExecutor.getServerName();


    PilotConfiguration _pilotConfiguration = commandExecutor.getPilotConfiguration();
    int _modeInt0 = _pilotConfiguration.getMode();

    String _mode0 = _pilotConfiguration.getDescriptor();

    int _modeColor0 = PilotConfiguration.getModeColorAsAttributedStyle(_modeInt0);


    TraderConfiguration _traderConfiguration = commandExecutor.getTraderConfiguration();
    int _modeInt1 = _traderConfiguration.getMode();
    int _stateInt = _traderConfiguration.getState();

    String _mode1 = _traderConfiguration.getModeAsString();
    String _state = _traderConfiguration.getStateAsString();

    int _modeColor1 = TraderConfiguration.getModeColorAsAttributedStyle(_modeInt1);
    int _statusColor = TraderConfiguration.getStateColorAsAttributedStyle(_stateInt);

    String _user = clientConfiguration.getLastUser();
    String _server = clientConfiguration.getLastServer();
    return new AttributedStringBuilder()
           .style(AttributedStyle.DEFAULT.foreground(_modeColor0 | AttributedStyle.BRIGHT))
           .append("["+_mode0+"]")
           .style(AttributedStyle.DEFAULT.foreground(_modeColor1 | AttributedStyle.BRIGHT))
           .append("["+_mode1+"]")
           .style(AttributedStyle.DEFAULT.foreground(_statusColor | AttributedStyle.BRIGHT))
           .append("["+_state+"]")
           .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW | AttributedStyle.BRIGHT))
           .append(_user)
           .style(AttributedStyle.DEFAULT)
           .append("@")
           .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW | AttributedStyle.BRIGHT))
           .append(_server)
           .style(AttributedStyle.DEFAULT)
           .append("#> ").toAnsi();
  }

//-------------------------------------------------------------------------------------
  public String getRightPrompt() {
    long _startTime = clientConfiguration.getStartTime();
//    debug.outln("_startTime="+_startTime+",_now="+_now+", _upTime="+_upTime);
    String _formattedUpTime = "-";
    if(_startTime > 0){
		ZoneId _zoneId = ZoneId.of(clientConfiguration.getZoneId());
//		ZoneId _zoneId = ZoneId.of("Europe/Bucharest");
      long _now = ZonedDateTime.now(_zoneId).toInstant().toEpochMilli();
      long _upTime = _now - _startTime;
      _formattedUpTime = formatUpTime(_upTime);
    }
    return new AttributedStringBuilder()
//                    .style(AttributedStyle.DEFAULT.background(AttributedStyle.RED))
//                    .append(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
           .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED | AttributedStyle.BRIGHT))
           .append("Uptime: "+ _formattedUpTime)
           .toAnsi();
  }

//-------------------------------------------------------------------------------------
    public static String formatUpTime(long _time) {
      long _t;
        _time = _time/1000;
        String __time = "";
        _t = _time/86400;
        if(_t > 0){
          __time += String.format("%02d", _t) + "d";
          return __time;
        }
        _t = _time/3600;
        if(_t > 0){
          __time += String.format("%02d", _t) + "h";
          return __time;
        }
        _t = (_time/60)%60;
        if(_t > 0){
          __time += String.format("%02d", _t) + "m";
          return __time;
        }
        _t = _time%60;
        __time += String.format("%02d", _t) + "s";
        return __time;
    }


//-------------------------------------------------------------------------------------
  public String readLine() {
    try {
      String _leftPrompt = getLeftPrompt();

      String _rightPrompt = getRightPrompt();

      return reader.readLine(_leftPrompt, _rightPrompt, (MaskingCallback) null, null);
    } catch (UserInterruptException _e1) {
//      debug.outln("Ctrl+C caught...");
      commandExecutor.executeExit(true);
    } catch (EndOfFileException _e2) {
    }
    return null;
  }
//-------------------------------------------------------------------------------------

}
//-------------------------------------------------------------------------------------
