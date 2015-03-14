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
     * 设置request中的出错信息
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
