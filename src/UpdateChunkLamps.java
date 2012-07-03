import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class UpdateChunkLamps{
    
    public static void update(LampChunk chunk, boolean night){
        if(chunk.getLamps().isEmpty()) return;
        try{
            synchronized(chunk.getLamps()){
                Iterator<LampLocation> itl = chunk.getLamps().iterator();
                while(itl.hasNext()){
                    LampLocation loc = itl.next();
                    int direction = 0, go = 8;
                    World dim = etc.getServer().getWorld(loc.getWorld())[loc.getDimension()];
                    Sign sign = (Sign)dim.getComplexBlock(loc.getX(), loc.getY(), loc.getZ());
                    if(sign != null && sign.getText(0).equals("§6[StreetLamp]")){
                        if(sign.getText(1).equalsIgnoreCase("x-")){
                            direction = 1;
                        }
                        else if(sign.getText(1).equalsIgnoreCase("x+")){
                            direction = 2;
                        }
                        else if(sign.getText(1).equalsIgnoreCase("z-")){
                            direction = 3;
                        }
                        else if(sign.getText(1).equalsIgnoreCase("z+")){
                            direction = 4;
                        }
                        else if(sign.getText(1).equalsIgnoreCase("y-")){
                            direction = 5;
                        }
                        
                        if(!sign.getText(2).equals("")){
                           try{
                               go = Integer.parseInt(sign.getText(2)) + 1;
                           }
                           catch(NumberFormatException NFE){
                               go = 8;
                           }
                        }
                    }
                    else{
                        continue;
                    }
                    switch (direction){
                    case 0:
                        for(int y = loc.getY(); y < loc.getY()+go; y++){
                            Block block = dim.getBlockAt(loc.getX(), y, loc.getZ());
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, loc.getX(), y, loc.getZ());
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, loc.getX(), y, loc.getZ());
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                        
                    case 1:
                        for(int x = loc.getX(); x > loc.getX()-go; --x){
                            Block block = dim.getBlockAt(x, loc.getY(), loc.getZ());
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, x, loc.getY(), loc.getZ());
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, x, loc.getY(), loc.getZ());
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                        
                    case 2:
                        for(int x = loc.getX(); x < loc.getX()+go; ++x){
                            Block block =  dim.getBlockAt(x, loc.getY(), loc.getZ());
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, x, loc.getY(), loc.getZ());
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, x, loc.getY(), loc.getZ());
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                        
                    case 3:
                        for(int z = loc.getZ(); z > loc.getZ()-go; --z){
                            Block block = dim.getBlockAt(loc.getX(), loc.getY(), z);
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, loc.getX(), loc.getY(), z);
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, loc.getX(), loc.getY(), z);
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                        
                    case 4:
                        for(int z = loc.getZ(); z < loc.getZ()+go; ++z){
                            Block block = dim.getBlockAt(loc.getX(), loc.getY(), z);
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, loc.getX(), loc.getY(), z);
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, loc.getX(), loc.getY(), z);
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                        
                    case 5:
                        for(int y = loc.getY(); y > loc.getY()-go; --y){
                            Block block = dim.getBlockAt(loc.getX(), y, loc.getZ());
                            if(night){
                                switch(block.blockType){
                                case Glass: 
                                    dim.setBlockAt(89, loc.getX(), y, loc.getZ());
                                    break;
                                case LightStone: 
                                    break;
                                default:
                                    continue;
                                }
                            }
                            else{
                                switch(block.blockType){
                                case Glass:
                                    break;
                                case LightStone:
                                    dim.setBlockAt(20, loc.getX(), y, loc.getZ());
                                    break;
                                default:
                                    continue;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch(ConcurrentModificationException cme){
            System.out.println("[StreetLamps-Debug] ERROR @ UCL");
        }
    }
}
