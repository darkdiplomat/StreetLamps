import java.util.ArrayList;

public class UpdateAllLampsThread extends Thread{
    private boolean night;
    private ArrayList<LampChunk> chunks;
    private StreetLampsListener SLL;
    
    public UpdateAllLampsThread(StreetLampsListener SLL, ArrayList<LampChunk> chunks,  boolean night){
        this.night = night;
        this.chunks = chunks;
        this.SLL = SLL;
        this.setName("StreetLamps-UpdateAllLampsThread-"+(night ? "ON" : "OFF"));
    }
    
    public void run(){
        synchronized(chunks){
            for(LampChunk chunk : chunks){
                chunk.verifyLoaded();
                if(!chunk.isLoaded()) continue;
                new UpdateChunkLamps(SLL, chunk, night).start();
            }
        }
    }
}
