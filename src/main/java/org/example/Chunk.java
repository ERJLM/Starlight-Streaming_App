package org.example;

import java.io.File;

public class Chunk
{
    int movie;
    String name, hash;

    public Chunk(int movie,String name,String hash)
    {
        this.movie = movie;
        this.hash = hash;
        this.name = name;
    }

    static public Chunk[] getChunks(int movie,File[] files)
    {
        Chunk[] chunks = new Chunk[files.length];
        for(int i=0;i< files.length;i++)
            chunks[i] = new Chunk(movie,files[i].getName(), SHA256.getHash(files[i]));
        return chunks;
    }
}
