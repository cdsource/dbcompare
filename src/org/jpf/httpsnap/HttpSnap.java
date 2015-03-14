/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-2-2 下午4:48:29 
 * 类说明 
 */

package org.wupf.httpsnap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;




/**
 * 
 */
public class HttpSnap
{
	private static final Logger logger = Logger.getLogger(HttpSnap.class);
    //访问间隔基础时间
	private long iSleepBase=3;
	//访问间隔时间总和
	private long iSleepCount=0;
	
	//淘宝网起始URL
	String strStartUrl="http://shopsearch.taobao.com/browse/shop_search.htm?stat=5&cattype=1&q=&cat=50006843&catName=%C5%AE%D0%AC&rootCat=1&tab=1";
	//String Url_Start="http://shopsearch.taobao.com/browse/shop_search.htm?stat=5&cattype=1&q=&cat=1512&catName=%CA%D6%BB%FA&rootCat=2&tab=1";
	
	//循环URL
	//String strNextUrl="http://shopsearch.taobao.com/browse/shop_search.htm?cattype=1&";
	String strNextUrl="http://shopsearch.taobao.com/browse/shop_search.htm?cattype=1";
	
	String strShopUrl="<a target=\"_blank\" href=\"http://rate.taobao.com/user-rate-";
	//存放主页面URL
	Vector v=new Vector();
	//存放店铺URL
	Vector v2=new Vector();
	
