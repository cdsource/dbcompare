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
    private String DbDomain;
    private String DevJdbcUrl;
    private String PdmInfo;
    private String strExcludeTable;
<<<<<<< HEAD

	private String strMails;
    
	//输出类型
    private int sendResulType;
    
=======
    /**
	 * @return the sendresulttype
	 */
	public int getSendresulttype()
	{
		return sendresulttype;
	}
	/**
	 * @param sendresulttype the sendresulttype to set
	 */
	public void setSendresulttype(int sendresulttype)
	{
		this.sendresulttype = sendresulttype;
	}

	private String strMails;
    
    private int sendresulttype;
>>>>>>> origin/master
    private String AllMail;
    //比较类型
    private int CompareType;
    
	//保存差异结果到数据库
    private int DoSaveSql;
    //执行差异结果SQL
	private int DoExec;
    //比较类型 表：1--10，索引 11-15
    private String ResultTypes;
<<<<<<< HEAD
    //版本信息或日期信息
    private String pdmDtVers;
    
    private String pdmDbName;

	private String PdmJdbcUrl;
	
    /**
	 * @return the sendresulttype
	 */
	public int getSendresulttype()
	{
		return sendResulType;
	}
	/**
	 * @param sendresulttype the sendresulttype to set
	 */
	public void setSendresulttype(int sendResulType)
	{
		this.sendResulType = sendResulType;
	}

=======
    private String pdmDtVers;//版本信息或日期信息
>>>>>>> origin/master
    public String getPdmDtVers() {
		return pdmDtVers;
	}
	public void setPdmDtVers(String pdmDtVers) {
		this.pdmDtVers = pdmDtVers;
	}
    
    public int getCompareType()
	{
		return CompareType;
	}
	public void setCompareType(int compareType)
	{
		CompareType = compareType;
	}
	public String getAllMail()
	{
		return AllMail;
	}
	public void setAllMail(String allMail)
	{
		AllMail = allMail;
	}

<<<<<<< HEAD
=======
	private String PdmJdbcUrl;
>>>>>>> origin/master
    
    public String getPdmJdbcUrl()
	{
		return PdmJdbcUrl;
	}
	public void setPdmJdbcUrl(String strPdmJdbcUrl)
	{
		this.PdmJdbcUrl = strPdmJdbcUrl;
	}
	public int getDoSaveSql()
	{
		return DoSaveSql;
	}
	public void setDoSaveSql(int doSaveSql)
	{
		DoSaveSql = doSaveSql;
	}


    
    public String getResultTypes()
	{
		return ResultTypes;
	}
	public void setResultTypes(String ResultTypes)
	{
		this.ResultTypes = ResultTypes;
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
	public String getDbDomain()
	{
		return DbDomain;
	}
	public void setDbDomain(String strDomain)
	{
		this.DbDomain = strDomain;
	}
	public String getDevJdbcUrl()
	{
		return DevJdbcUrl;
	}
	public void setDevJdbcUrl(String strJdbcUrl)
	{
		this.DevJdbcUrl = strJdbcUrl;
	}
	public String getPdmInfo()
	{
		return PdmInfo;
	}
	public void setPdmInfo(String strPdmInfo)
	{
		this.PdmInfo = strPdmInfo;
	}
	public String getStrExcludeTable()
	{
		return strExcludeTable;
	}
	public void setStrExcludeTable(String strExcludeTable)
	{
		this.strExcludeTable = strExcludeTable;
	}
	public String getPdmDbName() {
		return pdmDbName;
	}
	public void setPdmDbName(String pdmDbName) {
		this.pdmDbName = pdmDbName;
	}
}
