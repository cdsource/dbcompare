/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2012-8-13 下午1:19:30 
 * 类说明 
 */

package org.jpf.svn;

import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;


/**
 * 
 */
public class CheckSvn
{

	/**
	 * 
	 */
	public CheckSvn()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 *            update 2012-8-13
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		try
		{

			// Create a Parser
			CommandLineParser parser = new BasicParser();
			Options options = new Options();
			options.addOption("h", "help", false, "Print this usage information");
			options.addOption("v", "verbose", false, "Print out VERBOSE information");
			options.addOption("dc", "file", true, "删除中文文件名");
			options.addOption("ds", "file", true, "删除SVN目录");
			options.addOption("e", "file", true, "检查文件编码");
			// Parse the program arguments
			CommandLine commandLine = parser.parse(options, args);
			// Set the appropriate variables based on supplied options
			boolean verbose = false;
			String strParamStr = "";

			if (commandLine.hasOption('h'))
			{
				System.out.println("Help Message");
				System.exit(0);
			}
			if (commandLine.hasOption('v'))
			{
				verbose = true;
			}
			if (commandLine.hasOption("dc"))
			{
				strParamStr = commandLine.getOptionValue("dc");
				CheckFileName checkFileName=new CheckFileName(strParamStr,verbose);
			}
			if (commandLine.hasOption("ds"))
			{
				strParamStr = commandLine.getOptionValue("ds");
				CleanSvnFile cleanSvnFile=new CleanSvnFile(strParamStr,verbose);
			}		
			if (commandLine.hasOption("e"))
			{
				strParamStr = commandLine.getOptionValue("e");
				String strFileType=".java";
				boolean IsModify=false;
				if (commandLine.hasOption("es"))
				{
					strFileType=commandLine.getOptionValue("es");
				}
				if (commandLine.hasOption("em"))
				{
					IsModify=true;
				}
				FileCode fileCode=new FileCode(strParamStr,verbose,IsModify,strFileType);
			}			
		} catch (Exception ex)
		{
			// TODO: handle exception
			System.out.println(ex.getMessage());
		}
	}

}
