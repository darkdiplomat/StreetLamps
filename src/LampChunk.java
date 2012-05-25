import java.util.ArrayList;

public class LampChunk {
    private ArrayList<LampLocation> lamps = new ArrayList<LampLocation>();
    private int x, z, dimension;
    private String world;
    private boolean loaded = false;
    private Chunk chunk;
    
    public LampChunk(int x, int z, String world, int dimension){
        this.x = x;
        this.z = z;
        this.world = world;
        this.dimension = dimension;
        this.chunk = getChunk();
    }
    
    public int getX(){
        return x;
    }
    
    public int getZ(){
        return z;
    }
    
    public String getWorld(){
        return world;
    }
    
    public int getDimension(){
        return dimension;
    }
    
    public void addLamp(LampLocation loc){
        if(!lamps.contains(loc)){
            lamps.add(loc);
        }
    }
    
    public void removeLamp(LampLocation loc){
        if(lamps.contains(loc)){
            lamps.remove(loc);
        }
    }
    
    public void setLoaded(boolean loaded){
        this.loaded = loaded;
    }
    
    public boolean isLoaded(){
        return loaded;
    }
    
    public void verifyLoaded(){
        if(lamps.isEmpty()) return;
        World[] world = etc.getServer().getWorld(this.world);
        if(world != null){
            World dim = world[dimension];
            LampLocation loc = lamps.get(0);
            if(dim.isChunkLoaded(loc.getX(), loc.getY(), loc.getZ())){
                loaded = true;
            }
            else{
                loaded = false;
            }
        }
    }
    
    public Chunk getChunk(){
        if(chunk != null){
            return chunk;
        }
        verifyLoaded();
        if(isLoaded()){
            World dim = etc.getServer().getWorld(this.world)[dimension];
            LampLocation loc = lamps.get(0);
            this.chunk = dim.getChunk(loc.getX(), loc.getY(), loc.getZ());
            return chunk;
        }
        return null;
    }
    
    public ArrayList<LampLocation> getLamps(){
        return lamps;
    }
    
    public boolean containsLamp(LampLocation lamp){
        int checkx = lamp.getX() >> 4;
        int checkz = lamp.getZ() >> 4;
        return checkx == x && checkz == z && lamp.getWorld().equals(world) && lamp.getDimension() == dimension;
    }
    
    public boolean equals(Object obj){
        if(obj instanceof LampChunk){
            LampChunk toCheck = (LampChunk)obj;
            return x == toCheck.getX() && z == toCheck.getZ() && toCheck.getWorld().equals(world) && toCheck.getDimension() == dimension;
        }
        return false;
    }
}
