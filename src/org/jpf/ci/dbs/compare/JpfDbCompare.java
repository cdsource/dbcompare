/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月14日 上午1:26:12 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.sql.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;
import org.jpf.xmls.JpfXmlUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class JpfDbCompare
{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public JpfDbCompare(String strConfigFileName)
	{
		// TODO Auto-generated constructor stub
		try
		{
			// read config
			//System.out.println(System.getProperty("user.dir"));
			//System.out.println(System.getProperty("java.class.path"));
			DbDescInfo cDbDescInfo = null;
			JpfFileUtil.CheckFile(strConfigFileName);
			NodeList nl = JpfXmlUtil.GetNodeList("dbsource", strConfigFileName);
			logger.debug(nl.getLength());
			String strDefaultMail=""; 
			if(1==nl.getLength())
			{
				Element el = (Element) nl.item(0);
				String strJdbcUrl = JpfXmlUtil.GetParStrValue(el, "dburl");
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
				strDefaultMail= JpfXmlUtil.GetParStrValue(el, "dbmails");
				logger.debug(strJdbcUrl);
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				cDbDescInfo = new DbDescInfo(strJdbcUrl, strDbUsr, strDbPwd);
			}else {
				logger.error("error source db info");
			}
			nl = JpfXmlUtil.GetNodeList("dbcompare", strConfigFileName);
			logger.debug(nl.getLength());
			for (int j = 0; j < nl.getLength(); j++)
			{
				// System.out.println(nl.item(j).getNodeValue());
				Element el = (Element) nl.item(j);
				String strJdbcUrl = JpfXmlUtil.GetParStrValue(el, "dburl");
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
				String strDomain = JpfXmlUtil.GetParStrValue(el, "dbdomain");
				String strMails = JpfXmlUtil.GetParStrValue(el, "dbmails")+","+strDefaultMail;
				logger.debug(strDomain);
				logger.debug(strJdbcUrl);
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				logger.debug(strMails);


				DbDescInfo cDbDescInfo2 = new DbDescInfo(strJdbcUrl, strDbUsr, strDbPwd);
				
				Connection conn_product = null;
				Connection conn_develop = null;
				try
				{
					conn_product =  cDbDescInfo.GetConn();
					conn_develop = cDbDescInfo2.GetConn();
					// 比较表
					System.out.println(".....................................................................................................................");
					System.out.println("compare tables...");
					logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					CompareTable cCompareTable = new CompareTable();
					cCompareTable.DoCompare(conn_product, conn_develop, strDomain,strMails,strJdbcUrl+"/"+strDomain);
					
					// 比较索引
					System.out.println(".....................................................................................................................");
					System.out.println("compare index...");
					logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					CompareIndex cCompareIndex = new CompareIndex();
					//cCompareIndex.DoCompare(conn_product, conn_develop, strDomain,strMails,strJdbcUrl+"/"+strDomain);

					// 检查是否母表是否存在
					//logger.info(".....................................................................................................................");
					//logger.info("CheckParentTableExist...");
					//logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					//CheckParentTableExist cCheckParentTableExist = new CheckParentTableExist();
					//cCheckParentTableExist.DoCheck(conn_product,strDomain);
					//cCheckParentTableExist.DoCheck(conn_develop,strDomain);
					
					System.out.println(".....................................................................................................................");
					System.out.println("CheckSameTableName...");
					logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					CheckSameTableName cCheckSameTableName = new CheckSameTableName();
					//cCheckSameTableName.DoCheck(conn_product,strDomain);
					//cCheckSameTableName.DoCheck(conn_develop,strDomain);
					
					System.out.println(".....................................................................................................................");
					System.out.println("Compare Data...");
					logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					CompareData cCompareData = new CompareData();
					//cCompareData.DoCompare(conn_product, conn_develop, "zd.sys_partition_rule","base_name");
					
					System.out.println(".....................................................................................................................");
					System.out.println("Check sub table...");
					logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					CompareSubTables cCompareSubTables = new CompareSubTables();
					//cCompareSubTables.DoCheck(conn_develop,strDomain);
					//cCompareSubTables.DoCheck(conn_product,strDomain);
					

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				} finally
				{
					JpfDbUtils.DoClear(conn_product);
					JpfDbUtils.DoClear(conn_develop);
				}
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		logger.info("game over");
	}

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2015年2月14日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (1 == args.length)
		{
			JpfDbCompare cJpfDbCompare = new JpfDbCompare(args[0]);
		}
	}

}
