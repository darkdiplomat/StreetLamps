import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

import net.visualillusionsent.viutils.Updater;
import net.visualillusionsent.viutils.VersionCheck;

public class StreetLamps extends Plugin {
    public final static Logger log = Logger.getLogger("Minecraft");
    private StreetLampsListener SLL;
    private final StreetLampsData SLD = new StreetLampsData();
    
    private static final String vercheckurl = "http://www.visualillusionsent.net/cmod_plugins/versions.php?plugin=StreetLamps";
    private static final String updateurl = "https://dl.dropbox.com/u/25586491/CanaryPlugins/StreetLamps.jar";

    public final String name = "StreetLamps";
    public final String author = "DarkDiplomat";
    
    public static final String version = "1.3.0";
    public static final ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(4);
    public static final VersionCheck vercheck = new VersionCheck(version, vercheckurl);
    
    public static final Updater update = new Updater(updateurl, "/plugins/StreetLamps.jar", "StreetLamps", log);

    public void enable() {
        log.info(name+" "+version+" "+author+" enabled!");
        if(!vercheck.isLatest()){
            log.info(name+": New Version Availble! v"+vercheck.getCurrentVersion());
            log.info(name+": Use '/streetlamps updateplugin' to update");
        }
    }
    
    public void initialize(){
        SLL = new StreetLampsListener(this, SLD);
        etc.getLoader().addListener(PluginLoader.Hook.TIME_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.SIGN_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.WEATHER_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.CHUNK_LOADED, SLL, this, PluginListener.Priority.MEDIUM);
    }
    
    public void disable() {
        threadpool.shutdown();
        log.info(name+" "+version+" disabled!");
    }
}
