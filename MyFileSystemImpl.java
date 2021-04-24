
package com.leapfinance;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class MyFileSystemImpl  implements IFileSystem{

    private File myfile = null;
    private String pathname = "myFile.txt";

    public MyFileSystemImpl(String filename) throws Exception{

        try {
            myfile = new File(pathname);
            if (myfile.createNewFile()) {
                System.out.println("File created at " + myfile.getCanonicalPath());
                System.out.println("File name: " + myfile.getName());
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            throw e;
        }

    }

    //create key value pair in text file
    public synchronized void create(String key,String value, long timeToLive) throws Exception{

        FileWriter fr = null;
        BufferedWriter br = null;


        try {

            if(key.length() > 32){
                throw new Exception("key is larger than 32 characters");
            }

            if(value.getBytes(StandardCharsets.UTF_8).length > 16 * 1024){
                throw new Exception("value is larger than 16KB ");
            }

            //check time to live and delete key value if it exceeds time
            delete(null);

            fr = new FileWriter(myfile, true);
            br = new BufferedWriter(fr);


            if (ifKeyExist(key) == true)
            {
               throw new Exception("key already exists");
            }

            else {
                br.write(key);
                br.newLine();
                br.write(value);
                br.newLine();
                br.write(liveTime(timeToLive + System.currentTimeMillis()));
                br.newLine();
            }


        } catch (IOException e){
             throw e;
        } finally {

            try {
                br.close();
                fr.close();
            } catch (IOException e) {
               throw e;
            }

        }

        return ;

    }

    //delete key from text file
    public synchronized void delete(String key) throws Exception{

        String currentTime = liveTime(System.currentTimeMillis());

        try {
            FileReader fr = new FileReader(myfile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            LinkedList<String> ls = new LinkedList<>();


            while((line = br.readLine()) != null) {
                String value = br.readLine();
                String timeToLive = br.readLine();

                if( timeToLive.compareTo(currentTime) < 0) continue;

                if ( line.equals(key) ) {
                    
                    continue;
                }

                ls.add(line);
                ls.add(value);
                ls.add(timeToLive);
            }

            FileWriter fw = new FileWriter(myfile,false);
            fw.flush(); fw.close();

            fw = new FileWriter(myfile,true);
            BufferedWriter bw = new BufferedWriter(fw);

            while(ls.size() > 0){
                bw.write(ls.pollFirst());bw.newLine();
                bw.write(ls.pollFirst());bw.newLine();
                bw.write(ls.pollFirst());bw.newLine();
            }

            bw.close(); fw.close();
            }
        catch (IOException e){
            throw e;
        }

        return ;
    }

    //read key value from text file
    public synchronized String read(String key) throws Exception {

        FileReader fr = null;
        BufferedReader br = null;
        String currentTime = liveTime(System.currentTimeMillis());
        String value = "";
        try {
            fr = new FileReader(myfile);
            br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null) {
                value = br.readLine();
                String timeToLive = br.readLine();
                if (line.equals(key) && timeToLive.compareTo(currentTime) > 0 ) {
                   break;
                }
            }

        } catch (IOException e){
                throw e;

        } finally {
            try {
                br.close();
                fr.close();
            }catch(Exception e){
              throw e;
            }
        }

        return value;

    }

    // helper function
   private boolean ifKeyExist(String key){

       FileReader fr = null;
       BufferedReader br = null;
       boolean success = false;
        try {
        fr = new FileReader(myfile);
        br = new BufferedReader(fr);

        String line;
        while((line = br.readLine()) != null) {
            if (line.equals(key) ) {
                success = true;
                break;
            }
                br.readLine();
                br.readLine();
        }

        } catch (IOException e){


        } finally {
            try {
                br.close();
                fr.close();
            }catch(Exception e){

            }
        }


        return success;

    }

    private String liveTime(long time){
        return String.valueOf(time);
    }


}
