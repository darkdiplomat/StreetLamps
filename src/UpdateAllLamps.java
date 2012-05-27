import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

public class UpdateAllLamps implements Runnable{
    private boolean night, forceload;
    private ArrayList<LampChunk> chunks;
    
    public UpdateAllLamps(ArrayList<LampChunk> chunks,  boolean night, boolean forceload){
        this.night = night;
        this.chunks = chunks;
    }
    
    public void run(){
        synchronized(chunks){
            try{
                for(LampChunk chunk : chunks){
                    if(forceload && !chunk.isLoaded()){
                        chunk.load();
                    }
                    else if(chunk.isLoaded()){
                        StreetLamps.threadpool.schedule(new UpdateChunkLamps(chunk, night), 5L, TimeUnit.MILLISECONDS);
                    }
                    try{
                        Thread.sleep(10L);
                    }
                    catch(InterruptedException ie){}
                }
            }
            catch(ConcurrentModificationException cme){}
            
        }
    }
}
