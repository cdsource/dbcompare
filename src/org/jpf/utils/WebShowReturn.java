package org.jpf.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jpf.frame.baseclass.baseRespInfo;
import org.jpf.utils.JpfConstUtil;

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
public class WebShowReturn {
    private static WebShowReturn instance;

    private WebShowReturn() {
    }

    public static synchronized WebShowReturn getInstance() {

        if (instance == null)
            instance = new WebShowReturn();
        return instance;
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

    public static void showReturnMsg(HttpServletRequest request,
            HttpServletResponse response,
            baseRespInfo cbaseRespInfo,
            String strActionUrl) {

        if (strActionUrl == null || "".equalsIgnoreCase(strActionUrl)) {
            strActionUrl = UrlUtil.GetUrlName(request.getHeader("Referer"));
        }
        if (cbaseRespInfo.iRet < 0) {
            strActionUrl = "javascript:history.back()";
        }
        request.getSession().setAttribute("RETURN_ACTION_URL", strActionUrl);

        // cbaseRespInfo.reStr=Encrypt.DoEncrypt(cbaseRespInfo.reStr);
        request.getSession().setAttribute("RETURN_MSG_TITLE",
                cbaseRespInfo.reStr);
        // cbaseRespInfo.strErrInfo=Encrypt.DoEncrypt(cbaseRespInfo.strErrInfo);
        request.getSession().setAttribute("RETURN_MSG_DETAIL",
                cbaseRespInfo.strErrInfo);
        try {

            request.getRequestDispatcher("/include/errorPage.jsp").forward(
                    request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ����request�еĳ�����Ϣ
     * 
     * @param request
     *            HttpServletRequest
     * @param strErrorMessage
     *            String
     * @return
     */
    public static void setRequestErrorInfo(HttpServletRequest request,
            String strErrorMessage) {

        request.setAttribute(JpfConstUtil.APP_ERROR_MESSAGE_KEY, strErrorMessage);

    }

    public static String getRequestErrorInfo(HttpServletRequest request) {

        return (String) request.getAttribute(JpfConstUtil.APP_ERROR_MESSAGE_KEY);

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

            request.getRequestDispatcher("/common_errorPage.jsp")
                    .forward(request, response);

        } catch (ServletException ex)
        {
            ex.printStackTrace();
        } catch (IOException iex)
        {
            iex.printStackTrace();
        }

    }
}
