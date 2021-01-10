package de.daver.unihub.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipArchiver {

    private File source;
    private String name;

    public ZipArchiver(String path){
        this.source = new File(path);
        this.name = source.getName();
    }

    public ZipArchiver(File file){
        this.source = file;
        this.name = file.getName();
    }

    public void compress(String targetDir){
        try(FileOutputStream fileOut =  new FileOutputStream(targetDir + this.name + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fileOut)){
            rCompress(source, source.getName(), zipOut);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void compress(File targetDir){
        try(FileOutputStream fileOut =  new FileOutputStream(targetDir.getAbsolutePath() + this.name + ".zip");
            ZipOutputStream zipOut = new ZipOutputStream(fileOut)){
            rCompress(source, source.getName(), zipOut);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void rCompress(File source,String fileName,  ZipOutputStream zipOut) throws IOException{
        if(source.isHidden()){
            return;
        }
        if(source.isDirectory()){
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            for (File childFile : source.listFiles()) {
                rCompress(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;

        }
        writeZip(source,fileName, zipOut);
    }

    private void writeZip(File source, String fileName, ZipOutputStream zipOut) throws IOException{
        FileInputStream fis = new FileInputStream(source);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) >= 0) {
            zipOut.write(buffer, 0, length);
        }
        fis.close();
    }

    public File decompress(String targetDir) throws IOException{
        File target = new File(targetDir);
        byte[] buffer = new byte[1024];
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(source));
        ZipEntry entry =  zipIn.getNextEntry();
        while (entry != null){
            createDecompressedFile(target, entry, zipIn, buffer);
            entry = zipIn.getNextEntry();
        }

        zipIn.closeEntry();
        zipIn.close();
        return target;
    }

    private void createDecompressedFile(File target, ZipEntry entry, ZipInputStream zipIn, byte[] buffer) throws IOException{
        File file = new File(target, entry.getName());
        if(entry.isDirectory()){
            if(!file.isDirectory() && ! file.mkdirs()){
                throw new IOException("Failed to create directory: " + file.getName());
            }
            return;
        }

        File parent = file.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException("Failed to create directory " + parent);
        }
        FileOutputStream fos = new FileOutputStream(file);
        int len;
        while ((len = zipIn.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
    }
}
