/**
 * 
 */
package org.jpf.searcher;

/**
 * 自动计费测试系统
 * 
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hslf.model.*;
import org.apache.poi.hslf.usermodel.*;

public class LucenePPTDocument {
	public static StringBuffer getText(String filepath) {
		StringBuffer content = new StringBuffer();
		FileInputStream is = null;
		try {
			File file = new File(filepath);
			is = new FileInputStream(file);
			SlideShow ss = new SlideShow(is);
			Slide[] slides = ss.getSlides();
			// 获得每一张幻灯片
			for (int i = 0; i < slides.length; i++) {
//				System.out.println(slides[i].getTitle());
				TextRun[] t = slides[i].getTextRuns();
				for (int j = 0; j < t.length; j++) {
					content.append(t[j].getText().toString());
				}
			}
			
			
			/*PictureData[] pdata = ppt.getPictureData();
			for (int k = 0; k < pdata.length; k++) {
				PictureData pict = pdata[k];

				// picture data
				byte[] data = pict.getData();

				int type = pict.getType();
				String ext = null;
				switch (type) {
				case Picture.JPEG:
					ext = ".jpg";
					break;
				case Picture.PNG:
					ext = ".png";
					break;
				case Picture.WMF:
					ext = ".wmf";
					break;
				case Picture.EMF:
					ext = ".emf";
					break;
				case Picture.PICT:
					ext = ".pict";
					break;
				default:
					continue;
				}
				FileOutputStream outpic = new FileOutputStream("c:/pic/pict_"
						+ (k + 1) + ext);
				outpic.write(data);
				outpic.close();

			}*/
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
			content = null;
		} finally{
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		System.out.print(content.toString().trim());
		return content;
	}
	
	public static Document getDocument(File f){
		
		Document doc = new Document();
		try
        {
//			String content = new String(getText(f.getPath()).toString().getBytes(),"GBK");
			String content = new String(getText(f.getPath()));
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        	Date date = new Date(f.lastModified());
        	String time = sf.format(date);
        	doc.add(new Field("modify_time", time, Field.Store.YES,Field.Index.NO));
        	doc.add(new Field("size", f.length()/1024+" k", Field.Store.YES,Field.Index.NO));
			doc.add(new Field("title", f.getName(), Field.Store.YES,Field.Index.ANALYZED));
			doc.add(new Field("contents", content,Field.Store.NO,Field.Index.ANALYZED));
//			doc.add(new Field("contents", getText(f.getPath()).toString(),Field.Store.NO,Field.Index.TOKENIZED));
			doc.add(new Field("path", f.getAbsolutePath(), Field.Store.YES, Field.Index.NO));
			content = null;
        }
        catch(Exception e){
            e.printStackTrace();
            doc = null;
        } 
        return doc;
	}
}
