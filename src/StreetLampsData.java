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
    private File SLLocs;
    private ArrayList<LampChunk> lampchunks;
    
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
    
    public void save(){
        new Thread(){
            public void run(){
                try {
                    SLLocs.delete();
                    SLLocs.createNewFile();
                    BufferedWriter out = new BufferedWriter(new FileWriter(SLLocs));
                    for(LampChunk lchunk : lampchunks){
                        ArrayList<LampLocation> lamplocs = lchunk.getLamps();
                        for(LampLocation loc : lamplocs){
                            out.write(loc.toString()); out.newLine();
                        }
                    }
                    out.close();
                } catch (IOException e) {
                    StreetLamps.log.warning("[StreetLamps] Unable to save to StreetLampLocations.txt");
                }
            }
        }.start();
    }
    
    private void load(){
        new Thread(){
            public void run(){
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
            }
        }.start();
    }
    
    public void addLoc(LampLocation loc){
        LampChunk lchunk = loc.getChunk();
        if(lampchunks.contains(lchunk)){
            lchunk = lampchunks.get(lampchunks.indexOf(lchunk));
            lchunk.addLamp(loc);
        }
        else{
            lchunk.addLamp(loc);
            lampchunks.add(lchunk);
        }
    }
    
    public void removeLoc(LampLocation loc){
        if(lampchunks.contains(loc.getChunk())){
            lampchunks.get(lampchunks.indexOf(loc.getChunk())).removeLamp(loc);
        }
    }
    
    public LampChunk getChunk(LampChunk chunk){
        if(lampchunks.contains(chunk)){
            return lampchunks.get(lampchunks.indexOf(chunk));
        }
        return null;
    }
    
    public ArrayList<LampLocation> getAllLamps(){
        ArrayList<LampLocation> locs = new ArrayList<LampLocation>();
        for(LampChunk chunk : lampchunks){
            locs.addAll(chunk.getLamps());
        }
        return locs;
    }
    
    public ArrayList<LampChunk> getallchunks(){
        return lampchunks;
    }
}
