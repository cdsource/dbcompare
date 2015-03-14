/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 自动计费测试系统
 * 
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class LuceneExcelDocument {

	/**
	 * 对EXCEL文件建立索引,返回Document
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static Document getDocument(File f) {
		InputStream is = null;
		Document doc = new Document();
		StringBuffer content = new StringBuffer();
		try {
			is = new FileInputStream(f);
			HSSFWorkbook workbook = new HSSFWorkbook(is);// 创建对Excel工作簿文件的引用
			for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
				if (null != workbook.getSheetAt(numSheets)) {
					content.append(workbook.getSheetName(numSheets) + ";");
					HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
					for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
							.getLastRowNum(); rowNumOfSheet++) {
						if (null != aSheet.getRow(rowNumOfSheet)) {
							HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
							for (short cellNumOfRow = 0; cellNumOfRow <= aRow
									.getLastCellNum(); cellNumOfRow++) {
								if (null != aRow.getCell(cellNumOfRow)) {
									HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
									int a = aCell.getCellType();
									if (a == HSSFCell.CELL_TYPE_NUMERIC)
										content.append(aCell
												.getNumericCellValue()
												+ ";");
									else if (a == HSSFCell.CELL_TYPE_STRING)
										content.append(new String(aCell
												.getStringCellValue()
												.getBytes(), "GBK")
												+ ";");
									else if (a == HSSFCell.CELL_TYPE_BOOLEAN)
										content.append(aCell
												.getBooleanCellValue()
												+ ";");
								}
							}
						}
					}
				}
			}
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        	Date date = new Date(f.lastModified());
        	String time = sf.format(date);
			doc.add(new Field("modify_time", new Date(f.lastModified())+"", Field.Store.YES,Field.Index.NO));
        	doc.add(new Field("size", f.length()/1024+" k", Field.Store.YES,Field.Index.NO));
			doc.add(new Field("title", f.getName(), Field.Store.YES,
					Field.Index.ANALYZED));
			doc.add(new Field("contents", new String(content), Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("path", f.getCanonicalPath(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		} catch (Exception e) {
			e.printStackTrace();
			doc = null;
		} finally{
			try {
				content = null;
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}
}
