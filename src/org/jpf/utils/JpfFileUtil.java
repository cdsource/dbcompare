/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-7-26 下午2:33:31 
 * 类说明 
 */

package org.jpf.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 
 */
public class JpfFileUtil
{
	private static final Logger logger = LogManager.getLogger();

	/**
	 * 私有构造方法，防止类的实例化，因为工具类不需要实例化。
	 */
	private JpfFileUtil()
	{
	}

	public static String GetFileDate(String sFileName)
	{
		File fpath = new File(sFileName);
		long timeStamp = fpath.lastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String tsForm = formatter.format(new Date(timeStamp));
		// LOGGER.debug(tsForm);
		return tsForm;
	}

	/**
	 * @category 比较文件时间
	 * @param sFileName1
	 * @param sFileName2
	 * @return update 2012-9-6
	 */
	public static boolean CompareFileDate(String sFileName1, String sFileName2)
	{
		File fpath1 = new File(sFileName1);
		long timeStamp1 = fpath1.lastModified();
		File fpath2 = new File(sFileName2);
		long timeStamp2 = fpath2.lastModified();
		return timeStamp1 > timeStamp2;
	}

	static class CompratorByLastModified implements Comparator<File>
	{
		public int compare(File f1, File f2)
		{
			long diff = f1.lastModified() - f2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}

		public boolean equals(Object obj)
		{
			return true;
		}
	}

	/**
	 * @判断文件时间
	 * @param url
	 * @param time
	 * @return 被测试类名：TODO 被测试接口名:TODO 测试场景：TODO 前置参数：TODO 入参： 校验值： 测试备注： update
	 *         2014年3月13日
	 */
	public static boolean FileTime(String url, int time)
	{
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File f = new File(url);
		// 获得文件创建时间
		long file_time = f.lastModified();
		// 获得当前系统时间
		long now_time = System.currentTimeMillis();
		if ((now_time - file_time) > time * 60 * 1000)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static String GetFileTime(String url)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File f = new File(url);
		// 获得文件创建时间
		long file_time = f.lastModified();
		Date d = new Date(file_time);
		return sdf.format(d);
	}

	/**
	 * @todo:获取一个目录下所有文件
	 * @param inFilePath
	 * @param v
	 *            update 2012-8-13
	 */
	public static void GetFiles(String inFilePath, Vector v)
	{

		File f1 = new File(inFilePath);
		if (f1.isDirectory())
		{
			File[] f2 = f1.listFiles();

			Arrays.sort(f2, new CompratorByLastModified());
			for (int i = 0; i < f2.length; i++)
			{
				if (f2[i].isDirectory())
				{
					GetFiles(f2[i].toString(), v);
				} else
				{
					v.add(f2[i].toString());
				}
			}
		}
	}

