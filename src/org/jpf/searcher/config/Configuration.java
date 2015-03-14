/**
 * ��ȡ���������ļ�
 */
package org.jpf.searcher.config;

/**
 * �Զ��ƷѲ���ϵͳ
 * @Company: Asiainfo Technologies ��China��,Inc. Hangzhou
 * @author Asiainfo QA-HZ/������
 * @version 1.0 Copyright (c) 2008
 * @date 2008-3-18
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configuration {
	private static Log log = LogFactory.getLog(Configuration.class);

	private Properties config = new Properties();

	private String fn = null;

	public Configuration(String fileName) throws ConfigurationException {
		try {
			FileInputStream fin = new FileInputStream(fileName);
			this.config.load(fin);
			fin.close();
		} catch (IOException ex) {
			log.info("�޷���ȡָ���������ļ�:" + fileName);
			throw new ConfigurationException("�޷���ȡָ���������ļ�:" + fileName);
		}

		this.fn = fileName;
	}

	public String getValue(String itemName) {
		return this.config.getProperty(itemName);
	}

	public String getValue(String itemName, String defaultValue) {
		return this.config.getProperty(itemName, defaultValue);
	}

	public void setValue(String itemName, String value) {
		this.config.setProperty(itemName, value);
	}

	public void saveFile(String fileName, String description)
			throws ConfigurationException {
		FileOutputStream fout;
		try {
			fout = new FileOutputStream(fileName);
			this.config.store(fout, description);
			fout.close();
		} catch (IOException ex) {
			log.info("�޷�����ָ���������ļ�:" + fileName);
			throw new ConfigurationException("�޷�����ָ���������ļ�:" + fileName);
		}
	}

	public void saveFile(String fileName) throws ConfigurationException {
		saveFile(fileName, "");
	}

	public void saveFile() throws ConfigurationException {
		if (this.fn.length() == 0) {
			log.info("��ָ������������ļ���");
			throw new ConfigurationException("��ָ������������ļ���");
		}
		saveFile(this.fn);
	}
}