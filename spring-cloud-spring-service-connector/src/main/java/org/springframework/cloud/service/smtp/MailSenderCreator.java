package org.springframework.cloud.service.smtp;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.SmtpServiceInfo;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Simplified access to Spring MailSender.
 *
 * @author Ramnivas Laddad
 *
 */
public class MailSenderCreator extends AbstractServiceConnectorCreator<MailSender, SmtpServiceInfo> {
	@Override
	public MailSender create(SmtpServiceInfo serviceInfo, ServiceConnectorConfig config) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
        mailSender.setHost(serviceInfo.getHost());
        mailSender.setPort(serviceInfo.getPort());
        mailSender.setUsername(serviceInfo.getUserName());
        mailSender.setPassword(serviceInfo.getPassword());

        return mailSender;
	}
}
