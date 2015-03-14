/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-4-28 上午11:13:43 
* 类说明 
*/ 

package org.jpf.frame.tags;

import java.io.IOException;
import java.util.Random;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 
 */
public class RandomTag extends TagSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4903026498608799640L;
	
	private int intMin=0;
	private int intMax=Integer.MAX_VALUE;
	
	public void SetMin(int intMin)
	{
		this.intMin=intMin;
	}
	public void SetMax(int intMax)
	{
		this.intMax=intMax;
	}
	/**
	 * @overrid
	 */
	public int doStartTag()throws JspException
	{
		try
		{
			Random random=new Random();
			int result=intMin+random.nextInt(intMax-intMin);
			pageContext.getOut().write(String.valueOf(result));
		}catch(IOException ex){}
		return super.doStartTag();
	}
}
