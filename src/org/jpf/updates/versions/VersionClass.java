/**
 * 
 */
package org.jpf.updates.versions;

import java.util.Vector;


/**
 * @author ��ƽ��
 * 
 * @version ����ʱ�䣺2010-8-2 ����10:33:21
 */
public class VersionClass
{
	//�汾��
	private int iVersion = 0;
	//�汾������ϵ
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
	//���Ӱ汾������ϵ
	public int AddDependRelax(String strJarName,String strJarVersion)
	{
		JarVersioninfo cJarVersioninfo=new JarVersioninfo();
		cJarVersioninfo.JarName=strJarName;
		cJarVersioninfo.JarVersion=strJarVersion;
		vJarVersioninfo.add(cJarVersioninfo);
		return 0;
	}

    /**
     * ���ذ汾��
     * @return
     */
	public String GetSelfVersion()
	{
		return String.valueOf(iVersion);
	}
    /**
     * ���ذ汾��Ϣ
     * @return
     */
	public String GetVersionInfo()
	{
		return sb.toString();
	}
}
