package org.springframework.cloud.service.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 
 * @author Ramnivas Laddad
 *
 */
public class AmqpServiceInfoTest {
	@Test
	public void allArgs() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "myhost", 12345, "myuser", "mypass", "myvhost");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("myvhost", serviceInfo.getVirtualHost());
	}

	@Test
	public void allArgsEncodedVhost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "myhost", 12345, "myuser", "mypass", "my%2Fvhost");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("my/vhost", serviceInfo.getVirtualHost());
	}

	@Test
	public void allArgsEncodedRootVhost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "myhost", 12345, "myuser", "mypass", "%2F");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("/", serviceInfo.getVirtualHost());
	}

	@Test
	public void uriBasedParsing() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "amqp://myuser:mypass@myhost:12345/myvhost");
		
		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("myvhost", serviceInfo.getVirtualHost());
	}

	@Test
	public void uriBasedParsingEncodedVhost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "amqp://myuser:mypass@myhost:12345/my%2Fvhost");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("my/vhost", serviceInfo.getVirtualHost());
	}

	@Test
	public void uriBasedParsingEncodedRootVhost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "amqp://myuser:mypass@myhost:12345/%2F");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertEquals("/", serviceInfo.getVirtualHost());
	}

	@Test
	public void uriBasedParsingDefaultVhost() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id", "amqp://myuser:mypass@myhost:12345/");

		assertEquals("myhost", serviceInfo.getHost());
		assertEquals(12345, serviceInfo.getPort());
		assertEquals("myuser", serviceInfo.getUserName());
		assertEquals("mypass", serviceInfo.getPassword());
		assertNull(serviceInfo.getVirtualHost());
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingScheme() {
		new AmqpServiceInfo("id",  "://myuser:mypass@:12345/myvhost");
	}

	@Test
	public void amqpsSchemeAccepted() {
		AmqpServiceInfo serviceInfo = new AmqpServiceInfo("id",  "amqps://myuser:mypass@myhost:12345/myvhost");
		assertEquals("amqps", serviceInfo.getScheme());
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingHost() {
		new AmqpServiceInfo("id",  "amqp://myuser:mypass@:12345/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void badUserInfo() {
		new AmqpServiceInfo("id",  "amqp://myuser@myhost/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void missingUserInfo() {
		new AmqpServiceInfo("id",  "amqp://myhost:12345/myvhost");
	}

	@Test(expected=IllegalArgumentException.class)
	public void badVirtualHost() {
		new AmqpServiceInfo("id",  "amqp://myuser:mypass@myhost:12345/a/b");
	}
}
