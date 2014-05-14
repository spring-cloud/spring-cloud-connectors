package org.springframework.cloud.service.rabbit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.cloud.service.common.AmqpServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitServiceInfoTest {
	@Test
	public void uriBasedParsing() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id",  "amqp://myuser:mypass@myhost:12345/myvhost");
		
		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("myvhost", serviceInfo.getVirtualHost());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void badProtocol() {
		new AmqpServiceInfo("id",  "XX://myuser:mypass@myhost:12345/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingHost() {
		new AmqpServiceInfo("id",  "amqp://myuser:mypass@:12345/myvhost");
	}

	@Test
	public void missingPort() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id",  "amqp://myuser:mypass@myhost/myvhost");
		assertEquals(5672, serviceInfo.getPort()); // the default port is 5672
	}

	@Test(expected=IllegalArgumentException.class)
	public void badUserInfo() {
		new AmqpServiceInfo("id",  "amqp://myuser@myhost/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingUserInfo() {
		new AmqpServiceInfo("id",  "amqp://myhost:12345/myvhost");
	}

	@Test
	public void missingVirtualHost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id",  "amqp://myuser:mypass@myhost:12345");
		assertEquals("/", serviceInfo.getVirtualHost());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void badVirtualHost() {
		new AmqpServiceInfo("id",  "amqp://myuser:mypass@myhost:12345/a/b");
	}
}
