/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package porterstemerq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.tartarus.martin.Stemmer;

/**
 *
 * @author abooo
 */
public class PorterStemerq extends Stemmer{

    File writefrequency;
    /**
     * @param args the command line arguments
     */
    
    public void Frequency(File stemmed)
    {
            String read; int counts =0 ;
        try { 
             Set set = new HashSet();
            Scanner readfromfile = new  Scanner(stemmed);
            Scanner readstatement  = new Scanner(stemmed  );
            writefrequency = new File ("counts-statisitcs.txt");
            System.out.println(writefrequency.exists());
            PrintStream writer = new PrintStream(writefrequency);
            while(readstatement.hasNext())
            {
                read = readstatement.next();
                while(readfromfile.hasNext()){
                    if(readfromfile.next().equals(read))
                    counts++;
                }
                set.add(read +" "+counts);
                counts=0;
                readfromfile = new Scanner(stemmed);
            }
            Iterator iterator = set.iterator();
            while(iterator.hasNext())
            {
                writer.println(iterator.next());
            }

                   } catch (FileNotFoundException ex) {
            Logger.getLogger(PorterStemerq.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        PorterStemerq porter = new PorterStemerq();
        Stemmer x = new Stemmer();
        char array[];
        String read;
        File context = new File("content.txt");
        File stemmed = new File("stemmed-content.txt");
        try {
            PrintStream printsream = new PrintStream(stemmed);
            Scanner input = new Scanner(context);
            System.out.println(context.exists() +" "+ context.canRead() +" "+ context.canWrite());
            while(input.hasNext())
            {
                read=input.next();
                array =read.toCharArray();
                x.add(array,array.length);
                x.stem();
                printsream.println(x.toString());
            }
            porter.Frequency(stemmed);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PorterStemerq.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PorterStemerq.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

}
