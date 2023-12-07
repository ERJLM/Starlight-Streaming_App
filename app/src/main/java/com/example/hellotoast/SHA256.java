package com.example.hellotoast;

import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {


    static public String getHash(File file)
    {
        try
        {
            InputStream inputStream = new FileInputStream(file);
            byte buffer[] = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            for (int numRead = 0; (numRead = inputStream.read(buffer)) > 0; ) {
                digest.update(buffer, 0, numRead);
            }
            inputStream.close();
            return toHexString(digest.digest());
        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }
        return "";
    }

    static private String toHexString(byte b[]) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toHexString(b[i] & 0xFF));
        }
        return sb.toString();
    }
}
