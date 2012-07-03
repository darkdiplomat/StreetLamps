import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;

public class StreetLampsData {
    
    private final File SLDir = new File("plugins/config/StreetLamps/");
    private final String SLLoc = "plugins/config/StreetLamps/StreetLampLocations.txt";
    private File SLLocs;
    private ArrayList<LampChunk> lampchunks;
    public static boolean isLoaded = false;
    
    public StreetLampsData(){
        lampchunks = new ArrayList<LampChunk>();
        if(!SLDir.exists()){
            SLDir.mkdirs();
        }
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
    
    private void addLampToFile(LampLocation loc){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(SLLocs, true));
            out.write(loc.toString());
            out.flush();
            out.newLine();
            out.close();
        } catch (IOException e) {
            StreetLamps.log.warning("[StreetLamps] Unable to save to StreetLampLocations.txt");
        }
    }
    
    private void removeLampFromFile(LampLocation loc){
        try {
            File tempFile = new File(SLLocs.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(SLLocs));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(loc.toString())) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            if (!SLLocs.delete()) {
                StreetLamps.log.warning("[StreetLamps] Could not delete old file...");
                return;
            }
            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(SLLocs))
                StreetLamps.log.warning("[StreetLamps] Could not rename tempfile...");
        }
        catch (FileNotFoundException ex) {
            StreetLamps.log.log(Level.WARNING, "[StreetLamps] An unhandled exception occured...", ex);
        }
        catch (IOException ex) {
            StreetLamps.log.log(Level.WARNING, "[StreetLamps] An unhandled exception occured...", ex);
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
                    if(coords.length == 5){
                        String world = coords[0];
                        int dim = Integer.parseInt(coords[1]);
                        int x = Integer.parseInt(coords[2]);
                        int y = Integer.parseInt(coords[3]);
                        int z = Integer.parseInt(coords[4]);
                        LampLocation loc = new LampLocation(world, dim, x, y, z);
                        addLoc(loc);
                        i++;
                    }
                    else{
                        int dim = Integer.parseInt(coords[0]);
                        int x = Integer.parseInt(coords[1]);
                        int y = Integer.parseInt(coords[2]);
                        int z = Integer.parseInt(coords[3]);
                        LampLocation loc = new LampLocation(etc.getServer().getDefaultWorld().getName(), dim, x, y, z);
                        addLoc(loc);
                        i++;
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
        isLoaded = true;
    }
    
    public void addLoc(LampLocation loc){
        synchronized(lampchunks){
            for(LampChunk match : lampchunks){
                if(match.containsLamp(loc)){
                    match.addLamp(loc);
                    if(isLoaded){
                        addLampToFile(loc);
                    }
                    return;
                }
            }
            LampChunk lchunk = loc.getChunk();
            lchunk.addLamp(loc);
            lampchunks.add(lchunk);
            if(isLoaded){
                addLampToFile(loc);
            }
        }
    }
    
    public void removeLoc(LampLocation loc){
        synchronized(lampchunks){
            for(LampChunk match : lampchunks){
                if(match.containsLamp(loc)){
                    match.removeLamp(loc);
                    removeLampFromFile(loc);
                    return;
                }
            }
        }
    }
    
    public LampChunk getChunk(LampChunk chunk){
        LampChunk theChunk = null;
        synchronized(lampchunks){
            for(LampChunk match : lampchunks){
                if(match.equals(chunk)){
                    theChunk = match;
                }
            }
        }
        return theChunk;
    }
    
    public ArrayList<LampLocation> getAllLamps(){
        ArrayList<LampLocation> locs = new ArrayList<LampLocation>();
        synchronized(lampchunks){
            for(LampChunk chunk : lampchunks){
                locs.addAll(chunk.getLamps());
            }
        }
        return locs;
    }
    
    public ArrayList<LampChunk> getallchunks(){
        return lampchunks;
    }
}
