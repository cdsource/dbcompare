/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo.com 
 * @version 创建时间：2015年2月14日 上午1:26:12 
 * 类说明 
 */

package org.jpf.ci.dbs.compare;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfDbUtils;
import org.jpf.utils.JpfFileUtil;
import org.jpf.utils.SvnInfoUtil;
import org.jpf.xmls.JpfXmlUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 */
public class JpfDbCompare
{
	private static final Logger logger = LogManager.getLogger();
	private static HashMap<String, String> parent_child = new HashMap<String, String>();

	public static HashMap<String, String> getParentChild()
	{
		return parent_child;
	}

	/**
	 * 
	 */
	public void getPdmsBySvnKit(Element el, String pdmpath, String user, String password)
	{
		try
		{
			List<String> pdmUrlPath = new ArrayList<String>();
			// 更新svn上的pdm到指定文件中
			NodeList npath = el.getElementsByTagName("svnurl");
			for (int i = 0; i < npath.getLength(); i++)
			{
				Node child = npath.item(i);
				if (child instanceof Element)
				{
					String pdmurl = child.getFirstChild().getNodeValue();
					if (pdmurl.contains("/"))
					{
						int j = pdmurl.lastIndexOf("/");
						String url = pdmurl.substring(0, j);
						String name = pdmurl.substring(j + 1, pdmurl.length()).trim();
						// 获得倒数第二层目录
						int secd = url.lastIndexOf("/");
						String secdpath = url.substring(secd + 1, url.length());
						String subPdmPath = pdmpath + File.separator + secdpath;
						if (!pdmUrlPath.contains(url))
						{
							pdmUrlPath.add(url);
							// SvnInfoUtil.checkout(url,subPdmPath, user,
							// password);
						}
						// SvnInfoUtil.updateLatest(url, subPdmPath,name, user,
						// password);

					}
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}

	}

	// PDM直接与数据库比较
	public void PdmDbCompare(String strConfigFileName, String downPdmPath)
	{
		// TODO Auto-generated constructor stub
		try
		{
			GetTablefmPdm getTable = new GetTablefmPdm();
			// read config
			// System.out.println(System.getProperty("user.dir"));
			// System.out.println(System.getProperty("java.class.path"));
			DbDescInfo cPdmDbDescInfo = null;
			CompareInfo cCompareInfo = new CompareInfo();
			JpfFileUtil.checkFile(strConfigFileName);

			NodeList n = JpfXmlUtil.GetNodeList("pdmconf", strConfigFileName);
			if (1 == n.getLength())
			{
				Element el = (Element) n.item(0);
				cCompareInfo.setPdmJdbcUrl("pdm");
				getTable.setPDMPath(downPdmPath);
				String svnUser = JpfXmlUtil.GetParStrValue(el, "svnuser");// 下载pdm时用户名
				String svnPassword = JpfXmlUtil.GetParStrValue(el, "svnpwd");// 下载pdm时密码
				cCompareInfo.setSendresulttype(JpfXmlUtil.GetParIntValue2(el, "sendresulttype"));
				// 将pdm从svn上下载到本地
				getPdmsBySvnKit(el, downPdmPath, svnUser, svnPassword);
				// oracle_mysql,oracle与mysql映射关系
				NodeList npdm = el.getElementsByTagName("oracle_mysql");
				for (int i = 0; i < npdm.getLength(); i++)
				{
					Node child = npdm.item(i);
					if (child instanceof Element)
					{
						String oracleTomy = child.getFirstChild().getNodeValue();
						String[] s = oracleTomy.toLowerCase().trim().split(";");
						String[] s1;
						String[] s2;
						if (s.length >= 2)
						{
							if (s[0].contains("(") && s[1].contains("("))
							{
								s1 = s[0].split("\\(");
								s2 = s[1].split("\\(");
								getTable.setConverData(s1[0], s2[0]);
							} else
							{
								getTable.setConverData(s[0], s[1]);
							}

						}
					}
				}

				// forceConvert强制需要转换的域
				NodeList nforce = el.getElementsByTagName("forceConvert");
				for (int i = 0; i < nforce.getLength(); i++)
				{
					Node child = nforce.item(i);
					if (child instanceof Element)
					{
						String forceData = child.getFirstChild().getNodeValue();
						if (forceData != null)
						{
							if (forceData.contains(";"))
							{
								String[] s = forceData.toLowerCase().trim().split(";");
								if (s.length >= 1)
								{
									for (String s1 : s)
									{
										getTable.setForceDomain(s1);
									}

								}
							}
						}
					}

				}

				// forceConvertDtType 指定某个域某个表某个字段的数据类型
				NodeList ntype = el.getElementsByTagName("forceConvertDtType");
				for (int i = 0; i < ntype.getLength(); i++)
				{
					Node child = ntype.item(i);
					if (child instanceof Element)
					{
						String DataType = child.getFirstChild().getNodeValue();
						if (DataType != null)
						{
							if (DataType.contains(","))
							{
								String[] s = DataType.toLowerCase().trim().split(",");
								if (s.length >= 4)
								{
									getTable.setMap(s[0], s[1], s[2], s[3]);
								}

							}
						}
					}

				}

				// forceConvertDefType 指定某个域某个表某个字段的默认值数据类型
				NodeList ndtype = el.getElementsByTagName("forceConvertDtType");
				for (int i = 0; i < ndtype.getLength(); i++)
				{
					Node child = ndtype.item(i);
					if (child instanceof Element)
					{
						String DataType = child.getFirstChild().getNodeValue();
						if (DataType != null)
						{
							if (DataType.contains(","))
							{
								String[] s = DataType.toLowerCase().trim().split(",");
								if (s.length >= 4)
								{
									getTable.setDefMap(s[0], s[1], s[2], s[3]);
								}

							}
						}
					}

				}

			}
			// 将pdm中的表存入map中
			getTable.getPdmsMap();

			NodeList ns = JpfXmlUtil.GetNodeList("pdmdbinform", strConfigFileName);
			if (1 == ns.getLength())
			{
				Element el = (Element) ns.item(0);
				cCompareInfo.setAllMail(JpfXmlUtil.GetParStrValue(el, "dbmails"));
				cCompareInfo.setStrCondName(JpfXmlUtil.GetParStrValue(el, "envname"));
				cCompareInfo.setDoExec(JpfXmlUtil.GetParStrValue(el, "doexecsql"));
			}

			NodeList nl = JpfXmlUtil.GetNodeList("pdmdbcompare", strConfigFileName);
			for (int j = 0; j < nl.getLength(); j++)
			{
				// System.out.println(nl.item(j).getNodeValue());
				Element el = (Element) nl.item(j);
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
				cCompareInfo.setDevJdbcUrl(JpfXmlUtil.GetParStrValue(el, "dburl"));
				cCompareInfo.setDbDomain(JpfXmlUtil.GetParStrValue(el, "dbdomain"));
				cCompareInfo.setStrMails(JpfXmlUtil.GetParStrValue(el, "dbmails") + "," + cCompareInfo.getAllMail());

				logger.info("DEV:{}", cCompareInfo.getDevJdbcUrl());
				logger.info(cCompareInfo.getDbDomain());
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				logger.info("mail to {}", cCompareInfo.getStrMails());

				NodeList nlParentChild = el.getElementsByTagName("parent_childen");

				// 获得需要进行特殊处理的分表——母表对应关系
				parent_child.clear();
				for (int i = 0; i < nlParentChild.getLength(); i++)
				{
					Node child = nlParentChild.item(i);
					if (child instanceof Element)
					{
						String s = child.getFirstChild().getNodeValue().toLowerCase().trim();
						String[] s1 = s.split(";");
						if (s1.length == 2)
						{
							parent_child.put(s1[1], s1[0]);
						}
					}
				}

				DbDescInfo cDbDescInfo2 = new DbDescInfo(cCompareInfo.getDevJdbcUrl(), strDbUsr, strDbPwd);

				Connection conn_develop = null;

				try
				{

					conn_develop = cDbDescInfo2.GetConn();

					// 带分表比对
					System.out.println(
							".....................................................................................................................");
					logger.info("Check sub table...");
					CompareSubTables cCompareSubTables = new CompareSubTables();
					cCompareInfo.setCompareType(1);
					cCompareSubTables.setDomain_table(getTable.getDomainTable());
					cCompareSubTables.DoCompare(null, conn_develop, cCompareInfo);
					cCompareSubTables = null;
					// 带分表比较索引
					System.out.println(
							".....................................................................................................................");
					logger.info("compare sub index...");
					CompareIndexSub cCompareIndexSub = new CompareIndexSub();
					cCompareInfo.setCompareType(2);
					cCompareIndexSub.setDomain_table(getTable.getDomainIndex());
					cCompareIndexSub.DoCompare(null, conn_develop, cCompareInfo);
					cCompareIndexSub = null;

					// 检查是否母表是否存在
					// logger.info(".....................................................................................................................");
					// logger.info("CheckParentTableExist...");
					// logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					// CheckParentTableExist cCheckParentTableExist = new
					// CheckParentTableExist();
					// cCheckParentTableExist.DoCheck(conn_product,strDomain);
					// cCheckParentTableExist.DoCheck(conn_develop,strDomain);

					// System.out.println(".....................................................................................................................");
					// System.out.println("CheckSameTableName...");
					// CheckSameTableName cCheckSameTableName = new
					// CheckSameTableName();
					// cCheckSameTableName.DoCheck(conn_product,strDomain);
					// cCheckSameTableName.DoCheck(conn_develop,strDomain);

					// System.out.println(".....................................................................................................................");
					// System.out.println("Compare Data...");
					// CompareData cCompareData = new CompareData();
					// cCompareData.DoCompare(conn_pdm, conn_develop,
					// strConfigFileName);

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				} finally
				{

					JpfDbUtils.DoClear(conn_develop);
				}
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		logger.info("FINISH AND  EXIT");
	}

	// PDM与PDM比对
	public void PdmPdmCompare(String strConfigFileName, String downPdmPath, String versionOrDate)
	{
		// TODO Auto-generated constructor stub
		try
		{

			// read config
			// System.out.println(System.getProperty("user.dir"));
			// System.out.println(System.getProperty("java.class.path"));
			versionOrDate = versionOrDate.trim();
			DbDescInfo cPdmDbDescInfo = null;
			CompareInfo cCompareInfo = new CompareInfo();
			JpfFileUtil.checkFile(strConfigFileName);
			String svnurl = null;
			String svnUser = null;// pdm更新时需要用户名及密码
			String svnPassword = null;
			GetTablefmPdm getTable1 = new GetTablefmPdm();
			GetTablefmPdm getTable2 = new GetTablefmPdm();
			NodeList n = JpfXmlUtil.GetNodeList("dbpdmandpdm", strConfigFileName);
			if (1 == n.getLength())
			{
				Element el = (Element) n.item(0);
				cCompareInfo.setStrCondName(JpfXmlUtil.GetParStrValue(el, "pdmname"));
				svnurl = JpfXmlUtil.GetParStrValue(el, "pdmsvnurl").trim();// pdm的svn路径

				svnUser = JpfXmlUtil.GetParStrValue(el, "svnuser").trim();// 下载pdm时用户名
				svnPassword = JpfXmlUtil.GetParStrValue(el, "svnpwd").trim();// 下载pdm时密码
				cCompareInfo.setStrMails(JpfXmlUtil.GetParStrValue(el, "dbmails"));
				cCompareInfo.setStrCondName(JpfXmlUtil.GetParStrValue(el, "envname"));
				String pdmpathperf = downPdmPath + File.separator + JpfXmlUtil.GetParStrValue(el, "pdmname");

				if (versionOrDate == null || versionOrDate == "" || versionOrDate == " ")
				{
					logger.debug("please input version or date to compare different versions of PDM ");
				} else if (versionOrDate.contains("-"))
				{
					String[] date = versionOrDate.split(":");
					if (date.length == 2)
					{
						System.out.println("date compare");
						String dt = date[0];
						// 更新svn上的pdm到指定目录
						SvnInfoUtil.checkout(svnurl, downPdmPath, svnUser, svnPassword);
						SvnInfoUtil.updateByDate(svnurl, downPdmPath, JpfXmlUtil.GetParStrValue(el, "pdmname"), dt,
								svnUser, svnPassword);
						getTable1.setPDMPath(pdmpathperf);
						getTable1.getPdmMap();

						dt = date[1];
						SvnInfoUtil.updateByDate(svnurl, downPdmPath, JpfXmlUtil.GetParStrValue(el, "pdmname"), dt,
								svnUser, svnPassword);
						getTable2.setPDMPath(pdmpathperf);
						getTable2.getPdmMap();
						cCompareInfo.setDevJdbcUrl(dt);
						cCompareInfo.setPdmDtVers("日期比对");
						cCompareInfo.setPdmJdbcUrl(dt);
						// 删除从svn更新的pdm
						// SvnInfoUtil.RemovePdm(svnurl, pdmpath);
					}
				} else
				{
					String[] version = versionOrDate.split(":");
					if (version.length == 2)
					{
						String vs = version[0];
						System.out.println("version compare");
						// 更新svn上的pdm到指定目录
						SvnInfoUtil.checkout(svnurl, downPdmPath, svnUser, svnPassword);
						// 通过svnkit，更新到指定版本
						SvnInfoUtil.updateByVersion(svnurl, downPdmPath, JpfXmlUtil.GetParStrValue(el, "pdmname"), vs,
								svnUser, svnPassword);

						getTable1.setPDMPath(pdmpathperf);
						getTable1.getPdmMap();

						cCompareInfo.setPdmJdbcUrl(vs);
						vs = version[1];
						SvnInfoUtil.updateByVersion(svnurl, downPdmPath, JpfXmlUtil.GetParStrValue(el, "pdmname"), vs,
								svnUser, svnPassword);

						getTable2.setPDMPath(pdmpathperf);
						getTable2.getPdmMap();
						cCompareInfo.setDevJdbcUrl(vs);
						cCompareInfo.setPdmDtVers("版本比对");
					}

				}
			}
			// 获取一个pdm中用到过的Owners
			LinkedList<String> userlist1 = getTable1.getUserlist();

			LinkedList<String> userlist2 = getTable2.getUserlist();

			for (String user : userlist2)
			{
				if (!userlist1.contains(user))
				{

					userlist1.add(user);
				}
			}
			for (String domain : userlist1)
			{
				cCompareInfo.setDbDomain(domain);
				System.out.println("begin to compare domain: " + domain);
				try
				{

					// conn_develop = cDbDescInfo2.GetConn();

					// 带分表比对
					System.out.println(
							".....................................................................................................................");
					logger.info("Check sub table...");
					CompareSubTables cCompareSubTables = new CompareSubTables();
					cCompareInfo.setCompareType(1);
					cCompareSubTables.setDomain_table(getTable1.getDomainTable());
					cCompareSubTables.setDomain_table2(getTable2.getDomainTable());
					cCompareSubTables.DoCompare(null, null, cCompareInfo);
					cCompareSubTables = null;
					// 带分表比较索引
					System.out.println(
							".....................................................................................................................");
					logger.info("compare sub index...");
					CompareIndexSub cCompareIndexSub = new CompareIndexSub();
					cCompareInfo.setCompareType(2);
					cCompareIndexSub.setDomain_table(getTable1.getDomainIndex());
					cCompareIndexSub.setDomain_table2(getTable2.getDomainIndex());
					cCompareIndexSub.DoCompare(null, null, cCompareInfo);
					cCompareIndexSub = null;

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				} finally
				{

					// JpfDbUtils.DoClear(conn_develop);
				}

			}

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		System.out.println("FINISH AND  EXIT");
	}

	// 数据库与数据库比对
	public void DbDbCompare(String strConfigFileName)
	{
		// TODO Auto-generated constructor stub
		try
		{
			// read config
			// System.out.println(System.getProperty("user.dir"));
			// System.out.println(System.getProperty("java.class.path"));
			DbDescInfo cPdmDbDescInfo = null;
			CompareInfo cCompareInfo = new CompareInfo();

			JpfFileUtil.checkFile(strConfigFileName);
			NodeList nl = JpfXmlUtil.GetNodeList("dbsource", strConfigFileName);
			if (1 == nl.getLength())
			{
				Element el = (Element) nl.item(0);
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
				cCompareInfo.setAllMail(JpfXmlUtil.GetParStrValue(el, "dbmails"));

				cCompareInfo.setStrExcludeTable(JpfXmlUtil.GetParStrValue(el, "excludetable"));
				cCompareInfo.setStrCondName(JpfXmlUtil.GetParStrValue(el, "envname"));
				cCompareInfo.setDoExec(JpfXmlUtil.GetParStrValue(el, "doexecsql"));
				cCompareInfo.setPdmJdbcUrl((JpfXmlUtil.GetParStrValue(el, "dburl")));

				cCompareInfo.setPdmInfo(JpfXmlUtil.GetParStrValue(el, "svnurl"));
				if (cCompareInfo.getPdmInfo() != null)
				{
					cCompareInfo.setPdmInfo(SvnInfoUtil.GetSvnFileAuthorDate(cCompareInfo.getPdmInfo()));
				}
				logger.info("PDM:{}", cCompareInfo.getPdmJdbcUrl());
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				cPdmDbDescInfo = new DbDescInfo(cCompareInfo.getPdmJdbcUrl(), strDbUsr, strDbPwd);
			} else
			{
				logger.error("error source db info");
			}
			nl = JpfXmlUtil.GetNodeList("dbcompare", strConfigFileName);
			for (int j = 0; j < nl.getLength(); j++)
			{
				// System.out.println(nl.item(j).getNodeValue());
				Element el = (Element) nl.item(j);
				String strDbUsr = JpfXmlUtil.GetParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.GetParStrValue(el, "dbpwd");
				cCompareInfo.setDevJdbcUrl(JpfXmlUtil.GetParStrValue(el, "dburl"));
				cCompareInfo.setDbDomain(JpfXmlUtil.GetParStrValue(el, "dbdomain"));
				cCompareInfo.setStrMails(JpfXmlUtil.GetParStrValue(el, "dbmails") + "," + cCompareInfo.getAllMail());

				logger.info("DEV:{}", cCompareInfo.getDevJdbcUrl());
				logger.info(cCompareInfo.getDbDomain());
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				logger.info("mail to {}", cCompareInfo.getStrMails());

				NodeList nlParentChild = el.getElementsByTagName("parent_childen");

				// 获得需要进行特殊处理的分表——母表对应关系
				parent_child.clear();
				for (int i = 0; i < nlParentChild.getLength(); i++)
				{
					Node child = nlParentChild.item(i);
					if (child instanceof Element)
					{
						String s = child.getFirstChild().getNodeValue().toLowerCase().trim();
						String[] s1 = s.split(";");
						if (s1.length == 2)
						{
							parent_child.put(s1[1], s1[0]);
						}
					}
				}

				DbDescInfo cDbDescInfo2 = new DbDescInfo(cCompareInfo.getDevJdbcUrl(), strDbUsr, strDbPwd);

				Connection conn_pdm = null;
				Connection conn_develop = null;
				try
				{
					conn_pdm = cPdmDbDescInfo.GetConn();
					conn_develop = cDbDescInfo2.GetConn();

					// 先执行SQL到PDM
					NodeList nlPreSql = el.getElementsByTagName("presql");
					for (int i = 0; i < nlPreSql.getLength(); i++)
					{
						Node child = nlPreSql.item(i);
						if (child instanceof Element)
						{
							String strSql = child.getFirstChild().getNodeValue().toLowerCase().trim();
							JpfDbUtils.ExecUpdateSql(conn_pdm, strSql);
						}
					}

					// 带分表比对
					System.out.println(
							".....................................................................................................................");
					logger.info("Check sub table...");
					CompareSubTables cCompareSubTables = new CompareSubTables();
					cCompareInfo.setCompareType(1);
					cCompareSubTables.DoCompare(conn_pdm, conn_develop, cCompareInfo);

					// 带分表比较索引
					System.out.println(
							".....................................................................................................................");
					logger.info("compare sub index...");
					CompareIndexSub cCompareIndexSub = new CompareIndexSub();
					cCompareInfo.setCompareType(2);
					cCompareIndexSub.DoCompare(conn_pdm, conn_develop, cCompareInfo);

					// 检查是否母表是否存在
					// logger.info(".....................................................................................................................");
					// logger.info("CheckParentTableExist...");
					// logger.debug("conn_product.isClosed()="+conn_product.isClosed());
					// CheckParentTableExist cCheckParentTableExist = new
					// CheckParentTableExist();
					// cCheckParentTableExist.DoCheck(conn_product,strDomain);
					// cCheckParentTableExist.DoCheck(conn_develop,strDomain);

					// System.out.println(".....................................................................................................................");
					// System.out.println("CheckSameTableName...");
					// CheckSameTableName cCheckSameTableName = new
					// CheckSameTableName();
					// cCheckSameTableName.DoCheck(conn_product,strDomain);
					// cCheckSameTableName.DoCheck(conn_develop,strDomain);

					// System.out.println(".....................................................................................................................");
					// System.out.println("Compare Data...");
					// CompareData cCompareData = new CompareData();
					// cCompareData.DoCompare(conn_pdm, conn_develop,
					// strConfigFileName);

				} catch (Exception ex)
				{
					// TODO: handle exception
					ex.printStackTrace();
				} finally
				{
					JpfDbUtils.DoClear(conn_pdm);
					JpfDbUtils.DoClear(conn_develop);
				}
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}
		System.out.println("FINISH AND  EXIT");

	}

	private boolean deleteDir(File dir)
	{
		if (dir.isDirectory())
		{
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++)
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	public JpfDbCompare(String strConfigFileName, String executeWay, String versionOrDate)
	{
		try
		{
			JpfFileUtil.checkFile(strConfigFileName);
			File directory = new File("");// 参数为空
			String downPdmPath = directory.getCanonicalPath();
			downPdmPath = downPdmPath + File.separator + "downPdmFmSvn";
			File pdmDirectory = new File(downPdmPath);
			// 删除下载到本地的PDM文件
			// deleteDir(pdmDirectory);
			// strExecute: 1:执行数据库与数据库比对，2：执行pdm与数据库比对，3：pdm与pdm比对
			if (executeWay.trim().equals("1"))
			{
				logger.info("Database compare with Database");
				DbDbCompare(strConfigFileName);
			} else if (executeWay.trim().equals("2"))
			{
				logger.info("PDM compare with Database");

				PdmDbCompare(strConfigFileName, downPdmPath);
			} else if (executeWay.trim().equals("3"))
			{
				logger.info("PDM compare with PDM");
				PdmPdmCompare(strConfigFileName, downPdmPath, versionOrDate);
			} else
			{
				logger.info("error: the type of comparison command is error");
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 *            被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *            update 2015年2月14日
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (3 == args.length)
		{

			JpfDbCompare cJpfDbCompare = new JpfDbCompare(args[0], args[1], args[2]);
		}
		if (2 == args.length)
		{
			JpfDbCompare cJpfDbCompare = new JpfDbCompare(args[0], args[1], null);

		}
	}

}
