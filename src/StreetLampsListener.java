import java.util.ArrayList;
import java.util.Collections;

import net.visualillusionsent.viutils.UpdateException;

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
                LampLocation loc = new LampLocation(sign.getWorld().getName(), sign.getWorld().getType().toIndex(), sign.getX(), sign.getY(), sign.getZ());
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
        if(!StreetLampsData.isLoaded){
            return false;
        }
        if(block.blockType.equals(Block.Type.WallSign) || block.blockType.equals(Block.Type.SignPost)){
            Sign sign = (Sign) block.getWorld().getComplexBlock(block);
            if(sign.getText(0).equalsIgnoreCase("§6[StreetLamp]")){
                LampLocation loc = new LampLocation(sign.getWorld().getName(), sign.getWorld().getType().toIndex(), sign.getX(), sign.getY(), sign.getZ());
                SLD.removeLoc(loc);
                for(int y = sign.getY(); y < loc.getY()+8; y++){
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
        if(!StreetLampsData.isLoaded){
            return false;
        }
        if(newValue){
            isRain = true;
            if(isDay){
                ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                Collections.copy(chunks, SLD.getallchunks());
                Collections.shuffle(chunks); //Randomize the list a little
                StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, true, false));
            }
        }
        else{
            isRain = false;
            if(isDay){
                ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                Collections.copy(chunks, SLD.getallchunks());
                Collections.shuffle(chunks); //Randomize the list a little
                StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, false, false));
            }
        }
        return false;
    }
    
    public boolean onTimeChange(World world, long newValue){
        if(!StreetLampsData.isLoaded){
            return false;
        }
        if(this.world.getRelativeTime() >= 12700 && this.world.getRelativeTime() <= 23500){
            if(isDay){
                isDay = false;
                if(!isRain){
                    ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                    Collections.copy(chunks, SLD.getallchunks());
                    Collections.shuffle(chunks); //Randomize the list a little
                    StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, true, false));
                }
            }
        }
        else{
            if(!isDay){
                isDay = true;
                if(!isRain){
                    ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                    Collections.copy(chunks, SLD.getallchunks());
                    Collections.shuffle(chunks); //Randomize the list a little
                    StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, false, false));
                }
            }
        }
        return false;
    }
    
    public boolean onBlockRightClick(Player player, Block block, Item iih){
        if(!StreetLampsData.isLoaded){
            return false;
        }
        if(block.blockType.equals(Block.Type.WallSign) || block.blockType.equals(Block.Type.SignPost)){
            Sign sign = (Sign) block.getWorld().getComplexBlock(block);
            if(sign.getText(0).equals("§6[StreetLamp]")){
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
                if(player.canUseCommand("/streetlampsadmin")){
                    LampLocation loc = new LampLocation(sign.getWorld().getName(), sign.getWorld().getType().toIndex(), sign.getX(), sign.getY(), sign.getZ());
                    SLD.addLoc(loc);
                }
                if(!sign.getText(1).toLowerCase().matches("x\\-|x\\+|z\\-|z\\+|y\\-")){
                   sign.setText(1, "");
                   sign.update();
                }
                if(!sign.getText(2).equals("")){
                    try{
                        Integer.parseInt(sign.getText(2));
                    }
                    catch(NumberFormatException NFE){
                        sign.setText(2, "");
                        sign.update();
                    }
                }
                if(!sign.getText(3).equals("")){
                    sign.setText(3, "");
                    sign.update();
                }
                return true;
            }
        }
        return false;
    }
    
    public boolean onCommand(Player player, String[] args){
        if(!StreetLampsData.isLoaded){
            return false;
        }
        if(args[0].equalsIgnoreCase("/streetlamps") && player.canUseCommand("/streetlamps")){
            if(args.length > 1){
                if(args[1].equalsIgnoreCase("list")){
                    int page = 1;
                    ArrayList<LampLocation> locs = SLD.getAllLamps();
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
                        LampLocation loc = locs.get(i);
                        player.sendMessage("§7X: §8"+loc.getX()+" §7Y: §8"+loc.getY()+" §7Z: §8"+loc.getZ()+" §7World: §8"+loc.getWorld()+" §7Dim: §8"+loc.getDimension());
                    }
                    return true;
                }
                else if(args[1].equalsIgnoreCase("forceupdate") && player.canUseCommand("/streetlampsadmin")){
                    if(!isDay || isRain){
                        ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                        Collections.copy(chunks, SLD.getallchunks());
                        Collections.shuffle(chunks); //Randomize the list a little
                        StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, true, true));
                    }
                    else{
                        ArrayList<LampChunk> chunks = new ArrayList<LampChunk>(SLD.getallchunks());
                        Collections.copy(chunks, SLD.getallchunks());
                        Collections.shuffle(chunks); //Randomize the list a little
                        StreetLamps.threadpool.execute(new UpdateAllLamps(chunks, false, true));
                    }
                    player.sendMessage("§6StreetLamps Activating!");
                    return true;
                }
                else if (args[1].toLowerCase().equals("updateplugin") && player.canUseCommand("/streetlampsadmin")){
                    try {
                        if(StreetLamps.update.performUpdate()){
                            etc.getLoader().reloadPlugin(SL.name);
                        }
                        else{
                            player.notify("Failed to update...");
                        }
                    }
                    catch (UpdateException e) {
                        player.notify(e.getMessage());
                    }
                }
            }
            else{
                player.sendMessage("§9--§6 "+SL.name+" v"+StreetLamps.version+" by §a"+SL.author+" §9--");
                if(player.canUseCommand("/streetlampsadmin")){
                    if(!StreetLamps.vercheck.isLatest()){
                        player.sendMessage("§9--§6 There is a new version availible! v"+StreetLamps.vercheck.getCurrentVersion()+" §9--");
                    }
                    else{
                        player.sendMessage("§9--§6 Latest Version Installed! §9--");
                    }
                }
                player.sendMessage("§9--§6 There are §2"+SLD.getAllLamps().size()+"§6 Lamps installed on this server. §9--");
                return true;
            }
        }
        return false;
    }
    
    public void onChunkLoaded(Chunk chunk){
        if(!StreetLampsData.isLoaded){
            return;
        }
        LampChunk lchunk = SLD.getChunk(new LampChunk(chunk));
        if(lchunk != null){
            if(!isDay || isRain){
                UpdateChunkLamps.update(lchunk, true);
            }
            else{
                UpdateChunkLamps.update(lchunk, false);
            }
        }
    }
}