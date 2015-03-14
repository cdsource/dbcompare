/** 
* @author ��ƽ�� 
* E-mail:wupf@asiainfo-linkage.com 
* @version ����ʱ�䣺2012-1-3 ����3:22:46 
* ��˵�� 
*/ 

package org.jpf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 */
public class ZipUtil
{

	  // ѹ��
	  public static final String compress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    gzip.write(str.getBytes());
	    gzip.close();
	    return out.toString("ISO-8859-1");
	  }

	  // ��ѹ��
	  public static String uncompress(String str) throws IOException {
	    if (str == null || str.length() == 0) {
	      return str;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ByteArrayInputStream in = new ByteArrayInputStream(str
	        .getBytes("ISO-8859-1"));
	    GZIPInputStream gunzip = new GZIPInputStream(in);
	    byte[] buffer = new byte[256];
	    int n;
	    while ((n = gunzip.read(buffer)) >= 0) {
	      out.write(buffer, 0, n);
	    }
	    // toString()ʹ��ƽ̨Ĭ�ϱ��룬Ҳ������ʽ��ָ����toString("GBK")
	    return out.toString();
	  }

	  // ���Է���
	  public static void main(String[] args) throws IOException {
	    System.out.println(ZipUtil.uncompress(ZipUtil.compress("�й�China")));
	  }
	  public static final String compress2(String str) {
		  if(str == null)
		  return null;
		   
		  byte[] compressed;
		  ByteArrayOutputStream out = null;
		  ZipOutputStream zout = null;
		   
		  try {
		  out = new ByteArrayOutputStream();
		  zout = new ZipOutputStream(out);
		  zout.putNextEntry(new ZipEntry("0"));
		  zout.write(str.getBytes());
		  zout.closeEntry();
		  compressed = out.toByteArray();
		  } catch(IOException e) {
		  compressed = null;
		  } finally {
		  if(zout != null) {
		  try{zout.close();} catch(IOException e){}
		  }
		  if(out != null) {
		  try{out.close();} catch(IOException e){}
		  }
		  }
		  
		  if (compressed.length>0)
		  {
			  return new sun.misc.BASE64Encoder().encodeBuffer(compressed);
		  }
		  return "";
	  }

}