	/**
	 * @todo 删除文件目录
	 * @param path
	 *            String
	 */
	public static void DelDirWithFiles(String path)
	{
		try
		{
			File dir = new File(path);
			if (dir.exists())
			{
				File[] tmp = dir.listFiles();
				for (int i = 0; i < tmp.length; i++)
				{
					if (tmp[i].isDirectory())
					{
						DelDirWithFiles(path + "\\" + tmp[i].getName());
					} else
					{
						logger.info("del file:" + tmp[i]);
						tmp[i].delete();

					}
				}
				logger.info("del dir:" + dir.getAbsolutePath());
				dir.delete();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * @category 保存文件
	 * @param strFileFullName
	 * @param sb
	 *            update 2012-9-5
	 */
	public static void SaveFile(String strFileFullName, StringBuffer sb)
	{
		FileOutputStream fout = null;
		try
		{

			logger.debug("saving filename:" + strFileFullName);
			File uploadFilePath = new File(strFileFullName);
			File parent = uploadFilePath.getParentFile();
			logger.debug(parent.getAbsolutePath());
			if (!parent.exists())
			{
				parent.mkdir();
			}
			fout = new FileOutputStream(strFileFullName);
			String str = new String(sb);
			fout.write(str.getBytes());
			fout.flush();
			fout.close();
			logger.debug("saved file ok:" + strFileFullName);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		} finally
		{
			if (fout != null)
			{
				try
				{
					fout.close();
				} catch (Exception ex)
				{
					// TODO: handle exception
				}

			}
		}
	}

	/**
	 * @category 保存文件
	 * @param strFileFullName
	 * @param sb
	 *            update 2012-9-5
	 */
	public static void SaveFile(String strFileFullName, String str)
	{
		FileOutputStream fout = null;
		try
		{

			logger.debug("saving filename:" + strFileFullName);
			File uploadFilePath = new File(strFileFullName);
			File parent = uploadFilePath.getParentFile();
			logger.debug(parent.getAbsolutePath());
			if (!parent.exists())
			{
				parent.mkdir();
			}
			fout = new FileOutputStream(strFileFullName);
			fout.write(str.getBytes());
			fout.flush();
			fout.close();
			logger.debug("saved file ok:" + strFileFullName);
		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		} finally
		{
			if (fout != null)
			{
				try
				{
					fout.close();
				} catch (Exception ex)
				{
					// TODO: handle exception
				}

			}
		}
	}

	public static String GetFileTxt(String FileName) throws Exception
	{
		File f = new File(FileName);
		StringBuffer sb = new StringBuffer();
		InputStreamReader read = new InputStreamReader(new FileInputStream(f), "GBK");

		BufferedReader reader = new BufferedReader(read);

		String line;

		while ((line = reader.readLine()) != null)
		{

			sb.append(line).append("\r\n");

		}
		reader.close();
		read.close();
		return sb.toString();
	}

	public static String GetFileTxt(String FileName, String strChat) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		File f = new File(FileName);
		FileInputStream in = new FileInputStream(f);
		// 指定读取文件时以UTF-8的格式读取
		if (strChat.length() > 0)
		{
			strChat = Charset.defaultCharset().name();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in, strChat));
		// BufferedReader br = new BufferedReader(new UnicodeReader(in,
		// Charset.defaultCharset().name()));
		String line = br.readLine();
		while (line != null)
		{
			System.out.println(line);
			sb.append(line).append("\n");
			line = br.readLine();
		}
		br.close();
		return sb.toString();
	}

	public static String GetFileName(String sFilePathName)
	{
		int i = sFilePathName.lastIndexOf(java.io.File.separator);
		if (i > 0)
		{
			sFilePathName = sFilePathName.substring(i + 1);
		}
		return sFilePathName;
	}

	/**
	 * @category获取文件路径
	 * @param sFilePathName
	 *            String
	 * @return String
	 */
	public static String GetFilePath(String sFilePathName)
	{
		int i = sFilePathName.lastIndexOf(java.io.File.separator);
		if (i > 0)
		{
			sFilePathName = sFilePathName.substring(0, i);
		}
		return sFilePathName;
	}

	/**
	 * @todo 压缩文件
	 * @param zipFileName
	 *            String
	 * @param arrFileList
	 *            String
	 * @throws Exception
	 */

	public static int ZipFile(String zipFileName, String arrFileList,
			String in_Path) throws Exception
	{

		if (arrFileList == null || arrFileList.trim() == "")
		{
			throw new Exception("待压缩的文件列表为空。");

		}
		String[] a = arrFileList.split(";");
		ArrayList b = new ArrayList();

		for (int i = 0; i < a.length; i++)
		{
			File m_file = new File(in_Path + a[i]);
			if (m_file.exists())
			{
				b.add(in_Path + a[i]);
			} else
			{
				throw new Exception("文件不存在:" + in_Path + a[i]);
			}
		}
		if (b == null || b.size() == 0)
		{
			// return -2;
			throw new Exception("待压缩的文件列表为空。");
		}

		ZipFile(zipFileName, b);

		return 0;
	}

	/**
	 * @todo 压缩文件
	 * @param zipFileName
	 *            String
	 * @param arrFileList
	 *            ArrayList
	 * @throws Exception
	 */
	public static void ZipFile(String zipFileName, ArrayList arrFileList)
			throws Exception
	{

		if (zipFileName == null || zipFileName.trim() == "")
		{
			throw new Exception("文件名不能为空。");
		}
		if (arrFileList == null || arrFileList.size() == 0)
		{
			throw new Exception("待压缩的文件列表为空。");
		}

		ZipOutputStream os = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		byte[] buf = new byte[1024 * 1024];
		os.setLevel(9);
		try
		{
			for (int i = 0; i < arrFileList.size(); i++)
			{
				String sPathFile = (String) arrFileList.get(i);
				FileInputStream is = new FileInputStream(sPathFile);
				String sFile = GetFileName(sPathFile);
				try
				{
					ZipEntry zEntry = new ZipEntry(sFile);
					os.putNextEntry(zEntry);
					int bFlag = -1;
					while ((bFlag = is.read(buf)) != -1)
					{
						os.write(buf, 0, bFlag);
					}
				} catch (Exception ex)
				{
					ex.printStackTrace();
					throw ex;
				} finally
				{
					is.close();
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			throw ex;
		} finally
		{
			os.close();
		}

	}

	/**
	 * @todo 删除文件
	 * @param in_Path
	 *            String
	 * @param arrFileList
	 *            ArrayList
	 * @throws Exception
	 */
	public static void DelFile(String in_Path, ArrayList arrFileList)
			throws Exception
	{
		for (int i = 0; i < arrFileList.size(); i++)
		{
			String sFile = (String) arrFileList.get(i);
			File delFile = new File(sFile);
			delFile.delete();
		}
	}

	/**
	 * @todo 删除文件
	 * @param in_Path
	 *            String
	 * @param arrFileList
	 *            ArrayList
	 * @throws Exception
	 */
	public static void DelFile(String in_FileName) throws Exception
	{

		File delFile = new File(in_FileName);
		delFile.delete();

	}

	/**
	 * @todo 拷贝文件
	 * @param in_Path
	 *            String
	 * @param arrFileList
	 *            ArrayList
	 * @throws Exception
	 */
	public static void CopyFile(String new_Path, String old_Path,
			ArrayList arrFileList) throws Exception
	{
		if (arrFileList == null || arrFileList.size() == 0)
		{
			throw new Exception("待压缩的文件列表为空。");
		}

		for (int i = 0; i < arrFileList.size(); i++)
		{
			String sFile = old_Path + (String) arrFileList.get(i);
			// int bytesum=0;
			int byteread = 0;
			File oldFile = new File(sFile);
			if (oldFile.exists())
			{
				FileInputStream inStream = new FileInputStream(oldFile);
				String m_FileName = new_Path + (String) arrFileList.get(i);
				FileOutputStream outStream = new FileOutputStream(m_FileName);
				byte[] buf = new byte[1024];
				while ((byteread = inStream.read(buf)) != -1)
				{
					// bytesum+=byteread;
					outStream.write(buf, 0, byteread);
				}
				inStream.close();
				outStream.close();
			}
		}
	}

	/**
	 * 
	 * @param NewFile
	 *            String
	 * @param OldFile
	 *            String
	 * @throws Exception
	 */
	public static void CopyFile(String NewFile, String OldFile)
			throws Exception
	{
		int byteread = 0;
		File oldFile = new File(OldFile);
		if (!oldFile.exists())
		{
			throw new Exception(NewFile + ":is not exist");
		}
		if (!oldFile.isFile())
		{
			throw new Exception(NewFile + ":is not a file");
		}
		// 检查目标目录是否存在
		String sSourceFilePath = GetFilePath(OldFile);
		File fNewFile = new File(sSourceFilePath);
		if (!fNewFile.exists())
		{
			if (!fNewFile.mkdirs())
			{
				throw new Exception(sSourceFilePath + ":创建目录失败");
			}
		}
		FileInputStream inStream = new FileInputStream(oldFile);
		FileOutputStream outStream = new FileOutputStream(NewFile);
		byte[] buf = new byte[1024];
		while ((byteread = inStream.read(buf)) != -1)
		{
			// bytesum+=byteread;
			outStream.write(buf, 0, byteread);
		}
		inStream.close();
		outStream.close();

	}

	public static void Mkdir(String sFilePath)
	{
		File f = new File(sFilePath);
		if (!f.exists())
		{
			f.mkdirs();
		}
	}

	public static void CheckFile(String strFileName) throws Exception
	{
		File oldFile = new File(strFileName);
		if (!oldFile.exists())
		{
			throw new Exception(strFileName + ":is not exist");
		}
		if (!oldFile.isFile())
		{
			throw new Exception(strFileName + ":is not a file");
		}
	}

	public static boolean IsEmptyFile(String strFileName) throws Exception
	{
		File f = new File(strFileName);
		if (f.exists() && f.isFile() && 0 == f.length())
		{
			return true;
		}
		return false;
	}
}
