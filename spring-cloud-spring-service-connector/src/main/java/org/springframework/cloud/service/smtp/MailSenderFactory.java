package org.springframework.cloud.service.smtp;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Spring factory bean for SMTP service.
 *
 * @author Ramnivas Laddad
 *
 */
public class MailSenderFactory extends AbstractCloudServiceConnectorFactory<JavaMailSender> {
	public MailSenderFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, JavaMailSender.class, serviceConnectorConfiguration);
	}
}
