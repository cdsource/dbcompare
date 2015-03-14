/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-4-5 下午1:40:12 
 * 类说明 
 */

package org.jpf.utils;

/**
 *
 */
public final class JpfStringUtil
{
	/**
    *
    */
	private static JpfStringUtil instance = new JpfStringUtil();

	/**
     *
     */
	private JpfStringUtil()
	{

	}

	/**
	 * 
	 * @return instance
	 */
	public static synchronized JpfStringUtil getInstance()
	{
		if (instance == null)
		{
			instance = new JpfStringUtil();
		}
		return instance;
	}

	private final String SPLIT_STRING = ",";

	/**
	 * 
	 * @param inString
	 *            规整前数据
	 * @return 规整数据
	 */
	public String GetRuleSplitString(final String inString)
	{
		StringBuffer sBuffer = new StringBuffer();

		if (inString == null)
		{
			sBuffer.append("0");
		} else
		{
			String[] tmpStrings = inString.split(SPLIT_STRING);
			for (int i = 0; i < tmpStrings.length; i++)
			{
				if (!tmpStrings[i].equalsIgnoreCase("") && IsNumber(tmpStrings[i]))
				{
					sBuffer.append(tmpStrings[i]).append(SPLIT_STRING);
				}
			}
			if (sBuffer.length() > 0)
			{
				sBuffer.deleteCharAt(sBuffer.length() - SPLIT_STRING.length());
			}

		}
		return sBuffer.toString();
	}

	/**
	 * 
	 * @param stra
	 *            input
	 * @return is number
	 */
	public static boolean IsNumber(final String stra)
	{
		try
		{
			if (stra == null || "".equalsIgnoreCase(stra))
			{
				return false;
			}
			Long.parseLong(stra);
		} catch (Exception ex)
		{
			return false;
		}
		return true;
	}

	/**
	 * 字符串经过从字符集iso-8859-1到字符集gb2312的转换，所有从字符集为
	 * iso-8859-1的系统中取得的字符串都必须经过这样的转换才能正常显示在 jsp页面上，否则中文显示会有乱码出现。
	 * 
	 * @param str
	 *            String 待转换的字符串
	 * @return String 转换过的字符串
	 */
	public static String GetCNString(String str)
	{
		if (str == null || "".equals(str))
		{
			return "";
		}

		try
		{
			return new String(str.trim().getBytes("ISO-8859-1"), "GB2312");
		} catch (Exception ex)
		{
		}

		return "";
	}

	private static final boolean IsChinese(char c)
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS)
		{
			return true;
		}
		return false;
	}
   /**
    * @todo:判断是否有中文 
    * @param strName
    * @return
    * update 2012-8-13
    */
	public static final boolean IsChinese(String strName)
	{
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++)
		{
			char c = ch[i];
			if (IsChinese(c))
			{
				return true;
			}
		}
		return false;
	}

}
