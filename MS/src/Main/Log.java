package Main;

import java.io.File;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Log {
    private File file = new File("log.txt");
    public Log(){
        try {
            if(!file.exists()){
                file.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(file);
                PrintStream p = new PrintStream(fileOut);
                p.println(String.format("%15s", "日志文件"));
                fileOut.close();
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void addLog(String str){
        try{
            FileOutputStream fileOut = new FileOutputStream(file, true);
            PrintStream p = new PrintStream(fileOut);
            Date day = new Date();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            FileInputStream fileIn = new FileInputStream(file);
            Scanner in = new Scanner(fileIn);
            p.println(df.format(day).toString() + " " + String.format("%15s", str));
            fileOut.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

}
