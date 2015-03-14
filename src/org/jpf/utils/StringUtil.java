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
 * Title: ���϶���
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
 * @author ��ƽ��
 * @version 1.0
 */
public class StringUtil
{
	private static final Logger logger = LogManager.getLogger(StringUtil.class);
    /**
     * �ؼ�����ʾΪ��ɫ
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
	 * ͨ���ַ���ת������Ӧ�����ͣ������ء�
	 * 
	 * @param strValue
	 *            String ��ת�����ַ���
	 * @return int ת����ɵ�����
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
	 * �õ��ǿյ��ַ��������ַ���Ϊnull���򷵻�""��
	 * 
	 * @param strValue
	 *            String��ת����ԭ�ַ���
	 * @return String ת������ַ���
	 * */
	public static String getNotNullStr(String strValue)
	{
		return (strValue == null ? "" : strValue.trim());
	}

	/**
	 * �õ��ǿյ��ַ��������ַ�������Ϊnull���򷵻�""��
	 * 
	 * @param objValue
	 *            Object��ת����ԭ�ַ�������
	 * @return String ת������ַ���
	 * */
	public static String getNotNullStr(Object objValue)
	{
		return (objValue == null ? "" : getNotNullStr(objValue.toString()));
	}

	/**
	 * �Ѵ�����ַ����ָ�Ϊ�����ʽ
	 * 
	 * @param str
	 *            ������ַ���
	 * 
	 *            * @param x �����
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
	 * �Ѵ�����ַ����ָ�Ϊ�����ʽ
	 * 
	 * @param str
	 *            ������ַ���
	 * 
	 *            * @param x �����
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
	 * ���ص�һ��ֵ aa;bb ����aa
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
		String s="BUG���ٵ�\n\nfrom ����� to ١��:\n\n       Bug���:     189310\n       ����:        ���軪�ֳ�-�ֳ�����-iBossBug���ͷ�������û�����ö�̬ģ�����ݵ�ʱ��ҳ��500��������������data_source��ȡ�쳣\n       Bug����:     һ��\n       Bug����:     ����ֵ\n       �����̶�:      ����\n       �׶�:        ���ܲ���\n       Bug��Դ:     QA����\n       ����ʱ��:    2011-12-26  01:17:18\n       ������:      ١��\n       ������Ա:    ١��\n       DEV������:   �����\n       ��������:    \n���軪�ֳ�-�ֳ�����-iBossBug���ͷ�������û�����ö�̬ģ�����ݵ�ʱ��ҳ��500��������������data_source��ȡ�쳣������ָ��\n       ���»ظ�:    ��. ����ԭ��\r\n   ����û�����ñ���ǰ̨û���п�\r\n�����޸ķ������޸������߼�����\r\n��������<���Ǻܼ򵥵Ľ����ַ����ȣ���ɺ���>\r\n   �������пղ��������Ӿ�̬����ʱ��code_value�е��ֶζ���Ҫ����ģ�����ã���������ֵΪ ,\r\n��������ڸ��ֶ�\r\n\r\n�����޸��漰�ļ��������������ϣ£Ľӿڣ��ģ£�������\r\n\r\n�����Ƿ��Բ�ͨ��\r\n   ��\r\n�����޸�����������ͳһ�� [��أ�ר��]\r\n\n\n\n\n�������ӣ�http://aiqcs.asiainfo-linkage.com/bug/query/bug_query_result.jsp?op_id=189310\n��¼PCSϵͳ��http://10.10.10.158/";
		//System.out.println(ReplaceChinaStr(s,"��¼PCSϵͳ",""));
		String b="��¼PCSϵͳ��http://10.10.10.158/";
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
	 * ɾ���ظ�����
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
	 * ��ȡ�ַ�����
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
