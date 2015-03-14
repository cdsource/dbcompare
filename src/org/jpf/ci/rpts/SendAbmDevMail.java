/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-16 下午1:33:28 
 * 类说明 
 */

package org.jpf.ci.rpts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 */
public class SendAbmDevMail extends SendDevMail
{
	private static final Logger logger =LogManager.getLogger();
    public String getQueryString()
    {
    	logger.info("sql="+"select distinct prj_name,prj_id from hz_prj  where prj_id in(9644,38506,37517)  order by prj_name");
    	return "select distinct prj_name,prj_id from hz_prj  where prj_id in(9644,38506,37517)  order by prj_name";
    }
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		int i=0;
		if (1 == args.length)
		{
			i = Integer.parseInt(args[0]);
			
		} 
		SendAbmDevMail cSendDevMail = new SendAbmDevMail(i);
	}

	public SendAbmDevMail (int iType)
	{
		super(iType);
	}
}
