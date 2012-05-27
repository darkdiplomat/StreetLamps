import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class UpdateChunkLamps implements Runnable{
    private boolean night;
    private LampChunk chunk;
    
    
    public UpdateChunkLamps(LampChunk chunk,  boolean night){
        this.night = night;
        this.chunk = chunk;
    }
    
    public void run(){
        if(chunk.getLamps().isEmpty()) return;
        try{
            synchronized(chunk.getLamps()){
                Iterator<LampLocation> itl = chunk.getLamps().iterator();
                while(itl.hasNext()){
                    //if(!chunk.isLoaded()) break; //Chunk unloaded during update...
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
                    //if(!chunk.isLoaded()) break; //Chunk unloaded during update...
                    switch (direction){
                    case 0:
                        for(int y = loc.getY(); y < loc.getY()+go; ++y){
                            Block block = dim.getBlockAt(loc.getX(), y, loc.getZ());
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                        
                    case 1:
                        for(int x = loc.getX(); x > loc.getX()-go; --x){
                            Block block = dim.getBlockAt(x, loc.getY(), loc.getZ());
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                        
                    case 2:
                        for(int x = loc.getX(); x < loc.getX()+go; ++x){
                            Block block =  dim.getBlockAt(x, loc.getY(), loc.getZ());
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                        
                    case 3:
                        for(int z = loc.getZ(); z > loc.getZ()-go; --z){
                            Block block = dim.getBlockAt(loc.getX(), loc.getY(), z);
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                        
                    case 4:
                        for(int z = loc.getZ(); z < loc.getZ()+go; ++z){
                            Block block = dim.getBlockAt(loc.getX(), loc.getY(), z);
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                        
                    case 5:
                        for(int y = loc.getY(); y > loc.getY()-go; --y){
                            Block block = dim.getBlockAt(loc.getX(), y, loc.getZ());
                            if(night){
                                if(block.blockType == Block.Type.Glass){
                                    block.setType(89);
                                    block.update();
                                    break;
                                }
                                else if(block.blockType == Block.Type.LightStone){
                                    break;
                                }
                            }
                            else if (block.blockType == Block.Type.LightStone){
                                block.setType(20);
                                block.update();
                                break;
                            }
                            else if(block.blockType == Block.Type.Glass){
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        catch(ConcurrentModificationException cme){ }
    }
}
