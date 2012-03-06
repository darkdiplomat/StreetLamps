import java.util.ArrayList;
import java.util.Iterator;

public class StreetLampsThread extends Thread{
    private boolean night;
    private ArrayList<Location> Lamps;
    private boolean loadUnloaded;
    
    public StreetLampsThread(StreetLampsListener SLL, ArrayList<Location> Lamps,  boolean night, boolean loadUnloaded){
        this.night = night;
        this.Lamps = Lamps;
        this.loadUnloaded = loadUnloaded;
    }
    
    public void run(){
        try {
            sleep(1000); //Delay it a little
        }
        catch (InterruptedException e) {
        }
        Iterator<Location> itl = Lamps.iterator();
        while(itl.hasNext()){
            Location loc = itl.next();
            try{
                if(loadUnloaded){
                    if(!etc.getServer().getWorld(loc.dimension).getChunk((int)loc.x, (int)loc.y, (int)loc.z).isLoaded()){
                        etc.getServer().getWorld(loc.dimension).loadChunk((int)loc.x, (int)loc.y, (int)loc.z);
                    }
                }
            }
            catch (NullPointerException NPE){ //If this is thrown then the chunk isn't loaded
                etc.getServer().getWorld(loc.dimension).loadChunk((int)loc.x, (int)loc.y, (int)loc.z);
            }
            int direction = 0, go = 8;
            Sign sign = (Sign)etc.getServer().getWorld(loc.dimension).getComplexBlock((int)loc.x, (int)loc.y, (int) loc.z);
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
            switch (direction){
            case 0:
                for(int y = (int)loc.y; y < loc.y+go; ++y){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt((int)loc.x, y, (int)loc.z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
                
            case 1:
                for(int x = (int)loc.x; x > loc.x-go; --x){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt(x, (int)loc.y, (int)loc.z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
                
            case 2:
                for(int x = (int)loc.x; x < loc.x+go; ++x){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt(x, (int)loc.y, (int)loc.z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
                
            case 3:
                for(int z = (int)loc.z; z > loc.z-go; --z){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt((int)loc.x, (int)loc.y, z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
                
            case 4:
                for(int z = (int)loc.z; z < loc.z+go; ++z){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt((int)loc.x, (int)loc.y, z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
                
            case 5:
                for(int y = (int)loc.y; y > loc.y-go; --y){
                    Block block = etc.getServer().getWorld(loc.dimension).getBlockAt((int)loc.x, y, (int)loc.z);
                    if(night){
                        if(block.blockType == Block.Type.Glass){
                            block.setType(89);
                            block.update();
                            break;
                        }
                    }
                    else if (block.blockType == Block.Type.LightStone){
                        block.setType(20);
                        block.update();
                        break;
                    }
                }
                break;
            }
        }
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
