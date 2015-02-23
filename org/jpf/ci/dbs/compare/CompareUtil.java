/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo.com 
 * @version ����ʱ�䣺2015��1��16�� ����4:52:03 
 * ��˵�� 
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
		String[] fileName = { "D://table//�������ڣ����������ڵı�.txt",
				"D://table//���������ڣ��������ڵı�.txt", "D://table//�������ڣ����������ڵ��ֶ�.txt",
				"D://table//���������ڣ��������ڵ��ֶ�.txt",
				"D://table//����ֶζ���ͬ�����ֶ����Ͳ�ͬ������.txt",
				"D://table//����ֶΡ��ֶ����Ͷ���ͬ�����ֶγ��Ȳ�ͬ������.txt" };
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
	 * ����ʹ��
	 */
	public static void appendIndex(Table table, TableIndex cTableIndex, IndexColumn cIndexColumn,int flag, StringBuffer[] sb) throws Exception
	{
		switch (flag)
		{
		case 1:
			System.out.println("1���������ڣ����������ڵı�" + table.getTableName());// ����
			sb[0].append(table.getTableName() + "\n");
			break;
		case 2:
			System.out.println("2�����������ڣ��������ڵı�" + table.getTableName());// ��Ҫ�˹��жϽű�
			sb[1].append(table.getTableName() + "\n");
			break;
		case 3:
			System.out.println("3���������ڣ����������ڵ��ֶΣ�" + table.getTableName()
					+ " | " + cTableIndex.getIndexName());// ���˹��ж���δ���
			sb[2].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ "\n");
			break;
		case 4:
			System.out.println("4�����������ڣ��������ڵ�������" + table.getTableName()
					+ " | " + cTableIndex.getIndexName());// ��Ҫ�˹��жϽű�
			sb[3].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ "\n");
			break;
		case 5:
			System.out.println("5������ֶζ���ͬ�����ֶ����Ͳ�ͬ�����ݣ�" + table.getTableName()
					+ " | " + cTableIndex.getIndexName() + " | "
					+ cIndexColumn.getColumnName());// ��Ҫ�˹��жϽű�
			sb[4].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getSeqIndex()+ "\n");
			break;
		case 6:
			System.out.println("6������ֶΡ��ֶ����Ͷ���ͬ�����ֶγ��Ȳ�ͬ�����ݣ�"
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// ��Ҫ�˹��жϽű�
			sb[5].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName() + "\n");
			break;
		case 7:
			System.out.println("7�����������ͬ���ֶβ��������ݣ�"
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// ��Ҫ�˹��жϽű�
			sb[5].append(table.getTableName() + " | " + cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName() + "\n");
			break;
		case 8:
			System.out.println("8������������ֶζ���ͬ�������ֶ�λ�ò�ͬ�����ݣ�"
					+ table.getTableName() + " | " +cTableIndex.getIndexName()
					+ " | " + cIndexColumn.getColumnName());// ��Ҫ�˹��жϽű�
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
			System.out.println("1��PDM���ڣ����������ڵı�" + table.getTableName());// ����
			sb[0].append(table.getTableName() + "\n");
			break;
		case 2:
			System.out.println("2��PDM�����ڣ��������ڵı�" + table.getTableName());// ��Ҫ�˹��жϽű�
			sb[1].append(table.getTableName() + "\n");
			break;
		case 3:
			System.out.println("3��PDM���ڣ����������ڵ��ֶΣ�" + table.getTableName()
					+ " | " + column.getColumnName());// ���˹��ж���δ���
			sb[2].append(table.getTableName() + " | " + column.getColumnName()
					+ "\n");
			break;
		case 4:
			System.out.println("4��PDM�����ڣ��������ڵ��ֶΣ�" + table.getTableName()
					+ " | " + column.getColumnName());// ��Ҫ�˹��жϽű�
			sb[3].append(table.getTableName() + " | " + column.getColumnName()
					+ "\n");
			break;
		case 5:
			System.out.println("5������ֶζ���ͬ�����ֶ����Ͳ�ͬ�����ݣ�" + table.getTableName()
					+ " | " + column.getColumnName() + " | "
					+ column.getDataType());// ��Ҫ�˹��жϽű�
			sb[4].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getDataType() + "\n");
			break;
		case 6:
			System.out.println("6������ֶΡ��ֶ����Ͷ���ͬ���Ƿ�Ϊ�ղ�ͬ��"
					+ table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable());// ��Ҫ�˹��жϽű�
			sb[5].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable() + "\n");
			break;
		case 7:
			System.out.println("7��ע�Ͳ�ͬ��"
					+ table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable()+ " | " +column.getComment());// ��Ҫ�˹��жϽű�
			sb[6].append(table.getTableName() + " | " + column.getColumnName()
					+ " | " + column.getNullable() + " | " +column.getComment()+ "\n");
			break;	
		}
	}
}
