import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StreetLampsData {
    
    private final File SLDir = new File("plugins/config/StreetLamps/");
    private final String SLLoc = "plugins/config/StreetLamps/StreetLampLocations.txt";
    private final PropertiesFile SLPF = new PropertiesFile("plugins/config/StreetLamps/StreetLamps.ini");
    private File SLLocs;
    private ArrayList<Location> Lamps;
    private Server serv = etc.getServer();
    private boolean loadUnloaded = true;
    
    public StreetLampsData(){
        Lamps = new ArrayList<Location>();
        if(!SLDir.exists()){
            SLDir.mkdirs();
        }
        loadUnloaded = SLPF.getBoolean("Load-UnloadedChunks", true);
        SLLocs = new File(SLLoc);
        if(!SLLocs.exists()){
            try {
                SLLocs.createNewFile();
            } catch (IOException e) {
                StreetLamps.log.warning("[StreetLamps] Unable to create StreetLampLoactions.txt");
            }
        }
        else{
            load();
        }
    }
    
    public void save(){
        try {
            SLLocs.delete();
            SLLocs.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(SLLocs));
            for(Location loc : Lamps){
                int x = (int)loc.x, y = (int)loc.y, z = (int)loc.z, w = loc.dimension;
                out.write(w+","+x+","+y+","+z); out.newLine();
            }
            out.close();
        } catch (IOException e) {
            StreetLamps.log.warning("[StreetLamps] Unable to save to StreetLampLocations.txt");
        }
    }
    
    private void load(){
        int i = 0;
        try{
            BufferedReader in = new BufferedReader(new FileReader(SLLocs));
            String line;
            while ((line = in.readLine()) != null) {
                String[] coords = line.split(",");
                try{
                    int w = Integer.parseInt(coords[0]);
                    double x = Double.parseDouble(coords[1]);
                    double y = Double.parseDouble(coords[2]);
                    double z = Double.parseDouble(coords[3]);
                    serv.getWorld(w).loadChunk((int)x, (int)y, (int)z);
                    Block block = serv.getWorld(w).getBlockAt((int)x, (int)y, (int)z);
                    if(block.getType() == 63){
                        Sign sign = (Sign)serv.getWorld(w).getComplexBlock(block);
                        if(sign.getText(0).equals("§6[StreetLamp]")){
                            Location loc = new Location(serv.getWorld(w), x, y, z);
                            Lamps.add(loc);
                            i++;
                        }
                    }
                }
                catch(NumberFormatException NFE){
                    continue;
                }
                catch(ArrayIndexOutOfBoundsException AIOOBE){
                    continue;
                }
            }
            in.close();
        }
        catch(IOException IOE){
            StreetLamps.log.warning("[StreetLamps] Unable to load StreetLampLoactions.txt");
        }
        StreetLamps.log.info("[StreetLamps]: Loaded "+i+" StreetLamps");
    }
    
    public void addLoc(Location loc){
        if(!Lamps.contains(loc)){
            Lamps.add(loc);
        }
    }
    
    public void removeLoc(Location loc){
        if(Lamps.contains(loc)){
            Lamps.remove(loc);
        }
    }
    
    public ArrayList<Location> getLamps(){
        return Lamps;
    }
    
    public boolean getLoadUnloaded(){
        return loadUnloaded;
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
