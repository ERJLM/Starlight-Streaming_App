package org.example;

import java.io.File;

public class FileManager
{
    static public void deleteFile(File f)
    {
        if(f.listFiles()!=null)
            for(File file : f.listFiles())
                deleteFile(file);
        f.delete();
    }

    static public void deleteFiles(File[] files)
    {
        for(File file:files)
            deleteFile(file);
    }

    static public File makeDir(String dir_path)
    {
        File dir = new File(dir_path);
        dir.mkdir();
        return dir;
    }
}
