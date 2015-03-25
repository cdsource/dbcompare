/** 
 * @author 吴平福 
 * E-mail:wupf@asiainfo-linkage.com 
 * @version 创建时间：2013-4-7 下午3:43:23 
 * 类说明 
 */

package org.jpf.mails;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpf.utils.conf.ConfigProp;

public class JpfMail
{
	private static final Logger logger = LogManager.getLogger();

	private static String host = ""; // smtp服务器
	private static final String username = ""; // 用户名
	private static final String password = ""; // 密码
	private static final String MailFrom = ""; // 发件人地址

	public static void SendMail(String login, String sMailText, String strMailEncode, String strTitle)
	{

		logger.info("send mail login=:{}", login);
		try
		{
			Properties props = new Properties();
			Session sendMailSession;
			String[] strMailTos = login.split(",");
			String strMailTo = "";
			if (strMailTos.length > 0)
			{
				for (int i = 0; i < strMailTos.length; i++)
				{
					if (i < 15)
					{
						if (strMailTos[i].length() > 0)
						{
							if (-1 == strMailTo.indexOf(strMailTos[i]))
							{
								if (strMailTos[i].endsWith("@asiainfo.com"))
								{
									strMailTo += strMailTos[i] + ",";
								} else
								{
									strMailTo += strMailTos[i] + "@asiainfo.com,";
								}

							}
						}
					}
				}
				strMailTo += MailFrom;
			} else
			{
				strMailTo += login + "@asiainfo..com";
			}

			logger.info("send mail to:{}", strMailTo);
			sendMailSession = Session.getInstance(props, null);
			sendMailSession.setDebug(true);
			/*
			 * String
			 * strLIMIT_MAIL=ConfigProp.getStrFromConfig("sonar_hz.properties",
			 * "LIMIT_MAIL"); logger.info("strLIMIT_MAIL:{}",strLIMIT_MAIL);
			 * if(!strLIMIT_MAIL.equalsIgnoreCase("1")) {
			 * strMailTo="wupf@asiainfo.com"; }
			 */
			// strMailTo="wupf@asiainfo.com";
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Message newMessage = new MimeMessage(sendMailSession);
			newMessage.setFrom(new InternetAddress(MailFrom));
			newMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(strMailTo));
			/*
			 * if(sCcMail!=null && !"".equals(sCcMail)){
			 * newMessage.setRecipients(Message.RecipientType.CC,
			 * InternetAddress.parse(sCcMail)); }
			 */
			newMessage.setSubject(strTitle);// 主题
			newMessage.setSentDate(new java.util.Date());
			if (strMailEncode == null || 0==strMailEncode.length() )
			{
				strMailEncode = "UTF-8";
			}
			newMessage.setDataHandler(new DataHandler(sMailText, "text/html;charset=" + strMailEncode));

			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(newMessage, newMessage.getAllRecipients());
			transport.close();

			logger.info("发送邮件成功！");

			// transport = sendMailSession.getTransport("smtp");
			// transport.send(newMessage);
		} catch (javax.mail.SendFailedException ex)
		{
			String strMail = ex.getMessage();
			if (strMail.indexOf("550") > 0)
			{
				if (strMail.indexOf("<") > 0)
				{
					strMail = strMail.substring(strMail.indexOf("<") + 1);
					strMail = strMail.substring(0, strMail.indexOf(">"));
					logger.info(strMail);
					logger.info(login);
					login = login.replaceAll(strMail, "");
					SendMail(login, sMailText, strMailEncode, strTitle);
				}
			}
			logger.error(ex);

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
			logger.error(ex);
		}

	}

	public static void SendMail(String login, String sMailText, String strMailEncode, String strTitle,
			String strFileName)
	{
		try
		{
			Properties props = new Properties();
			Session sendMailSession;
			String[] strMailTos = login.split(",");
			String strMailTo = "";
			if (strMailTos.length > 0)
			{
				for (int i = 0; i < strMailTos.length; i++)
				{
					if (strMailTos[i].length() > 0)
					{
						strMailTo += strMailTos[i] + "@asiainfo.com,";
					}
				}
				strMailTo += MailFrom;
			} else
			{
				strMailTo += login + "@asiainfo.com";
			}
			logger.info("send mail to:" + strMailTo);
			sendMailSession = Session.getInstance(props, null);

			String strLIMIT_MAIL = ConfigProp.GetStrFromConfig("sonar_hz.properties", "LIMIT_MAIL");
			if (!strLIMIT_MAIL.equalsIgnoreCase("1"))
			{
				strMailTo = "wupf@asiainfo.com";
			}

			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Message newMessage = new MimeMessage(sendMailSession);
			newMessage.setFrom(new InternetAddress(MailFrom));
			newMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(strMailTo));
			/*
			 * if(sCcMail!=null && !"".equals(sCcMail)){
			 * newMessage.setRecipients(Message.RecipientType.CC,
			 * InternetAddress.parse(sCcMail)); }
			 */
			newMessage.setSubject(strTitle);// 主题
			newMessage.setSentDate(new java.util.Date());
			if (strMailEncode == null)
			{
				strMailEncode = "UTF-8";
			}
			newMessage.setDataHandler(new DataHandler(sMailText, "text/html;charset=" + strMailEncode));
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();

			mbp = new MimeBodyPart();

			FileDataSource fds = new FileDataSource(strFileName); // 得到数据源
			mbp.setDataHandler(new DataHandler(fds)); // 得到附件本身并至入BodyPart
			mbp.setFileName(fds.getName()); // 得到文件名同样至入BodyPart
			mp.addBodyPart(mbp);
			newMessage.setContent(mp); // Multipart加入到信件
			newMessage.saveChanges();

			Transport transport = sendMailSession.getTransport("smtp");
			transport.connect(host, username, password);
			transport.sendMessage(newMessage, newMessage.getAllRecipients());
			transport.close();

			logger.info("发送邮件成功！");
			transport.close();

			// transport = sendMailSession.getTransport("smtp");
			// transport.send(newMessage);
		} catch (javax.mail.SendFailedException ex)
		{
			String strMail = ex.getMessage();
			if (strMail.indexOf("550") > 0)
			{
				if (strMail.indexOf("<") > 0)
				{
					strMail = strMail.substring(strMail.indexOf("<") + 1);
					strMail = strMail.substring(0, strMail.indexOf(">"));
					logger.info(strMail);
					logger.info(login);
					login = login.replaceAll(strMail, "");
					SendMail(login, sMailText, strMailEncode, strTitle, strFileName);
				}
			}
			logger.error(ex);

		} catch (Exception ex)
		{
			// TODO: handle exception
			ex.printStackTrace();
		}

	}

}
