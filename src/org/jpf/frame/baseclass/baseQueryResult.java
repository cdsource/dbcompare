package org.jpf.frame.baseclass;

/**
 *
 * <p>Title: 网上订单</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: </p>
 *
 * @author 吴平福
 * @version 1.0
 */
public class baseQueryResult
{
  public baseQueryResult()
  {
  }

  //总纪录数目
  public long lTotalCount;
  //所分得逻辑页数
  public long lPageCount;
  public long lCurrentPage;
  public String strQueryString = "";
}
