package org.jpf.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author wupf@asiainfo.com
 * @version 1.0
 */
public class JpfDateTimeUtil
{
	private static final Logger logger = LogManager.getLogger();

	/**
	 *
	 * @return String
	 */
	public static String GetCurrFormattedDateTime(String strFormatTo)
	{
		java.sql.Timestamp date = new java.sql.Timestamp(System.
				currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(strFormatTo);
		return formatter.format(date);
	}

	/**
	 * 得到当前日期，格式yyyy-MM-dd。
	 * 
	 * @return String 格式化的日期字符串
	 */
	public static String GetToday()
	{
		Date cDate = new Date();

		SimpleDateFormat cSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return cSimpleDateFormat.format(cDate);
	}

	public static String GetToday(String strFormat)
	{
		Date cDate = new Date();

		SimpleDateFormat cSimpleDateFormat = new SimpleDateFormat(strFormat);
		return cSimpleDateFormat.format(cDate);
	}

	/**
	 * 
	 * @param strFormat
	 * @param iDays
	 * @return update 2012-9-7
	 */
	public static String GetDay(String strFormat, int iDays)
	{
		Date cDate = new Date();
		cDate = AddDate(cDate, iDays);
		SimpleDateFormat cSimpleDateFormat = new SimpleDateFormat(strFormat);
		return cSimpleDateFormat.format(cDate);
	}

	/**
	 * 
	 * @param date
	 *            Date
	 * @param days
	 *            int
	 * @return Date
	 */
	public static Date AddDate(Date date, int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		long lTmp = c.getTimeInMillis();
		c.setTimeInMillis(lTmp + ((long) days) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 *
	 * @param dtDate
	 *            Date
	 * @param strFormatTo
	 *            String
	 * @return String
	 */
	public static String GetFormattedDateUtil(java.util.Date dtDate,
			String strFormatTo)
	{
		if (dtDate == null)
		{
			return "";
		}
		strFormatTo = strFormatTo.replace('/', '-');
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat(strFormatTo);
			return formatter.format(dtDate);
		} catch (Exception e)
		{
			System.out.println("转换日期字符串格式时出错;" + e.getMessage());
			return "";
		}
	}

	/**
	 * @todo 时间加月
	 * @param month
	 *            int
	 * @return String
	 */
	public static String AddMonth(int month)
	{ // 时间加月

		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMM");
		cal.add(Calendar.MONTH, month);
		sDateFormat.format(cal.getTime());
		System.out.println(sDateFormat.format(cal.getTime()));
		return sDateFormat.format(cal.getTime());
	}

	/**
	 * @todo 时间加月
	 * @param inDate
	 *            String
	 * @param month
	 *            int
	 * @return String
	 */
	public static String AddMonth(String inDate, int month)
	{ // 时间加月

		Calendar clFrom = new GregorianCalendar();
		int iYear = Integer.parseInt(inDate.substring(0, 4));
		int iMonth = Integer.parseInt(inDate.substring(4, 6));
		int iDay = 1;

		clFrom.set(iYear, iMonth, iDay, 0, 0, 0);
		java.text.SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMM");
		clFrom.add(Calendar.MONTH, month - 1);
		// sDateFormat.format(cal.getTime());
		System.out.println(sDateFormat.format(clFrom.getTime()));
		return sDateFormat.format(clFrom.getTime());
	}

	/**
	 * 得到当前日期，格式yyyy-MM-dd。
	 * 
	 * @return String 格式化的日期字符串
	 */

	public static String GetCurrDate()
	{
		return GetFormattedDate(GetDateByString(""));
	}

	/**
	 * 对输入的日期字符串按照默认的格式yyyy-MM-dd转换.
	 * 
	 * @param strDate
	 *            String 需要进行格式化的日期字符串
	 * 
	 * @return String 经过格式化后的字符串
	 */

	public static String GetFormattedDate(String strDate)

	{

		return GetFormattedDate(strDate, "yyyy-MM-dd");

	}

	/**
	 * 对输入的日期字符串进行格式化,如果输入的是0000/00/00 00:00:00则返回空串.
	 * 
	 * @param strDate
	 *            String 需要进行格式化的日期字符串
	 * 
	 * @param strFormatTo
	 *            String 要转换的日期格式
	 * 
	 * @return String 经过格式化后的字符串
	 */

	public static String GetFormattedDate(String strDate, String strFormatTo)

	{
		if (strDate == null || strDate.trim().equals(""))
		{
			return "";
		}

		strDate = strDate.replace('/', '-');
		strFormatTo = strFormatTo.replace('/', '-');
		if (strDate.equals("0000-00-00 00:00:00") ||
				strDate.equals("1800-01-01 00:00:00"))
		{
			return "";
		}

		String formatStr = strFormatTo; // "yyyyMMdd";
		if (strDate == null || strDate.trim().equals(""))
		{
			return "";
		}

		switch (strDate.trim().length())
		{
		case 6:
			if (strDate.substring(0, 1).equals("0"))
			{
				formatStr = "yyMMdd";
			} else
			{

				formatStr = "yyyyMM";

			}

			break;

		case 8:

			formatStr = "yyyyMMdd";

			break;

		case 10:

			if (strDate.indexOf("-") == -1)
			{

				formatStr = "yyyy/MM/dd";

			} else
			{

				formatStr = "yyyy-MM-dd";

			}

			break;

		case 11:

			if (strDate.getBytes().length == 14)
			{

				formatStr = "yyyy年MM月dd日";

			} else
			{

				return "";

			}

		case 14:

			formatStr = "yyyyMMddHHmmss";

			break;

		case 19:

			if (strDate.indexOf("-") == -1)
			{

				formatStr = "yyyy/MM/dd HH:mm:ss";

			} else
			{

				formatStr = "yyyy-MM-dd HH:mm:ss";

			}

			break;

		case 21:

			if (strDate.indexOf("-") == -1)
			{

				formatStr = "yyyy/MM/dd HH:mm:ss.S";

			} else
			{

				formatStr = "yyyy-MM-dd HH:mm:ss.S";

			}

			break;

		default:

			return strDate.trim();

		}

		try
		{

			SimpleDateFormat formatter = new SimpleDateFormat(formatStr);

			Calendar calendar = Calendar.getInstance();

			calendar.setTime(formatter.parse(strDate));

			formatter = new SimpleDateFormat(strFormatTo);

			return formatter.format(calendar.getTime());

		} catch (Exception e)
		{
			System.out.println("转换日期字符串格式时出错;" + e.getMessage());
			return "";
		}

	}

	/**
	 * 对输入的日期按照默认的格式yyyy-MM-dd转换.
	 * 
	 * @param strDate
	 *            java.sql.Timestamp 需要进行格式化的日期字符串
	 * 
	 * @return String 经过格式化后的字符串
	 */

	public static String GetFormattedDate(java.sql.Timestamp dtDate)

	{

		return GetFormattedDate(dtDate, "yyyy-MM-dd");

	}

	/**
	 * 对输入的日期进行格式化, 如果输入的日期是null则返回空串.
	 * 
	 * @param dtDate
	 *            java.sql.Timestamp 需要进行格式化的日期字符串
	 * 
	 * @param strFormatTo
	 *            String 要转换的日期格式
	 * 
	 * @return String 经过格式化后的字符串
	 */

	public static String GetFormattedDate(java.sql.Timestamp dtDate,
			String strFormatTo)

	{

		if (dtDate == null)

		{

			return "";

		}

		if (dtDate.equals(new java.sql.Timestamp(0)))

		{

			return "";

		}

		strFormatTo = strFormatTo.replace('/', '-');

		try

		{

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy");

			if (Integer.parseInt(formatter.format(dtDate)) < 1900)

			{

				return "";

			}

			else

			{

				formatter = new SimpleDateFormat(strFormatTo);

				return formatter.format(dtDate);

			}

		}

		catch (Exception e)
		{

			System.out.println("转换日期字符串格式时出错;" + e.getMessage());

			return "";

		}

	}

	/**
	 * 得到当前日期时间,格式为yyyy-MM-dd hh:mm:ss.
	 * 
	 * @return String
	 */

	public static String GetCurrDateTime()
	{
		java.sql.Timestamp date = new java.sql.Timestamp(System.
				currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	/**
	 * 得到一个月的最后一工作日
	 * 
	 * @param strDateStart
	 *            String
	 * @throws Exception
	 * @return Date
	 */
	public static Date GetLastWorkDayofMonth(String strDateStart) throws
			Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date_start = sdf.parse(strDateStart);
		Calendar cal_start = Calendar.getInstance();
		cal_start.setTime(date_start);

		int dayOfWeek = cal_start.get(Calendar.DAY_OF_WEEK) - 1;
		Date a = new Date();
		if (dayOfWeek == 1)
		{
			a = AddDate(date_start, -3);
		} else if (dayOfWeek == 7)
		{
			a = AddDate(date_start, -2);
		} else
		{
			a = AddDate(date_start, -1);
		}
		// System.out.println(GetFormattedDateUtil(a, "yyyy-MM-dd"));
		return a;
	}

	/**
	 * 判断是否是最后一个工作日
	 * 
	 * @param date_start
	 *            Date
	 * @throws Exception
	 * @return boolean
	 */
	public static boolean IsLastWorkDayofMonth(Date date_start) throws Exception
	{
		// System.out.println(getFormattedDateUtil(date_start, "yyyy-MM-dd"));
		String a = GetFormattedDateUtil(date_start, "yyyyMM");
		a = AddMonth(a, 1);
		a += "01";
		System.out.println(a);
		Date b = GetLastWorkDayofMonth(a);
		if (date_start.before(b))
		{
			return true;
		}
		return false;
	}

	public static String GetCurrentHour()
	{
		java.sql.Timestamp date = new java.sql.Timestamp(System.
				currentTimeMillis());
		SimpleDateFormat formatter = new SimpleDateFormat("HH");
		return formatter.format(date);
	}

	public static String GetFormattedDateTime(String strDate)

	{
		if (strDate == null || "".equalsIgnoreCase(strDate))
		{
			return "";
		}
		if (strDate.length() > 2)
		{
			return strDate.substring(0, strDate.length() - 2);
		}
		return strDate;
	}

	/**
	 * 根据传入的日期字符串转换成相应的日期对象，如果字符串为空或不符合日期格
	 * 
	 * 式，则返回当前时间。
	 * 
	 * @param strDate
	 *            String 日期字符串
	 * 
	 * @return java.sql.Timestamp 日期对象
	 * 
	 * */

	public static java.sql.Timestamp GetDateByString(String strDate)
	{

		if (strDate.trim().equals(""))

		{

			return new java.sql.Timestamp(System.currentTimeMillis());

		}

		try

		{

			strDate = GetFormattedDate(strDate, "yyyy-MM-dd HH:mm:ss") +
					".000000000";

			return java.sql.Timestamp.valueOf(strDate);

		}

		catch (Exception ex)
		{
			System.out.println(ex.getMessage());

			return new java.sql.Timestamp(System.currentTimeMillis());

		}

	}

	public static void main(String[] args)
	{
		// System.out.println(GetDay("yyMMdd", -1));
	}

	public static String GetDay(int month)
	{
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		cal.add(Calendar.MONTH, month);
		sDateFormat.format(cal.getTime());
		System.out.println(sDateFormat.format(cal.getTime()));
		return sDateFormat.format(cal.getTime());
	}

	/**
	 * @todo 时间比较
	 * @param DATE1
	 * @param DATE2
	 * @return update 2014年3月13日
	 */
	public static int Compare_date(String DATE1, String DATE2)
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{

			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime())
			{
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime())
			{
				// System.out.println("dt1在dt2后");
				return -1;
			} else
			{
				return 0;
			}
		} catch (Exception exception)
		{
			logger.error("DATE1:{}", DATE1);
			logger.error("DATE2:{}", DATE2);
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * @todo 返回时间差值天数
	 * @param strStartDate
	 * @param strEndDate
	 * @return
	 * @throws Exception
	 *             被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注：
	 *             update 2014年3月22日
	 */
	public static long GetDays(String strStartDate, String strEndDate) throws Exception
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(strStartDate);
		Date d2 = df.parse(strEndDate);
		long diff = d2.getTime() - d1.getTime();
		return diff / (1000 * 60 * 60 * 24);

	}

	public static java.util.Date AddUtilDate(String strDate, int iDays) throws Exception
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(strDate);
		return AddDate(d1, iDays);
	}

	public static java.sql.Date AddSqlDate(String strDate, int iDays) throws Exception
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(strDate);
		return new java.sql.Date(AddDate(d1, iDays).getTime());
	}

	public static java.sql.Date GetSqlDate() throws Exception
	{
		Date cDate = new Date();
		return new java.sql.Date(cDate.getTime());
	}
}
