/** 
* @author 鍚村钩绂� 
* E-mail:wupf@asiainfo-linkage.com 
* @version 鍒涘缓鏃堕棿锛�2012-2-6 涓婂崍8:24:19 
* 绫昏鏄� 
*/ 

package org.wupf.httpsnap;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 
 */
public class HttpInfo
{
	private static final Logger logger = LogManager.getLogger();	
	public HttpInfo()
	{
		
	}
	public String GetHttpInfo(String strUrl )
	{
		String strReString="";
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
					strReString=EntityUtils.toString(entity);
					//GetMainUrl(strHtml);
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
		return strReString;
	}
}
