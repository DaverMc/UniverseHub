package de.daver.unihub.io;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daver
 * @version 1.0.0
 * @since 1.0.0
 */

public class LanguageFile {

    private final File file;
    @Getter
    private Map<String, String> messageMap;

    /**
     * Creates a new language File if it not exist
     * <p>
     * the directory file will be created and all parent directories as well
     * the direct parent is the languages directory
     * @param dataDir the plugin data folder
     * @param name the language of the file
     */
    public LanguageFile(File dataDir, String name){
        this.file = new File(dataDir,  "languages/" + name + ".txt");
        FileUtils.createFile(this.file);
        this.messageMap = getMessages();
    }

    /**
     * Caches all messages from the file
     * <p>
     * A message format is for example:
     *      path: message
     * after mapping all messages from the file it will be returned
     * @return Map with key as path and value as message
     */
    private Map<String, String> getMessages() {
        HashMap<String, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(this.file))){
            String line;
            while((line = reader.readLine()) != null){
                String[] array = line.split(": ");
                if(array.length > 1){
                    map.put(array[0], line.replace(array[0], ""));
                }
            }
            return map;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reload all messages that are cached from file
     */
    public void reload(){
        this.messageMap = getMessages();
    }

    /**
     * Simply returns the message,
     * but if it does'nt exists the path will be returned instead
     * @param path The path of the message
     * @return the message or the path if the message doesn't exist
     */
    public String getMessage(String path){
        String message = messageMap.get(path);
        if(message == null) return path;
        return message;
    }

}
