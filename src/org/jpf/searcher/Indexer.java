/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.ai.searcher.config.Configuration;

/**
 * 自动计费测试系统
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class Indexer {

	private static Log log = LogFactory.getLog(Indexer.class);   
    private static Configuration config = null;
    public static void main(String[] args) throws Exception {  
    	config = new Configuration("config/config");
    	
    	
    	/**************************
    	 * WINDOW下支持中文目录 
    	 ***************************/
//    	File indexDir = new File(new String(config.getValue("INDEX_PATH").getBytes("ISO-8859-1"),"GBK"));   
//    	File dataDir = new File(new String(config.getValue("DATA_PATH").getBytes("ISO-8859-1"),"GBK"));  
        
    	/**************************************
    	 * unix下,根据系统支持的字符编码,待定 
    	 ***************************************/
    	File indexDir = new File(config.getValue("INDEX_PATH"));   
    	File dataDir = new File(config.getValue("DATA_PATH")); 
        
        System.out.println("开始建立索引,请等待......;"); 
        long start = new Date().getTime();   
  
        int numIndexed = index(indexDir, dataDir);   
  
        long end = new Date().getTime();   
  
        System.out.println("use:" + (end - start)+" ms;"); 
        System.out.println("共对"+numIndexed+"个文件建立索引;"); 
    }   
  
    public static int index(File indexDir, File dataDir) {   
        int ret = 0;   
        try {   
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_31);
            IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_31, analyzer);
            iwc.setOpenMode(OpenMode.CREATE);
            Directory dir = FSDirectory.open(indexDir);
            IndexWriter writer = new IndexWriter(dir, iwc);
           // IndexWriter writer = new IndexWriter(indexDir, new CJKAnalyzer(), Boolean.parseBoolean(config.getValue("REBUILD_INDEX")));   
            writer.setUseCompoundFile(true);
            
            writer.setMergeFactor(500);
            writer.setRAMBufferSizeMB(50);
            indexDirectory(writer, dataDir);   
  
            //ret = writer.docCount();   
            writer.optimize();
            writer.close();   
  
        } catch (Exception e) {   
            e.printStackTrace();  
            log.info(e.toString());
        }   
        return ret;   
    }   
  
    public static void indexDirectory(IndexWriter writer, File dir) {   
        try {
            File[] files = dir.listFiles();   
  
            for (File f : files) {   
                if (f.isDirectory()&&!(f.getName().toUpperCase().equals("CVS"))) {   
                    indexDirectory(writer, f);
                } else {
		            if(f.length()>(1024*1024*20)){//只对文档长度为5兆以内的文档做索引
//		            	System.out.println(f.getPath());
		            	continue;   
		            }
		            indexFile(writer, f); 
	            	f = null;  
                }   
            }   
        } catch (Exception e) {   
            e.printStackTrace();  
            log.info(e.toString());
        }   
    }   
    public static long iFileCount=0;
    public static void indexFile(IndexWriter writer, File f) {   
        try {   
        	 System.out.println("handle file count:"+iFileCount++);
        	Document doc = null;
        	String filename = f.getName();
        	if(filename.indexOf(".")!=-1&&config.getValue("UN_POSTFIX").indexOf(filename.substring(filename.lastIndexOf("."),filename.length()))==-1){
	        	if(config.getValue("POSTFIX").indexOf(filename.substring(filename.lastIndexOf("."),filename.length()))!=-1){  
	                System.out.println(f.getPath());
		        	if(f.getName().toLowerCase().endsWith(".doc")){
		        		doc = LuceneWordDocument.getDocument(f);
		        	}else if(f.getName().toLowerCase().endsWith(".docx")){
		        		doc = LuceneDocxDocument.getDocument(f);
		        	}else if(f.getName().toLowerCase().endsWith(".xls")){
		        		doc = LuceneExcelDocument.getDocument(f);
		        	}else if(f.getName().toLowerCase().endsWith(".pdf")){
		        		doc = LucenePDFDocument.getDocument(f);
		        	}else if(f.getName().toLowerCase().endsWith(".ppt")){
		        		doc = LucenePPTDocument.getDocument(f);
		        	}else{//包括 pdm,xml等需要索引的文件
		        		doc = LuceneOtherDocument.getDocument(f);
		        	}
		        	if(doc!=null){
		        		writer.addDocument(doc);
		        	}
		        	doc = null;
	        	}
        	}
        } catch (Exception e) {   
            e.printStackTrace();
            log.info(e.toString());
        }   
    }
}
