package org.springframework.cloud;

import org.junit.Test;
import org.springframework.cloud.util.StandardUriInfoFactory;
import org.springframework.cloud.util.UriInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Jens Deppe
 */
public class StandardUriInfoFactoryTest {

  private static StandardUriInfoFactory factory = new StandardUriInfoFactory();

  /**
   * Basic sanity
   */
  @Test
  public void createUri1() {
    String uri = "mysql://joe:joes_password@localhost:1527/big_db";
    UriInfo result = factory.createUri(uri);

    assertEquals("localhost", result.getHost());
    assertEquals(1527, result.getPort());
    assertEquals("joe", result.getUserName());
    assertEquals("joes_password", result.getPassword());
    assertEquals("big_db", result.getPath());
    assertEquals(uri, result.buildUri().toString());
  }

  /**
   * Test with a 'jdbc:...' URI
   */
  @Test
  public void createUri2() {
    String uri = "mysql://joe:joes_password@localhost:1527/big_db";
    String jdbcUri = "jdbc:" + uri;
    UriInfo result = factory.createUri(jdbcUri);

    assertEquals("localhost", result.getHost());
    assertEquals(1527, result.getPort());
    assertEquals("joe", result.getUserName());
    assertEquals("joes_password", result.getPassword());
    assertEquals("big_db", result.getPath());
    assertEquals(uri, result.buildUri().toString());
  }

  /**
   * Test without user/password
   */
  @Test
  public void createUri3() {
    String uri = "mysql://localhost:1527/big_db";
    UriInfo result = factory.createUri(uri);

    assertEquals("localhost", result.getHost());
    assertEquals(1527, result.getPort());
    assertNull(result.getUserName());
    assertNull(result.getPassword());
    assertEquals("big_db", result.getPath());
    assertEquals(uri, result.buildUri().toString());
  }

  /**
   * Test with just a user and no password
   */
  @Test(expected = IllegalArgumentException.class)
  public void createUri4() {
    String uri = "mysql://joe@localhost:1527/big_db";
    factory.createUri(uri);
  }

  /**
   * Test when creating URI with explicit components
   */
  @Test
  public void createUri5() {
    String uri = "mysql://joe:joes_password@localhost:1527/big_db";
    UriInfo result = factory.createUri("mysql", "localhost", 1527, "joe",
        "joes_password", "big_db");

    assertEquals("localhost", result.getHost());
    assertEquals(1527, result.getPort());
    assertEquals("joe", result.getUserName());
    assertEquals("joes_password", result.getPassword());
    assertEquals("big_db", result.getPath());
    assertEquals(uri, result.buildUri().toString());
  }
}
