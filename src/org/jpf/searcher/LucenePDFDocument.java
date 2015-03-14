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
 * �Զ��ƷѲ���ϵͳ
 * @Company: Asiainfo Technologies ��China��,Inc. Hangzhou
 * @author Asiainfo QA-HZ/������
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class LucenePDFDocument {

	/**
	 * ��PDF�ĵ���������,������Document
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

			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
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

		//		 pdf�ļ���
		String pdfFile = doc.getAbsolutePath();

		//		 �ļ��������������ı��ļ�
		Writer output = null;

		//		 �ڴ��д洢��PDF Document
		PDDocument document = null;

		StringBuffer content = new StringBuffer();

		try {

			try {

				// ���ȵ���һ��URL��װ���ļ�������õ��쳣�ٴӱ����ļ�ϵͳ//ȥװ���ļ�

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

				// �ر������

				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (document != null) {

				// �ر�PDF Document

				try {
					document.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

	}

}
