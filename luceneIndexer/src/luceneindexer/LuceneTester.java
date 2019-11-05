/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneindexer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Scanner;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author abooo
 */
public class LuceneTester {
    String indDir = "C:\\Users\\abooo\\OneDrive\\Documents\\NetBeansProjects\\luceneIndexer\\index";
    String dataDir = "C:\\Users\\abooo\\OneDrive\\Documents\\NetBeansProjects\\luceneIndexer\\training";
    Indexer indexer;
    Searcher searcher;
    public static void main(String args[]) throws IOException
    {
        LuceneTester tester;
            Scanner scan = new Scanner(System.in);
            int num;
            tester = new LuceneTester();
            System.out.println("[1] indexing \t [2] retrive");
            num =scan.nextInt();
            if(num==1)
            tester.createIndex();
            else if(num ==2){
                System.out.println("enter query");
                scan =new Scanner(System.in);
                String statement = scan.nextLine();
            tester.search(statement);
            
            }
            else
                System.out.println("wrong number");
            
        }

    private void createIndex() throws IOException {
    indexer = new Indexer(indDir);
    int numIndexed;
    long startTime = System.currentTimeMillis();
    numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
    long endTime = System.currentTimeMillis();
    indexer.Closer();
    System.out.println(numIndexed + " File indexed, time taken: = "+(endTime-startTime)+ "ms");
    
    
    }
    

    private void search(String searchQuery) throws IOException {
       searcher  = new Searcher(indDir);
       long startTime = System.currentTimeMillis();
       TopDocs hits = searcher.search(searchQuery);
       long endTime = System.currentTimeMillis();
       int count =0;
       for(ScoreDoc scorDoc: hits.scoreDocs)
       {
           Document doc = searcher.getDocument(scorDoc);
           System.out.println("File : " + doc.get(LuceneConstants.FILE_PATH));
           count++;
           
       }
       System.out.println(searchQuery +" matches in "+ count + " docs");
       searcher.close();
       
       
    }
    }
    

