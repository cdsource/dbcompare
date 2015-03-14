/**
 * 
 */
package org.jpf.searcher;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * �Զ��ƷѲ���ϵͳ
 * @Company: Asiainfo Technologies ��China��,Inc. Hangzhou
 * @author Asiainfo QA-HZ/������
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
public class FileNameFilter implements FilenameFilter {
	private Pattern pattern;

	public FileNameFilter(String regex) {
		pattern = Pattern.compile(regex);
	}

	public boolean accept(File dir, String name) {
		File file = new File(dir + "\\ " + name);
		return pattern.matcher(file.getName()).matches() && file.isFile();
	}

}
