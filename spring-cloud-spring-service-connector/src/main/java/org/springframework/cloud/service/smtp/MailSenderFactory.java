package org.springframework.cloud.service.smtp;

import org.springframework.cloud.service.AbstractCloudServiceConnectorFactory;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.mail.MailSender;

/**
 * Spring factory bean for SMTP service.
 *
 * @author Ramnivas Laddad
 *
 */
public class MailSenderFactory extends AbstractCloudServiceConnectorFactory<MailSender> {
	public MailSenderFactory(String serviceId, ServiceConnectorConfig serviceConnectorConfiguration) {
		super(serviceId, MailSender.class, serviceConnectorConfiguration);
	}
}
