/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;

/**
 *
 * @author abooo
 */
public class Bigram extends Stemmer {

    public void addAndStem(String temp, Stemmer x, HashMap<String, Integer> unigram, PrintWriter writef, ArrayList<String> test) {
        x.add(temp.toCharArray(), temp.length());
        x.stem();

        if (temp.length() != 0) {
            if (!unigram.containsKey(x.toString())) {
                unigram.put(x.toString(), 1);

            } else {
                unigram.put(x.toString(), 1 + unigram.get(x.toString()));

            }
            writef.println(x.toString() + " " + unigram.get(x.toString()));
            //1
            //   writer.println(x.toString());
        }
    }

    public void read(HashMap<String, Integer> unigram, File file, String Stemmed, String frequancywrite, ArrayList<String> testing) {
        testing = new ArrayList<String>();
        Scanner read;
        int count = 0;
        char syntax[];
        char msysntax[];
        File frequancy;
        File stemed;
        String temp = "";
        Stemmer x = new Stemmer();
        String store = "";
        if (file.exists() && file.isDirectory()) {
            int i = 0;
            int j;
            try {
                PrintWriter write = new PrintWriter(Stemmed);
                PrintWriter writef = new PrintWriter(frequancywrite);
                File dirs[] = file.listFiles();
                while (i < dirs.length) {
                    j = 0;
                    File files[] = dirs[i].listFiles();
                    while (j < files.length && files[j].isFile()) {
                        //System.out.println(count++);

                        read = new Scanner(files[j]);
                        while (read.hasNext()) {
                            store = read.next();
                            syntax = store.toCharArray();
                            for (int c = 0; c < syntax.length; c++) {
                                if (Character.isLetter(syntax[c])) {
                                    temp += Character.toLowerCase(syntax[c]);

                                } else {
                                    addAndStem(temp, x, unigram, writef, null);
                                    temp = "";
                                }
                            }

                            addAndStem(temp, x, unigram, writef, null);
                            temp = "";
                        }
                        j++;
                    }
                    i++;
                }
                // writef.print(bigrams.get("been")+1);
                PrintWriter unigramcotent = new PrintWriter("unigram.txt");
                unigram.put("UNK", 1);
                Set<String> set = unigram.keySet();
                Iterator<String> ite = set.iterator();
                String templete;
                while (ite.hasNext()) {
                    templete = ite.next();
                    unigramcotent.println(templete + " " + unigram.get(templete));
                }
                unigramcotent.close();
                //System.out.println(unigram.size() + "------------");

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Bigram.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (file.exists() && file.isFile()) {
            try {
                read = new Scanner(file);
                while (read.hasNext()) {
                    store = read.next();
                    syntax = store.toCharArray();
                    for (int c = 0; c < syntax.length; c++) {
                        if (Character.isLetter(syntax[c])) {
                            temp += Character.toLowerCase(syntax[c]);

                        } else {
                            addAndStem(temp, x, unigram, null, testing);
                            temp = "";
                        }
                    }

                    addAndStem(temp, x, unigram, null, testing);
                    temp = "";
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Bigram.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public HashMap<String, HashMap<String, Integer>> freqbi(HashMap<String, HashMap<String, Integer>> bigram, HashMap<String, Integer> unigram) {
        try {
            int count = 0;
            File file = new File("frequancy.txt");
            Scanner read = new Scanner(file);
            PrintWriter bigramfreq = new PrintWriter("bigramfreq.txt");
            String temp = "";
            String tempprev;

            while (read.hasNext()) {
                if (count == 0) {
                    tempprev = read.next();
                    read.next();
                    temp = read.next();
                    read.next();
                    count++;
                } else {
                    tempprev = temp;
                    temp = read.next();
                    if (read.hasNext()) {
                        read.next();
                    }

                }

                if (bigram.containsKey(temp)) {
                    bigramfreq.print("<s> " + temp + " <s>");
                    if (bigram.get(temp).containsKey(tempprev)) {
                        bigram.get(temp).put(tempprev, bigram.get(temp).get(tempprev) + 1);
                        bigramfreq.print(" " + tempprev + " " + bigram.get(temp).get(tempprev));

                    } else {
                        bigram.get(temp).put(tempprev, 2);
                        bigramfreq.print(" " + tempprev + " " + bigram.get(temp).get(tempprev));
                    }
                    bigramfreq.println();

                }

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bigram.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bigram;
    }

    void print_stmt_with_pairs(HashMap<String, HashMap<String, Integer>> bigram, HashMap<String, Integer> unigram) {
        int count = 0;
        try {
            PrintWriter frequancybigram = new PrintWriter("frequancybigram.txt");
            PrintWriter proability = new PrintWriter("probability.txt");
            Set<String> set = bigram.keySet();
            Iterator<String> iterator = set.iterator();
            Set<String> setuni;
            Iterator<String> iteratoruni;
            String tempe = "";
            String tampo = "";
            Integer countWP, countW, countzroes;
            double Propapility;
            double probabilityZeroes;
            int unigramsize = unigram.size();
            while (iterator.hasNext()) {
                tempe = iterator.next();
                frequancybigram.print("<s> " + tempe + " <-s-> ");
                proability.print("<s> " + tempe + " <-s-> ");
                if (bigram.containsKey(tempe)) {
                    setuni = bigram.get(tempe).keySet();
                    iteratoruni = setuni.iterator();
                    while (iteratoruni.hasNext()) {
                        tampo = iteratoruni.next();
                        if (bigram.get(tempe).containsKey(tampo)) {
                            countWP = bigram.get(tempe).get(tampo);
                            countW = unigram.get(tampo);
                            Propapility = (double) ((countWP + 1) * countW) / ((countW + unigramsize) * 1.0);
                            frequancybigram.print(tampo + " " + countWP + " ");
                            proability.print(tampo + " " + Propapility + " ");
                            count++;
                        }

                    }

                }
                frequancybigram.println();
                proability.println();
            }
            proability.flush();
           // System.out.print(bigram.size());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bigram.class.getName()).log(Level.SEVERE, null, ex);
        }
      //  System.out.println("count" + count);
    }

    public String addAndStemtest(String temp, Stemmer x, ArrayList<String> testing, String Jded, boolean i, int f, HashMap<String, Integer> unigram) {
        if (temp.equals("")) {
            return Jded;

        }
        x.add(temp.toCharArray(), temp.length());
        x.stem();
        if (!unigram.containsKey(x.toString())) {
            unigram.put("UNK", unigram.get("UNK") + 1);
            if (f == 0) {
                Jded += "UNK";
            } else {
                if (!x.toString().equals(" ")) {
                    Jded += " UNK";
                }
            }
        } else {
            if (f == 0) {
                Jded += x.toString();
            } else {
                if (!x.toString().equals(" ")) {
                    Jded += " " + x.toString();
                }
            }
        }

        return Jded;
    }

    public ArrayList<String> readtesting(File file, ArrayList<String> testing, HashMap<String, Integer> unigram) {
        try {
            String jded = "";
            testing = new ArrayList<String>();
            Scanner read;
            int count = 0;
            char syntax[];
            char msysntax[];
            File frequancy;
            File stemed;
            String temp = "";
            Stemmer x = new Stemmer();
            String store = "";
            String statement;
            String arra[];
            int arraylemgth;
            read = new Scanner(file);
            while (read.hasNextLine()) {
                jded = "";
                arra = read.nextLine().split(" ");
                for (int i = 0; i < arra.length; i++) {
                    statement = arra[i];
                    syntax = arra[i].toCharArray();
                    arraylemgth = statement.length();

                    for (int c = 0; c < syntax.length; c++) {
                        if (Character.isLetter(syntax[c])) {
                            temp += Character.toLowerCase(syntax[c]);

                        } else {
                            jded = addAndStemtest(temp, x, testing, jded, i == arra.length - 1, i, unigram);
                            temp = "";
                        }

                    }
                    if (arra[i].length() == 0 || arra[i].equals(" ")) {
                        continue;
                    } else {

                        jded = addAndStemtest(temp, x, testing, jded, i == arra.length - 1, i, unigram);
                        temp = "";
                    }

                }

                if (jded.length() != 0) {
                    testing.add(jded);
                }
            }

           // System.out.println("-------------" + testing.size() + "-----");

        } catch (FileNotFoundException e) {

        }
        return testing;
    }

    /**
     * @param args the command line arguments
     */
    public void countprob(HashMap<String, HashMap<String, Double>> bigram, ArrayList<String> content, HashMap<String, Integer> unigram) {
        Pair<Pair<String, String>, Double> pair;

        String array[];
        String temp, syntax;
        HashMap<String, Double> unk = new HashMap<String, Double>();
        int fullCount = 0;
        for (String key : unigram.keySet()) {
            fullCount += unigram.get(key);
        }
        for (int i = 0; i < content.size(); i++) {
            List<String> zeft = new ArrayList<String>();
            array = content.get(i).split(" ");
            for (String z : array) {
                if (!z.equals("")) {
                    zeft.add(z);
                }
            }
            array = zeft.toArray(new String[0]);
            double prob = 0.0;
            
            
          
            for (int j = 0; j < array.length - 1;) {
                syntax = array[j++];

                temp = array[j];
                if (bigram.containsKey(temp) && bigram.get(temp).containsKey(syntax)) {

                    prob += Math.log10(bigram.get(temp).get(syntax));
                } else {
                    if(unigram.containsKey(syntax)){
                    int count = unigram.get(syntax);
                    prob += Math.log10(((double) count) / (count + unigram.size()));
                    }
                    else
                    {
                         prob += Math.log10((double) (unigram.get("UNK")) / (unigram.get("UNK")+ unigram.size()));
                    }
       
                }
            }
            System.out.println(prob);

        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scan = new Scanner(System.in);
        int flag;
        System.out.println("[1]learnin\t [2]testing ");
        flag = scan.nextInt();
        File file;
        if (flag == 1) {
            String temp = "";
            Bigram one = new Bigram();
            file = new File("training");

            HashMap<String, Integer> unigram = new HashMap<String, Integer>();
            HashMap<String, HashMap<String, Integer>> bigram = new HashMap<String, HashMap<String, Integer>>();
            String Stemmed, freqancy;
            Stemmed = "stemmed.txt";
            freqancy = "frequancy.txt";
            one.read(unigram, file, Stemmed, freqancy, null);
            Set<String> setuni = unigram.keySet();
            Iterator<String> iteratoruni = setuni.iterator();
            Set<String> set = unigram.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                bigram.put(iterator.next(), new HashMap<String, Integer>());
            }
            HashMap<String, HashMap<String, Integer>> bies = one.freqbi(bigram, unigram);
            one.print_stmt_with_pairs(bies, unigram);

        }//flag==1
        else {
            File bigramprop = new File("probability.txt");
            file = new File("content.txt");
            File unigramf = new File("unigram.txt");
            String temp10;
            Integer temp12 = 0;
            try {
                HashMap<String, Integer> unigram = new HashMap<String, Integer>();
                Scanner readuni = new Scanner(unigramf);
                while (readuni.hasNext()) {
                    temp10 = readuni.next();
                    if (readuni.hasNextInt()) {
                        temp12 = readuni.nextInt();
                        unigram.put(temp10, temp12);
                    }
                }
                ArrayList<String> testing = new ArrayList<String>();
                HashMap<String, HashMap<String, Double>> bigram = new HashMap<String, HashMap<String, Double>>();
                Bigram two = new Bigram();
                Scanner read = new Scanner(file);
                Scanner readT = new Scanner(bigramprop);
                HashMap<String, Double> linepairs = null;
                testing = two.readtesting(file, testing, unigram);
                String temp, temp2;
                Pattern patern;
                int count = 0;
                String line;
                String subline;
                String key;
                Double value;
                String symbol, between;
                while (readT.hasNext()) {
                    symbol = readT.next();
                    if (symbol.equals("<s>")) {

                        between = readT.next();
                        linepairs = new HashMap<String, Double>();
                        bigram.put(between, linepairs);
                    } else if (symbol.equals("<-s->")) {
                        if (readT.hasNext()) {
                            key = readT.next();
                            if (readT.hasNextDouble()) {
                                value = readT.nextDouble();
                               // System.out.println(" key = " + key + " value = " + value);
                                linepairs.put(key, value);
                            }
                            count++;
                        }
                    } else {
                        if (readT.hasNextDouble()) {
                            linepairs.put(symbol, readT.nextDouble());
                            count++;
                        }
                    }
                }
                two.countprob(bigram, testing, unigram);
               // System.out.println("------" + count + " -----------------");


            } catch (FileNotFoundException ex) {
                Logger.getLogger(Bigram.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
