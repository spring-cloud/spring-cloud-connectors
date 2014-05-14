package org.springframework.cloud.service.smtp;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.SmtpServiceInfo;
import org.springframework.mail.MailSender;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MailSenderFactoryTest extends AbstractCloudServiceConnectorFactoryTest<MailSenderFactory, MailSender, SmtpServiceInfo> {
	@Mock MailSender mockConnector;
	
	public MailSenderFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new MailSenderFactory(id, config);
	}
	
	public Class<MailSender> getConnectorType() {
		return MailSender.class;
	}
	
	public MailSender getMockConnector() {
		return mockConnector;
	}
	
	public SmtpServiceInfo getTestServiceInfo(String id) {
		return new SmtpServiceInfo(id, "host", 500, "username", "password");
	}
}
