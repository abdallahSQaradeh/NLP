/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regexfindout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

/**
 *
 * @author abooo
 */

public class Regexfindout {

    InputStream readinput=null;
    PrintWriter writer;
    Scanner input;
    Matcher match;
    Pattern pattern;
    /**
     * @param args the command line arguments
     */
    public void p1(File read)  
{       
        File write = new File("p1.txt");
       System.out.println(write.exists());
        String source ;
        String thispattern;
        try {
            writer = new PrintWriter("p1.txt");
            input = new Scanner(read);
           // readinput = new FileInputStream(read);
            while(input.hasNext())
            {
               source = input.next();
               thispattern = "([A-Z].{4,})";
               pattern = Pattern.compile(thispattern);
               match = pattern.matcher(source);
               while(match.find())
               {
                   
                   String h = match.group(1);
                   System.out.println(h);
                   writer.println(h);
                   writer.flush();
               }
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Regexfindout.class.getName()).log(Level.SEVERE, null, ex);
        }

}
        public void p2(File read)
{
File write = new File("p2.txt");
       System.out.println(write.exists());
        String source ;
        String thispattern;
        try {
            writer = new PrintWriter("p2.txt");
            input = new Scanner(read);
           // readinput = new FileInputStream(read);
            while(input.hasNext())
            {
               source = input.next();
               thispattern = "^([^aoiue]+e[^aoiu]?)$";
               pattern = Pattern.compile(thispattern);
               match = pattern.matcher(source);
               while(match.find())
               {

                   String h = match.group();
                   System.out.println(h);
                   writer.println(h);
                   writer.flush();
               }
            }


        } catch (FileNotFoundException ex) {
            Logger.getLogger(Regexfindout.class.getName()).log(Level.SEVERE, null, ex);
        }
}
            public void p3(File read)
{
    File write = new File("p3.txt");
       System.out.println(write.exists());
        String source ;
        String thispattern;
        try {
            writer = new PrintWriter("p3.txt");
            input = new Scanner(read);
           // readinput = new FileInputStream(read);
            while(input.hasNext())
            {
               source = input.next();
               thispattern = "^(([A-Z]\\.?)+)$";
               pattern = Pattern.compile(thispattern);
               match = pattern.matcher(source);
               while(match.find())
               {

                   String h = match.group();
                   System.out.println(h);
                   writer.println(h);
                   writer.flush();
               }
            }


        } catch (FileNotFoundException ex) {
            Logger.getLogger(Regexfindout.class.getName()).log(Level.SEVERE, null, ex);
        }

}
    public static void main(String[] args) {
        // TODO code application logic here
        Regexfindout regex = new Regexfindout();
        File content= new File("content.txt");
        System.out.println(content.exists());
        regex.p1(content);
        regex.p2(content);
        regex.p3(content);
        
        
    }

}
