/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * 自动计费测试系统
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class LuceneOtherDocument {

	public static Document getDocument(File f) {
		Reader txtReader = null;
		Document doc = null;
		try {
			doc = new Document();
			String l = f.length() + "";
			char[] c = new char[Integer.parseInt(l)];
			txtReader = new FileReader(f);
			txtReader.read(c);

			/*byte[] b = new byte[Integer.parseInt(l)];
			 FileInputStream fis = new FileInputStream(f);
			 fis.read(b);*/
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        	Date date = new Date(f.lastModified());
        	String time = sf.format(date);
//        	String content = new String(new String(c).getBytes("ISO-8859-1"),"GBK");
        	String content = new String(c);
        	doc.add(new Field("modify_time", time, Field.Store.YES,Field.Index.NO));
			doc.add(new Field("modify_time", new Date(f.lastModified())+"", Field.Store.YES,Field.Index.NO));
        	doc.add(new Field("size", f.length()/1024+" k", Field.Store.YES,Field.Index.NO));
			doc.add(new Field("title", f.getName(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("contents", content, Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("path", f.getCanonicalPath(), Field.Store.YES,
					Field.Index.NO));
			content = null;
		} catch (Exception e) {
			e.printStackTrace();
			doc = null;
		} finally {
			try {
				txtReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}

}
