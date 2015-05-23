/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月14日 上午1:26:12 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
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

	private static String DbDomain = "";

	public static void CleanBuf(String strDomain)
	{
		DbDomain = strDomain;
		// sbAlterSql.setLength(0);
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
		String[] fileName = { "生产存在，开发不存在的表.txt",
				"生产不存在，开发存在的表.txt",
				"生产存在，开发不存在的字段.txt",
				"生产不存在，开发存在的字段.txt",
				"表和字段都相同，但字段类型不同的内容.txt",
				"表和字段、字段类型都相同，但字段长度不同的内容.txt" };
		for (int i = 0; i < fileName.length; i++)
		{
			File file = new File(fileName[i]);
			OutputStream os = new FileOutputStream(file);

			os.write(sb[i].toString().getBytes());
			os.flush();
			os.close();
		}
	}

	public static void SendMail(StringBuffer[] sb, String strMailTitle, CompareInfo cCompareInfo, String strHtmlName,
			String strTotalInfo) throws Exception
	{
		logger.debug("write Result File...");

		String strMailText = JpfFileUtil.GetFileTxt(strHtmlName);
		strMailText = strMailText.replaceAll("#wupf1", JpfDateTimeUtil.GetCurrDateTime());
		strMailText = strMailText.replaceAll("#wupf2", cCompareInfo.getStrPdmInfo());
		strMailText = strMailText.replaceAll("#wupf3", cCompareInfo.getStrJdbcUrl());
		String tmpStr = sb[0].toString() + sb[1].toString() + sb[2].toString() + sb[3].toString() + sb[4].toString()
				+ sb[5].toString() + sb[6].toString() + sb[7].toString() ;
		
		tmpStr = java.util.regex.Matcher.quoteReplacement(tmpStr);
		strMailText = strMailText.replaceAll("#wupf4", tmpStr);

		if (strTotalInfo != null)
		{
			strMailText = strMailText.replaceAll("#diffs", strTotalInfo);
		}
		// strMailText+=sbAlterSql.toString();
		JpfMail.SendMail(cCompareInfo.getStrMails(), strMailText, "GBK", cCompareInfo.getStrCondName() + strMailTitle
				+ " 比对库" + cCompareInfo.getStrJdbcUrl());
	}

	/*
	 * ����ʹ��
	 */
	public static void appendIndex(Table table, TableIndex cTableIndexPdm, TableIndex cTableIndexDev, int flag,
			StringBuffer[] sb, Vector<ExecSqlInfo> vSql) throws Exception
	{
		iCount++;
		String strClassTypeString = "class=\"alt\"";
		if (iCount % 2 == 0)
		{
			strClassTypeString = "";
		}
		String strSql = "";
		switch (flag)
		{
		case 10:
			System.out.println("11、PDM存在，开发不存在的索引：" + table.getTableName() + "." + cTableIndexPdm.getIndexName());
			sb[0].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">11")
					.append("</th><td>").append(table.getTableName())
					.append("</td><td >").append(cTableIndexPdm.getIndexName())
					.append("</td><td >").append(cTableIndexPdm.getColNames())
					.append("</td><td >").append(cTableIndexPdm.getConstraint_type())
					.append("</td><td ></td><td ></td><td ></td><td ></td><td >");
			strSql = "alter table " + DbDomain + "." + table.getTableName() + " add ";
			if (cTableIndexPdm.getConstraint_type() != null
					&& cTableIndexPdm.getConstraint_type().equals("PRIMARY KEY"))
			{
				strSql += " PRIMARY key("
						+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";
			} else
			{
				if (cTableIndexPdm.getNON_UNIQUE() == 0)
				{
					strSql += " unique ";
				}
				strSql += " index " + cTableIndexPdm.getIndexName() + "("
						+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";

			}
			AddSqlVector(vSql,strSql,10,table.getTableName());
			sb[0].append(strSql).append("</td></tr>");
			break;
		case 11:
			System.out.println("12、PDM不存在，开发存在的索引：" + table.getTableName());// 需要人工判断脚本
			sb[1].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">12").append("</th><td>")
					.append(GetParentTableName(table.getTableName()))
					.append("</td><td ></td><td ></td><td ></td><td >")
					.append(table.getTableName()).append("</td><td >")
					.append(cTableIndexDev.getIndexName()).append("</td><td >")
					.append(cTableIndexDev.getColNames())
					.append("</td><td>")
					.append(cTableIndexDev.getConstraint_type())
					.append("</td><td >");
			strSql = "alter table " + DbDomain + "." + table.getTableName() + " drop ";
			if (null != cTableIndexDev.getConstraint_type())
			{
				if (cTableIndexDev.getConstraint_type().equals("PRIMARY KEY"))
				{
					strSql += "  PRIMARY key ";
				} else if (cTableIndexDev.getConstraint_type().equals("FOREIGN KEY"))
				{
					strSql += "  foreign key " + cTableIndexDev.getIndexName();
				} else
				{
					strSql += "  index " + cTableIndexDev.getIndexName();
				}
			} else
			{
				strSql += "  index " + cTableIndexDev.getIndexName();
			}
			strSql +=";";
			AddSqlVector(vSql,strSql,11,table.getTableName());
			sb[1].append(strSql).append("</td></tr>");

			break;
		case 12:
			System.out.println("13、 索引内容不同：" + table.getTableName()
					+ " | " + cTableIndexPdm.getIndexName());// 需人工判断如何处理
			sb[2].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">13")
					.append("</th><td>").append(GetParentTableName(table.getTableName()))
					.append("</td><td >").append(cTableIndexPdm.getIndexName())
					.append("</td><td >").append(cTableIndexPdm.getColNames())
					.append("</td><td >").append(cTableIndexPdm.getNON_UNIQUE())
					.append("</td><td>").append(table.getTableName())
					.append("</td><td >").append(cTableIndexDev.getIndexName())
					.append("</td><td >").append(cTableIndexDev.getColNames())
					.append("</td><td>")
					.append("</td><td>");

			MakeAlterSql(sb[2], table, cTableIndexPdm, cTableIndexDev, vSql,12);
			sb[2].append("</td></tr>");
			break;
		case 13:
			System.out.println("14、 索引内容不同：" + table.getTableName()
					+ " | " + cTableIndexPdm.getIndexName());// 需人工判断如何处理
			sb[3].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">14")
					.append("</th><td>").append(GetParentTableName(table.getTableName()))
					.append("</td><td >").append(cTableIndexPdm.getIndexName())
					.append("</td><td >").append(cTableIndexPdm.getColNames())
					.append("</td><td >").append(cTableIndexPdm.getNON_UNIQUE())
					.append("</td><td>").append(table.getTableName())
					.append("</td><td >").append(cTableIndexDev.getIndexName())
					.append("</td><td >").append(cTableIndexDev.getColNames())
					.append("</td><td>").append(cTableIndexDev.getNON_UNIQUE())
					.append("</td><td>");

			MakeAlterSql(sb[3], table, cTableIndexPdm, cTableIndexDev, vSql,13);
			sb[3].append("</td></tr>");
			break;
		case 14:
			System.out.println("15、 索引内容不同：" + table.getTableName()
					+ " | " + cTableIndexPdm.getIndexName());// 需人工判断如何处理
			sb[4].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">15")
					.append("</th><td>").append(GetParentTableName(table.getTableName()))
					.append("</td><td >").append(cTableIndexPdm.getIndexName())
					.append("</td><td >").append(cTableIndexPdm.getColNames())
					.append("</td><td >").append(cTableIndexPdm.getConstraint_type())
					.append("</td><td>").append(table.getTableName())
					.append("</td><td >").append(cTableIndexDev.getIndexName())
					.append("</td><td >").append(cTableIndexDev.getColNames())
					.append("</td><td>").append(cTableIndexDev.getConstraint_type())
					.append("</td><td>");

			MakeAlterSql(sb[4], table, cTableIndexPdm, cTableIndexDev, vSql,14);
			sb[4].append("</td></tr>");
			break;
		case 15:
			System.out.println("11、PDM存在，开发不存在的索引：" + table.getTableName());
			sb[0].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">11")
					.append("</th><td>").append(GetParentTableName(table.getTableName()))
					.append("</td><td >").append(cTableIndexPdm.getIndexName())
					.append("</td><td >").append(cTableIndexPdm.getColNames())
					.append("</td><td >").append(cTableIndexPdm.getConstraint_type())
					.append("</td><td >").append(table.getTableName())
					.append("</td><td ></td><td ></td><td ></td><td >");
			strSql = "alter table " + DbDomain + "." + table.getTableName() + " add ";
			if (cTableIndexPdm.getConstraint_type() != null
					&& cTableIndexPdm.getConstraint_type().equals("PRIMARY KEY"))
			{
				strSql += "PRIMARY key("
						+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";
			}
			else
			{
				if (cTableIndexPdm.getNON_UNIQUE() == 0)
				{
					strSql += "unique ";
				}
				strSql += "index " + cTableIndexPdm.getIndexName() + "("
						+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";

			}
			AddSqlVector(vSql,strSql,10,table.getTableName());
			sb[0].append(strSql).append("</td></tr>");
			break;
		}

	}

	private static void MakeAlterSql(StringBuffer sb, Table table, TableIndex cTableIndexPdm,
			TableIndex cTableIndexDev, Vector<ExecSqlInfo> vSql,int iSqlType)
	{
		String strSql = "alter table " + DbDomain + "." + table.getTableName();
		if (cTableIndexDev.getConstraint_type() != null)
		{
			if (cTableIndexDev.getConstraint_type().equals("PRIMARY KEY"))
			{
				strSql += " drop PRIMARY key;";
			} else if (cTableIndexDev.getConstraint_type().equals("FOREIGN KEY"))
			{
				strSql += " drop foreign key " + cTableIndexDev.getIndexName() + ";";

			} else
			{
				strSql += "drop  index " + cTableIndexDev.getIndexName() + ";";
			}
		} else
		{
			strSql += " drop index " + cTableIndexDev.getIndexName() + ";";
		}
		AddSqlVector(vSql,strSql,iSqlType,table.getTableName());
		sb.append(strSql);
		strSql += "alter table " + DbDomain + "." + table.getTableName() + " add ";
		if (cTableIndexPdm.getConstraint_type() != null
				&& cTableIndexPdm.getConstraint_type().equalsIgnoreCase("PRIMARY KEY"))
		{
			strSql += "PRIMARY key("
					+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";
		}
		else
		{
			if (cTableIndexPdm.getNON_UNIQUE() == 0)
			{
				strSql += "unique ";
			}
			strSql += "index " + cTableIndexPdm.getIndexName() + "("
					+ cTableIndexPdm.getColNames().substring(0, cTableIndexPdm.getColNames().length() - 1) + ");";
		}
		AddSqlVector(vSql,strSql,iSqlType,table.getTableName());
		sb.append(strSql);
	}

	private static int iCount = 0;

	public static boolean IsSubTable(String key_table)
	{
		String regex = ".*_[0-9].*";
		return key_table.matches(regex);
	}

	// 判断是否是分表:先判断是否有特定规则，后按照 分表=母表+_[0-9].*,进行匹配判断
	public static boolean IsSubTable(String key_table, String strTableName)
	{
		HashMap<String, String> pc = JpfDbCompare.getParentChild();
		String ParentTabelName = GetParentTableNameConven(strTableName);
		if (pc.containsKey(ParentTabelName))
		{
			if (pc.get(ParentTabelName).equals(key_table))
			{
				return true;
			}
			else
				return false;
		}
		String regex = key_table + "_[0-9].*";
		return strTableName.matches(regex);
	}

	public static String GetParentTableNameConven(String strTableName)
	{
		String regex = "_[0-9]";
		Pattern pat = Pattern.compile(regex);
		String[] str = pat.split(strTableName);
		return str[0];
	}

	// 获得母表
	public static String GetParentTableName(String strTableName)
	{

		HashMap<String, String> pc = JpfDbCompare.getParentChild();
		String ParentTabelName = GetParentTableNameConven(strTableName);
		if (pc.containsKey(ParentTabelName))
		{
			return pc.get(ParentTabelName);
		}
		else
		{
			return ParentTabelName;
		}
	}

	private static String AddDefault(Column pmdColumn)
	{
		String tmpStr = "";
		if (pmdColumn.getNullable().equalsIgnoreCase("no"))
		{
			tmpStr = " NOT NULL";
		}
		if (null != pmdColumn.getColumnDefault() && pmdColumn.getColumnDefault().length() > 0)
		{
			tmpStr += " DEFAULT " + pmdColumn.getColumnDefault();
		}
		if (null != pmdColumn.getExtra() && pmdColumn.getExtra().length() > 0)
		{
			tmpStr += " " + pmdColumn.getExtra();
		}
		return tmpStr;
	}

	private static String GetPreColName(Table table_pdm, Column pmdColumn)
	{
		int iPos = pmdColumn.getOrdinal_position();
		if (1 == iPos)
		{
			return " first";
		} else
		{
			iPos = iPos - 1;
		}
		for (Iterator  iter_column = table_pdm.columns.keySet().iterator(); iter_column.hasNext();)
		{
			String key_column = (String) iter_column.next();
			Column column_pdm = (Column) table_pdm.columns.get(key_column);
			if (iPos == column_pdm.getOrdinal_position())
			{
				return " after " + column_pdm.getColumnName();
			}
		}
		return "";
	}
	private static void AddSqlVector(Vector<ExecSqlInfo> vSql,String strSql,int iType,String strTableName)
	{
		ExecSqlInfo cExecSqlInfo=new ExecSqlInfo();
		cExecSqlInfo.setiType(iType);
		cExecSqlInfo.setStrSql(strSql);
		vSql.add(cExecSqlInfo);
	}
	/*
	 * ���
	 */
	public static void append(Table tablePdm,Table tableDev, Column pmdColumn, Column developColumn, int flag, StringBuffer[] sb, Vector<ExecSqlInfo> vSql)
			throws Exception
	{
		iCount++;
		String strClassTypeString = "class=\"alt\"";
		if (iCount % 2 == 0)
		{
			strClassTypeString = "";
		}
		String strSql = "";
		switch (flag)
		{
		case 1:
			System.out.println("1、PDM存在，比对库不存在的表：" + tablePdm.getTableName());// 跳过
			// sb[0].append(table.getTableName() + "\n");
			sb[0].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">1")
					.append("</th><td>")
					.append(tablePdm.getTableName())
					.append("</td><td></td><td ></td><td ></td><td ></td><td ></td><td ></td><td ></td><td></td></tr>");
			break;
		case 2:
			System.out.println("2、PDM不存在，比对库存在的表：" + tableDev.getTableName());// 需要人工判断脚本
			// sb[1].append(table.getTableName() + "\n");
			sb[1].append("<tr ").append(strClassTypeString)
					.append("><th scope=\"row\"  abbr=\"L2 Cache\" class=\"specalt\">2")
					.append("</th><td>(").append(GetParentTableName(tableDev.getTableName()))
					.append(")</td><td></td><td ></td><td ></td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td ></td><td >");
			if (tableDev.getTable_type().equalsIgnoreCase("view"))
			{
				sb[1].append("view");
				strSql = "drop view ";
			} else
			{
				strSql = "drop table ";
			}

			strSql += " if exists " + DbDomain + "." + tableDev.getTableName() + ";";
			AddSqlVector(vSql,strSql,2,tableDev.getTableName());
			sb[1].append("</td><td>").append(strSql).append("</td></tr>");

			// System.out.println(sb[1]);
			break;
		case 3:
			System.out.println("3、PDM存在，比对库不存在的字段：" + tablePdm.getTableName()
					+ " | " + pmdColumn.getColumnName());// 需人工判断如何处理
			sb[2].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">3").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName()).append("</td><td >")
					.append(pmdColumn.getDataType()).append("</td><td ></td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td ></td><td ></td><td >");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " ADD COLUMN "
					+ pmdColumn.getColumnName() + " "
					+ pmdColumn.getDataType() + AddDefault(pmdColumn) + GetPreColName(tablePdm, pmdColumn) + ";";
			AddSqlVector(vSql,strSql,3,tableDev.getTableName());
			sb[2].append(strSql).append("</td></tr>");
			break;
		case 4:
			System.out.println("4、PDM不存在，比对库存在的字段：" + tableDev.getTableName()
					+ " | " + pmdColumn.getColumnName());// 需要人工判断脚本

			sb[3].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">4").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td ></td><td ></td><td ></td><td >")
					.append(tableDev.getTableName()).append("</td><td >")
					.append(pmdColumn.getColumnName()).append("</td><td >")
					.append(pmdColumn.getDataType()).append("</td><td ></td><td >");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " DROP COLUMN "
					+ pmdColumn.getColumnName() + ";";
			AddSqlVector(vSql,strSql,4,tableDev.getTableName());
			sb[3].append(strSql).append("</td></tr>");
			break;
		case 5:
			System.out.println("5、表和字段都相同，但字段类型不同的内容：" + tableDev.getTableName()
					+ " | " + pmdColumn.getColumnName() + " | "
					+ pmdColumn.getDataType() + "---" + developColumn.getDataType());
			sb[4].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">5").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >").append(pmdColumn.getDataType()).append("</td><td >")
					.append(ShowDefaultValue(pmdColumn.getColumnDefault())).append("</td><td>")
					.append(tableDev.getTableName()).append("</td><td ></td><td>")
					.append(developColumn.getDataType()).append("</td><td>")
					.append(ShowDefaultValue(developColumn.getColumnDefault())).append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " MODIFY " + pmdColumn.getColumnName()
					+ " " + pmdColumn.getDataType()
					+ AddDefault(pmdColumn) + ";";
			AddSqlVector(vSql,strSql,5,tableDev.getTableName());
			sb[4].append(strSql).append("</td></tr>");
			break;
		case 6:
			System.out.println("6、表和字段、字段类型都相同，是否为空不同："
					+ tableDev.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getNullable() + "---" + developColumn.getNullable());
			sb[5].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">6").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >")
					.append(pmdColumn.getNullable()).append("</td><td ></td>")
					.append(ShowDefaultValue(pmdColumn.getColumnDefault())).append("<td>").append(tableDev.getTableName())
					.append("</td><td ></td><td>")
					.append(developColumn.getNullable()).append("</td><td>")
					.append(ShowDefaultValue(developColumn.getColumnDefault())).append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " MODIFY " + pmdColumn.getColumnName()
					+ " "
					+ pmdColumn.getDataType() + AddDefault(pmdColumn) + ";";
			AddSqlVector(vSql,strSql,6,tableDev.getTableName());
			sb[5].append(strSql).append(";</td></tr>");
			break;
		case 7:
			System.out.println("7、表和字段、字段类型都相同，默认值不同："
					+ tableDev.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getNullable() + "---" + developColumn.getNullable());
			sb[6].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">7").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >")
					.append(pmdColumn.getNullable()).append("</td><td >")
					.append(pmdColumn.getColumnDefault())
					.append("</td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td>")
					.append(developColumn.getNullable()).append("</td><td >")
					.append(developColumn.getColumnDefault()).append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " MODIFY " + pmdColumn.getColumnName()
					+ " "
					+ pmdColumn.getDataType() + AddDefault(pmdColumn) + ";";
			AddSqlVector(vSql,strSql,7,tableDev.getTableName());
			sb[6].append(strSql).append(";</td></tr>");
			break;
		case 8:
			System.out.println("8、表和字段、字段类型都相同，默认值不同："
					+ tableDev.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getNullable() + "---" + developColumn.getNullable());
			sb[7].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">8").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >")
					.append(pmdColumn.getNullable()).append("</td><td >")
					.append(pmdColumn.getExtra())
					.append("</td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td>")
					.append(developColumn.getNullable()).append("</td><td >")
					.append(developColumn.getExtra()).append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " MODIFY " + pmdColumn.getColumnName()
					+ " "
					+ pmdColumn.getDataType() + AddDefault(pmdColumn) + ";";
			AddSqlVector(vSql,strSql,8,tableDev.getTableName());
			sb[7].append(strSql).append(";</td></tr>");
			break;
		case 9:
			System.out.println("9、表和字段、字段类型都相同，字段顺序不同："
					+ tableDev.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getOrdinal_position() + "---" + developColumn.getOrdinal_position());
			sb[8].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">9").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >")
					.append(pmdColumn.getOrdinal_position()).append("</td><td >")
					.append(pmdColumn.getExtra())
					.append("</td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td>")
					.append(developColumn.getOrdinal_position()).append("</td><td >")
					.append(developColumn.getExtra()).append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " change " + pmdColumn.getColumnName()
					+ " "
					+ pmdColumn.getColumnName() + " " + pmdColumn.getDataType() + AddDefault(pmdColumn)
					+ GetPreColName(tablePdm, pmdColumn) + ";";
			AddSqlVector(vSql,strSql,9,tableDev.getTableName());
			sb[8].append(strSql).append("</td></tr>");
			break;
		case 10:
			System.out.println("10、表和字段、字段类型都相同，字符集不同："
					+ tableDev.getTableName() + " | " + pmdColumn.getColumnName()
					+ " | " + pmdColumn.getCHARACTER_SET_NAME() + "---" + developColumn.getCHARACTER_SET_NAME());
			sb[9].append("<tr").append(strClassTypeString)
					.append("><th scope=\"row\" abbr=\"L2 Cache\" class=\"specalt\">10").append("</th><td>")
					.append(GetParentTableName(tableDev.getTableName()))
					.append("</td><td >").append(pmdColumn.getColumnName())
					.append("</td><td >")
					.append(pmdColumn.getCHARACTER_SET_NAME()).append("</td><td >")
					.append(pmdColumn.getDataType())
					.append("</td><td >").append(tableDev.getTableName())
					.append("</td><td ></td><td>")
					.append(developColumn.getCHARACTER_SET_NAME()).append("</td><td >")
					.append(pmdColumn.getDataType())	.append("</td><td>");
			strSql = "ALTER TABLE " + DbDomain + "." + tableDev.getTableName() + " change " + pmdColumn.getColumnName()
					+ " "
					+ pmdColumn.getColumnName() + " " + pmdColumn.getDataType() + AddDefault(pmdColumn)
					+ " CHARACTER SET " +pmdColumn.getCHARACTER_SET_NAME()+ " "+pmdColumn.getCOLLATION_NAME()+";";
			//COLLATE
			AddSqlVector(vSql,strSql,10,tableDev.getTableName());
			sb[9].append(strSql).append("</td></tr>");
			break;
		
		}
		
	}

	public static String ShowDefaultValue(String strInput)
	{
		if (strInput == null)
			return "";
		return strInput;
	}
}
