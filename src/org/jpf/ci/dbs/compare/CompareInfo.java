/** 
* @author 吴平福 
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年5月18日 下午4:20:59 
* 类说明 
*/ 

package org.jpf.ci.dbs.compare;

/**
 * 
 */
public class CompareInfo
{

	/**
	 * 
	 */
	public CompareInfo()
	{
		// TODO Auto-generated constructor stub
	}
	
    private String strCondName;
    private String strDomain;
    private String strJdbcUrl;
    private String strPdmInfo;
    private String strExcludeTable;
    private String strMails;
    
    //保存差异结果到数据库
    private int DoSaveSql;
    //执行差异结果SQL
	private int DoExec;
    //比较类型 表：1--10，索引 11-15
    private String CompareTypes;
    
    public String getCompareTypes()
	{
		return CompareTypes;
	}
	public void setCompareTypes(String CompareTypes)
	{
		this.CompareTypes = CompareTypes;
	}


    
	public int getDoExec()
	{
		return DoExec;
	}
	public void setDoExec(int doExec)
	{
		DoExec = doExec;
	}
	public void setDoExec(String strDoExec)
	{
		if (strDoExec!=null && "1".equalsIgnoreCase(strDoExec))
		{
			DoExec=1;
		}else {
			DoExec = 0;
		}

	}
	public String getStrMails()
	{
		return strMails;
	}
	public void setStrMails(String strMails)
	{
		this.strMails = strMails;
	}
	public String getStrCondName()
	{
		return strCondName;
	}
	public void setStrCondName(String strCondName)
	{
		this.strCondName = strCondName;
	}
	public String getStrDomain()
	{
		return strDomain;
	}
	public void setStrDomain(String strDomain)
	{
		this.strDomain = strDomain;
	}
	public String getStrJdbcUrl()
	{
		return strJdbcUrl;
	}
	public void setStrJdbcUrl(String strJdbcUrl)
	{
		this.strJdbcUrl = strJdbcUrl;
	}
	public String getStrPdmInfo()
	{
		return strPdmInfo;
	}
	public void setStrPdmInfo(String strPdmInfo)
	{
		this.strPdmInfo = strPdmInfo;
	}
	public String getStrExcludeTable()
	{
		return strExcludeTable;
	}
	public void setStrExcludeTable(String strExcludeTable)
	{
		this.strExcludeTable = strExcludeTable;
	}
}
