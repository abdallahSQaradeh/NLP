/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matcher.pattern;
import java.io.File;
import java.io.IOException;
import java.util.regex.*;
import java.util.Scanner;
/**
 *
 * @author abooo
 */
public class MatcherPattern {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File file = new File("content.txt");
        if(!file.exists())
        {
            file.createNewFile();
        }
       Scanner scan= new Scanner(file);
       String test = "";
     while(scan.hasNextLine())
     {
         test = test +" "+ scan.next();
     }
     //System.out.println(test);
        String patern = "(([\\-|\\s|\\.]*\\w+[\\s|\\-|\\.]*\\w+[\\-|\\s|\\.]*\\w+[\\s|\\-|\\.]*\\w+,))";
        Pattern patr = Pattern.compile(patern);
        Matcher mat = patr.matcher(test);
      //  System.out.println(mat.matches());//\s*\w+\s*\w+\s*\w+\s*\w+,
        while(mat.find())
        {
            System.out.println(mat.group(1).split(",")[0]);
        }
    }

}
