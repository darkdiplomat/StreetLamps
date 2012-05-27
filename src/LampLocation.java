
public class LampLocation {
    
    private String world;
    private int x, y, z, dimension;
    
    public LampLocation(String world, int dimension, int x, int y, int z){
        this.world = world;
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorld(){
        return world;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getZ(){
        return z;
    }
    
    public int getDimension(){
        return dimension;
    }
    
    public LampChunk getChunk(){
        return new LampChunk(x >> 4, z >> 4, world, dimension);
    }
    
    public int hashCode(){
        return (x*97)+(y*97)+(z*97)+world.hashCode()+(dimension*97);
    }
    
    public boolean equals(Object obj){
        if(obj instanceof LampLocation){
            LampLocation check = (LampLocation)obj;
            if(check.getX() == this.getX()){
                if(check.getY() == this.getY()){
                    if(check.getZ() == this.getZ()){
                        if(check.getDimension() == this.getDimension()){
                            if(check.getWorld().equals(this.getWorld())){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public String toString(){
        StringBuilder toRet = new StringBuilder();
        toRet.append(world);
        toRet.append(',');
        toRet.append(dimension);
        toRet.append(',');
        toRet.append(x);
        toRet.append(',');
        toRet.append(y);
        toRet.append(',');
        toRet.append(z);
        return toRet.toString();
    }
}
