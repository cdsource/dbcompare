/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2014��10��16�� ����7:33:09 
 * ��˵�� 
 */

package org.jpf.ci.dbs;

/** 
 * @author ��ƽ�� 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version ����ʱ�䣺2013��12��10�� ����3:32:01 
 * ��˵�� 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;

/**
 * 
 */
public class DbChangeApp
{
	private static final Logger logger = LogManager.getLogger();
	// ��ǰDDL�ļ�����
	private String strCurrentFileName = "";

	private final int iStmtTimeOut = 10;

	StringBuffer sbBuffer = new StringBuffer();
	private int iDoChange = 0;

	// ɾ���ķֱ���
	private Vector<TableInfo> vSubTableName = new Vector<TableInfo>();

	private DbChangeMail cDbChangeMail;

	private boolean bWarning = false;

	// SQL�ļ��б�
	Vector<String> g_FileVector = new Vector<String>();

	int iDbType = 1;

	private void AddTableInfo(String strDbName, String strParentTable, String strSubTable)
	{
		TableInfo cTableInfo = new TableInfo(strDbName, strParentTable, strSubTable);
		vSubTableName.add(cTableInfo);
	}

	public static void main(String[] args)
	{
		try
		{
			if (2 == args.length)
			{
				DbChangeApp cDbChange = new DbChangeApp(args[0], args[1]);
			}
			if (1 == args.length)
			{
				DbChangeApp cDbChange = new DbChangeApp(args[0]);
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}

	private Connection GetConn() throws Exception
	{
		String driver = "com.mysql.jdbc.Driver";
		Class.forName(driver).newInstance();
		// 10.10.13.90:6511 boss/boss6511
		// 10.10.13.90:6502 intf/intf6502
		String URL = "jdbc:mysql://10.10.13.90:6512";
		String dbuser = "boss";
		String dbpass = "boss6512";
		return DriverManager.getConnection(URL, dbuser, dbpass);
	}

	private void ExecSubTable(String strSql, String _strParentTableName) throws Exception
	{
		Connection conn = null;

		try
		{
			strSql = SqlStringUtil.TrimSql(strSql);

			String tmpTableName = _strParentTableName.replaceAll("`", "");
			String strDbName = tmpTableName.split("\\.")[0].toLowerCase();
			String strTableName = tmpTableName.split("\\.")[1];

			conn = GetConn();

			conn.setAutoCommit(false);

			sbBuffer.append(strSql).append("<br><br>");
			try
			{
				ExecSql(strSql);
			} catch (Exception ex)
			{
				// TODO: handle exception
				logger.error(ex);
			}

			// insert delete ��ִ�зֱ�
			if (!strSql.trim().toLowerCase().startsWith("insert") || !strSql.trim().toLowerCase().startsWith("delete"))
			{
				java.sql.Statement stmt = conn.createStatement();

				String strFindSubSql = "SELECT  * FROM information_schema.TABLES WHERE table_schema='"
						+ strDbName
						+ "' and table_name REGEXP '^"
						+ strTableName.toLowerCase() + "_[0-9]'";
				// logger.info(strFindSubSql);
				ResultSet rs = stmt.executeQuery(strFindSubSql);

				boolean bSubTable = false;
				while (rs.next())
				{
					if (!bSubTable)
					{
						sbBuffer.append("�Զ������ֱ�ִ�е�SQL<br>");
					}
					bSubTable = true;
					String strNewTablName = rs.getString("TABLE_NAME").trim();
					// vSubTableName.add(strNewTablName);
					if (strSql.toLowerCase().startsWith("drop"))
					{
						AddTableInfo(strDbName, _strParentTableName, strNewTablName);
					}

					String strNewSql = strSql.replaceAll(strTableName, strNewTablName);

					// logger.info("sub table sql:=" + strNewSql);
					//
					String[] strKeys = strSql.split(" ");
					if (strKeys.length > 3 && strKeys[3].equalsIgnoreCase("rename"))
					{
						strNewTablName = strNewTablName.toLowerCase().trim();
						strTableName = strTableName.toLowerCase().trim();
						int i = strNewTablName.indexOf(strTableName) + strTableName.length();
						String tmpStr = strNewTablName.substring(i, strNewTablName.length());
						if (strKeys[4].trim().endsWith(";"))
						{
							tmpStr = strKeys[4].trim().subSequence(0, strKeys[4].trim().length() - 1) + tmpStr + ";";
						} else
						{
							tmpStr = strKeys[4].trim() + tmpStr;
						}
						strNewSql = strNewSql.replaceAll(strKeys[4].trim(), tmpStr);
						logger.info("sub table sql:=" + strNewSql);
					}
					sbBuffer.append(strNewSql).append("<br>");
					ExecSql(strNewSql);
				}

				DoSubTable(strSql, conn);
			}
			conn.commit();
		} catch (SQLException ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			if (conn != null)
			{
				conn.rollback();
			}
			throw ex;
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			throw ex;
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}

	}

	/**
	 * @todo:ִ��SQL
	 * @param conn
	 * @param strSql
	 * @throws Exception
	 *             ������������TODO �����Խӿ���:TODO ���Գ�����TODO ǰ�ò�����TODO ��Σ� У��ֵ�� ���Ա�ע��
	 *             update 2014��3��30��
	 */
	private void ExecSql(String strSql) throws Exception
	{
		Connection conn = null;
		try
		{
			logger.info("exec sql:={}", strSql);
			conn = GetConn();
			java.sql.Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(iStmtTimeOut);
			stmt.executeUpdate(strSql);
		} catch (SQLException ex)
		{
			// TODO: handle exception
			logger.error("ex.getErrorCode()=" + ex.getErrorCode());
			// �ֶ��Ѿ����ڡ�
			if (ex.getErrorCode() == 1060 || 1091 == ex.getErrorCode())
			{
			} else if (1146 == ex.getErrorCode())
			{
				logger.error(ex.getMessage());
			} else
			{
				ex.printStackTrace();
				throw ex;
			}

		} finally
		{
			JpfDbUtils.DoClear(conn);
		}

	}

	/**
	 * @todo:����Ƿ���ɾ���ķֱ���Ҫ�½�
	 * 
	 *                      ������������TODO �����Խӿ���:TODO ���Գ�����TODO ǰ�ò�����TODO ��Σ� У��ֵ��
	 *                      ���Ա�ע�� update 2014��3��30��
	 */
	private void DoSubTable(String strSql, Connection conn) throws Exception
	{
		if (strSql.toLowerCase().startsWith("create"))
		{
			for (int i = 0; i < vSubTableName.size(); i++)
			{
				TableInfo cTableInfo = (TableInfo) vSubTableName.get(i);
				String strNewSql = "";
				String strRegex = cTableInfo.strDbName + "." + cTableInfo.strParentTable;

				if (strSql.toLowerCase().indexOf(strRegex) > 0)
				{
					strNewSql = strSql.replaceAll("(?i)" + strRegex, cTableInfo.strSubTable);
				}

				strRegex = "`" + cTableInfo.strDbName + "`." + cTableInfo.strParentTable;
				if (strSql.toLowerCase().indexOf(strRegex) > 0)
				{
					strNewSql = strSql.replaceAll("(?i)" + strRegex, cTableInfo.strSubTable);
				}

				strRegex = cTableInfo.strDbName + ".`" + cTableInfo.strParentTable + "`";
				if (strSql.toLowerCase().indexOf(strRegex) > 0)
				{
					strNewSql = strSql.replaceAll("(?i)" + strRegex, cTableInfo.strSubTable);
				}

				strRegex = "`" + cTableInfo.strDbName + "`.`" + cTableInfo.strParentTable + "`";
				if (strSql.toLowerCase().indexOf(strRegex) > 0)
				{
					strNewSql = strSql.replaceAll("(?i)" + strRegex, cTableInfo.strSubTable);
				}

				if (strNewSql.length() > 0)
				{
					logger.info("sub table sql:=" + strNewSql);
					sbBuffer.append(strNewSql).append("<br>");
					ExecSql(strNewSql);
					strNewSql = "";
				}
			}
		}
	}

	/**
	 * @TODO: ��ȡ������
	 * @param strText
	 * @return ��Σ� У��ֵ�� ���Ա�ע�� update 2014��3��16��
	 */
	private void GetTableName(String strText, int iRowNum)
	{
		try
		{

			// create table
			logger.info("Inputsql={}", strText);

			strText = SqlStringUtil.RemoveSqlNote(strText);

			String[] strCols = strText.trim().split(" +");
			// test

			String strParentTableName = "";

			if (strCols[2].toLowerCase().trim().equalsIgnoreCase("if"))
			{
				if (strCols[0].toLowerCase().trim().equalsIgnoreCase("create"))
				{
					strParentTableName = strCols[5];
				} else
				{
					strParentTableName = strCols[4];
				}

			} else
			{
				strParentTableName = strCols[2];
			}
			// ����Ƿ�û�пո��������
			if (strParentTableName.indexOf("(") > 0)
			{
				strParentTableName = strParentTableName.substring(0, strParentTableName.indexOf("("));
			}
			if (strParentTableName.indexOf(";") > 0)
			{
				strParentTableName = strParentTableName.substring(0, strParentTableName.indexOf(";"));
			}
			if (strParentTableName.length() > 0)
			{
				DbChangeCheck.CheckTableName(strParentTableName);
				ExecSubTable(strText, strParentTableName);

			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	public DbChangeApp(String strFilePath, String iType) throws Exception
	{
		try
		{
			iDoChange = Integer.parseInt(iType);
		} catch (Exception ex)
		{
			// TODO: handle exception
			logger.error(ex);
		}
		DoChange(strFilePath, iDoChange);
	}

	public DbChangeApp(String strFilePath) throws Exception
	{
		DoChange(strFilePath, 1);
	}

	/**
	 * @todo:��ȡ�ļ��б�
	 * @param g_FileVector
	 * @param strFilePath
	 * @throws Exception
	 *             update 2014��3��30��
	 */
	private void GetFiles(String strFilePath) throws Exception
	{
		// ��ȡ�ļ��б�����
		g_FileVector.clear();
		File f = new File(strFilePath);
		if (!f.exists())
		{
			throw new Exception("Ŀ���ļ��в�����");
		}
		JpfFileUtil.GetFiles(strFilePath, g_FileVector);
		logger.info("g_FileVector.size()=" + g_FileVector.size());
		for (int i = g_FileVector.size() - 1; i >= 0; i--)
		{
			// logger.info("i=" + i);
			File file = new File((String) g_FileVector.get(i));
			String tmpFileName = file.getAbsolutePath();
			logger.info(tmpFileName);

			if (tmpFileName.indexOf(".svn") >= 0)
			{
				// logger.info(tmpFileName);
				g_FileVector.remove(i);
				continue;
			}
			if (!tmpFileName.endsWith(".sql"))
			{
				logger.info(tmpFileName);
				g_FileVector.remove(i);
				continue;
			}
			logger.info(tmpFileName);

		}

		DbFileCompare ct = new DbFileCompare();
		Collections.sort(g_FileVector, ct);
		// g_FileVector.clear();
	}

	private void DoChange(String strFilePath, int i) throws Exception
	{

		int iCount = 0;
		try
		{

			GetFiles(strFilePath);

			for (int j = 0; j < g_FileVector.size(); j++)
			{
				vSubTableName.clear();
				strCurrentFileName = (String) g_FileVector.get(j);

				iCount++;
				logger.info(iCount + ". find file: " + strCurrentFileName);

				String strSql = "";
				int iRowNum = 0;
				bWarning = false;
				try
				{

					if (strCurrentFileName.indexOf("/id/") > 0)
					{
						iDbType = 2;
					} else
					{
						iDbType = 1;
					}
					String strText = JpfFileUtil.GetFileTxt(strCurrentFileName, "GBK").trim();
					// strText = strText.replaceAll("`", "");

					String[] colsString = strText.split("\n");
					boolean b = false;
					boolean bCreate = false;
					boolean bDrop = false;
					boolean bAlter = false;
					boolean bInsert = false;
					boolean bDelete = false;
					boolean bCommit = false;

					boolean bIdxCreate = false;
					boolean bIdxDrop = false;
					boolean bIdxAlter = false;

					for (int n = 0; n < colsString.length; n++)
					{

						Pattern pattern = Pattern.compile("^drop[ ]+table.*");
						Matcher matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bDrop = matcher.matches();

						pattern = Pattern.compile("^alter[ ]+table.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bAlter = matcher.matches();

						pattern = Pattern.compile("^create[ ]+table.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bCreate = matcher.matches();

						pattern = Pattern.compile("^insert[ ]+into.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bInsert = matcher.matches();

						pattern = Pattern.compile("^delete[ ]+from.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bDelete = matcher.matches();

						pattern = Pattern.compile("^drop[ ]+index.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bIdxDrop = matcher.matches();

						pattern = Pattern.compile("^create[ ]+index.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bIdxCreate = matcher.matches();

						pattern = Pattern.compile("^alter[ ]+index.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bIdxAlter = matcher.matches();

						pattern = Pattern.compile("^commit.*");
						matcher = pattern.matcher(colsString[n].trim().toLowerCase());
						bCommit = matcher.matches();

						if (bDrop || bAlter || bCreate || bInsert || bDelete || bIdxCreate || bIdxAlter || bIdxDrop)
						{
							if (strSql.length() > 0)
							{
								GetTableName(strSql, iRowNum);
							}
							strSql = colsString[n] + "\n";
							b = true;
							iRowNum = n + 1;
						} else
						{
							if (!bCommit)
							{
								if (b)
								{
									strSql += colsString[n] + "\n";
								}
							} else
							{
								cDbChangeMail.strMsg = "<br>DB�������COMMIT��"
										+ MakeMailTxt(strCurrentFileName, "")
										+ "<br>�����У�" + iRowNum + 1
										+ "<br>����SQL��" + strText;
								cDbChangeMail.SendMail(strCurrentFileName, 2);
							}
							// b = false;
						}
						if (bInsert || bDelete)
						{
							bWarning = true;
						}
					}
					if (strSql.length() > 0)
					{
						GetTableName(strSql, iRowNum);
					}
				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
					String strErrMsg = "";
					if (ex instanceof SQLException)
					{
						strErrMsg = ((SQLException) ex).getMessage();
						strErrMsg = new String(strErrMsg.getBytes("utf-8"), "gbk");
						strErrMsg = "<br>������룺" + ((SQLException) ex).getErrorCode() + ";<br>������ʾ��" + strErrMsg;
						// ��������
						if (((SQLException) ex).getErrorCode() == 1215)
						{
							DbFkCheck cDbFkCheck = new DbFkCheck();
							strErrMsg += cDbFkCheck.CheckSqlError(strSql);
						}
					} else
					{
						strErrMsg = ex.getMessage();
					}
					cDbChangeMail.strMsg = MakeMailTxt(strCurrentFileName, "")
							+ "<br>�����У�" + iRowNum
							+ "<br>����SQL��" + strSql
							+ "<br><font color='#FF0000'>������ʾ��" + strErrMsg + "</font>";

					cDbChangeMail.SendMail(strCurrentFileName, 1);

					sbBuffer.setLength(0);
				}
				// GetFileAuthor(strCurrentFileName)

			}
			logger.info("db change exec file Count:=" + iCount);

		} catch (Exception ex)
		{
			ex.printStackTrace();

		}
		logger.info("db change exec finish");

		// DoBack("ad");
		// ִ��SQL
		// ���ʧ�ܣ������ʼ������ӱ������ݿ�ָ�

	}

	private String MakeMailTxt(String strFileName, String strDate)
	{
		return "<br>�ļ����ƣ�" + strFileName
				+ "<br>ִ��ʱ�䣺" + strDate
				+ "<br>�ļ�ʱ�䣺" + JpfFileUtil.GetFileDate(strFileName);
	}

	private class DbFileCompare implements Comparator
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Object o1, Object o2)
		{
			// TODO Auto-generated method stub
			String e1 = (String) o1;
			String e2 = (String) o2;
			File f1 = new File(e1);
			File f2 = new File(e2);
			if (f1.lastModified() > f2.lastModified())// �����Ƚ��ǽ���,�����-1�ĳ�1��������.
			{
				return 1;
			}
			else if (f1.lastModified() < f2.lastModified())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}

}
