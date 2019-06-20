package org.springframework.cloud;

import org.junit.Test;
import org.springframework.cloud.util.StandardUriInfoFactory;
import org.springframework.cloud.util.UriInfo;

import static org.junit.Assert.assertEquals;

/**
 * @author Jens Deppe
 */
public class StandardUriInfoFactoryTest {

	private static StandardUriInfoFactory factory = new StandardUriInfoFactory();

	@Test
	public void createUri() {
		String uri = "mysql://joe:joes_password@localhost:1527/big_db";
		UriInfo result = factory.createUri(uri);

		assertUriInfoEquals(result, "localhost", 1527, "joe", "joes_password", "big_db", null);
		assertEquals(uri, result.getUriString());
	}

	@Test
	public void createUriWithQuery() {
		String uri = "mysql://joe:joes_password@localhost:1527/big_db?p1=v1&p2=v2";
		UriInfo result = factory.createUri(uri);

		assertUriInfoEquals(result, "localhost", 1527, "joe", "joes_password", "big_db", "p1=v1&p2=v2");
		assertEquals(uri, result.getUriString());
	}

	@Test
	public void createNoUsernamePassword() {
		String uri = "mysql://localhost:1527/big_db";
		UriInfo result = factory.createUri(uri);

		assertUriInfoEquals(result, "localhost", 1527, null, null, "big_db", null);
		assertEquals(uri, result.getUriString());
	}

	@Test
	public void createWithUsernameNoPassword() {
		String uri = "mysql://joe@localhost:1527/big_db";
		UriInfo result = factory.createUri(uri);

		assertUriInfoEquals(result, "localhost", 1527, "joe", null, "big_db", null);
		assertEquals(uri, result.getUriString());
	}

	@Test
	public void createWithExplicitParameters() {
		String uri = "mysql://joe:joes_password@localhost:1527/big_db";
		UriInfo result = factory.createUri("mysql", "localhost", 1527, "joe", "joes_password", "big_db");

		assertUriInfoEquals(result, "localhost", 1527, "joe", "joes_password", "big_db", null);
		assertEquals(uri, result.getUriString());
	}

	private void assertUriInfoEquals(UriInfo result, String host, int port,
									 String username, String password, String path, String query) {
		assertEquals(host, result.getHost());
		assertEquals(port, result.getPort());
		assertEquals(username, result.getUserName());
		assertEquals(password, result.getPassword());
		assertEquals(path, result.getPath());
		assertEquals(query, result.getQuery());
	}
}
