package org.jpf.frame.baseclass;

import org.jpf.utils.JpfConstUtil;


/**
 * <p>Title: Õ¯…œ∂©µ•</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author Œ‚∆Ω∏£
 * @version 1.0
 */
public class baseRespInfo
{
  public baseRespInfo()
  {
    iRet = JpfConstUtil.ResponseInfo.REQUEST_SUCESS;
    reStr = JpfConstUtil.ResponseInfo.RESP_SUCCESS;
  }

  public int iRet;
  public long iValue;
  public String reStr;
  public String strErrInfo;
  private String strDataMsg = "";
  public void setStrDataMsg(String strMsg)
  {
    strMsg = strMsg.trim();
    if (strMsg.length() > 254) {
      strMsg = strMsg.substring(0, 254);
    }
    strDataMsg = strMsg;
  }

  public String getStrDataMsg()
  {
    return strDataMsg;
  }
}
