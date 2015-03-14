/**
 * 读取本地配置文件
 */
package org.jpf.searcher.config;

/**
 * 自动计费测试系统
 * @Company: Asiainfo Technologies （China）,Inc. Hangzhou
 * @author Asiainfo QA-HZ/刘晓芳
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
			log.info("无法读取指定的配置文件:" + fileName);
			throw new ConfigurationException("无法读取指定的配置文件:" + fileName);
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
			log.info("无法保存指定的配置文件:" + fileName);
			throw new ConfigurationException("无法保存指定的配置文件:" + fileName);
		}
	}

	public void saveFile(String fileName) throws ConfigurationException {
		saveFile(fileName, "");
	}

	public void saveFile() throws ConfigurationException {
		if (this.fn.length() == 0) {
			log.info("需指定保存的配置文件名");
			throw new ConfigurationException("需指定保存的配置文件名");
		}
		saveFile(this.fn);
	}
}