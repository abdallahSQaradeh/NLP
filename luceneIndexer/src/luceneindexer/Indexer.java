/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneindexer;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author abooo
 */
public class Indexer {
     public static final String CONTENTS="contents";
    public static final String FILE_NAME="filename";
    public static final String FILE_PATH="filepath";
    public static final int MAX_SEARCH=12000;
    private IndexWriter writer;
    public Indexer(String indexDirectoryPath)
    {
        try {
            Directory indexdirectory = FSDirectory.open(new File(indexDirectoryPath));
            writer = new IndexWriter(indexdirectory, new StandardAnalyzer(Version.LUCENE_20),true,IndexWriter.MaxFieldLength.UNLIMITED);
            
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void Closer() 
    {
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Document getDocument(File file){
        Document document = new Document();
         try {
             Field contentField = new Field(CONTENTS, new FileReader(file));
             Field fileNameField = new Field(FILE_NAME,file.getName(),Field.Store.YES,Field.Index.NOT_ANALYZED);
             Field filePAthField = new Field(FILE_PATH, file.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED);
             document.add(contentField);
             document.add(fileNameField);
             document.add(filePAthField);
             
         } catch (FileNotFoundException ex) {
             Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
             Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
         }
         return document;
    }
    private void indexFile(File file)
    {
         try {
             System.out.println("Indexing " + file.getCanonicalPath());
             Document document = getDocument(file);
             writer.addDocument(document);
         } catch (IOException ex) {
             Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    public int createIndex(String dataDirpath , TextFileFilter filter) throws IOException
    {
        File [] files = new File(dataDirpath).listFiles();
        for(File file: files)
        {
            File [] Docs = file.listFiles();
            for(File doc : Docs)
            if(!doc.isDirectory() && !doc.isHidden() && doc.exists() && filter.accept(file))
            {
                indexFile(doc);
            }
        }
        return writer.numDocs();
    }
    
}
