package net.visualillusionsent.viutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Visual Illusions Entertainment Java jar file updater<br>
 * Used to update jar files on the fly<br>
 * This File is part of the VIUtils Java Software package (net.visualillusionsent.viutils)
 * 
 * @author Jason Jones
 * @version 1.1
 */
public class Updater {
    private String downloadurl, jarloc, jarname;
    private Logger logger;
    
    public Updater(String downloadurl, String jarloc, String jarname, Logger logger){
        this.downloadurl = downloadurl;
        this.jarloc = jarloc;
        this.logger = logger;
        this.jarname = jarname;
    }
    
    /**
     * Performs jar file update
     * @return true if successful
     * @throws UpdateException
     *              use the getMessage() method to retrieve the reason why
     */
    public boolean performUpdate() throws UpdateException{
        logger.info("[VIUtils] Please wait, downloading latest version of "+jarname+"...");
        
        if(!jarloc.endsWith(".jar")){
            logger.info("[VIUtils] The jar file location needs to end with .jar... Terminating update...");
            throw new UpdateException("Failed to update due to: 'Incorrect File Extension'");
        }
        
        File local = new File(jarloc);
        if(!local.exists()){
            logger.warning("[VIUtils] Unable to find "+jarloc+"... Terminating update...");
            throw new UpdateException("Failed to update due to: 'FileNotFound'");
        }
        
        //BackUp just in case of failure
        File bak = null;
        try {
             bak = backupjar(jarloc);
        } catch (IOException e) {
            throw new UpdateException("Failed to update due to: 'Backup failed'");
        }
        
        try{
            if(loadAllClasses(jarloc, logger)){
                try{
                    OutputStream outputStream = new FileOutputStream(local);
                    URL url = new URI(downloadurl).toURL();
                    InputStream inputStream = url.openConnection().getInputStream();
        
                    byte[] buffer = new byte[1024];
                    int read = 0;
         
                    while ((read = inputStream.read(buffer)) > 0){
                        outputStream.write(buffer, 0, read);
                    }
        
                    outputStream.close();
                    inputStream.close();
                    logger.info("[VIUtils] Successfully downloaded latest version of "+jarname+"!");
                    bak.delete();
                    return true;
                } 
                catch (IOException IOE){
                    logger.log(Level.WARNING, "[VIUtils] Failed to download new version. Restoring old version...", IOE);
                    
                    //Restore
                    if(restorejar(jarloc)){
                        bak.delete();
                    }
                    throw new UpdateException("Failed to update due to: 'Failed to download'");
                } catch (URISyntaxException urise) {
                    //Restore
                    if(restorejar(jarloc)){
                        bak.delete();
                    }
                    logger.log(Level.WARNING, "[VIUtils] There was an error with the URI syntax... Restoring old version...", urise);
                    throw new UpdateException("Failed to update due to: 'Failed to download'");
                }
            }
        }
        catch(IOException ioe){
            
        } 
        catch (ClassNotFoundException e) {
            
        }
        return false;
    }
    
    /**
     * loads all the jar's files for updating
     * 
     * @param jarloc    The location of the jar file to be updated
     * @param logger    The logger to use for logging messages
     * @return true if successfully loaded all classes
     */
    private boolean loadAllClasses(String jarloc, Logger logger) throws IOException, ClassNotFoundException{
        try{
            // Load the jar
            JarFile jar = new JarFile(jarloc);
            
            // Walk through all of the entries
            Enumeration<JarEntry> enumeration = jar.entries();

            while (enumeration.hasMoreElements()){
                JarEntry entry = enumeration.nextElement();
                String name = entry.getName();
                
                // is it a class file?
                if (name.endsWith(".class") && !name.contains("$")){
                    // convert to package
                    String path = name.replaceAll("/", ".");
                    path = path.substring(0, path.length() - ".class".length());

                    // Load it
                    ClassLoader.getSystemClassLoader().loadClass(path);
                }
            }
            return true;
        } 
        catch (IOException IOE){
            logger.log(Level.SEVERE, "[VIUtils] An IOException has occurred! Update terminated!", IOE);
        }
        catch (ClassNotFoundException CNFE){
            logger.log(Level.SEVERE, "[VIUtils] An ClassNotFoundException has occurred! Update terminated!", CNFE);
        }
        catch (Exception E){
            logger.log(Level.SEVERE, "[VIUtils] An Unexpected Exception has occurred! Update terminated!", E);
        }
        return false;
    }
    
    private File backupjar(String jarfile) throws IOException{
        File bak = new File(jarloc.substring(0, jarloc.lastIndexOf("/")+1)+jarname+".bak");
        OutputStream outputStream = new FileOutputStream(bak);
        InputStream inputStream = new FileInputStream(jarfile);
            
        int read = 0;
 
        while ((read = inputStream.read()) != -1){
            outputStream.write(read);
        }

        outputStream.close();
        inputStream.close();
        return bak;
    }
    
    private boolean restorejar(String jarfile){
        try{
            File bak = new File(jarloc.substring(0, jarloc.lastIndexOf("/")+1)+jarname+".bak");
            OutputStream outputStream = new FileOutputStream(jarfile);
            InputStream inputStream = new FileInputStream(bak);
            
            int read = 0;
 
            while ((read = inputStream.read()) != -1){
                outputStream.write(read);
            }

            outputStream.close();
            inputStream.close();
            return true;
        }
        catch (IOException IOE){ }
        return false;
    }
}
