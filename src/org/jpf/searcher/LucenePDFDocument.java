/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * 自动计费测试系统
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class LucenePDFDocument {

	/**
	 * 对PDF文档建立索引,并返回Document
	 * @param doc
	 * @return
	 */
	public static Document getDocument(File doc) {
		Document document = new Document();
		try{
//			String contents = new String(new String(geText(doc)).getBytes(), "GBK");
			String contents = new String(geText(doc));
			if(contents==null)
				return null;

			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        	Date date = new Date(doc.lastModified());
        	String time = sf.format(date);
        	document.add(new Field("modify_time", time, Field.Store.YES,Field.Index.NO));
        	document.add(new Field("size", doc.length()/1024+" k", Field.Store.YES,Field.Index.NO));
			document.add(new Field("title", doc.getName(), Field.Store.YES,
					Field.Index.ANALYZED));
			document.add(new Field("contents", contents, Field.Store.NO,
					Field.Index.ANALYZED));
			document.add(new Field("path", doc.getAbsolutePath(), Field.Store.YES,
					Field.Index.NO));
			contents = null;
		}catch (Exception e) {
			e.printStackTrace();
			document = null;
		}
		return document;
	}

	private static StringBuffer geText(File doc) {

		//		 pdf文件名
		String pdfFile = doc.getAbsolutePath();

		//		 文件输入流，生成文本文件
		Writer output = null;

		//		 内存中存储的PDF Document
		PDDocument document = null;

		StringBuffer content = new StringBuffer();

		try {

			try {

				// 首先当作一个URL来装载文件，如果得到异常再从本地文件系统//去装载文件

				URL url = new URL(pdfFile);

				document = PDDocument.load(url);

				PDFTextStripper stripper = new PDFTextStripper();

				content.append(stripper.getText(document));

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			return content;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {

			if (output != null) {

				// 关闭输出流

				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (document != null) {

				// 关闭PDF Document

				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

}
