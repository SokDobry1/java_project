package sfedu.railway.utils;

import java.io.FileInputStream;
import java.io.IOException;
import sfedu.railway.exceptions.FileLoadErrorException;

public class FileLoad {
    public void loadFile(String fileName) throws FileLoadErrorException {
        try{
            FileInputStream fis = new FileInputStream(fileName);
            fis.close();
        }catch (IOException e){
            throw new FileLoadErrorException("Could not load file: " + fileName);
        }
    }
}
