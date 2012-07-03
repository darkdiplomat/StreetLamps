import java.util.ConcurrentModificationException;
import java.util.List;

public class UpdateAllLamps extends Thread{
    private boolean night, forceload;
    private List<LampChunk> chunks;
    
    public UpdateAllLamps(List<LampChunk> chunks,  boolean night, boolean forceload){
        this.night = night;
        this.chunks = chunks;
    }
    
    public void run(){
        synchronized(chunks){
            try{
                for(LampChunk chunk : chunks){
                    if(forceload){
                        chunk.load();
                    }
                    if(chunk.isLoaded()){
                        UpdateChunkLamps.update(chunk, night);
                        try{
                            Thread.sleep(5);
                        }
                        catch(InterruptedException ie){
                            continue;
                        }
                    }
                }
            }
            catch(ConcurrentModificationException cme){
                System.out.println("[StreetLamps-Debug] ERROR @ UAL");
            }
        }
    }
}
