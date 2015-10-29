/** 
<<<<<<< HEAD
* @author 吴平福
* E-mail:wupf@asiainfo.com 
* @version 创建时间：2015年4月7日 下午10:45:47 
* 类说明 
=======
* @author ��ƽ�� 
* E-mail:wupf@asiainfo.com 
* @version ����ʱ�䣺2015��4��7�� ����10:45:47 
* ��˵�� 
>>>>>>> origin/master
*/ 

package org.jpf.ci.dbs.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Vector;
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
public class LoadSqlToDB
{
	private static final Logger logger = LogManager.getLogger();
	private final String DBINFOFILE="importdbinfo.xml";
	{
		
	}
	/**
	 * 
	 */
	public LoadSqlToDB(String strSqlFilePath)
	{
		// TODO Auto-generated constructor stub
		Connection conn=null;
		try
		{
			DbDescInfo cDbDescInfo=null;
			JpfFileUtil.checkFile(DBINFOFILE);
<<<<<<< HEAD
			NodeList nl = JpfXmlUtil.getNodeList("dbsource", DBINFOFILE);
=======
			NodeList nl = JpfXmlUtil.GetNodeList("dbsource", DBINFOFILE);
>>>>>>> origin/master
			logger.debug(nl.getLength());
			if(1==nl.getLength())
			{
				Element el = (Element) nl.item(0);
				String strJdbcUrl = JpfXmlUtil.getParStrValue(el, "dburl");
				String strDbUsr = JpfXmlUtil.getParStrValue(el, "dbusr");
				String strDbPwd = JpfXmlUtil.getParStrValue(el, "dbpwd");
				logger.debug(strJdbcUrl);
				logger.debug(strDbUsr);
				logger.debug(strDbPwd);
				cDbDescInfo = new DbDescInfo(strJdbcUrl, strDbUsr, strDbPwd);
			}else {
				logger.error("error source db info");
			}
			conn=cDbDescInfo.getConn();
			Vector<String> v_File=new Vector<String>();
			JpfFileUtil.getFiles(strSqlFilePath, v_File);
			for(String strSqlFile : v_File )
			{
				readSQLFile(conn,strSqlFile);
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}finally
		{
			JpfDbUtils.doClear(conn);
		}
	}

		/**
	 * @param args
<<<<<<< HEAD
	 * 被测试类名：TODO
	 * 被测试接口名:TODO
	 * 测试场景：TODO
	 * 前置参数：TODO
	 * 入参：
	 * 校验值：
	 * 测试备注：
	 * update 2015年4月7日
=======
	 * ������������TODO
	 * �����Խӿ���:TODO
	 * ���Գ�����TODO
	 * ǰ�ò�����TODO
	 * ��Σ�
	 * У��ֵ��
	 * ���Ա�ע��
	 * update 2015��4��7��
>>>>>>> origin/master
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		if (1==args.length)
		{
		LoadSqlToDB cLoadSqlToDB=new LoadSqlToDB(args[0]);
		}
	}
 
    public void readSQLFile(Connection conn ,String strSqlFileName) {  
    	BufferedReader bufferedReader=null;
        try {  
    		File f = new File(strSqlFileName);
    		
    		 bufferedReader = new BufferedReader(new FileReader(f));
            StringBuilder sBuilder = new StringBuilder("");  
            String str = bufferedReader.readLine();  
            while (str != null) {  
<<<<<<< HEAD
                // 去掉一些注释，和一些没用的字符  
                if (!str.startsWith("#") && !str.startsWith("/*")  
                        && !str.startsWith("–") && !str.startsWith("\n"))  
=======
                // ȥ��һЩע�ͣ���һЩû�õ��ַ�  
                if (!str.startsWith("#") && !str.startsWith("/*")  
                        && !str.startsWith("�C") && !str.startsWith("\n"))  
>>>>>>> origin/master
                    sBuilder.append(str);  
                str = bufferedReader.readLine();  
            }  
            String[] strArr = sBuilder.toString().split(";");  

            for (String strSql : strArr) {  

                logger.debug(strSql);
                JpfDbUtils.execUpdateSql(conn, strSql);
            }  
<<<<<<< HEAD
            // 创建数据连接对象，下面的DBConnection是我的一个JDBC类  
=======
            // �����������Ӷ��������DBConnection���ҵ�һ��JDBC��  
>>>>>>> origin/master

        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                bufferedReader.close();  

            } catch (Exception e) {  
            }  
        }  

    }


}
