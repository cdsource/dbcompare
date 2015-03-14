/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.lang.management.ManagementFactory;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hwpf.extractor.WordExtractor;

//import com.sun.management.OperatingSystemMXBean;

/**
 * 自动计费测试系统
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class LuceneWordDocument { 

    /**
     * 对DOC文件建立索引，返回Document
     * @param doc
     * @return
     */
    public static Document getDocument(File doc){
        String docPath = doc.getAbsolutePath();
        String title = doc.getName();
        InputStream inputStream = null;
        Document document = new Document();
        try
        {
            inputStream = new FileInputStream(doc);
//    		OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();  
//    		System.out.println("已用内存总计：" + osmb.getCommittedVirtualMemorySize() / 1024/1024 + "MB");
            org.apache.poi.hwpf.HWPFDocument hdoc = new org.apache.poi.hwpf.HWPFDocument(inputStream);
            
        	WordExtractor extractor = new WordExtractor(hdoc);
//        	String contents = new String(extractor.getText().getBytes(),"GBK");
        	String contents = extractor.getText();
        	java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        	Date date = new Date(doc.lastModified());
        	String time = sf.format(date);
        	document.add(new Field("modify_time", time, Field.Store.YES,Field.Index.NO));
        	document.add(new Field("size", doc.length()/1024+" k", Field.Store.YES,Field.Index.NO));
            document.add(new Field("title", title, Field.Store.YES,Field.Index.ANALYZED));
            document.add(new Field("contents", contents,Field.Store.NO,Field.Index.ANALYZED));
            document.add(new Field("path", docPath, Field.Store.YES, Field.Index.NO));
            time = null;
            title = null;
            contents = null;
            docPath = null;
            hdoc = null;
            extractor = null;
        }
        catch(Exception e){
            e.printStackTrace();
            document = null;
        } finally{
        	try {
				inputStream.close();
				inputStream = null;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return document;
    }

}
