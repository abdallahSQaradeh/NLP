/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package luceneindexer;


import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



/**
 * This terminal application creates an Apache Lucene index in a folder and adds files into this index
 * based on the input of the user.
 */
public class Searcher {
    IndexSearcher indexsearcher;
    QueryParser queryparser;
    Query query;
    public Searcher(String indexDirectoryPath)
    {
        try {
            Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
            indexsearcher  = new IndexSearcher(indexDirectory);
            queryparser = new QueryParser(Version.LUCENE_36, LuceneConstants.CONTENTS, new StandardAnalyzer(Version.LUCENE_36));
            
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public TopDocs search(String searchquery) throws IOException
    {
        try {
            query = queryparser.parse(searchquery);
        } catch (ParseException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            return indexsearcher.search(query, LuceneConstants.MAX_SEARCH);
        } catch (IOException ex) {
            Logger.getLogger(Searcher.class.getName()).log(Level.SEVERE, null, ex);
        }
         return indexsearcher.search(query, LuceneConstants.MAX_SEARCH);
    }
    public Document getDocument(ScoreDoc scoredoc) throws IOException
    {
        return indexsearcher.doc(scoredoc.doc);
    }
    public void close() throws IOException
    {
        indexsearcher.close();
    }
}