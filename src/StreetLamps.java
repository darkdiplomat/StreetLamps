import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class StreetLamps extends Plugin {
    public final static Logger log = Logger.getLogger("Minecraft");
    private StreetLampsListener SLL;
    private final StreetLampsData SLD = new StreetLampsData();
    
    public final float version = 1.2F;
    public float currver = version;
    public final String name = "StreetLamps";
    public final String author = "DarkDiplomat";

    public void enable() {
        log.info(name+" "+version+" "+author+" enabled!");
        if(!isLatest()){
            log.info(name+": New Version Availble! "+currver);
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
    }
    
    public void disable() {
        SLD.save();
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

/*******************************************************************************\
* StreetLamps v1.x                                                              *
* Copyright (C) 2012 Visual Illusions Entertainment                             *
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of StreetLamps                                              *
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                          *
* See the GNU General Public License for more details.                          *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html    *
*                                                                               *
\*******************************************************************************/
