/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-8-15 上午11:19:50 
 * 类说明 
 */

package org.jpf.svn;

import info.monitorenter.cpdetector.CharsetPrinter;
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
import org.mozilla.universalchardet.UniversalDetector;
import org.apache.log4j.Logger;
import org.jpf.utils.FileUtil;

/**
 * @author root
 */
public class FileCode
{
	private static final Logger LOGGER = Logger.getLogger(FileCode.class);
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
			FileCode fileCode = new FileCode(args[0].trim(), true, true, args[1]);
		} else if (args.length==3)
		{
			FileCode fileCode = new FileCode(args[0].trim(), args[1].trim(),args[2].trim());
		}else
		{
			FileCode fileCode = new FileCode("D:\\ob60\\cpf\\dbe\\nrm",true,true,".cpp");
			//FileCode fileCode2 = new FileCode("D:/ob60/billing",true,true,".h");
			// FileCode fileCode = new
			// FileCode("D:/Jenkins/workspace/productmgnt");
		}

	}
	private boolean IsModify=true;
	/**
	 * @category:根据SVN UP信息更新文件
	 * @param sourPath
	 * @param destPath
	 * @param strSvnFile
	 */
	public FileCode(String sourPath,String destPath,String strSvnFile)
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(strSvnFile)));
			String str = "";
			while ((str = in.readLine()) != null)
			{
				if(str.startsWith("A"))
				{
					if (IsModify)
					{
						String strCode = guessEncoding(sourPath+java.io.File.pathSeparator+str);
						write(destPath+java.io.File.pathSeparator+str  , "utf8",
								read(sourPath+java.io.File.pathSeparator+str,
										strCode));

						System.out.println("修改后编码：" + guessEncoding(destPath+java.io.File.pathSeparator+str));
					}					
				}
			}
			in.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}		
	}
	public FileCode(String strFilePath)
	{
		DoFileCode(strFilePath, false, false, ".java");
	}

	public FileCode(String strFilePath, boolean verbose, boolean IsModify, String strFileType)
	{
		System.out.println("strFilePath=" + strFilePath);
		System.out.println("strFileType=" + strFileType);
		System.out.println("IsModify=" + IsModify);
		DoFileCode(strFilePath, verbose, IsModify, strFileType);

	}

	private void DoFileCode(String strFilePath, boolean verbose, boolean IsModify, String strFileType)
	{
		if (strFilePath == null || "".equalsIgnoreCase(strFilePath))
		{
			LOGGER.error("input param is null");
			return;
		}
		try
		{
			Vector<String> vector = new Vector<String>();
			FileUtil.GetFiles(strFilePath, vector);

			LOGGER.info("check file count:=" + vector.size());
			for (int i = 0; i < vector.size(); i++)
			{
				String tmpString = (String) vector.get(i);
				if (tmpString.indexOf(".svn")>0)
				{
					continue;
				}
				if (null != strFileType)
				{

					if (tmpString.endsWith(strFileType))
					{
						String strCode = guessEncoding(tmpString);
						//getFileEncode(tmpString);
						//System.out.println("strCode="+strCode);
						if (!"UTF-8".equalsIgnoreCase(strCode))
						{
							System.out.println(tmpString + ":guessEncoding--" + strCode);
							System.out.println(tmpString+":GetFileEncoding:"+GetFileEncoding(tmpString));
							System.out.println(tmpString+":getFileEncode:"+getFileEncode(tmpString));
							if (IsModify)
							{
								write(tmpString, "utf8",
										read(tmpString,
												strCode));

								System.out.println("修改后编码：" + guessEncoding(tmpString));
							}
						}
						getFileEncode(tmpString);
						
						/*
						if (!getFileEncode(tmpString))
						{
							if (IsModify)
							{
								write(tmpString, "utf8",
										read(tmpString,
												strCode));

								System.out.println("修改后编码：" + guessEncoding(tmpString));
							}
						}
						*/
					}
				}

				if (i % 100 == 0)
				{
					System.out.println(i);
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
	public static String read(String fileName, String encoding)
	{
		String string = "";
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), encoding));
			String str = "";
			while ((str = in.readLine()) != null)
			{
				string += str + "\n";
			}
			in.close();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
		return string;
	}

	/**
	 * @describe write str to fileName with specified encode
	 * @param fileName
	 * @param encoding
	 * @param str
	 * @return null
	 */
	public static void write(String fileName, String encoding, String str)
	{
		try
		{
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), encoding));
			out.write(str);
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
    public boolean getFileEncode(String fileName) {

    	try
		{
			
		//File file = new File(fileName);

        InputStream ios = new java.io.FileInputStream(fileName);

        byte[] b = new byte[3];

        ios.read(b);

        ios.close();

        if (b[0] == -1 && b[1] == -2 )  
        	return  true;
        if (b[0] == -17 && b[1] == -69 && b[2] == -65)
            //System.out.println(file.getName() + "编码为UTF-8");
    		return true;

        else

            System.out.println(fileName + "可能是GBK，也可能是其他编码。");
           System.out.println(b[0]+","+b[1]+","+b[2]);
           
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.equals(ex);
		}
		
        return false;
     }
    public String GetFileEncoding(String fileName)
    {
    	String encoding=null;
    	try
		{
            byte[] buf = new byte[4096];
            java.io.FileInputStream fis = new java.io.FileInputStream(fileName);

            // (1)
            UniversalDetector detector = new UniversalDetector(null);

            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
              detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();

            // (4)
             encoding = detector.getDetectedCharset();
            if (encoding != null) {
              System.out.println("Detected encoding = " + encoding);
            } else {
              System.out.println("No encoding detected.");
            }

            // (5)
            detector.reset();    			
		} catch (Exception ex)
		{
			// TODO: handle exception
			LOGGER.error(ex);
		}
	
        return encoding;
    }
}
