/**
 * 
 */
package org.jpf.updates.versions;

import java.util.Vector;


/**
 * @author 吴平福
 * 
 * @version 创建时间：2010-8-2 上午10:33:21
 */
public class VersionClass
{
	//版本号
	private int iVersion = 0;
	//版本依赖关系
	//JarVersioninfo[] GetDependRelax();
	Vector vJarVersioninfo=new Vector();
	StringBuffer sb = new StringBuffer();

	public void ShowVersion(String sDate, String sAuthor, String sTitle)
	{
		iVersion++;
		sb.append("-------------------------------------------\r\n")
				.append("Version " + iVersion).append("\r\n")
				.append(sDate).append("\r\n")
				.append("author:" + sAuthor).append("\r\n")
				.append(sTitle).append("\r\n");
		
	}
	//增加版本依赖关系
	public int AddDependRelax(String strJarName,String strJarVersion)
	{
		JarVersioninfo cJarVersioninfo=new JarVersioninfo();
		cJarVersioninfo.JarName=strJarName;
		cJarVersioninfo.JarVersion=strJarVersion;
		vJarVersioninfo.add(cJarVersioninfo);
		return 0;
	}

    /**
     * 返回版本号
     * @return
     */
	public String GetSelfVersion()
	{
		return String.valueOf(iVersion);
	}
    /**
     * 返回版本信息
     * @return
     */
	public String GetVersionInfo()
	{
		return sb.toString();
	}
}
