package org.springframework.cloud.service.smtp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.cloud.service.common.SmtpServiceInfo;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.ServerAddress;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class SmtpServiceConnectorCreatorTest {
	private static final String TEST_HOST = "10.20.30.40";
	private static final int TEST_PORT = 1234;
	private static final String TEST_USERNAME = "myuser";
	private static final String TEST_PASSWORD = "mypass";

	
	private MailSenderCreator testCreator = new MailSenderCreator();

	@Test
	public void cloudMailSenderCreation() throws Exception {
		SmtpServiceInfo serviceInfo = createServiceInfo();

		MailSender dataSource = testCreator.create(serviceInfo, null);

		assertConnectorProperties(serviceInfo, dataSource);
	}

	public SmtpServiceInfo createServiceInfo() {
		return new SmtpServiceInfo("id", TEST_HOST, TEST_PORT, TEST_USERNAME, TEST_PASSWORD);
	}

	private void assertConnectorProperties(SmtpServiceInfo serviceInfo, MailSender connector) {
		assertNotNull(connector);
		
		JavaMailSenderImpl javaMailSender = (JavaMailSenderImpl) connector;
		assertEquals(serviceInfo.getHost(), javaMailSender.getHost());
		assertEquals(serviceInfo.getPort(), javaMailSender.getPort());
		assertEquals(serviceInfo.getUserName(), javaMailSender.getUsername());
		assertEquals(serviceInfo.getPassword(), javaMailSender.getPassword());
	}
}
