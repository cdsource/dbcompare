/** 
* @author 吴平福 
* E-mail:wupf@asiainfo-linkage.com 
* @version 创建时间：2011-9-20 上午11:51:15 
* 类说明 
*/ 

package org.jpf.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 */
public class OperInfoUtil
{
	public static void main(String[] args)
	{
		String a="6,357,580,581,582,583,584,585,586,587,591,6,810,858";
		StringBuffer sb=new StringBuffer();
		sb.append(a);
		DelRepeat2(sb);
		System.out.print(sb.toString());
	}
	private static void DelRepeat2(StringBuffer sb)
	{
		String stra=sb.toString();
		String[] arrs=stra.split(",");
		List<String> list = new LinkedList<String>();
		sb.setLength(0);		
		sb.append("0");
		for(int i = 0; i < arrs.length; i++) {
            if(!list.contains(arrs[i])) {
                list.add(arrs[i]);
                sb.append(",").append(arrs[i]);
            }
        }
        
		}		
}