	public static void main(String[] args)
	{
		if(args.length==2)
		{
		
		HttpSnap t = new HttpSnap(args[0],args[1]);
		}else
		{
			System.out.println("没有参数，演示");
			HttpSnap t = new HttpSnap();
			//System.out.println("错误参数");
		}
	}
    public HttpSnap(String Url_Start ,String strNextUrl )
	{
    	this.strStartUrl=Url_Start;
    	this.strNextUrl=strNextUrl;
    	DoWork();
	}
    public HttpSnap()
	{
    	DoWork();
	}
    public void DoWork()
	{
    	System.out.println("淘宝网起始URL="+this.strStartUrl);
    	System.out.println("循环URL="+this.strNextUrl);
    	
		long sTime = System.currentTimeMillis();
		UrlType cUrlType=new UrlType();
		cUrlType.strUrl=strStartUrl.trim();
		v.add(cUrlType);		
		for(int j=0;j<v.size();j++)
		{
			UrlType cUrlType2=(UrlType)v.get(j);
			if (cUrlType2.iState==0)
			{
				cUrlType2.iState=1;
				HttpInfo cHttpInfo=new HttpInfo();
				GetMainUrl(cHttpInfo.GetHttpInfo(cUrlType2.strUrl));
				System.out.println("找到的店铺:"+v2.size());
				//GetHttpInfo(cUrlType2.strUrl);
				try{
					long  i=Math.round(Math.random()*9+iSleepBase);
					iSleepCount+=i;
					System.out.println("sleep "+i+"s");
					Thread.sleep(i*1000);//参数代表休眠多久，1000代表1000ms也就是一秒，这个方法会抛出异常所以要try catch
				}catch(Exception e){
					logger.error(e);
				}
			}
		}
		System.out.println("找到的店铺:"+v2.size());
		for(int j=0;j<v2.size();j++)
		{
			System.out.println((String)v2.get(j));
		}
		v2.clear();
		v.clear();
	    long eTime = System.currentTimeMillis();
	    System.out.println(" 数据生成总用时(单位MS):" + (eTime - sTime));
		System.out.println("休眠时间:"+iSleepCount+"秒");
	}
    /**
     * 
     * @param strUrl
     */
	public void GetHttpInfo(String strUrl )
	{
		HttpClient httpclient = new DefaultHttpClient();
		try
		{
			strUrl=strUrl.replaceAll("amp;","");
			HttpGet httpget = new HttpGet(strUrl);

			/*
			System.out.println(response.getProtocolVersion());
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getStatusLine().getReasonPhrase());
			System.out.println(response.getStatusLine().toString());
            */
			System.out.println("----------------------------------------");
			// Execute HTTP request
			System.out.println("executing request " + httpget.getURI());
		    HttpResponse response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
			//System.out.println("----------------------------------------");
            
			// Get hold of the response entity
			HttpEntity entity = response.getEntity();

			// If the response does not enclose an entity, there is no need
			// to bother about connection release
			if (entity != null)
			{
				//InputStream instream = entity.getContent();
				try
				{
					//instream.read();
					// do something useful with the response
					//System.out.println(EntityUtils.toString(entity));
					String strHtml=EntityUtils.toString(entity);
					GetMainUrl(strHtml);
					//GetNextUrl(strHtml);
				} catch (IOException ex)
				{
					// In case of an IOException the connection will be released
					// back to the connection manager automatically
					throw ex;
				} catch (RuntimeException ex)
				{
					// In case of an unexpected exception you may want to abort
					// the HTTP request in order to shut down the underlying
					// connection immediately.
					httpget.abort();
					throw ex;
				} finally
				{
					// Closing the input stream will trigger connection release
					/*
					try
					{
						instream.close();
					} catch (Exception ignore)
					{
					}
					*/
				}
			}
		}catch(Exception ex)
		{
			logger.error(ex);
		} finally
		{
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}
    /**
     * 
     * @param strHtml
     */
	public void GetMainUrl(String strHtml)
	{
		//System.out.println(strHtml);
		int i=strHtml.indexOf(strShopUrl);
		String tmpString="";
		String tmpStr2="";
		int j=0;
		while(i>=0)
		{
			tmpString="";
			j=strHtml.indexOf("</a>", i);
			//tmpString=strHtml.substring(i, i+80);
			//System.out.println("j="+j);
			tmpStr2=strHtml.substring(i, j);
			strHtml=strHtml.substring(i+80);
			//System.out.println(tmpString);
			//System.out.println(tmpStr2);
			AddNextUrl2(tmpStr2);
			 i=strHtml.indexOf("<a target=\"_blank\" href=\"http://rate.taobao.com/user-rate-");
		}
		GetNextUrl(strHtml);
	}
	/**
	 * 
	 * @param strHtml
	 */
	public void GetNextUrl(String strHtml)
	{
		//System.out.println(strHtml);

		int i=strHtml.indexOf(strNextUrl);
		String tmpStr2="";
		int j=0;
		while(i>=0)
		{
			tmpStr2="";
			j=strHtml.indexOf("\"", i);
			//tmpString=strHtml.substring(i, i+80);
			//System.out.println("j="+j);
			tmpStr2=strHtml.substring(i, j);
			strHtml=strHtml.substring(i+80);
			//System.out.println(tmpString);
			//System.out.println(tmpStr2);
			AddNextUrl(tmpStr2);
			 i=strHtml.indexOf(strNextUrl);
		}		
	}
	/**
	 * 
	 * @param strUrl
	 */
	private void AddNextUrl(String strUrl)
	{
		if(strUrl==null)
		{
			return;
		}
		boolean isExist=false;
		for(int i=0;i<v.size();i++)
		{
			UrlType cUrlType=(UrlType)v.get(i);
			if (cUrlType.strUrl.trim().equalsIgnoreCase(strUrl.trim()))
			{
				isExist=true;
				break;
			}
		}
		if(!isExist)
		{
			UrlType cUrlType=new UrlType();
			cUrlType.strUrl=strUrl.trim();
			v.add(cUrlType);
			System.out.println("add url="+strUrl.trim());
		}
	}
	/**
	 * 
	 * @param strUrl
	 */
	private void AddNextUrl2(String strUrl)
	{
		if(strUrl==null)
		{
			return;
		}
		boolean isExist=false;
		for(int i=0;i<v2.size();i++)
		{
			String tmpStr=(String)v2.get(i);
			if (tmpStr.equalsIgnoreCase(strUrl.trim()))
			{
				isExist=true;
				break;
			}
		}
		if(!isExist)
		{
			v2.add(strUrl.trim());
		}
	}

}
