/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-11-7 下午2:17:00 
 * 类说明 
 */

package org.jpf.ci;

import info.monitorenter.cpdetector.CharsetPrinter;
import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.ByteOrderMarkDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.JpfFileUtil;
import org.jpf.utils.JpfStringUtil;

/**
 * @author root
 */
public class CheckFile
{
	private static final Logger LOGGER = LogManager.getLogger(CheckFile.class);
	// Input file name
	private String inputFileName;
	// The encoding when reading input file
	private String inputFileCode;
	// Output file name
	private String outputFileName;
	// The encoding when write to a file
	private String outputFileCode;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Convert file's encoding to 'utf8'.
		/*
		 * write("c:/studio/test_file/test.txt", "utf8",
		 * read("c:/studio/test_file/test.txt",
		 * guessEncoding("c:/studio/test_file/test.txt"))); // output new
		 * encoding
		 * System.out.println(guessEncoding("c:/studio/test_file/test.txt")); //
		 * output file's content
		 * System.out.println(read("c:/studio/test_file/test.txt",
		 * guessEncoding("c:/studio/test_file/test.txt")));
		 */
		if (args.length == 2)
		{
			CheckFile fileCode = new CheckFile(args[0].trim(), true, true, args[1]);
		} else if (args.length == 3)
		{
			CheckFile fileCode = new CheckFile(args[0].trim(), args[1].trim(), args[2].trim());
		} else
		{
			CheckFile fileCode = new CheckFile("D:\\ob60\\mediation\\acquisition\\realtime\\client", true, true, ".cpp");
			// FileCode fileCode2 = new
			// FileCode("D:/ob60/billing",true,true,".h");
			// FileCode fileCode = new
			// FileCode("D:/Jenkins/workspace/productmgnt");
		}

	}

	private boolean IsModify = true;

	/**
	 * @category:根据SVN UP信息更新文件
	 * @param sourPath
	 * @param destPath
	 * @param strSvnFile
	 */
	public CheckFile(String sourPath, String destPath, String strSvnFile)
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(strSvnFile)));
			String str = "";
			while ((str = in.readLine()) != null)
			{
				if (str.startsWith("A"))
				{
					if (IsModify)
					{
						String strCode = guessEncoding(sourPath + java.io.File.pathSeparator + str);
						write(destPath + java.io.File.pathSeparator + str, "utf8",
								read(sourPath + java.io.File.pathSeparator + str,
										strCode));

						System.out.println("修改后编码：" + guessEncoding(destPath + java.io.File.pathSeparator + str));
					}
				}
			}
			in.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public CheckFile(String strFilePath)
	{
		DoFileCode(strFilePath, false, false, ".java");
	}

	public CheckFile(String strFilePath, boolean verbose, boolean IsModify, String strFileType)
	{
		System.out.println("strFilePath=" + strFilePath);
		System.out.println("strFileType=" + strFileType);
		System.out.println("IsModify=" + IsModify);
		// Add the implementations of
		// info.monitorenter.cpdetector.io.ICodepageDetector:
		// This one is quick if we deal with unicode codepages:

		detector.add(new ByteOrderMarkDetector());
		// The first instance delegated to tries to detect the meta charset
		// attribut in html pages.
		// detector.add(new ParsingDetector(true)); // be verbose about parsing.
		// This one does the tricks of exclusion and frequency detection, if
		// first implementation is
		// unsuccessful:
		detector.add(JChardetFacade.getInstance()); // Another singleton.
		detector.add(ASCIIDetector.getInstance()); // Fallback, see javadoc.

		DoFileCode(strFilePath, verbose, IsModify, strFileType);

	}

	private boolean CheckFileType(String[] strFileSuffix, String strFileName)
	{
		for (int i = 0; i < strFileSuffix.length; i++)
		{
			if (strFileName.endsWith(strFileSuffix[i]))
				return true;
		}
		return false;
	}

	private void DoFileCode(String strFilePath, boolean verbose, boolean IsModify, String strFileType)
	{
		if (strFilePath == null || "".equalsIgnoreCase(strFilePath))
		{
			LOGGER.error("input param is null");
			return;
		}
		if (null == strFileType || "".equalsIgnoreCase(strFileType.trim()))
		{
			return;
		}
		try
		{

			Vector<String> vector = new Vector<String>();
			JpfFileUtil.GetFiles(strFilePath, vector);
			LOGGER.info("check file count:=" + vector.size());

			String[] strFileSuffix = strFileType.split(",");

			//for (int i = 0; i < vector.size(); i++)
			int i=0;
			while(vector.size()>0)
			{
				i++;
				String tmpString = (String) vector.get(0);
				vector.remove(0);
				if (tmpString.indexOf(".svn") > 0)
				{
					continue;
				}
				if (JpfStringUtil.IsChinese(tmpString))
				{
					LOGGER.info("delete file:" + tmpString);
					JpfFileUtil.DelFile(tmpString);
					continue;
				}
				if (CheckFileType(strFileSuffix, tmpString))
				{
					String strCode = guessEncoding(tmpString);
					// getFileEncode(tmpString);
					// System.out.println("strCode=" + strCode);
					// System.out.println(tmpString + ":guessEncoding--" +
					// strCode);
					// getFileEncode(tmpString);
					// System.out.println(tmpString+":GetFileEncoding3:"+GetFileEncoding3(tmpString));

					// getFileEncode(tmpString);
					if (!"UTF-8".equalsIgnoreCase(strCode))
					{
						System.out.println(tmpString + ":guessEncoding--" + strCode);
						if (IsModify)
						{
							write(tmpString, "utf8", read(tmpString, strCode));

							// System.out.println(tmpString + ":guessEncoding--"
							// + strCode);
							// System.out.println(tmpString+":GetFileEncoding:"+GetFileEncoding(tmpString));
							// getFileEncode(tmpString);
						}
					}
					if (getFileEncode2(tmpString))
					{
						write(tmpString, "utf8", read(tmpString, "ISO-8859-1"));
						System.out.println("修改后编码：");
					}
					// String source = FileUtils.readFileToString(new
					// File(tmpString),"UTF-8");
					// System.out.println(source);
					// getFileEncode(tmpString);

					/*
					 * if (!getFileEncode(tmpString)) { if (IsModify) {
					 * write(tmpString, "utf8", read(tmpString, strCode));
					 * 
					 * System.out.println("修改后编码：" + guessEncoding(tmpString));
					 * } }
					 */
				}

				if (i % 100 == 0)
				{
					System.out.println(i );
				}
			}
			vector.clear();
			LOGGER.info("game over");
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.error(ex);
		}
	}

	/**
	 * @describe Guess the encoding of the file specified by filename
	 * @param filename
	 * @return the encoding of the file
	 */
	public String guessEncoding(String filename)
	{
		try
		{
			CharsetPrinter charsetPrinter = new CharsetPrinter();
			String encode = charsetPrinter.guessEncoding(new File(filename));
			return encode;
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * @describe Read file with specified encode
	 * @param filename
	 * @param encoding
	 * @return the content of the file in the form of string
	 */
	public  StringBuffer read(String fileName, String encoding)throws Exception
	{
		File f = new File(fileName);
		StringBuffer sb=new StringBuffer();
		InputStreamReader read = new InputStreamReader(new FileInputStream(f), encoding);

		BufferedReader reader = new BufferedReader(read);

		String line;

		while ((line = reader.readLine()) != null)
		{
			sb.append(line);
		}
		reader.close();
		read.close();
		return sb;
	}

	/**
	 * @describe write str to fileName with specified encode
	 * @param fileName
	 * @param encoding
	 * @param str
	 * @return null
	 */
	public  void write(String fileName, String encoding, StringBuffer sbBuffer)
	{
		try
		{
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), encoding));
			out.write(sbBuffer.toString());
			out.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public String getInputFileName()
	{
		return inputFileName;
	}

	public void setInputFileName(String inputFileName)
	{
		this.inputFileName = inputFileName;
	}

	public String getInputFileCode()
	{
		return inputFileCode;
	}

	public void setInputFileCode(String inputFileCode)
	{
		this.inputFileCode = inputFileCode;
	}

	public String getOutputFileName()
	{
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName)
	{
		this.outputFileName = outputFileName;
	}

	public String getOutputFileCode()
	{
		return outputFileCode;
	}

	public void setOutputFileCode(String outputFileCode)
	{
		this.outputFileCode = outputFileCode;
	}
    /*
	public boolean getFileEncode(String fileName)
	{

		try
		{

			// File file = new File(fileName);

			InputStream ios = new java.io.FileInputStream(fileName);

			byte[] b = new byte[3];

			ios.read(b);

			ios.close();

			if (b[0] == -1 && b[1] == -2)
				return true;
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)
				// System.out.println(file.getName() + "编码为UTF-8");
				return true;

			else

				System.out.println(fileName + ":getFileEncode:可能是GBK，也可能是其他编码。" + b[0] + "," + b[1] + "," + b[2]);

		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.equals(ex);
		}

		return false;
	}
   */
	public boolean getFileEncode2(String fileName)
	{
		try
		{
			InputStream ios = new java.io.FileInputStream(fileName);
			byte[] b = new byte[3];
			ios.read(b);
			ios.close();
			if (b[0] == 35 && b[1] == 105 && b[2] == 110)
				// System.out.println(file.getName() + "编码为UTF-8");
				return true;

		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.equals(ex);
		}

		return false;
	}
    /*
	public String GetFileEncoding(String fileName)
	{
		String encoding = null;
		try
		{
			byte[] buf = new byte[4096];
			java.io.FileInputStream fis = new java.io.FileInputStream(fileName);

			// (1)
			UniversalDetector detector = new UniversalDetector(null);

			// (2)
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone())
			{
				detector.handleData(buf, 0, nread);
			}
			// (3)
			detector.dataEnd();

			// (4)
			encoding = detector.getDetectedCharset();
			
			  if (encoding != null) { System.out.println("Detected encoding = "
			 + encoding); } else {
			 System.out.println("No encoding detected."); }
			 
			// (5)
			detector.reset();
			fis.close();
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.error(ex);
		}

		return encoding;
	}
    */
	CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance(); // A
																			// singleton.
    /*
	public boolean GetFileEncoding3(String fileName)
	{
		boolean ret = false;
		try
		{
			// Work with the configured proxy:
			File document = new File(fileName);
			java.nio.charset.Charset charset = null;
			charset = detector.detectCodepage(document.toURL());
			if (charset == null)
			{
				LOGGER.error("GetFileEncoding:fail");
			}
			else
			{
				// Open the document in the given code page:
				// ava.io.Reader reader = new java.io.InputStreamReader(new
				// java.io.FileInputStream(document),charset);
				// Read from it, do sth., whatever you desire. The character are
				// now - hopefully - correct..
				System.out.println("GetFileEncoding3:" + charset);
				ret = true;
			}
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.error(ex);
		}

		return ret;
	}
	*/
}
