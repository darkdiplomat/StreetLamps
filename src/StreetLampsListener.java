import java.util.ArrayList;

public class StreetLampsListener extends PluginListener{
    private boolean isDay = true;
    private boolean isRain = etc.getServer().getDefaultWorld().isRaining();
    private StreetLampsData SLD;
    private StreetLamps SL;
    private final World world = etc.getServer().getDefaultWorld();
    
    public StreetLampsListener(StreetLamps SL, StreetLampsData SLD){
        this.SL = SL;
        this.SLD = SLD;
    }
    
    public boolean onSignChange(Player player, Sign sign){
        if(sign.getText(0).toLowerCase().matches("\\[streetlamp\\]|\\[sl\\]")){
            if(player.canUseCommand("/streetlamps")){
                Location loc = new Location(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
                SLD.addLoc(loc);
                player.sendMessage("§6New StreetLamp Created!");
                sign.setText(0, "§6[StreetLamp]");
            }
            else{
                sign.setText(0, "?!");
            }
        }
        return false;
    }
    
    public boolean onBlockBreak(Player player, Block block){
        if(block.getType() == 63){
            Sign sign = (Sign) block.getWorld().getComplexBlock(block);
            if(sign.getText(0).equalsIgnoreCase("§6[StreetLamp]")){
                Location loc = new Location(sign.getWorld(), sign.getX(), sign.getY(), sign.getZ());
                SLD.removeLoc(loc);
                for(int y = sign.getY(); y < loc.y+8; y++){
                    Block glow = sign.getWorld().getBlockAt(sign.getX(), y, sign.getZ());
                    if(glow.getType() == 89){
                        glow.setType(20);
                        glow.update();
                        break;
                    }
                }
            }
        }
        else if(block.getType() == 89){
            for(int y = block.getY(); y > block.getY()-8; y--){
                Block sign = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
                if(sign.getType() == 63){
                    Sign s = (Sign) block.getWorld().getComplexBlock(sign);
                    if(s.getText(0).equalsIgnoreCase("§6[StreetLamp]")){
                        return true;
                    }
                }
            }          
        }
        return false;
    }
    
    public boolean onWeatherChange(World world, boolean newValue){
        if(newValue){
            isRain = true;
            if(isDay){
                new StreetLampsThread(this, SLD.getLamps(), true, SLD.getLoadUnloaded()).start();
            }
        }
        else{
            isRain = false;
            if(isDay){
                new StreetLampsThread(this, SLD.getLamps(), false, SLD.getLoadUnloaded()).start();
            }
        }
        return false;
    }
    
    public boolean onTimeChange(World world, long newValue){
        if(this.world.getRelativeTime() >= 12700 && this.world.getRelativeTime() <= 23500){
            if(isDay){
                isDay = false;
                if(!isRain){
                    new StreetLampsThread(this, SLD.getLamps(), true, SLD.getLoadUnloaded()).start();
                }
            }
        }
        else{
            if(!isDay){
                isDay = true;
                if(!isRain){
                    new StreetLampsThread(this, SLD.getLamps(), false, SLD.getLoadUnloaded()).start();
                }
            }
        }
        return false;
    }
    
    public boolean onBlockRightClick(Player player, Block block, Item iih){
        if(block.getType() == 63){
            Sign sign = (Sign) block.getWorld().getComplexBlock(block);
            if(sign.getText(0).equalsIgnoreCase("§6[StreetLamp]")){
                for(int y = block.getY(); y < block.getY()+8; y++){
                    Block glow = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
                    if(!isDay || isRain){
                        if(glow.getType() == 20){
                            glow.setType(89);
                            glow.update();
                            break;
                        }
                    }
                    else if(glow.getType() == 89){
                        glow.setType(20);
                        glow.update();
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean onCommand(Player player, String[] args){
        if(args[0].equalsIgnoreCase("/streetlamps") && player.canUseCommand("/streetlamps")){
            if(args.length > 1){
                if(args[1].equalsIgnoreCase("list")){
                    int page = 1;
                    ArrayList<Location> locs = SLD.getLamps();
                    int lastpage = Math.round(locs.size()/9);
                    if(args.length > 2){
                        try{
                            page = Integer.parseInt(args[2]);
                            if(page > lastpage){
                                page = lastpage;
                            }
                        }
                        catch(NumberFormatException NFE){
                            page = 1;
                        }
                    }
                    
                    player.sendMessage("§6StreetLamps Locations Page: "+page+" of "+lastpage);
                    for(int i = page-1; i < page+8 && i < locs.size(); i++){
                        Location loc = locs.get(i);
                        player.sendMessage("§7X: §8"+(int)loc.x+" §7Y: §8"+(int)loc.y+" §7Z: §8"+(int)loc.z+" §7World: §8"+loc.getWorld().getType().name());
                    }
                    return true;
                }
                else if(args[1].equalsIgnoreCase("forceupdate") && player.isAdmin()){
                    if((!isRain && !isDay) || (isRain && isDay)){
                        new StreetLampsThread(this, SLD.getLamps(), true, SLD.getLoadUnloaded()).start();
                    }
                    else if(!isRain && isDay){
                        new StreetLampsThread(this, SLD.getLamps(), false, SLD.getLoadUnloaded()).start();
                    }
                    player.sendMessage("§6StreetLamps Activating!");
                    return true;
                }
            }
            else{
                player.sendMessage("§9--§6 "+SL.name+" v"+SL.version+" by §a"+SL.author+" §9--");
                if(!SL.isLatest()){
                    player.sendMessage("§9--§6 There is a new version availible! v"+SL.currver+" §9--");
                }
                else{
                    player.sendMessage("§9--§6 Latest Version Installed! §9--");
                }
                player.sendMessage("§9--§6 There are §2"+SLD.getLamps().size()+"§6 Lamps installed on this server. §9--");
                return true;
            }
        }
        else if(args[0].equalsIgnoreCase("/#stop") || args[0].equalsIgnoreCase("/#save-all")){
            if(player.isOp()){
                SLD.save();
            }
        }
        return false;
    }
    
    public boolean onConsoleCommand(String[] args){
        if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("save-all")){
            SLD.save();
        }
        return false;
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
