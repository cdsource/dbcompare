package org.jpf.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 * <p>
 * Title: 网上订单
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author 吴平福
 * @version 1.0
 */
public class StringUtil
{
	private static final Logger logger = LogManager.getLogger(StringUtil.class);
    /**
     * 关键字显示为红色
     * @return
     */
	public static String ShowKeyRed(String a,String b)
	{
		if (a==null || b==null)
			return "";
		String c="";
		int i=a.indexOf(b);

		if(i>=0)
		{
			c=a.substring(0,i)+"<font color='red'>"+b+"</font>"+a.substring(i+b.length(),a.length());

		}else
		{
			c=a;
		}
		return c;
	}
	public static String replaceStr(String strReplace, String strOld,
									String strNew)

	{
		if (strReplace == null)
		{
			return "";
		}
		StringBuffer stbReplace = new StringBuffer(strReplace);

		int iOldStrLen = strOld.length();

		Vector<Integer> vcFindStr = new Vector<Integer>();

		int iIndex = 0;

		while (true)

		{

			iIndex = strReplace.toString().indexOf(strOld, iIndex);

			if (iIndex < 0)

			{

				break;

			}

			vcFindStr.add(0, new Integer(iIndex));

			iIndex += 1;

		}

		for (int i = 0; i < vcFindStr.size(); i++)

		{

			Integer vIndex = (Integer) vcFindStr.get(i);

			int iBeginIndex = vIndex.intValue();

			int iEndIndex = iBeginIndex + iOldStrLen;

			stbReplace = stbReplace.replace(iBeginIndex, iEndIndex, strNew);

		}

		return stbReplace.toString();

	}


	/**
	 * 通过字符串转换成相应的整型，并返回。
	 * 
	 * @param strValue
	 *            String 待转换的字符串
	 * @return int 转换完成的整型
	 * */
	public static int getStrToInt(String strValue)
	{
		if (null == strValue)
		{
			return 0;
		}

		int iValue = 0;

		try
		{
			iValue = new Integer(strValue.trim()).intValue();
		} catch (Exception ex)
		{
			iValue = 0;
		}

		return iValue;
	}

	/**
	 * 得到非空的字符串，若字符串为null，则返回""。
	 * 
	 * @param strValue
	 *            String待转换的原字符串
	 * @return String 转换后的字符串
	 * */
	public static String getNotNullStr(String strValue)
	{
		return (strValue == null ? "" : strValue.trim());
	}

	/**
	 * 得到非空的字符串，若字符串对象为null，则返回""。
	 * 
	 * @param objValue
	 *            Object待转换的原字符串对象
	 * @return String 转换后的字符串
	 * */
	public static String getNotNullStr(Object objValue)
	{
		return (objValue == null ? "" : getNotNullStr(objValue.toString()));
	}

	/**
	 * 把传入的字符串分割为数组格式
	 * 
	 * @param str
	 *            传入的字符串
	 * 
	 *            * @param x 间隔符
	 * 
	 * @return String
	 * 
	 * */

	public static String[] split(String str, String x)
	{

		if (str == null)

		{

			return null;

		}

		if (x == null)

		{

			return null;

		}

		Vector<String> v = new Vector<String>();

		StringTokenizer stToken = new StringTokenizer(str, x);

		int iIndex = 0;

		while (stToken.hasMoreTokens())

		{

			String strToken = stToken.nextToken();

			v.add(iIndex++, strToken);

		}

		String[] seqResult = new String[v.size()];

		for (int i = 0; i < v.size(); i++)

		{

			String strTemp = (String) v.get(i);

			seqResult[i] = strTemp;

		}

		return seqResult;

	}

	/**
	 * 把传入的字符串分割为数组格式
	 * 
	 * @param str
	 *            传入的字符串
	 * 
	 *            * @param x 间隔符
	 * 
	 * @return String
	 * 
	 * */

	public static String[] split(String str, char x)
	{

		StringBuffer sbSplit = new StringBuffer(x);

		return split(str, sbSplit.toString());

	}

	/**
	 * 返回第一个值 aa;bb 返回aa
	 * 
	 * @param str
	 *            String
	 * @param x
	 *            String
	 * @return String
	 */
	public static String getFirstPosStr(String str, String x)
	{
		int i = str.indexOf(x);
		if (i > 0)
		{
			str = str.substring(0, i);
		}
		return str;
	}

