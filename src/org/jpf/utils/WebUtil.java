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
 * @author ��ƽ��
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
	 * @deprecated ����request����Ͳ������Ƶõ��������ַ���ֵ
	 *@param request
	 *            HttpServletRequest
	 *@param strParamName
	 *            String ��������
	 *@return String �������ַ���ֵ
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
	 * ����ҵ����û�õĴ�����Ϣ������servlet���ض��򵽴�����Ϣҳ����ʾ����
	 * 
	 * ��Ϣ��ʾ���Լ�ָ���ڴ�����Ϣҳ��ȷ�������һҳurl��
	 * 
	 * @param request
	 *            HttpServletRequest
	 * 
	 * @param response
	 *            HttpServletResponse
	 * 
	 * @param cErrorMsg
	 *            CBSErrorMsg ��ҵ������л�õĴ������
	 * 
	 * @param iGuiType
	 *            int ������Ϣҳ����ʾ�İ�ť���ͣ�1�����أ�2��ȷ�����ض�������ҳ��
	 * 
	 * @param strActionUrl
	 *            String �ض�������ҳ��url���ò�����ҪiGuiType=2
	 */

	public static void set_commonErr(HttpServletRequest request,
			HttpServletResponse response, String sErrMsg, int iGuiType,
			String strActionUrl)

	{

		String strGuiType = "����";

		if (iGuiType == 1) {

			strGuiType = "ȷ��";

		}

		else {
			if (iGuiType == 2)

			{

				strGuiType = "����";

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

		String strGuiType = "����";

		if (iGuiType == 1) {

			strGuiType = "ȷ��";

		}

		else {
			if (iGuiType == 2)

			{

				strGuiType = "����";

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
	 * @todo REQUEST ȡ�����ַ���
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
	 * @todo REQUEST ȡint
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
	 * @todo REQUEST ȡdouble
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
