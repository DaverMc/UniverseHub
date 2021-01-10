package de.daver.unihub.io;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 */

public class ConfigurationFile {

    private final File file;
    private YamlConfiguration configuration;

    public ConfigurationFile(File dir, String name){
        this.file = FileUtils.createFile(new File(dir, name + ".yml"));
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Reloads all Content from the file
     */
    public void reload(){
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Saves all content to the {@link File}
     */
    public void save(){
        try{
            this.configuration.save(this.file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Getter for configuration
     * @return the {@link YamlConfiguration} of the {@link ConfigurationFile}
     */
    public YamlConfiguration getYaml(){
        return this.configuration;
    }

}