	public static void main(String[] args)
	{
		String s="BUG跟踪单\n\nfrom 陈明璐 to 佟宇:\n\n       Bug编号:     189310\n       标题:        【歌华现场-现场测试-iBossBug】客服在数据没有配置动态模板数据的时候页面500错误，配置信心中data_source读取异常\n       Bug级别:     一般\n       Bug类型:     正常值\n       紧急程度:      紧急\n       阶段:        功能测试\n       Bug来源:     QA测试\n       建档时间:    2011-12-26  01:17:18\n       建档人:      佟宇\n       测试人员:    佟宇\n       DEV受理人:   陈明璐\n       问题描述:    \n【歌华现场-现场测试-iBossBug】客服在数据没有配置动态模板数据的时候页面500错误，配置信心中data_source读取异常，报空指针\n       最新回复:    １. 错误原因：\r\n   数据没有配置报错，前台没有判空\r\n２．修改方案／修复处理逻辑：　\r\n　　　　<若是很简单的界面地址错误等，则可忽略>\r\n   已增加判空操作，增加静态数据时，code_value中的字段都需要按照模板配置，可设置其值为 ,\r\n但必须存在该字段\r\n\r\n３．修改涉及文件／公共函数／ＯＢＤ接口／ＤＢ／参数：\r\n\r\n４．是否自测通过\r\n   是\r\n５．修复功能适用于统一版 [或ＸＸ专版]\r\n\n\n\n\n快速链接：http://aiqcs.asiainfo-linkage.com/bug/query/bug_query_result.jsp?op_id=189310\n登录PCS系统：http://10.10.10.158/";
		//System.out.println(ReplaceChinaStr(s,"登录PCS系统",""));
		String b="登录PCS系统：http://10.10.10.158/";
		s=s.trim();
		if (s.endsWith(b))
		{
			s=s.substring(0,s.length()-b.length());
		}
		System.out.println(s);
	}
	public static String HtmlEncode(String str)
	{
		str = str.replace(">","&lt;"); 
		str = str.replace(">","&lt;");
		return str;
	}
	public static String htmEncode(String s)
	{
		StringBuffer stringbuffer = new StringBuffer();
		int j = s.length();
		for (int i = 0; i < j; i++)
		{
			char c = s.charAt(i);
			switch (c)
			{
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && s.charAt(i + 1) == 10)
				{
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && s.charAt(i + 1) == ' ')
				{
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}
	/**
	 * 删除重复内容
	 * @param sb
	 */
	public static void DelRepeat(StringBuffer sb)
	{
		String stra = sb.toString();
		String[] arrs = stra.split(",");
		List<String> list = new LinkedList<String>();
		sb.setLength(0);
		sb.append("0");
		for (int i = 0; i < arrs.length; i++)
		{
			if(arrs[i]!=null & !"".equalsIgnoreCase(arrs[i]))
			{
			if (!list.contains(arrs[i]))
			{
				list.add(arrs[i]);
				sb.append(",").append(arrs[i]);
			}
			}
		}
		list.clear();

	}
	/**
	 * 获取字符长度
	 * @param s
	 * @return
	 */
	public  static int GetStringCount(String s)
    {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }
	
	public static String ReplaceChinaStr(String strOld,String regex,String replacement)
	{
		try
		{
			//regex=new String(regex.getBytes("UTF8"),"gb2312");
			regex=new String(regex.getBytes("gb2312"),"unicode");
			System.out.println(regex);
			strOld=new String(strOld.getBytes("gb2312"),"unicode");
			replacement=new String(replacement.getBytes("gb2312"),"unicode");
			strOld=strOld.replaceAll(regex, replacement);
			return new String(strOld.getBytes("unicode"),"gb2312");
		} catch (Exception ex)
		{
			// TODO: handle exception
		}
		
		return "";
	}
	
    public static String getEncoding(String str) {   
        String encode = "GB2312";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s = encode;   
                return s;   
            }   
        } catch (Exception exception) {   
        }   
        encode = "ISO-8859-1";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s1 = encode;   
                return s1;   
            }   
        } catch (Exception exception1) {   
        }   
        encode = "ISO-8859-1";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s1 = encode;   
                return s1;   
            }   
        } catch (Exception exception1) {   
        } 
        encode = "UTF-8";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s2 = encode;   
                return s2;   
            }   
        } catch (Exception exception2) {   
        }   
        encode = "GBK";   
        try {   
            if (str.equals(new String(str.getBytes(encode), encode))) {   
                String s3 = encode;   
                return s3;   
            }   
        } catch (Exception exception3) {   
        }   
        return "";   
    } 
}
