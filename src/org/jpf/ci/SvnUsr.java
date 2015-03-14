package org.jpf.ci;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.ci.AppConn;
import org.jpf.utils.JpfDbUtils;

import java.util.Vector;
import java.sql.Statement;

public class SvnUsr
{
	private String strCmd = "";
	String PrjName = "";
	private StringBuffer sBuffer = new StringBuffer();
	private static final Logger logger =LogManager.getLogger(SvnUsr.class);
	private String strType = "";

	public SvnUsr(String PrjName, String PrjPath, String strType)
	{
		this.PrjName = PrjName;
		this.strType = strType;
		sBuffer.append("delete from hz_dev where prj_name='" + PrjName + "';");
		strCmd = "cd " + PrjPath + ";svn status -v";
		// logger.info(strCmd);
		runexec();
	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (args.length == 3)
		{
			SvnUsr cvsOpBean = new SvnUsr(args[0], args[1], args[2]);

		} else
		{
			logger.info("no input parameter");
		}

	}

	private void setstrRshRet(int value)
	{
		this.strRshRet = value;
	}

	private int strRshRet;

	static String regEx = "['   ']+";

	private void ExecInsert()
	{
		Connection conn = null;
		try
		{
			String[] strSqlStrings = sBuffer.toString().split(";");

			if (strSqlStrings.length < 2)
			{
				logger.info("no svn info find");
				return;
			}
			conn = AppConn.GetInstance().GetConn();
			Statement stmt = conn.createStatement();
			conn.setAutoCommit(false);

			for (int i = 0; i < strSqlStrings.length; i++)
			{
				// System.out.println(strSqlStrings[i]);
				stmt.addBatch(strSqlStrings[i]);
			}
			stmt.executeBatch();

			// stmt.executeUpdate(sBuffer.toString());
			conn.commit();
			logger.info("commit sql count=" + (strSqlStrings.length - 1));
			sBuffer.setLength(0);
			logger.info("game over");
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			JpfDbUtils.DoClear(conn);
		}
	}



	final String strJavaKey = "src/main/java/";
	final String strJavaKey2 = "framework/";
	final String strJavaKey3 = "src/";
	private long lTotalSql = 0;
	private long lOutFileCount = 0;

	private void MakeSql(String strInput)
	{

		if (strInput.indexOf("/build/") >= 0)
		{
			lOutFileCount++;
			return;
		}
		/*
		if (strInput.indexOf("test/") >= 0)
		{
			lOutFileCount++;
			return;
		}
		*/
		strInput = strInput.trim();
		// System.out.println(strInput);
		if (strInput.endsWith(".java") || strInput.endsWith(".cpp") || strInput.endsWith(".c")
				|| strInput.endsWith(".h"))
		{
			String[] mResult = strInput.split(regEx);
			// System.out.println(mResult.length);
			if (5 == mResult.length || 4 == mResult.length)
			{
				String loginString = "";

				String fileNameString = "";
				String filePathName = "";
				if (5 == mResult.length)
				{
					fileNameString = mResult[4].substring(mResult[4].lastIndexOf("/") + 1);
					filePathName = mResult[4].trim();
					loginString = mResult[3];
				}
				if (4 == mResult.length)
				{
					fileNameString = mResult[3].substring(mResult[3].lastIndexOf("/") + 1);
					filePathName = mResult[3].trim();
					loginString = mResult[2];
				}

				// System.out.println(mResult[4]);
				// System.out.println(fileNameString);
				// String filePathName = mResult[4];

				if (strInput.endsWith(".java"))
				{
					//System.out.println(filePathName);
					// System.out.println(filePathName.indexOf("test"));
					// System.out.println(filePathName.indexOf("Test"));
					// if (filePathName.indexOf(strJavaKey) >= 0 &&
					// filePathName.indexOf("test")<0 &&
					// filePathName.indexOf("Test")<0)

					// System.out.println(filePathName);
					if (filePathName.indexOf(strJavaKey) >= 0)
					{
						// for maven
						filePathName = filePathName.substring(filePathName.indexOf(strJavaKey) + strJavaKey.length(),
								filePathName.length() - 5);
						// filePathName = filePathName.substring(14,
						// filePathName.length()-5);
					}
					else if (filePathName.indexOf(strJavaKey2) >= 0)
					{
						// for ant
						filePathName = filePathName.substring(filePathName.indexOf(strJavaKey2) + strJavaKey2.length(),
								filePathName.length() - 5);
					} else if (filePathName.indexOf(strJavaKey3) >= 0)
					{
						filePathName = filePathName.substring(filePathName.indexOf(strJavaKey3) + strJavaKey3.length(),
								filePathName.length() - 5);
					}
					//System.out.println(filePathName);

					filePathName = filePathName.replaceAll("/", ".");
					// filePathName=filePathName.substring(0,filePathName.length()-5);
					// filePathName = filePathName.replaceAll("/", ":");
				}
				if (strInput.endsWith(".cpp") || strInput.endsWith(".c") || strInput.endsWith(".h"))
				{
					// System.out.println("filePathName="+filePathName);

					if (strType.equalsIgnoreCase("1"))
					{
						filePathName = filePathName.replaceFirst("/", ":");
					}
					
					// filePathName=filePathName.replaceFirst("_", ":");
				}
				// System.out.println("filePathName="+filePathName);
				sBuffer.append("insert into hz_dev(login,prj_name,file_path,file_name) values ('" + loginString
						+ "','"
						+ PrjName + "','" + filePathName + "'" + ",'" + fileNameString + "');");
				lTotalSql++;
			} else
			{
				System.out.println(strInput);
			}

		}
	}

	Vector<String> gVector = new Vector<String>();

	private String runexec()
	{
		try
		{
			String[] cmd = new String[] { "/bin/sh", "-c", strCmd };
			Process process = Runtime.getRuntime().exec(cmd);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			String line;
			while ((line = input.readLine()) != null)
			{
				// System.out.println(line);
				// MakeSql(line);
				// Sleep(1);
				line=line.trim();
				if (line.endsWith(".java") || line.endsWith(".cpp") || line.endsWith(".c") || line.endsWith(".h"))
				{
					gVector.add(line);
					System.out.println(line);
				}
			}
			process.waitFor();
			int iRetValue = process.exitValue();
			setstrRshRet(iRetValue);
			for (int i = 0; i < gVector.size(); i++)
			{
				MakeSql((String) gVector.get(i));
			}
			ExecInsert();
			// System.out.println(sBuffer);
			logger.info("total file sql=" + lTotalSql);
			logger.info("gVector.size=" + gVector.size());
			logger.info("lOutFileCount=" + lOutFileCount);
			return "";
		} catch (IOException e)
		{
			e.printStackTrace();

		} catch (Exception e)
		{
			e.printStackTrace();

		}
		return "";
	}

}
