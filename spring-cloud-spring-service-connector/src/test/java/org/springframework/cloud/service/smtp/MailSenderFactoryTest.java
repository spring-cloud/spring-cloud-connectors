package org.springframework.cloud.service.smtp;

import org.mockito.Mock;
import org.springframework.cloud.service.AbstractCloudServiceConnectorFactoryTest;
import org.springframework.cloud.service.ServiceConnectorConfig;
import org.springframework.cloud.service.common.SmtpServiceInfo;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class MailSenderFactoryTest extends AbstractCloudServiceConnectorFactoryTest<MailSenderFactory, JavaMailSender, SmtpServiceInfo> {
	@Mock JavaMailSender mockConnector;
	
	public MailSenderFactory createTestCloudServiceConnectorFactory(String id, ServiceConnectorConfig config) {
		return new MailSenderFactory(id, config);
	}
	
	public Class<JavaMailSender> getConnectorType() {
		return JavaMailSender.class;
	}
	
	public JavaMailSender getMockConnector() {
		return mockConnector;
	}
	
	public SmtpServiceInfo getTestServiceInfo(String id) {
		return new SmtpServiceInfo(id, "host", 500, "username", "password");
	}
}
