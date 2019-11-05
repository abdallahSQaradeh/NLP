/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import com.sun.jndi.toolkit.dir.DirSearch;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.util.Pair;

/**
 *
 * @author abooo
 */
public class Classifier extends Stemmer {
 Vector<String> orderd = new Vector<String>();
    PrintWriter write; 
    int docsCount =0;
    HashMap<String,Double> unigramtraining = new HashMap<String,Double>();
    List<ClassInfo> classinformation = new ArrayList<ClassInfo>();
    HashMap<String,HashMap<String,Float>> classGivenwor = new HashMap<String,HashMap<String, Float>>();
    Set<String> Stopwords = new HashSet<String>();
    HashMap<String,Float> classes =new HashMap<String,Float>();
    private void printOnFile() {
        Set<String> read= classGivenwor.keySet();
        Iterator<String> reading = read.iterator();
        try {
            PrintWriter porobability = new PrintWriter("wordICProbalbility.txt");
            HashMap<String,Float> holding;
             Set<String> entrymap ;
             Iterator<String> entry;
             String classname;
             String word;
             while(reading.hasNext()){
             classname = reading.next();
             holding = classGivenwor.get(classname);
             entrymap = holding.keySet();
             entry = entrymap.iterator();
              porobability.write("<s> "+classname+" <-s-> ");
             while(entry.hasNext())
             {
              word = entry.next();
              porobability.write(word +" "+classGivenwor.get(classname).get(word)+ " ");
              porobability.flush();
             }
             porobability.println();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        }

    class ClassInfo {
        int numberOfDoc;
        int numberOfWord;
        String name;
        float probability;
    }
  void readStopWord() {
      
      File stopwords = new File("stopwords.txt");
      if(stopwords.exists() && stopwords.isFile())
      {
          try {
              Scanner reader  = new Scanner(stopwords);
              while(reader.hasNext())
              {
                  Stopwords.add(reader.next());
              }
          } catch (FileNotFoundException ex) {
              Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
      
    }
  
 
    public void addAndStem(String temp, Stemmer x, HashMap<String, Integer> unigram, PrintWriter writef, ArrayList<String> test) {
        x.add(temp.toCharArray(), temp.length());
        x.stem();

        if (temp.length() != 0) {
            if (!unigram.containsKey(x.toString())) {
                unigram.put(x.toString(), 1);
               

            } else {
                unigram.put(x.toString(), 1 + unigram.get(x.toString()));

            }
             orderd.add(temp);//all word
            writef.println(x.toString() + " " + unigram.get(x.toString()));
            writef.flush();
            //   writer.println(x.toString());
        }
    }

    public void read(HashMap<String, Integer> unigram, File file, String Stemmed, String frequancywrite, ArrayList<String> testing,String unigramfile) {
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
        boolean hasAstopWord = false;
       // ClassInfo information = new ClassInfo();
        if (file.exists() /*&& file.isDirectory()*/) {
            int i = 0;
            int location;
            float value=0;
            int j,flag=0;
            int countword=0;
           try {
                PrintWriter write = new PrintWriter(Stemmed);
                PrintWriter writef = new PrintWriter(frequancywrite);
                File dirs[] = file.listFiles();
                while (i < dirs.length) {
                    j = 0;
                    ClassInfo info =new ClassInfo();
                   File files[] = dirs[i].listFiles();
                   String parents;
                 while (j < files.length && files[j].isFile()) {
                        System.out.println(count++);

                        read = new Scanner(files[j]);
                        while (read.hasNext()) {
                            store = read.next();
                            hasAstopWord = Stopwords.contains(store);
                            if(hasAstopWord)
                            {
                                continue;
                            }
                            else{
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
                            countword++;
                            info.numberOfWord = countword;
                          //location=dirs[i].getName();
                          parents = dirs[i].getName();
                         // information.name= parents;1
                         
                          //information.numberOfWord=countword;
                          info.name=parents;
                        if(classGivenwor.containsKey(parents))
                        {
                            if(classGivenwor.get(parents).containsKey(orderd.get(orderd.size()-1))){
                                
                                value = classGivenwor.get(parents).get(orderd.elementAt(orderd.size()-1));
                                value++;
                              classGivenwor.get(parents).put(orderd.get(orderd.size()-1),value);
                        }
                            else
                            {
                               
                               classGivenwor.get(parents).put(orderd.get(orderd.size()-1),(float)1); 
                            }
                        }else{
                            HashMap<String,Float> newEntr = new HashMap<String,Float>();
                            newEntr.put(orderd.elementAt(orderd.size()-1),(float) 1);
                            
                      classGivenwor.put(parents,newEntr);
                      if(flag==0)
                            {
                                classGivenwor.get(parents).put("UNK",(float)1);
                                flag++;
                            }
                    }
                   
                        }
                        
                        classes.put(dirs[i].getName(),(float) files.length);
                          
                    
                 // }
             
                // writef.print (bigrams.get("been")+1);
                PrintWriter unigramcotent = new PrintWriter(unigramfile);
                unigram.put("UNK", 1);
                Set<String> set = unigram.keySet();
                Iterator<String> ite = set.iterator();
                String templete;
                while (ite.hasNext()) {
                    templete = ite.next();
                    unigramcotent.println(templete + " " + unigram.get(templete));
                }
                unigramcotent.flush();
                unigramcotent.close();
                //System.out.println(unigram.size() + "------------");
                 j++;
                 docsCount++; 
                 info.numberOfDoc = j;
        }
                 classinformation.add(info);
                 countword=0;
                 flag=0;
                  i++;
        }  
if (file.exists() && file.isFile()) {
            try {
                read = new Scanner(file);
                while (read.hasNext()) {
                    store = read.next();
                     hasAstopWord = Stopwords.contains(store);
                            if(hasAstopWord)
                            {
                                continue;
                            }
                            else{
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
                }
              //  System.out.println(unigram.size());
                //System.out.println(testing.size());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        }   catch (FileNotFoundException ex) {
                Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        System.out.println("end read");
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
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
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
            File tests[] = file.listFiles();
            String jded = "";
            testing = new ArrayList<String>();
            Scanner read = new Scanner(file);
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
            prob += Math.log10(unigram.get(array[0]) / (double) fullCount);
            for (int j = 0; j < array.length - 1;) {
                syntax = array[j++];

                temp = array[j];
                if (bigram.containsKey(temp) && bigram.get(temp).containsKey(syntax)) {

                    prob += Math.log10(bigram.get(temp).get(syntax));
                } else {
                    int count = unigram.get(syntax);
                    prob += Math.log10((double) count / (count + unigram.size()));
       
                }
            }
            System.out.println(prob);

        }
    }
    HashMap<String,HashMap<String,Double>> testDockProbability(HashMap<String,HashMap<String,HashMap<String,Double>>> classtesting)
    {
        Double max=-100.0;
        Double value1,value2;
        List<ClassInfo> classinformationT = new ArrayList<ClassInfo>();
        HashMap<String,Double> holder = new HashMap<String,Double>();
        ClassInfo informationT = new ClassInfo();
        File traininginfo = new File("trainningInfo.txt");
        String ClassT  ;
     try {
         Scanner readinfo = new Scanner(traininginfo);
         while(readinfo.hasNext())
         {
           
           informationT.name = readinfo.next();
           informationT.probability = readinfo.nextFloat();
           informationT.numberOfDoc = readinfo.nextInt();
           informationT.numberOfWord = readinfo.nextInt();
           classinformationT.add(informationT);
           informationT =null;
           informationT=new ClassInfo();
         }
     } catch (FileNotFoundException ex) {
         Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
     }
     
     Set<String> probabilitySet;
     Iterator<String> walkOnprob;
        HashMap<String,Double> entry;
        HashMap<String,HashMap<String,Double>> DocWithClassProb = new HashMap<String,HashMap<String,Double>>();
        Set<String> ClassesSet = classtesting.keySet();
        Iterator<String> Iteratorclass = ClassesSet.iterator();
        Iterator<String> Iteratordocs;
        Iterator<String> Iteratorwords;
        String classkey = "",docskey="",wordskey ="";
        Set<String> DocsSet;
        Set<String> WordsSet;
        Double prob=0.0;
        double value=0;
       int loc=0;
       ClassInfo []TrainingInfo = new ClassInfo[classinformationT.size()] ;
       classinformationT.toArray(TrainingInfo);
        while(Iteratorclass.hasNext())
        {
            classkey = Iteratorclass.next();
            DocsSet = classtesting.get(classkey).keySet();
            Iteratordocs = DocsSet.iterator();
            while(Iteratordocs.hasNext())
            {
                docskey = Iteratordocs.next();
                 for(int i=0;i<classinformationT.size();i++){
                
                        WordsSet= classtesting.get(classkey).get(docskey).keySet();
                        Iteratorwords = WordsSet.iterator();
                        while(Iteratorwords.hasNext())
                        {
                           
                            
                           wordskey = Iteratorwords.next();
                           value = classtesting.get(classkey).get(docskey).get(wordskey);
                           if(value==0.0)
                           {
                               for(int k=0;k<TrainingInfo.length;k++)
                               {
                                   if(TrainingInfo[k].name.equals(classkey)){
                                       loc =k;
                                   break;
                                   }
                               }
                                     value = (classtesting.get(classkey).get(docskey).get(wordskey)+1.0)/(double)(TrainingInfo[loc].numberOfWord+unigramtraining.size()); 
                                   
                               
                               
                         
                        }else if(wordskey.equals("UNK"))
                        {
                             for(int k=0;k<TrainingInfo.length;k++)
                               {
                                   if(TrainingInfo[k].name.equals(classkey)){
                                       loc =k;
                                   break;
                                   }
                               }
                            value =( classtesting.get(classkey).get(docskey).get(wordskey) + 1.0)/((double)TrainingInfo[loc].numberOfWord+unigramtraining.size());
                        }
                                        prob +=Math.log10(value);
                                       
                                        
                                    }
                                        /*entry = new HashMap<String,Double>();
                                        entry.put(docskey,prob*classinformationT.get(loc).probability);*/
                                        holder.put(TrainingInfo[i].name,prob+Math.log10(TrainingInfo[i].probability));
                                        //DocWithClassProb.put(classkey,entry);
                                        Iteratorwords = WordsSet.iterator();
                                        prob=0.0;
                            }
                 probabilitySet = holder.keySet();
                 walkOnprob = probabilitySet.iterator();
                 while(walkOnprob.hasNext()){
                     value1=holder.get(walkOnprob.next());
                     
                     if(max>value1)
                     max =max;
                     else
                         max=value1;
                     
                             }
                 if(DocWithClassProb.containsKey(classkey))
                 {
                    HashMap<String,Double> estmateValue = new HashMap<String,Double>();
                 estmateValue.put(docskey, max);
                 DocWithClassProb.get(classkey).put(docskey, max) ;
                 }
                 else
                 {
                      HashMap<String,Double> estmateValue = new HashMap<String,Double>();
                 estmateValue.put(docskey, max);
                 DocWithClassProb.put(classkey,estmateValue);
                 }
                
               //  DocWithClassProb.get(classkey).put(estmateValue);
                 
            }
        }
       return  DocWithClassProb;
        }
    void retrriveTraining()
    {
        
    }
    void coputeFSCore( HashMap<String,HashMap<String,Double>> predection)
    {
        File test = new File("test");
        int i=0;
        HashMap<String,HashMap<String,Integer>> testfile = new HashMap<String,HashMap<String,Integer>>();
        if(test.exists() && test.isDirectory())
        {
            File []dirs = test.listFiles();
            while(i<dirs.length)
            {
               testfile.put(dirs[i].getName(), new HashMap<String,Integer>());
              File files[] = dirs[i].listFiles();
              int j=0;
              HashMap<String,Integer> docs ;
              while(j<files.length)
              {
                  testfile.get(dirs[i].getName()).put(files[j].getName(),0);
                  j++;
              }
              i++;
            }
            
        }
        
        Set<String> classespredection = predection.keySet();
        Set<String> docspredection ;
        Set<String> classtest  = testfile.keySet();
        Set<String> docstest;
        Iterator<String> iteratorclasspredection;
        Iterator<String> iteratordocspredection;
        Iterator<String> iteratortestclass;
        Iterator<String> iteratortestdocs;
        iteratorclasspredection = classespredection.iterator();
        iteratortestclass = classtest.iterator();
        String predectionclass;
        String testclass;
        String docsPredection , docsTest;
        int value =0;
        int truePos = 0;
        int falsePos = 0;
        while(iteratorclasspredection.hasNext())
        {
            predectionclass=iteratorclasspredection.next();
            testclass = iteratortestclass.next();
            docspredection = predection.get(predectionclass).keySet();
            docstest = testfile.get(testclass).keySet();
            iteratordocspredection = docspredection.iterator();
            iteratortestdocs = docstest.iterator();
            while(iteratordocspredection.hasNext())
            {
                docsPredection = iteratordocspredection.next();
                while(iteratortestdocs.hasNext())
                {
                   docsTest =iteratortestdocs.next();
                   if(docsPredection.equals(docsTest))
                   {
                       value =  testfile.get(testclass).get(docsTest);
                       testfile.get(testclass).put(docsTest,++value );
                       truePos++;
                   }
                   else falsePos++;
                }
                 iteratortestdocs = docstest.iterator(); 
            }
        }
        File readvocabulary = new File("unigramtraining.txt");
        
         Vector<String> vocabulary = new Vector<String>();
     try {
         Scanner read = new Scanner(readvocabulary);
         while(read.hasNext())
         {
             vocabulary.add(read.next());
             if(read.hasNext())
                 read.next();
         }
     } catch (FileNotFoundException ex) {
         Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
     }
     
        
            System.out.println("accuracy :" + (double) truePos / (double) (truePos + falsePos));
        
        
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scan = new Scanner(System.in);
        int flag;
         HashMap<String, Integer> unigram = new HashMap<String, Integer>();
        System.out.println("[1]learnin\t [2]testing ");
        flag = scan.nextInt();
        File file;
        String unigramfile = "unigramtraining.txt";
        if (flag == 1) {
            String temp = "";
            Classifier one = new Classifier();
           file = new File("training");
            String Stemmed, freqancy;
            Stemmed = "stemmed.txt";
            freqancy = "frequancy.txt";
            File classinfo = new File("trainningInfo.txt");
            one.readStopWord();
            one.read(unigram, file, Stemmed, freqancy, null,unigramfile);
            one.classprop(classinfo);
            one.computMLE(unigram);
            one.printOnFile();

        }//flag==1
        else {
            HashMap<String,HashMap<String,Double>> predection;
            File ClassesT = new File("wordICProbalbility.txt");
            file = new File("test");
            File unigramf = new File("unigramtraining.txt");
            String temp10;
            Integer temp12 = 0;
            try {
                Scanner readuni = new Scanner(unigramf);
                while (readuni.hasNext()) {
                    temp10 = readuni.next();
                    if (readuni.hasNextInt()) {
                        temp12 = readuni.nextInt();
                        unigram.put(temp10, temp12);
                    }
                }
                String unigramtest = "unigramtest.txt";
                String stemmed = "teststem.txt";
                String frequency = "testfrequency.txt";
                ArrayList<String> testing = new ArrayList<String>();
                HashMap<String, HashMap<String, Double>> ClassesTraining = new HashMap<String, HashMap<String, Double>>();
                HashMap<String,HashMap<String,HashMap<String,Double>>> TestClasses = new HashMap<String,HashMap<String,HashMap<String,Double>>>(); 
                Classifier two = new Classifier();
                two.readTrainingValue(ClassesTraining,ClassesT);//read from training file
                two.readStopWord();
                two.read(unigram,file,frequency,stemmed, null,unigramtest);
                two.orderd.clear();
                two.DocsPorobability(file,TestClasses,unigram,ClassesTraining);
                File classinfo = new File("testingInfo.txt");
                two.classprop(classinfo);
                predection = two.testDockProbability(TestClasses);
                two.coputeFSCore(predection);
                two.countprob(ClassesTraining, testing, unigram);
               // System.out.println("------" + count + " -----------------");


            } catch (FileNotFoundException ex) {
                Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    void DocsPorobability(File file , HashMap<String,HashMap<String,HashMap<String,Double>>> TestClasses,HashMap<String, Integer> unigram , HashMap<String,HashMap<String,Double>> ClassesTraining )
    {
         Scanner read;
        int count = 0;
        char syntax[];
        char msysntax[];
        File frequancy;
        File stemed;
        String doc;
        String temp = "";
        Stemmer x = new Stemmer();
        String store = "";
        boolean hasAstopWord = false;
       // ClassInfo information = new ClassInfo();
        if (file.exists() && file.isDirectory()) {
            int i = 0;
            int location;
            double value=0;
            int j,flag=0;
            int countword=0;
            String lastwodInorder =" ";
           try {
                PrintWriter write = new PrintWriter("teststem.txt");
                PrintWriter writef = new PrintWriter("testfrequency.txt");
                File dirs[] = file.listFiles();
                while (i < dirs.length) {
                    j = 0;
                    ClassInfo info =new ClassInfo();
                   File files[] = dirs[i].listFiles();
                   String parents;
                 while (j < files.length && files[j].isFile()) {
                        System.out.println(count++);

                        read = new Scanner(files[j]);
                        while (read.hasNext()) {
                            store = read.next();
                            hasAstopWord = Stopwords.contains(store);
                            if(hasAstopWord)
                            {
                                continue;
                            }
                            else{
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
                            countword++;
                            info.numberOfWord = countword;
                          parents = dirs[i].getName();
                            doc = files[j].getName();
                          info.name=parents;
                          lastwodInorder =orderd.get(orderd.size()-1); 
                          if(TestClasses.containsKey(parents))
                          {
                              if(TestClasses.get(parents).containsKey(doc))
                              {
                                  if(TestClasses.get(parents).get(doc).containsKey(lastwodInorder))
                                  {
                                      if(ClassesTraining.get(parents).containsKey(lastwodInorder)){
                                       value = ClassesTraining.get(parents).get(lastwodInorder);
                                       TestClasses.get(parents).get(doc).put(lastwodInorder,value);
                                      }
                                      else
                                      {
                                          if(unigram.containsKey(lastwodInorder))
                                          {
                                              value=0;
                                              TestClasses.get(parents).get(doc).put(lastwodInorder,value);
                                          }
                                          else
                                          {
                                             TestClasses.get(parents).get(doc).put("UNK",1.0); 
                                          }
                                         
                                      }
                                       
                                  }
                                  else
                                  {
                                       if(ClassesTraining.get(parents).containsKey(lastwodInorder))
                                       {
                                                value=ClassesTraining.get(parents).get(lastwodInorder);
                                                TestClasses.get(parents).get(doc).put(lastwodInorder,value);
                                       }
                                       else if(unigram.containsKey(lastwodInorder))
                                       {
                                          value=0.0;
                                          TestClasses.get(parents).get(doc).put(lastwodInorder,value) ;
                                       }
                                       else
                                       {
                                        if(TestClasses.get(parents).get(doc).containsKey("UNK")) {  
                           
                                        value =TestClasses.get(parents).get(doc).get("UNK");
                                        value++;
                                        TestClasses.get(parents).get(doc).put("UNK",value);
                                        }else
                                        {
                                       
                                        value=1;
                                        TestClasses.get(parents).get(doc).put("UNK",value);
                                        }
                                        
                                       }
                                      
                                  }
                              }
                              else
                              {
                               
                                   HashMap<String,HashMap<String,Double>> document = new HashMap<String, HashMap<String,Double>>();
                              if(ClassesTraining.get(parents).containsKey(lastwodInorder)){
                                        value = ClassesTraining.get(parents).get(lastwodInorder);
                                        HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put(lastwodInorder,value);
                                         document.put(doc, newEntr);
                                         TestClasses.get(parents).put(doc, newEntr);
                                         //TestClasses.put(parents, document);
                              }
                              else
                              {
                                  if(unigram.containsKey(lastwodInorder))
                                  {
                                        HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put(lastwodInorder,0.0);
                                         document.put(doc, newEntr);
                                         TestClasses.get(parents).put(doc, newEntr);
                                  }
                                  else
                                  {
                                      HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put("UNK",1.0);
                                         document.put(doc, newEntr);
                                         TestClasses.get(parents).put(doc, newEntr);
                                  }
                                }
                              }
                          }else
                          {
                             
                              HashMap<String,HashMap<String,Double>> document = new HashMap<String, HashMap<String,Double>>();
                              if(ClassesTraining.get(parents).containsKey(lastwodInorder)){
                                        value = ClassesTraining.get(parents).get(lastwodInorder);
                                        HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put(lastwodInorder,value);
                                         document.put(doc, newEntr);
                                         TestClasses.put(parents, document);
                              }
                              else
                              {
                                  if(unigram.containsKey(lastwodInorder))
                                  {
                                        HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put(lastwodInorder,0.0);
                                         document.put(doc, newEntr);
                                         TestClasses.put(parents, document);
                                  }
                                  else
                                  {
                                      HashMap<String,Double> newEntr = new HashMap<String,Double>();
                                         newEntr.put("UNK",1.0);
                                         document.put(doc, newEntr);
                                         TestClasses.put(parents, document);
                                  }
                              }
                          }
                   
                        }
                        
                classes.put(dirs[i].getName(),(float) files.length);
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
                 j++;
                 docsCount++; 
                 info.numberOfDoc = j;
        }
                 classinformation.add(info);
                 countword=0;
                 flag=0;
                  i++;
        }
        
    }        catch (FileNotFoundException ex) {
                 Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
             }
           System.out.println("end comput");
        }
        
    }
    void readTrainingValue(HashMap<String, HashMap<String, Double>> Classes , File trainigfile)
    {
        try {
             String symbol, between,key;
                int count = 0;
                Double value;
            Scanner readT = new Scanner(trainigfile);
             HashMap<String, Double> linepairs = null;
              while (readT.hasNext()) {
                    symbol = readT.next();
                    if (symbol.equals("<s>")) {

                        between = readT.next();
                        linepairs = new HashMap<String, Double>();
                        Classes.put(between, linepairs);
                    } else if (symbol.equals("<-s->")) {
                        if (readT.hasNext()) {
                            key = readT.next();
                            if (readT.hasNextDouble()) {
                                value = readT.nextDouble();
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
             
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
               
        
    }
    void computMLE(HashMap<String,Integer> unigram)
    {
        List<ClassInfo> info =  classinformation;
        
        HashMap<String,Float> entrymap;
        Set<String> classes = classGivenwor.keySet();
        Set<String> entry ;
        Iterator<String> readEntry;
        Iterator<String> iterator = classes.iterator();
        String word;
        String classKey;
        ClassInfo information;
         for(int i=0;i<info.size();i++)
        {
            information =info.get(i);
            classKey=information.name;
            entrymap = classGivenwor.get(classKey);
            entry = entrymap.keySet();
           readEntry=entry.iterator();
           while(readEntry.hasNext())
           {
               word = readEntry.next();
               classGivenwor.get(classKey).put(word, (classGivenwor.get(classKey).get(word)+1)/(information.numberOfWord+unigram.size()));
           }
        
       }
    }
   void classprop(File file) {
       ArrayList<ClassInfo> collection = (ArrayList<ClassInfo>) classinformation;
         ClassInfo inform = new ClassInfo();
     try {
         PrintWriter printinfo = new PrintWriter(file);
         for(int i=0;i<collection.size();i++)
         {
             inform = collection.get(i);
             inform.probability=inform.numberOfDoc/(float)docsCount;
             printinfo.write(inform.name+" "+inform.probability+ " "+inform.numberOfDoc+" "+inform.numberOfWord+"\n");
         }
         printinfo.flush();
     } catch (FileNotFoundException ex) {
         Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
     }

    }
}
