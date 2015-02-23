/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年1月16日 下午4:52:03 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class CompareUtil
{
	private static final Logger logger = LogManager.getLogger();
	public static void SortSb(StringBuffer sb)
	{

	}
	public static void writeFile(String strTitle,StringBuffer sb) throws Exception
	{
		logger.info("write Result File...");
		String fileName =strTitle+".txt";
		OutputStream os=null;
		try
		{
			File file = new File(fileName);
			os = new FileOutputStream(file);
			
			os.write(sb.toString().getBytes());
			os.flush();
			os.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}finally
		{
			if(os!=null)
				os.close();
		}
	
	}
	public static void writeFile(StringBuffer[] sb) throws Exception
	{
		logger.info("write Result File...");
		String[] fileName = { "D://table//生产存在，开发不存在的表.txt",
				"D://table//生产不存在，开发存在的表.txt", "D://table//生产存在，开发不存在的字段.txt",
				"D://table//生产不存在，开发存在的字段.txt",
				"D://table//表和字段都相同，但字段类型不同的内容.txt",
				"D://table//表和字段、字段类型都相同，但字段长度不同的内容.txt" };
		for (int i = 0; i < fileName.length; i++)
		{
			File file = new File(fileName[i]);
			OutputStream os = new FileOutputStream(file);

			os.write(sb[i].toString().getBytes());
			os.flush();
			os.close();
		}
	}
	/*
	 * 索引使用
	 */
	public static void appendIndex(Table table, TableIndex cTableIndex, IndexColumn cIndexColumn,int flag, StringBuffer[] sb) throws Exception
	{
		switch (flag)
		{
		case 1:
			System.out.println("1、生产存在，开发不存在的表：" + table.getTableName());// 跳过
			sb[0].append(table.getTableName() + "\n");
			break;
		case 2:
			System.out.println("2、生产不存在，开发存在的表：" + table.getTableName());// 需要人工判断脚本
			sb[1].append(table.getTableName() + "\n");
			break;
		case 3:
			System.out.println("3、生产存在，开发不存在的字段：" + table.getTableName()
					+ " | " + cTableIndex.getIndexName());// 需人工判断如何处理
			sb[2].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ "\n");
			break;
		case 4:
			System.out.println("4、生产不存在，开发存在的索引：" + table.getTableName()
					+ " | " + cTableIndex.getIndexName());// 需要人工判断脚本
			sb[3].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ "\n");
			break;
		case 5:
			System.out.println("5、表和字段都相同，但字段类型不同的内容：" + table.getTableName()
					+ " | " + cTableIndex.getIndexName() + " | "
					+ cIndexColumn.getColumnName());// 需要人工判断脚本
			sb[4].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getSeqIndex()+ "\n");
			break;
		case 6:
			System.out.println("6、表和字段、字段类型都相同，但字段长度不同的内容："
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// 需要人工判断脚本
			sb[5].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName() + "\n");
			break;
		case 7:
			System.out.println("7、表和索引相同，字段不存在内容："
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// 需要人工判断脚本
			sb[5].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName() + "\n");
			break;
		case 8:
			System.out.println("8、表和索引、字段都相同，索引字段位置不同的内容："
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// 需要人工判断脚本
			sb[5].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName() + "\n");
			break;

		}
		
	}
	public static void append(Table table, Column column, int flag, StringBuffer[] sb) throws Exception
	{
		switch (flag)
		{
		case 1:
			System.out.println("1、PDM存在，开发不存在的表：" + table.getTableName());// 跳过
			sb[0].append(table.getTableName() + "\n");
			break;
		case 2:
			System.out.println("2、PDM不存在，开发存在的表：" + table.getTableName());// 需要人工判断脚本
			sb[1].append(table.getTableName() + "\n");
			break;
		case 3:
			System.out.println("3、PDM存在，开发不存在的字段：" + table.getTableName()
					+ " | " + column.getColumnName());// 需人工判断如何处理
			sb[2].append(table.getTableName() + " | " + column.getColumnName()
					+ "\n");
			break;
		case 4:
			System.out.println("4、PDM不存在，开发存在的字段：" + table.getTableName()
					+ " | " + column.getColumnName());// 需要人工判断脚本
			sb[3].append(table.getTableName() + " | " + column.getColumnName()
					+ "\n");
			break;
		case 5:
			System.out.println("5、表和字段都相同，但字段类型不同的内容：" + table.getTableName()
					+ " | " + column.getColumnName() + " | "
					+ column.getDataType());// 需要人工判断脚本
			sb[4].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getDataType() + "\n");
			break;
		case 6:
			System.out.println("6、表和字段、字段类型都相同，是否为空不同："
					+ table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable());// 需要人工判断脚本
			sb[5].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable() + "\n");
			break;
		case 7:
			System.out.println("7、注释不同："
					+ table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable()+ " | " +column.getComment());// 需要人工判断脚本
			sb[6].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable() + " | " +column.getComment()+ "\n");
			break;	
		}
	}
}
