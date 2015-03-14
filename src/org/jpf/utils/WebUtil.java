package org.jpf.utils;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <p>
 * Title: WBASS
 * </p>
 * <p>
 * Description: WBASS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: asiainfo
 * </p>
 * 
 * @author 吴平福
 * @version 2.0
 */
public class WebUtil {
	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	/*
	 * public static String getRequestErrorInfo(HttpServletRequest request) {
	 * 
	 * return (String) request.getAttribute(ConstUtil.
	 * APPLICATION_ERROR_MESSAGE_KEY);
	 * 
	 * }
	 */
	/**
	 * @deprecated 根据request对象和参数名称得到参数的字符串值
	 *@param request
	 *            HttpServletRequest
	 *@param strParamName
	 *            String 参数名称
	 *@return String 参数的字符串值
	 * */

	public static String getStrParamter(HttpServletRequest request,
			String strParamName)

	{

		String strParamValue = request.getParameter(strParamName);

		if (null == strParamValue) {
			strParamValue = "";
		}

		return strParamValue;

	}

	/**
	 * 根据业务调用获得的错误信息对象，在servlet中重定向到错误信息页，显示错误
	 * 
	 * 信息提示，以及指定在错误信息页中确定后的下一页url。
	 * 
	 * @param request
	 *            HttpServletRequest
	 * 
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @param cErrorMsg
	 *            CBSErrorMsg 从业务调用中获得的错误对象
	 * 
	 * @param iGuiType
	 *            int 错误信息页中显示的按钮类型，1，返回；2，确定（重定向到其他页）
	 * 
	 * @param strActionUrl
	 *            String 重定向到其他页的url，该参数需要iGuiType=2
	 */

	public static void set_commonErr(HttpServletRequest request,
			HttpServletResponse response, String sErrMsg, int iGuiType,
			String strActionUrl)

	{

		String strGuiType = "返回";

		if (iGuiType == 1) {

			strGuiType = "确定";

		}

		else {
			if (iGuiType == 2)

			{

				strGuiType = "返回";

			}
		}

		request.getSession().setAttribute("common_errGuiType", strGuiType);

		request.getSession().setAttribute("common_errAction", strActionUrl);

		request.getSession().setAttribute("common_errMsg", sErrMsg);

		try

		{

			// String strUrl =
			// "common_errorPage.jsp?iGuiType="+Integer.toString(iGuiType)+"errAction="+strActionUrl
			// ;

			request.getRequestDispatcher("/commons/common_errorPage.jsp")
					.forward(request, response);

		}

		catch (ServletException ex)

		{

			ex.printStackTrace();

		}

		catch (IOException iex)

		{

			iex.printStackTrace();

		}

	}

	public static void set_commonErr(HttpServletRequest request,
			HttpServletResponse response, String sErrMsg, String sErrDetail,
			int iGuiType, String strActionUrl)

	{

		String strGuiType = "返回";

		if (iGuiType == 1) {

			strGuiType = "确定";

		}

		else {
			if (iGuiType == 2)

			{

				strGuiType = "返回";

			}
		}

		request.getSession().setAttribute("common_errGuiType", strGuiType);
		request.getSession().setAttribute("common_errAction", strActionUrl);
		request.getSession().setAttribute("common_errMsg", sErrMsg);
		request.getSession().setAttribute("common_errMsgDetail", sErrDetail);
		try

		{

			// String strUrl =
			// "common_errorPage.jsp?iGuiType="+Integer.toString(iGuiType)+"errAction="+strActionUrl
			// ;

			request.getRequestDispatcher("/commons/common_errorPage.jsp")
					.forward(request, response);

		}catch (ServletException ex)
		{
			ex.printStackTrace();
		}catch (IOException iex)
		{
			iex.printStackTrace();
		}

	}

	public static void set_commonErr(HttpServletRequest request,
			HttpServletResponse response, String strErrMsg)

	{

		set_commonErr(request, response, strErrMsg, 2,
				"javascript:history.back()");

		return;

	}

	/**
	 * @todo REQUEST 取中文字符串
	 * @param request
	 *            HttpServletRequest
	 * @param requstStr
	 *            String
	 * @throws Exception
	 * @return String
	 */
	public static String getRequestForChinaString(HttpServletRequest request,
			String strRequest) throws Exception {
		String sStr = request.getParameter(strRequest);
		sStr = (sStr == null) ? "" : sStr.trim();
		return new String(sStr.getBytes("iso8859-1"), "gbk");
	}

	/**
	 * @todo REQUEST 取int
	 * @param request
	 *            HttpServletRequest
	 * @param requestStr
	 *            String
	 * @throws Exception
	 * @return String
	 */
	public static int getRequestForInt(HttpServletRequest request,
			String strRequest) {
		return Integer.parseInt(request.getParameter(strRequest));
	}

	public static long getRequestForLong(HttpServletRequest request,
			String strRequest) throws Exception {
		return Long.parseLong(request.getParameter(strRequest));
	}

	/**
	 * @todo REQUEST 取double
	 * @param request
	 *            HttpServletRequest
	 * @param requestStr
	 *            String
	 * @throws Exception
	 * @return String
	 */
	public static double getRequestForDouble(HttpServletRequest request,
			String strRequest) throws Exception {
		return Double.parseDouble(request.getParameter(strRequest));
	}

}
