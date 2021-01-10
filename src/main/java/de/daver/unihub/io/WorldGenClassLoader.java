package de.daver.unihub.io;

import de.daver.unihub.util.PrefixLogger;
import de.daver.unihub.util.WorldGenerator;
import lombok.Getter;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 */

public class WorldGenClassLoader {

    @Getter
    private final Map<String, WorldGenerator> generatorMap;
    private final File dir;
    private final PrefixLogger logger;

    /**
     * Loading external WorldGenerators
     * <p>
     * If the directory "generator" is not existing in the datadir it will be created
     * reading all Jars in the "generator" Dir and caching the {@link WorldGenerator}
     * in a Map where the key is the name of the {@link WorldGenerator}
     * @param logger The logger for logging progress
     * @param dataDir directory where all the plugin-Data is put in
     */
    public WorldGenClassLoader(PrefixLogger logger, File dataDir){
        this.dir = FileUtils.createFile(new File(dataDir, "generator"));
        this.logger = logger;
        this.generatorMap = getWorldGenerators();
    }

    /**
     * Initialise the generatorMap
     * <p>
     * Checks the directory for jars and tries to transform every jar in a {@link WorldGenerator}
     * @return a {@link Map} of {@link WorldGenerator} and their names as keys
     */
    private Map<String, WorldGenerator> getWorldGenerators() {
        Map<String, WorldGenerator> map = new HashMap<>();
        this.logger.info("Start Loading WorldGenerator");
        File[] contents = this.dir.listFiles();
        if(contents == null) return map;
        for(File file : contents){
            if(file.getName().endsWith(".jar")){
                WorldGenerator generator = getGenerator(file);
                if(generator == null) continue;
                map.put(generator.getName(), generator);
            }
        }
        this.logger.info("Finished Loading WorldGenerators");
        return map;
    }


    /**
     * Creates out of a JarFile a WorldGenerator
     * <p>
     * Searches for a SettingsFile out of that a main class path
     * and after that invoke the main class to a WorldGenerator
     * Catches many Exceptions and logs warning to everyone
     * @param jarFile An existing JarFile
     * @return if found the {@link WorldGenerator} out of the Jar
     */
    private WorldGenerator getGenerator(File jarFile){
        try{
            File settingsFile = getSettingsFile( importJar(jarFile));
            String mainClassName = getMainClass(settingsFile);
            Class<?> generatorClass = Class.forName(mainClassName);
            return (WorldGenerator) generatorClass.getDeclaredConstructor().newInstance();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException e){
            this.logger.warn("The jar does not contain a Main class");
            return null;
        } catch (InstantiationException | IllegalAccessException   e) {
            this.logger.warn("The mainClass doesn't implements the WorldGenerator interface");
            return null;
        }
    }

    /**
     * Search for the main class in the settings.txt of a jar
     * @param file The settings.txt
     * @return path of the main class
     * @throws IOException caused by stream usage
     */
    private String getMainClass(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null){
            String[] array = line.split(": ");
            if(array[0].equals("main")){
                return array[1];
            }
        }
        return null;
    }

    /**
     * Getting the settings.txt out of a jar
     * <p>
     * Uses a {@link URLClassLoader} to search through the jar for the settings.txt
     * Creates a temporary {@link File} where all the content of the settings.txt is in
     * @param loader should not be null
     * @return a temporary copy of the settings.txt
     * @throws IOException caused by stream usage
     */
    private File getSettingsFile(URLClassLoader loader) throws IOException{
        if(loader == null) return null;
        File file = File.createTempFile("settings.txt", null);
        FileOutputStream resourceOut = new FileOutputStream(file);
        byte[] byteArray = new byte[1024];
        int i;
        InputStream classIn = loader.getResourceAsStream("resources/settings.txt");
        if(classIn == null) return null;
        while ((i = classIn.read(byteArray)) > 0){
            resourceOut.write(byteArray, 0, i);
        }
        classIn.close();
        resourceOut.close();
        return file;
    }

    /**
     * Creates a ClassLoader of the given file
     * <p>
     * returns null if the file doesn't exist
     * @param file {@link File}
     * @return {@link URLClassLoader} of the given jar
     */
    private URLClassLoader importJar(File file){
        URLClassLoader classLoader = null;
        try{
            URL[] classLoaderUrls = new URL[]{file.toURI().toURL()};
            classLoader = new URLClassLoader(classLoaderUrls);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return classLoader;
    }
}
