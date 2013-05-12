package org.springframework.cloud.service.rabbit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.cloud.service.common.RabbitServiceInfo;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class RabbitServiceInfoTest {
	@Test
	public void uriBasedParsing() {
		RabbitServiceInfo serviceInfo = new RabbitServiceInfo("id",  "amqp://myuser:mypass@myhost:12345/myvhost");
		
		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("myvhost", serviceInfo.getVirtualHost());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void badProtocol() {
		new RabbitServiceInfo("id",  "XX://myuser:mypass@myhost:12345/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingHost() {
		new RabbitServiceInfo("id",  "amqp://myuser:mypass@:12345/myvhost");
	}

	@Test
	public void missingPort() {
		RabbitServiceInfo serviceInfo = new RabbitServiceInfo("id",  "amqp://myuser:mypass@myhost/myvhost");
		assertEquals(5672, serviceInfo.getPort()); // the default port is 5672
	}

	@Test(expected=IllegalArgumentException.class)
	public void badUserInfo() {
		new RabbitServiceInfo("id",  "amqp://myuser@myhost/myvhost");
	}

	@Test
	public void missingUserInfo() {
		RabbitServiceInfo serviceInfo = new RabbitServiceInfo("id",  "amqp://myhost:12345/myvhost");
		assertEquals("guest", serviceInfo.getUserName());
		assertEquals("guest", serviceInfo.getPassword());
	}

	@Test
	public void missingVirtualHost() {
		RabbitServiceInfo serviceInfo = new RabbitServiceInfo("id",  "amqp://myuser:mypass@myhost:12345");
		assertEquals("/", serviceInfo.getVirtualHost());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void badVirtualHost() {
		new RabbitServiceInfo("id",  "amqp://myuser:mypass@myhost:12345/a/b");
	}
}
