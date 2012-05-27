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
    
    public LampChunk(Chunk chunk){
        this.x = chunk.getX();
        this.z = chunk.getZ();
        this.world = chunk.getWorld().getName();
        this.dimension = chunk.getWorld().getType().toIndex();
        this.chunk = chunk;
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
        World[] world = etc.getServer().getWorld(this.world);
        if(world != null){
            World dim = world[dimension];
            if(dim.isChunkLoaded(x << 4, 64, z << 4)){
                loaded = true;
            }
        }
        return loaded;
    }
    
    public void load(){
        World[] world = etc.getServer().getWorld(this.world);
        if(world != null){
            World dim = world[dimension];
            dim.loadChunk(x << 4, 64, z << 4);
            loaded = true;
        }
    }
    
    public Chunk getChunk(){
        if(chunk != null){
            return chunk;
        }
        if(isLoaded()){
            World dim = etc.getServer().getWorld(this.world)[dimension];
            this.chunk = dim.getChunk(x << 4, 64, z << 4);
            return chunk;
        }
        return null;
    }
    
    public ArrayList<LampLocation> getLamps(){
        return lamps;
    }
    
    public boolean containsLamp(LampLocation lamp){
        if(lamps.contains(lamp)){
            return true;
        }
        int checkx = lamp.getX() >> 4;
        int checkz = lamp.getZ() >> 4;
        return checkx == x && checkz == z && lamp.getWorld().equals(world) && lamp.getDimension() == dimension;
    }
    
    public int hashCode(){
        return (x*97)+(z*97)+world.hashCode()+(dimension*97);
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof LampLocation){
            LampLocation check = (LampLocation)obj;
            if(check.getX() == this.getX()){
                if(check.getZ() == this.getZ()){
                    if(check.getDimension() == this.getDimension()){
                        if(check.getWorld().equals(this.getWorld())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
