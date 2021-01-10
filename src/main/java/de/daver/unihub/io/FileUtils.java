package de.daver.unihub.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Daver_Mc
 * @version 1.0.0
 * @since 1.0.0
 */
public class FileUtils {

    /**
     * This constructor blocks the usage of the FileUtils class as an Object
     */
    private FileUtils(){}

    /**
     * Creates the given file
     * <p>
     * If the file exists the file is not created and only returned.
     * The file will be created and all parents as well,
     * if the creation is successfully it returns the file if not it returns null.
     * This method catches an IOException which if its thrown is printed.
     * @param file The file that has to be created
     * @return the {@link File} parameter or if something went wrong null
     */
    public static File createFile(File file){
        if(file.exists()) return file;
        if(file.isDirectory()){
            if(file.mkdirs()) return file;
            return null;
        }
        try{
            if(!file.getParentFile().exists()){
                if(!file.getParentFile().mkdirs()) return null;
            }
            if(file.createNewFile()) return file;
            return null;
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Copies the given file into the targetDir
     * <p>
     * If the sourceFile is a directory it will be copied with all contents in it
     * if it is only a file than only the file will be copied to the targetDir
     * @param sourceFile The {@link File} which is copied
     * @param targetDir The directory where the file is copied into
     * @return false if the targetDir is not a directory
     */
    public static boolean copy(File sourceFile, File targetDir){
        if(!checkDir(targetDir)) return false;
        rCopyContents(sourceFile,targetDir);
        return true;
    }

    /**
     * Copies the content of the sourceDir to the destDir
     * <p>
     * Checks if both inputs are directories
     * then recursively copies all the content from one directory to another
     * @param sourceDir The directory where the content comes from
     * @param destDir The directory where the content is copied to
     * @return if one of the parameters isn't a directory or the content of the sourceDir is null
     *         returns false
     */
    public static boolean copyContents(File sourceDir, File destDir){
        if(!sourceDir.isDirectory() || !checkDir(destDir)){
            return false;
        }

        File[] files = sourceDir.listFiles();
        if(files == null) return false;
        for(File file : files){
            rCopyContents(file, destDir);
        }
        return true;
    }

    /**
     * Deletes a {@link File}
     * <p>
     * If the source is a file it will be deleted
     * otherwise the content of the directory will be
     * recursively deleted and finally the directory.
     * @param source The {@link File} that should be deleted
     */
    public static void delete(File source){
        if(source.isFile()) source.delete();

        File[] contents = source.listFiles();
        if(contents == null){
            source.delete();
            return;
        }
        for(File file : contents){
            delete(file);
        }
        source.delete();
    }

    /**
     * Recursively Method to copy content
     * <p>
     * Creates a newFile in the targetDir based on the sourceFile
     * If the sourceFile is a directory it goes recursivly trough all Files in it
     * @param sourceFile A file that exist
     * @param targetDir The targetDirectory where the source is pasted in
     */
    private static void rCopyContents(File sourceFile, File targetDir){
        File newFile = FileUtils.createFile(new File(targetDir, sourceFile.getName()));
        if(!sourceFile.isDirectory()) {
            writeFile(sourceFile, newFile);
        }

        File[] contents = sourceFile.listFiles();
        if(contents == null) return;
        for(File file : contents){
            rCopyContents(file, newFile);
        }
    }

    /**
     * Writes the content of the source to the destination {@link File}
     * <p>
     * Opens an Input- and OutputStream to write the content of the source {@link File}
     * to the destination {@link File}
     * Catches {@link IOException} and printing the stacktrace
     * @param source An existing file
     * @param destination An existing file to copy the content
     */
    private static void writeFile(File source, File destination){
        try(
            FileInputStream fileIn = new FileInputStream(source);
            FileOutputStream fileOut = new FileOutputStream(destination);) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileIn.read(buffer)) > 0) {
                fileOut.write(buffer, 0, length);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Checks if a file is a directory
     * <p>
     * If the file doesn't exist the directory is created with all parent directories
     * @param dir The {@link File} to check if it is a directory
     * @return if the {@link File} is a directory
     */
    private static boolean checkDir(File dir){
        if(!dir.exists()){
            return dir.mkdirs();
        }else{
            return dir.isDirectory();
        }
    }

}
