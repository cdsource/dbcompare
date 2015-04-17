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
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.mails.JpfMail;
import org.jpf.utils.JpfDateTimeUtil;
import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class CompareUtil
{
	private static final Logger logger = LogManager.getLogger();
	// ����޸�SQL
	private static StringBuffer sbAlterSql = new StringBuffer();

	public static void CleanBuf()
	{
		sbAlterSql.setLength(0);
	}

	public static void SortSb(StringBuffer sb)
	{

	}

	public static void writeFile(String strTitle, StringBuffer sb) throws Exception
	{
		logger.debug("write Result File...");
		String fileName = strTitle + ".txt";
		OutputStream os = null;
		try
		{
			File file = new File(fileName);
			os = new FileOutputStream(file);

			os.write(sb.toString().getBytes());
			os.flush();
			os.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			if (os != null)
				os.close();
		}

	}

	public static void writeFile(StringBuffer[] sb) throws Exception
	{
		logger.debug("write Result File...");
		String[] fileName = { "�������ڣ����������ڵı�.txt",
				"���������ڣ��������ڵı�.txt",
				"�������ڣ����������ڵ��ֶ�.txt",
				"���������ڣ��������ڵ��ֶ�.txt",
				"����ֶζ���ͬ�����ֶ����Ͳ�ͬ������.txt",
				"����ֶΡ��ֶ����Ͷ���ͬ�����ֶγ��Ȳ�ͬ������.txt" };
		for (int i = 0; i < fileName.length; i++)
		{
			File file = new File(fileName[i]);
			OutputStream os = new FileOutputStream(file);

			os.write(sb[i].toString().getBytes());
			os.flush();
			os.close();
		}
	}

	public static void SendMail(StringBuffer[] sb, String strMailers, String strDbInfo, String strPdmInfo,
			String strHtmlName) throws Exception
	{
		logger.debug("write Result File...");

		String strMailText = JpfFileUtil.GetFileTxt(strHtmlName);
		strMailText = strMailText.replaceAll("#wupf1", JpfDateTimeUtil.GetCurrDate());
		strMailText = strMailText.replaceAll("#wupf2", strPdmInfo);
		strMailText = strMailText.replaceAll("#wupf3", strDbInfo);

		strMailText = strMailText.replaceAll(
				"#wupf4",
				sb[0].toString() + sb[1].toString() + sb[2].toString() + sb[3].toString() + sb[4].toString()
						+ sb[5].toString());
		JpfMail.SendMail(strMailers, strMailText, "GBK", "���ݿ�ȶԽ��(�Զ�����) �ȶԿ�" + strDbInfo);
	}

	/*
	 * ����ʹ��
	 */
	public static void appendIndex(Table table, TableIndex cTableIndexPdm, TableIndex cTableIndexDev, int flag,
			StringBuffer[] sb) throws Exception
	{
		iCount++;
		String strClassTypeString = "class=\"alt\"";
		if (iCount % 2 == 0)
		{
			strClassTypeString = "";
		}
		switch (flag)
		{
		case 1:
			System.out.println("1��PDM���ڣ����������ڵ�������" + table.getTableName());
			sb[0].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">7")
					.append(table.getTableName())
					.append("</th><td >").append(cTableIndexPdm.getIndexName()).append("</td><td ></td><td >")
					.append("</td><td >")
					.append("</td><td ></td></tr>");
			break;
		case 2:
			System.out.println("2��PDM�����ڣ��������ڵ�������" + table.getTableName());// ��Ҫ�˹��жϽű�
			sb[1].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">8")
					.append("</th><td ></td><td ></td><td >")
					.append(table.getTableName()).append("</td><td >")
					.append(cTableIndexDev.getIndexName()).append("</td><td ></td></tr>");
			break;
		case 3:
			System.out.println("3�� �������ݲ�ͬ��" + table.getTableName()
					+ " | " + cTableIndexPdm.getIndexName());// ���˹��ж���δ���
			sb[2].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">9")
					.append(table.getTableName())
					.append("</th><td >").append(cTableIndexPdm.getIndexName()).append("</td><td >")
					.append(cTableIndexPdm.getColNames())
					.append("</td><td >")
					.append("</td><td >")
					.append("</td><td >")
					.append(cTableIndexDev.getColNames())
					.append("</td></tr>");
			break;

		}

	}

	private static int iCount = 0;

	public static boolean IsSubTable(String key_table)
	{
		String regex = ".*_[0-9].*";
		return key_table.matches(regex);
	}

	public static String GetParentTableName(String strTableName)
	{
		String regex = "_[0-9]";
		Pattern pat = Pattern.compile(regex);
		String[] str = pat.split(strTableName);
		return str[0];
	}

	/*
	 * ���
	 */
	public static void append(Table table, Column pmdColumn, Column descColumn, int flag, StringBuffer[] sb)
			throws Exception
	{
		iCount++;
		String strClassTypeString = "class=\"alt\"";
		if (iCount % 2 == 0)
		{
			strClassTypeString = "";
		}
		String tmpStr = "";
		switch (flag)
		{
		case 1:
			System.out.println("1��PDM���ڣ��ȶԿⲻ���ڵı�" + table.getTableName());// ����
			// sb[0].append(table.getTableName() + "\n");
			sb[0].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">1")
					.append("</th><td>")
					.append(table.getTableName())
					.append("</td><td></td><td ></td><td ></td><td ></td><td ></td></tr>");
			sbAlterSql.append("create table ").append("\n");
			// System.out.println(sb[0]);// ����
			break;
		case 2:
			System.out.println("2��PDM�����ڣ��ȶԿ���ڵı�" + table.getTableName());// ��Ҫ�˹��жϽű�
			// sb[1].append(table.getTableName() + "\n");
			sb[1].append("<tr ").append(strClassTypeString).append("><th scope=\"row\"  class=\"specalt\">2")
					.append("</th><td>")
					.append("</td><td></td><td ></td><td ></td>").append(table.getTableName())
					.append("<td ></td><td ></td></tr>");

			sbAlterSql.append("drop table ").append(table.getTableName()).append("\n");
			// System.out.println(sb[1]);
			break;
		case 3:
			System.out.println("3��PDM���ڣ��ȶԿⲻ���ڵ��ֶΣ�" + table.getTableName()
					+ " | " + pmdColumn.getColumnName());// ���˹��ж���δ���
			sb[2].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">3").append("</th><td>")
					.append(table.getTableName())
					.append("</td><td >").append(pmdColumn.getColumnName()).append("</td><td >")
					.append(pmdColumn.getDataType()).append("</td><td ></td><td ></td><td ></td></tr>");
			// System.out.println(sb[2]);
			break;
		case 4:
			System.out.println("4��PDM�����ڣ��ȶԿ���ڵ��ֶΣ�" + table.getTableName()
					+ " | " + pmdColumn.getColumnName());// ��Ҫ�˹��жϽű�

			sb[3].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">4").append("</th><td>");
			tmpStr = GetParentTableName(table.getTableName());
			if (tmpStr.equalsIgnoreCase(table.getTableName()))
			{
				sb[3].append(table.getTableName()).append("</td><td ></td><td ></td><td >");
			} else
			{
				sb[3].append(tmpStr).append("</td><td ></td><td ></td><td >").append(table.getTableName());
			}

			sb[3].append("</td><td >")
					.append(pmdColumn.getColumnName()).append("</td><td >")
					.append(pmdColumn.getDataType()).append("</td></tr>");

			break;
		case 5:
			System.out.println("5������ֶζ���ͬ�����ֶ����Ͳ�ͬ�����ݣ�" + table.getTableName()
					+ " | " + pmdColumn.getColumnName() + " | "
					+ pmdColumn.getDataType() + "---" + descColumn.getDataType());
			sb[4].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">5").append("</th><td>");
			tmpStr = GetParentTableName(table.getTableName());
			if (tmpStr.equalsIgnoreCase(table.getTableName()))
			{
				sb[4].append(table.getTableName()).append("</td><td >").append(pmdColumn.getColumnName())
						.append("</td><td >")
						.append(pmdColumn.getDataType()).append("</td><td >");
			} else
			{
				sb[4].append(tmpStr).append("</td><td >").append(pmdColumn.getColumnName())
				.append("</td><td >").append(pmdColumn.getDataType()).append("</td><td >").append(table.getTableName());
			}

			sb[4].append("</td><td ></td><td>")
					.append(descColumn.getDataType()).append("</td></tr>");

			break;
		case 6:
			System.out.println("6������ֶΡ��ֶ����Ͷ���ͬ���Ƿ�Ϊ�ղ�ͬ��"
					+ table.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getNullable() + "---" + descColumn.getNullable());
			sb[5].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">6").append("</th><td>");
			
			tmpStr = GetParentTableName(table.getTableName());
			if (tmpStr.equalsIgnoreCase(table.getTableName()))
			{
				sb[5].append(table.getTableName()).append("</td><td >").append(pmdColumn.getColumnName())
						.append("</td><td >")
						.append(pmdColumn.getNullable()).append("</td><td >");
			} else
			{
				sb[5].append(tmpStr).append("</td><td >").append(pmdColumn.getColumnName())
				.append("</td><td >").append(pmdColumn.getNullable()).append("</td><td >").append(table.getTableName());
			}

			sb[5].append("</td><td ></td><td>")
					.append(descColumn.getNullable()).append("</td></tr>");
			

			break;

		}
	}
}
