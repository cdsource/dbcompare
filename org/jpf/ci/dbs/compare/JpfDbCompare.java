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
import org.jpf.ci.dbs.DbUtils;
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
			System.out.println(System.getProperty("user.dir"));
			System.out.println(System.getProperty("java.class.path"));
			DbDescInfo cDbDescInfo = null;
			JpfFileUtil.CheckFile(strConfigFileName);
			NodeList nl = JpfXmlUtil.GetNodeList("dbsource", strConfigFileName);
			logger.debug(nl.getLength());
			if(1==nl.getLength())
			{
				Element el = (Element) nl.item(0);
				String strJdbcUrl = JpfXmlUtil.GetParStrValue(el, "dburl");
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
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
				String strMails = JpfXmlUtil.GetParStrValue(el, "dbmails");
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
					logger.info("compare tables...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CompareTable cCompareTable = new CompareTable();
					cCompareTable.DoCompare(conn_product, conn_develop, strDomain,strMails);
					
					// 比较索引
					logger.info("compare index...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CompareIndex cCompareIndex = new CompareIndex();
					cCompareIndex.DoCompare(conn_product, conn_develop, strDomain,strMails);

					// 检查是否母表是否存在
					logger.info("CheckParentTableExist...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CheckParentTableExist cCheckParentTableExist = new CheckParentTableExist();
					cCheckParentTableExist.DoCheck(conn_product);
					cCheckParentTableExist.DoCheck(conn_develop);
					
					logger.info("CheckSameTableName...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CheckSameTableName cCheckSameTableName = new CheckSameTableName();
					cCheckSameTableName.DoCheck(conn_product);
					cCheckSameTableName.DoCheck(conn_develop);
					
					logger.info("Compare Data...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CompareData cCompareData = new CompareData();
					cCompareData.DoCompare(conn_product, conn_develop, "zd.sys_partition_rule","base_name");
					
					
					logger.info("CheckSameTableName...");
					logger.info("conn_product.isClosed()="+conn_product.isClosed());
					CompareSubTables cCompareSubTables = new CompareSubTables();
					cCompareSubTables.DoCheck(conn_product);
				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				} finally
				{
					DbUtils.DoClear(conn_product);
					DbUtils.DoClear(conn_develop);
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
