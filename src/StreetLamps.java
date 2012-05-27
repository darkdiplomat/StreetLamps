import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

public class StreetLamps extends Plugin {
    public final static Logger log = Logger.getLogger("Minecraft");
    private StreetLampsListener SLL;
    private final StreetLampsData SLD = new StreetLampsData();
    
    public final float version = 1.3F;
    public float currver = version;
    public final String name = "StreetLamps";
    public final String author = "DarkDiplomat";
    public static final ScheduledThreadPoolExecutor threadpool = new ScheduledThreadPoolExecutor(3);

    public void enable() {
        log.info(name+" "+version+" "+author+" enabled!");
        if(!isLatest()){
            log.info(name+": New Version Availble! "+currver);
        }
    }
    
    public void initialize(){
        SLL = new StreetLampsListener(this, SLD);
        threadpool.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        etc.getLoader().addListener(PluginLoader.Hook.TIME_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.SIGN_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.WEATHER_CHANGE, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.SERVERCOMMAND, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.CHUNK_LOADED, SLL, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.CHUNK_UNLOAD, SLL, this, PluginListener.Priority.MEDIUM);
    }
    
    public void disable() {
        SLD.save();
        threadpool.shutdownNow();
        log.info(name+" "+version+" disabled!");
    }
    
    public boolean isLatest(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name).openStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                currver = Float.valueOf(inputLine);
            }
            in.close();
            return version >= currver;
        } 
        catch (Exception E) {
        }
        return true;
    }
}
