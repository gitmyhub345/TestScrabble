/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dragoncave.home.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Rider1
 */
public class TestPropFile {
    private Path path;
    private File file;
    private FileInputStream fileInput;
    private BufferedReader bufferedReader;
    public TestPropFile(){}
    
    public void openfile(String filename) throws FileNotFoundException {
        //Path path = FileSystems.getDefault().getPath("/users/rider1");
        path = Paths.get(System.getProperty("user.home"),"documents","testFiles",filename);
        file = new File(path.toString());
        fileInput = new FileInputStream(file);
    }
    
    public boolean readFile(byte[] a) throws FileNotFoundException, IOException {
        boolean endOfFile = true;
        int c=fileInput.read(a, 0, a.length);
        
        if (c > 0 && c <= a.length){
            endOfFile = false;
//            System.out.println("number of bytes read: "+c+"\nLast byte: \t"+(char)a[c-1]);
        }
        
        return endOfFile;
    }
    
    public boolean readFile(String str) throws Exception{
        boolean result = false;
//        path = Paths.get(System.getProperty("user.home"),"documents","testFiles",filename);
        try {
//            BufferedReader br = new BufferedReader(new FileReader(path.toString()));
            str = bufferedReader.readLine();
            result =true;
            
        } catch (FileNotFoundException e){
            result = false;
        }
        
        return result;
    }
    
    public void closeFile(){
        try {
            fileInput.close();
        } catch (IOException ex){
//            Logger.getLogger(Javaconsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
